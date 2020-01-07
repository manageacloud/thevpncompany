package com.manageacloud.vpn.service;

import com.manageacloud.vpn.model.*;
import com.manageacloud.vpn.model.api.v1.ApiV1UserBandwidthDTO;
import com.manageacloud.vpn.model.dto.ClientDTO;
import com.manageacloud.vpn.model.payment.*;
import com.manageacloud.vpn.repository.*;
import com.manageacloud.vpn.repository.dns.DigitalOceanGateway;
import com.manageacloud.vpn.repository.payments.PayableResultRepository;
import com.manageacloud.vpn.repository.payments.PinNetGateway;
import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.MailUtil;
import com.myjeeva.digitalocean.pojo.Delete;
import com.myjeeva.digitalocean.pojo.DomainRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanService {

    private static final Logger log = LoggerFactory.getLogger(PlanService.class);

    private final PlanRepository planRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;

    private final PinNetGateway pinNetGateway;

    private final LocationRepository locationRepository;

    private final UserLocationRepository userLocationRepository;

    private final DigitalOceanGateway digitalOceanGateway;

    private final PayableResultRepository payableResultRepository;

    private final
    MailUtil mailUtil;

    public PlanService(PlanRepository planRepository, SubscriptionRepository subscriptionRepository, UserRepository userRepository, PinNetGateway pinNetGateway, MailUtil mailUtil, LocationRepository locationRepository, UserLocationRepository userLocationRepository, DigitalOceanGateway digitalOceanGateway, PayableResultRepository payableResultRepository) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.pinNetGateway = pinNetGateway;
        this.mailUtil = mailUtil;
        this.locationRepository = locationRepository;
        this.userLocationRepository = userLocationRepository;
        this.digitalOceanGateway = digitalOceanGateway;
        this.payableResultRepository = payableResultRepository;
    }

    public Subscription createSubscription(User user, ClientDTO clientDTO) {

        Plan.Type planType = Plan.Type.valueOf(clientDTO.getPlan());

        if ( planType == null ) {
            return null;
        }

        Plan plan;

        // temporal workaround to create free accounts
        plan = planRepository.findById(planType.getId()).orElseThrow();
        String identifier = "user-" + user.getId();
        String ip = CoreUtils.getRemoteIp();

        Cc cc = new Cc(user, identifier, clientDTO, ip);

        Subscription subscription = new Subscription(user, plan);
        subscriptionRepository.save(subscription);

        if ( plan.getType().isFree() ) { // free account

            subscription.setEnabled();
            subscriptionRepository.save(subscription);

        } else { // non free account

            CardPayableResult card = pinNetGateway.createCardToken(subscription, cc);

            if (card.is2xxCode()) {
                CustomerPayableResult customer = pinNetGateway.createCustomerToken(subscription, card.getTxtid());

                if (customer.is2xxCode()) {
                    SubscriptionPayableResult subscriptionPayable = pinNetGateway.createSubscription(subscription, planType, card, customer);

                    if (subscriptionPayable.is2xxCode()) {


                        subscription.setId_external(subscriptionPayable.getTxtid());
                        subscription.setEnabled();
                        subscriptionRepository.save(subscription);

                        String idCookie = CoreUtils.getCookieId().orElse("");

                        Email emailInvoice = Email.builder()
                                .with(ip, idCookie)
                                .fromSupport()
                                .to(user.getEmail())
                                .withType(Email.Type.INVOICE)
                                .withSubscription(subscription)
                                .build();

                        mailUtil.sendEmail(emailInvoice);

                    }
                }
            }
        }

        // create default DNS records
        if ( subscription.isEnabled() ) {
            // default plan will be be connected
            Location location = locationRepository.findDefaultLocation();
            UserLocation userLocation = userLocationRepository.findByUser(user).orElse(new UserLocation(user, location));
            userLocation = userLocationRepository.save(userLocation);

            DomainRecord userCname = digitalOceanGateway.findCNAME(userLocation);
            if ( userCname == null) {
                userCname = digitalOceanGateway.createCNAME(userLocation);
            } else {
                userCname = digitalOceanGateway.updateCNAME(userLocation);
            }
            userLocation.setExternal_cname(userCname.getId().toString());
            userLocationRepository.save(userLocation);
        }

        return subscription;
    }


    public PayableResult findLatestPayableResult(Subscription subscription) {
        return payableResultRepository.findLastPayableResult(subscription.getId());
    }

    public PayableResult updateCreditCard(User user, ClientDTO clientDTO) {

        Subscription subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElse(null);

        if ( subscription == null ) {
            return null;
        }

        String identifier = "user-" + user.getId();
        String ip = CoreUtils.getRemoteIp();

        Cc cc = new Cc(user, identifier, clientDTO, ip);

        // get customer from subscription
        CustomerPayableResult customer = pinNetGateway.getCustomerFromSubscription(subscription);
        //CardPayableResult oldCard = pinNetRespository.getCardFromSubscription(subscription, user);

        // add credit card to customer
        CardPayableResult newCard = pinNetGateway.addCardtoCustomer(subscription, customer, cc);

        if ( ! newCard.is2xxCode() ) {
            return newCard;
        } else {
            return pinNetGateway.updateSubscriptionCreditCard(subscription, newCard);
        }

        // remove old credit card (error as it is the primary card)
        //PayableResult deleteCard = pinNetRespository.deleteCustomerCard(customer, oldCard, user);

        // associate the new credit card to subscription


    }

    /**
     * Change Subscription allows to change subscriptions from the different available plans
     *
     * There are three types of changes
     * - From Free to Paid Subscription
     * - From Paid to Free Subscription
     * - From Paid to Paid Subscription
     * - From No Subscription (Cancelled Subscription) to Free Subscription
     * - From No Subscription (Cancelled Subscription) to Paid Subscription
     *
     *
     * @param user user who is making the request
     * @param clientDTO DTO that contains the client data
     * @return
     */
    public Subscription changeSubscription(User user, ClientDTO clientDTO) {

        Plan.Type planType = Plan.Type.valueOf(clientDTO.getPlan());
        if ( planType == null ) {
            return null;
        }
        Optional<Plan> plan = planRepository.findById(planType.getId());
        Subscription newSubscription = new Subscription(user, plan.orElseThrow());
        subscriptionRepository.save(newSubscription);

        // cancel previous plan
        Subscription oldSubscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElse(null);

        // create a new plan from existing customer and existing credit card
        CustomerPayableResult customer;
        CardPayableResult card;
        if (  oldSubscription == null || oldSubscription.getPlan().getType().isFree() ) {
            // create a new credit card if the previous subscription was cancelled or of it is free
            String identifier = "user-" + user.getId();
            String ip = CoreUtils.getRemoteIp();
            Cc cc = new Cc(user, identifier, clientDTO, ip);
            card = pinNetGateway.createCardToken(newSubscription,cc);
            customer = pinNetGateway.createCustomerToken(newSubscription, card.getTxtid());
        } else { // use the previous credit card
            customer = pinNetGateway.getCustomerFromSubscription(oldSubscription);
            card = pinNetGateway.getCardFromSubscription(oldSubscription);
        }

        // if the card and the customer are OK
        if ( card.is2xxCode() && customer.is2xxCode() ) {

            // cancel current subscription if exists
            if ( oldSubscription != null ) {
                oldSubscription.setDisabled_request();
                subscriptionRepository.save(oldSubscription);
                if ( !oldSubscription.getPlan().getType().isFree() ) {
                    SubscriptionPayableResult subscriptionCancelationResult = pinNetGateway.cancelSubscription(oldSubscription);
                    if (subscriptionCancelationResult.is2xxCode()) {
                        oldSubscription.setDisabled_confirmed();
                        subscriptionRepository.save(oldSubscription);
                    }
                }
            }

            // create new paid subscription
            if (!newSubscription.getPlan().getType().isFree()) { // new subscription is not free
                SubscriptionPayableResult subscriptionPayable = pinNetGateway.createSubscription(newSubscription, planType, card, customer);
                if (subscriptionPayable.is2xxCode()) {
                    newSubscription.setId_external(subscriptionPayable.getTxtid());
                    newSubscription.setEnabled();
                    subscriptionRepository.save(newSubscription);
                }
            } else { // new subscription is free
                newSubscription.setEnabled();
                subscriptionRepository.save(newSubscription);
            }

        } else { // card or customer are not correct, so we cancel the new subscription
            newSubscription.setDisabled_request();
            newSubscription.setDisabled_confirmed();
            subscriptionRepository.save(newSubscription);
        }

        return newSubscription;
    }

    /**
     * Cancels the subscription.
     *
     * @param user
     * @return
     */
    public Subscription cancelSubscription(User user) {

        Optional<Subscription> optionalSubscription = subscriptionRepository.findEnabledSubscriptionByUser(user);

        if ( optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.orElseThrow();
            if ( subscription.isEnabled()) {
                subscription.setDisabled_request();
                subscriptionRepository.save(subscription);

                if ( subscription.getPlan().getType().isFree() ) {
                    subscription.setDisabled_confirmed();
                } else { // cancel paid plan
                    SubscriptionPayableResult subscriptionCancelationResult = pinNetGateway.cancelSubscription(subscription);
                    if (subscriptionCancelationResult.is2xxCode()) {
                        subscription.setDisabled_confirmed();
                    }
                }
                subscriptionRepository.save(subscription);
            }

            // delete DNS record
            UserLocation userLocation = userLocationRepository.findByUser(user).orElseThrow();
            if ( userLocation != null ) {
                Delete deleteAction = digitalOceanGateway.deleteCNAME(userLocation);
                if ( deleteAction.getIsRequestSuccess() ) {
                    userLocationRepository.delete(userLocation);
                }
            }

            return subscription;
        } else {
            return null;
        }
    }


    public Optional<Subscription> findSubscriptionByUser(User user) {
        return subscriptionRepository.findEnabledSubscriptionByUser(user);
    }

    /**
     * Checks if the user is currently active, and it can use VPNs
     *
     * @param user user that needs to be checked
     * @return if True, the user is active and can use VPN.
     */
    public boolean isUserActive(User user) {
        Subscription subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElse(null);

        if ( subscription == null ) { // the user has no active subscription
            return false;
        } else if ( subscription.getPlan().getType() == Plan.Type.FREE_500 ) {

            if (subscription.isUsed() ) {
                return false;
            } else {
                return true;
            }

        }

        // paid working subscription
        return true;

    }

    public void processBandwidthNotification(User user, ApiV1UserBandwidthDTO apiV1UserBandwidthDTO) {

        Subscription subscription = subscriptionRepository.findEnabledSubscriptionByUser(user)
                .orElseThrow(); // we should not be receiving notifications from inactive users

        if ( subscription.getPlan().getType() == Plan.Type.FREE_500
                && apiV1UserBandwidthDTO.getMegabits() >= subscription.getPlan().getData() ) {
            subscription.setUsed();
            subscriptionRepository.save(subscription);
        } else if ( subscription.getPlan().getType() == Plan.Type.FREE_500
                && apiV1UserBandwidthDTO.getMegabits() == 0 ) {
            subscription.setUnused();
            subscriptionRepository.save(subscription);
        }
    }

}