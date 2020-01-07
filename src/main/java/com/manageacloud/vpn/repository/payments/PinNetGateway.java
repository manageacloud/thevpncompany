/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.payments;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manageacloud.vpn.model.Plan;
import com.manageacloud.vpn.model.Subscription;
import com.manageacloud.vpn.model.payment.*;
import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.PayableResultsFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class PinNetGateway {


    private final PayableResultRepository payableResultRepository;

    private final PayableResultsFactory payableResultsFactory;

    private static final Logger log = LoggerFactory.getLogger(PinNetGateway.class);

    @Value("${pinnet.api_url}")
    private String pinnetApiUrl;

    @Value("${pinnet.public_key}")
    private String pinnetPublicKey;

    @Value("${pinnet.secret_key}")
    private String pinnetSecretKey;

    @Value("${pinnet.token_monthly_plan}")
    private String pinnetTokenMonthlyPlan;

    @Value("${pinnet.token_6_months_plan}")
    private String pinnetToken6MonthsPlan;

    @Value("${pinnet.token_12_months_plan}")
    private String pinnetToken12MonthsPlan;
    
    

    public PinNetGateway(PayableResultRepository payableResultRepository, PayableResultsFactory payableResultsFactory) {
        this.payableResultRepository = payableResultRepository;
        this.payableResultsFactory = payableResultsFactory;
    }

    private enum Method {
        GET,
        PUT,
        POST,
        DELETE
    }

    private enum Key {
        NONE(),
        DEFAULT("token"),
        CARD("card_token"),
        SUBSCRIPTION(""), // to define
        CUSTOMER("customer_token");

        private String label;

        Key() {

        }
        Key(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }


    /**
     * Creates a customer
     *
     * @param subscription
     * @param cardToken
     * @return CustomerPayableResult
     */
    public CustomerPayableResult createCustomerToken(Subscription subscription, String cardToken) {

        List<BasicNameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("email", subscription.getUser().getEmail()));
        nameValuePairs.add(new BasicNameValuePair("card_token", cardToken));

        return (CustomerPayableResult) this.executeRequest(subscription, PayableResultsFactory.Types.CUSTOMER, Key.DEFAULT, Method.POST, "/customers", nameValuePairs);

    }

    /**
     * Creates a credit card
     *
     * @param subscription
     * @param cc
     * @return CardPayableResult
     */
    public CardPayableResult createCardToken(Subscription subscription, Cc cc) {

        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("pinnetPublicKey", pinnetPublicKey));
        nameValuePairs.add(new BasicNameValuePair("number", cc.getNumber()));
        nameValuePairs.add(new BasicNameValuePair("expiry_month", cc.getMonth()));
        nameValuePairs.add(new BasicNameValuePair("expiry_year", cc.getYear()));
        nameValuePairs.add(new BasicNameValuePair("cvc", cc.getCvv()));
        nameValuePairs.add(new BasicNameValuePair("name", cc.getName()));
        nameValuePairs.add(new BasicNameValuePair("address_line1", cc.getAddress()));
        nameValuePairs.add(new BasicNameValuePair("address_line2", ""));
        nameValuePairs.add(new BasicNameValuePair("address_city", cc.getCity()));
        nameValuePairs.add(new BasicNameValuePair("address_postcode", "28220"));
        nameValuePairs.add(new BasicNameValuePair("address_country", cc.getCountry()));

        return (CardPayableResult) this.executeRequest(subscription, PayableResultsFactory.Types.CARD, Key.DEFAULT, Method.POST, "/cards", nameValuePairs);

    }


    /**
     * Creates a credit card which is linked to a Customer
     *
     * @param subscription
     * @param customer
     * @param cc
     * @return CardPayableResult
     */
    public CardPayableResult addCardtoCustomer(Subscription subscription, CustomerPayableResult customer, Cc cc) {

        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("pinnetPublicKey", pinnetPublicKey));
        nameValuePairs.add(new BasicNameValuePair("number", cc.getNumber()));
        nameValuePairs.add(new BasicNameValuePair("expiry_month", cc.getMonth()));
        nameValuePairs.add(new BasicNameValuePair("expiry_year", cc.getYear()));
        nameValuePairs.add(new BasicNameValuePair("cvc", cc.getCvv()));
        nameValuePairs.add(new BasicNameValuePair("name", cc.getName()));
        nameValuePairs.add(new BasicNameValuePair("address_line1", cc.getAddress()));
        nameValuePairs.add(new BasicNameValuePair("address_line2", ""));
        nameValuePairs.add(new BasicNameValuePair("address_city", cc.getCity()));
        nameValuePairs.add(new BasicNameValuePair("address_postcode", "28220"));
        nameValuePairs.add(new BasicNameValuePair("address_country", cc.getCountry()));

        return (CardPayableResult) this.executeRequest(subscription, PayableResultsFactory.Types.CARD, Key.DEFAULT, Method.POST, "/customers/" + customer.getTxtid() + "/cards", nameValuePairs);

    }

    public  PayableResult deleteCustomerCard(Subscription subscription, CustomerPayableResult customer, CardPayableResult oldCard) {
        return this.executeRequest(subscription, PayableResultsFactory.Types.DEFAULT, Key.NONE, Method.DELETE, "/customers/" + customer.getTxtid() + "/cards/" + oldCard.getTxtid(), null);
    }


    /**
     * Creates a subscription
     *
     * @param subscription
     * @param planType
     * @param card
     * @param customer
     * @return SubscriptionPayableResult
     */
    public SubscriptionPayableResult createSubscription(Subscription subscription, Plan.Type planType, CardPayableResult card, CustomerPayableResult customer) {
        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);
        if ( planType.equals(Plan.Type.MONTHLY)) {
            nameValuePairs.add(new BasicNameValuePair("plan_token", pinnetTokenMonthlyPlan));
        } else if ( planType.equals(Plan.Type.SEMESTER)) {
            nameValuePairs.add(new BasicNameValuePair("plan_token", pinnetToken6MonthsPlan));
        } else if ( planType.equals(Plan.Type.YEAR)) {
            nameValuePairs.add(new BasicNameValuePair("plan_token", pinnetToken12MonthsPlan));
        } else {
            throw new UnsupportedOperationException("Token Plan does not exists for plan " + planType);
        }
        nameValuePairs.add(new BasicNameValuePair("card_token", card.getTxtid()));
        nameValuePairs.add(new BasicNameValuePair("customer_token", customer.getTxtid()));

        return (SubscriptionPayableResult) this.executeRequest(subscription, PayableResultsFactory.Types.SUBSCRIPTION, Key.DEFAULT, Method.POST, "/subscriptions", nameValuePairs);
    }


    /**
     * Cancel an existing subscription
     *
     * @param subscription
     * @return SubscriptionPayableResult
     */
    public SubscriptionPayableResult cancelSubscription(Subscription subscription) {
        String token = subscription.getId_external();
        return (SubscriptionPayableResult) this.executeRequest(subscription, PayableResultsFactory.Types.SUBSCRIPTION, Key.DEFAULT,  Method.DELETE, "/subscriptions/" + token, null);

    }

    /**
     * Get the Customer token from a Subscription
     *
     * @param subscription
     * @return CustomerPayableResult
     */
    public CustomerPayableResult getCustomerFromSubscription(Subscription subscription) {

        PayableResultsFactory.Types type = PayableResultsFactory.Types.CUSTOMER;

        return (CustomerPayableResult) this.executeRequest(subscription, type, Key.CUSTOMER, Method.GET, "/subscriptions/" + subscription.getId_external(), null);
    }

    /**
     * Get a card token from a Subscription
     *
     * @param subscription
     * @return CardPayableResult
     */
    public CardPayableResult getCardFromSubscription(Subscription subscription) {

        PayableResultsFactory.Types type  = PayableResultsFactory.Types.CARD;

        return (CardPayableResult) this.executeRequest(subscription, type, Key.CARD, Method.GET, "/subscriptions/" + subscription.getId_external(), null);
    }

    /**
     * Update the card information from a given subscription
     *
     * @param subscription
     * @param card
     * @return SubscriptionPayableResult
     */
    public SubscriptionPayableResult updateSubscriptionCreditCard(Subscription subscription, CardPayableResult card) {

        PayableResultsFactory.Types type  = PayableResultsFactory.Types.SUBSCRIPTION;

        List<BasicNameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("card_token", card.getTxtid()));

        return (SubscriptionPayableResult) this.executeRequest(subscription, type, Key.CARD, Method.PUT, "/subscriptions/" + subscription.getId_external(), nameValuePairs);
    }


    private PayableResult executeRequest(Subscription subscription, PayableResultsFactory.Types type, Key key, Method method, String url, List<BasicNameValuePair> nameValuePairs) {

        HttpRequestBase request = null;
        HttpEntityEnclosingRequestBase enclosingRequest = null;
        if ( method == Method.GET ) {
            request = new HttpGet(pinnetApiUrl + url);
        } else if ( method == Method.PUT ) {
            enclosingRequest = new HttpPut(pinnetApiUrl + url);
        } else if ( method == Method.POST ) {
            enclosingRequest = new HttpPost(pinnetApiUrl + url);
        } else if ( method == Method.DELETE ) {
            request = new HttpDelete(pinnetApiUrl + url);
        } else {
            throw new UnsupportedOperationException();
        }

        HttpClient client = HttpClientBuilder.create().build();

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(pinnetSecretKey, "");
        PayableResult payableResult;
        try {
            if ( enclosingRequest != null && nameValuePairs != null ) {
                enclosingRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
            HttpResponse response = null;
            if ( request != null ) {
                Header header = new BasicScheme(StandardCharsets.UTF_8).authenticate(creds, request, null);
                request.addHeader(header);
                log.info("GET EndPoint: " + request.getURI());
                response = client.execute(request);
            } else {
                Header header = new BasicScheme(StandardCharsets.UTF_8).authenticate(creds, enclosingRequest, null);
                enclosingRequest.addHeader(header);
                log.info("GET EndPoint: " + enclosingRequest.getURI());
                response = client.execute(enclosingRequest);
            }

            payableResult = this.processResponse(subscription, response, type, key);

        } catch (AuthenticationException | IOException e) {
            log.error("Payment Exception: " + e.getMessage(), e);
            payableResult = new PayableResult(subscription, subscription.getUser(), e.getMessage(), "", "");
        }

        return payableResultRepository.save(payableResult);
    }


    /**
     * Process a response.
     *
     * @param subscription
     * @param response
     * @param type
     * @param key
     * @return
     * @throws IOException
     */
    private PayableResult processResponse(Subscription subscription, HttpResponse response, PayableResultsFactory.Types type, Key key) throws IOException {

        PayableResult payableResult;
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuilder rawResponse = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            rawResponse.append(line);
        }
        log.info("RAW Response: " + rawResponse);
        int code = response.getStatusLine().getStatusCode();
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(rawResponse.toString());
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            final String token;
            if ( key != Key.NONE) {
                token = jsonObject.get("response").getAsJsonObject().get(key.getLabel()).getAsString();
            } else {
                token = null;
            }
            log.info(String.format("Token: %s", token));
            payableResult =  payableResultsFactory.getPayableResult(type, subscription, subscription.getUser(), String.valueOf(code), rawResponse.toString(), token);
        } catch (NullPointerException e) {
            log.warn(String.format("Error: %s", rawResponse));
            payableResult = this.processError(subscription, type, code,  rawResponse);
        }
        return payableResult;
    }

    /**
     * Process an error messaged returned by the gateway
     *
     *
     * @param subscription Subscription related with the request
     * @param type
     * @param code
     * @param rawResponse
     * @return
     */
    private PayableResult processError(Subscription subscription, PayableResultsFactory.Types type, int code, StringBuilder rawResponse) {
        PayableResult payableResult;
        try {
            // this is an error message Let's try to parse it.
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(rawResponse.toString());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("messages").getAsJsonArray();
            Iterator<JsonElement> iterator=jsonArray.iterator();
            StringBuilder errorMessage = new StringBuilder("");
            while (iterator.hasNext()) {
                JsonObject jsonErrorElement = iterator.next().getAsJsonObject();
                errorMessage.append(jsonErrorElement.get("message").getAsString());
                errorMessage.append(". ");
            }
            payableResult = payableResultsFactory.getPayableResult(type, subscription, subscription.getUser(), String.valueOf(code), errorMessage.toString(), "");
        } catch (NullPointerException e1) {
            // we save the raw error
            payableResult = payableResultsFactory.getPayableResult(type, subscription, subscription.getUser(), String.valueOf(code), rawResponse.toString(), "");
        }
        return payableResult;
    }







}
