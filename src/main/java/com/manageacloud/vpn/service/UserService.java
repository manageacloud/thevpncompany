package com.manageacloud.vpn.service;

import com.manageacloud.vpn.model.*;
import com.manageacloud.vpn.model.dto.*;
import com.manageacloud.vpn.repository.TokenRepository;
import com.manageacloud.vpn.repository.UserRepository;
import com.manageacloud.vpn.repository.UserRoleRepository;
import com.manageacloud.vpn.utils.AuthUtils;
import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.DateUtils;
import com.manageacloud.vpn.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11); // TODO create bean along WebSecurityConfig

    private final
    UserRoleRepository userRoleRepository;

    private final
    UserRepository userRepository;

    private final
    TokenRepository tokenRepository;

    private final
    MailUtil mailUtil;

    public UserService(UserRoleRepository userRoleRepository, UserRepository userRepository, TokenRepository tokenRepository, MailUtil mailUtil) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.mailUtil = mailUtil;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserSession(user.getUsername(), user.getPassword(), user.getName(), user.getAuthority());
    }

    public User findUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


    public User createUser(ClientDTO clientDTO) {

        String email = clientDTO.getEmail().toLowerCase().trim();

        User user = userRepository.findOneByEmail(email);

        // user exists, abort user creation
        if ( user != null ) {
            return null;
        }

        String name = clientDTO.getName();
        String plainPassword = CoreUtils.generatePassword();

        log.debug("Plain Password: " + plainPassword);

        Timestamp autopass = DateUtils.currentTimestamp();
        UserRole.Role role = UserRole.Role.ROLE_USER;
        String phone = null;

        String ip = CoreUtils.getRemoteIp();

        String encryptedPassword = CoreUtils.bcrypt(plainPassword);

        // create user
        user  = new User(name, email, encryptedPassword, email, phone, ip, autopass, "nocookieyet");
        user.setPlainPassword(plainPassword);
        user.setLoginToken(CoreUtils.sha256(encryptedPassword + email));
        user = userRepository.save(user);

        String idCookie = CoreUtils.getCookieId().orElse("");

        String randomToken = CoreUtils.sha256(CoreUtils.generatePassword());
        Token token = new Token(email, Token.Type.RESET_PASSWORD, randomToken);
        tokenRepository.save(token);

        Email newUserEmail = Email.builder()
                .with(ip, idCookie)
                .fromSupport()
                .to(email)
                .withType(Email.Type.NEW_USER)
                .withPlainPassword(plainPassword)
                .withRecoverPassword(token)
                .withUser(user)
                .build();

        mailUtil.sendEmail(newUserEmail);

        if ( role != UserRole.Role.ROLE_USER) {
            List<UserRole> userRoles = userRoleRepository.findByEmail(email);
            UserRole userRole;
            if ( userRoles.isEmpty()  ) {
                userRole = new UserRole(user, role);
            } else {
                userRole = userRoles.get(0);
            }

            userRoleRepository.save(userRole);
        }

        return user;
    }

    public User authenticateUser(User user) {

        String email = user.getEmail();
        String plainPassword = user.getPlainPassword();

        UserSession userSession = null;

        if ( passwordEncoder.matches(plainPassword, user.getPassword()) ) {
            userSession = new UserSession(email.toLowerCase(), user.getPassword(), user.getName(), user.getAuthority());
        }

        AuthUtils.authenticate(userSession);

        return user;
    }

    public User getAuthenticatedUser() {
        return userRepository.findOneByEmail(AuthUtils.getLoggedEmail());
    }

    public Token generateToken(ResetPasswordDTO resetPasswordDTO) {

        String email = resetPasswordDTO.getEmail().toLowerCase().trim();

        User user = userRepository.findOneByEmail(email);

        if ( user == null ) {
            return null;
        }

        String randomToken = CoreUtils.sha256(CoreUtils.generatePassword() + new Date());
        Token token = new Token(email, resetPasswordDTO.getType(), randomToken);
        tokenRepository.save(token);
        return token;
    }

    public boolean validateToken(Token.Type type, Tokenized tokenized) {
        Token token = tokenRepository.findByToken(tokenized.getToken());
        if ( token == null ) {
            return false;
        } else if ( ! token.getType().equals(type.toString())) {
            return false;
        } else if ( token.isUsed()) {
            return false;
        } else {
            token.setUsed();
            tokenRepository.save(token);
            return true;
        }
    }

    public User findUserByToken(Tokenized tokenized) {
        Token token = tokenRepository.findByToken(tokenized.getToken());
        final User user;
        if ( token.getUser() == null ) {
            user = userRepository.findOneByEmail(token.getEmail());
        } else {
            user = token.getUser();
        }
        return user;
    }

    public boolean resetPassword(User user, UpdatePassword updatePasswordDTO) {

        String encryptedPassword = CoreUtils.bcrypt(updatePasswordDTO.getPassword());

        user.setPassword(encryptedPassword);
        userRepository.save(user);

        return true;

    }

}
