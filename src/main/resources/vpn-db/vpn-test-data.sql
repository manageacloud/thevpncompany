
-- usermame to test login
INSERT INTO users (name, username, password, email, login_token)
            -- password 'aaaaaaaa'
            VALUES ('Test', 'test@email.com', '$2a$11$vC3ebOmc8h6QTqEXkEPWP.Q0ODo1UYmZT6u1DDrNdkKigS32k/6x2', 'test@email.com', 'autotoken1'),
                   ('Test 2', 'test2@email.com', '$2a$11$vC3ebOmc8h6QTqEXkEPWP.Q0ODo1UYmZT6u1DDrNdkKigS32k/6x2', 'test2@email.com', 'autotoken1'),
                   ('updateUserCertsTests', 'test3@email.com', '$2a$11$vC3ebOmc8h6QTqEXkEPWP.Q0ODo1UYmZT6u1DDrNdkKigS32k/6x2', 'test3@email.com', 'autotoken1'), -- updateUserCertsTests;
                   ('createUserCertsTests', 'test4@email.com', '$2a$11$vC3ebOmc8h6QTqEXkEPWP.Q0ODo1UYmZT6u1DDrNdkKigS32k/6x2', 'test4@email.com', 'autotoken4'), -- createUserCertsTests;
                   ('createUserDeviceConfigurationsTests', 'test5@email.com', '$2a$11$vC3ebOmc8h6QTqEXkEPWP.Q0ODo1UYmZT6u1DDrNdkKigS32k/6x2', 'test5@email.com', 'autotoken5'), -- createUserDeviceConfigurationsTests
                   ('updateUserStatusTest', 'updateUserStatusTest@email.com', '$2a$11$vC3ebOmc8h6QTqEXkEPWP.Q0ODo1UYmZT6u1DDrNdkKigS32k/6x2', 'updateUserStatusTest@email.com', 'autotoken5') -- updateUserStatusTest



                   ;

-- id, user_id, supplier_location_id, id_external, ipv4, deletion_protection, creation_request, creation_completed, creation_failed, deletion_request, deletion_completed, deletion_failed, created, deleted)
INSERT INTO servers (id, user_id, supplier_location_id, creation_request, creation_completed, creation_failed  )
VALUES
       (1, -1, 2, null, null, null), -- markServerAsRequestedTest
       (2, -1, 2, now(), null, null), -- markServerAsRequestFailedTest
       (3, -1, 2, now(), null, null) -- markServerAsRequestFailedTest
;

-- add default certificates
INSERT INTO vpn_user_certs (vpn_id, user_id, private, public, ca, static_key)
VALUES (1, 5, 'private', 'public', 'ca', 'static_key');

INSERT INTO subscriptions (user_id, plan_id, id_external, enabled)
    VALUES (6, 5, '500mb plan', now()) -- updateUserStatusTest
    ;