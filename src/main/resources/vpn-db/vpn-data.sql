
--
-- Plans
--
INSERT INTO users (id, name, username, email)
VALUES (-1, 'Admin', 'admin@thevpncompany.com.au', 'admin@thevpncompany.com.au');


INSERT INTO plans (name[0], price, price_discounted, data)
VALUES
('TheVPNCompany 1 Month Plan',  11.95, 5.95, -1),
('TheVPNCompany 6 Month Plan', 9.95, 4.95, -1),
('TheVPNCompany 12 Month Plan', 7.95, 3.95, -1);

INSERT INTO plans (name[0], price, price_discounted, data)
VALUES
('Free Account No Limits', 0, 0, -1);

INSERT INTO plans (name[0], price, price_discounted, data)
VALUES
('Free 500MB per month', 0, 0, 500);

--
-- default users and roles
--

INSERT INTO user_roles (username, role) VALUES
('tk421.1+admin@gmail.com', 'ROLE_ADMIN');

--
-- default locations
--

INSERT INTO locations (iso, sort, hostname, name, parent_region, enabled, standard) VALUES
('AU_NSW', 10, 'au-nsw.cloud.thevpncompany.com.au', 'Australia', 'Asia Pacific', now(), now()),
('US_CA', 20, 'us-ca.cloud.thevpncompany.com.au', 'California', 'America', now(), null),
('US_NY', 21, 'us-ny.cloud.thevpncompany.com.au', 'New York', 'America', now(), null),
('CA_ON', 22, 'ca-on.cloud.thevpncompany.com.au', 'Canada', 'America', now(), null),
('SG_01', 11, 'sg-01.cloud.thevpncompany.com.au', 'Singapore', 'Asia Pacific', now(), null),
('GB_ENG', 31, 'gb-eng.cloud.thevpncompany.com.au', 'London', 'Europe', now(), null),
('NL_NH', 32, 'nl-nh.cloud.thevpncompany.com.au', 'Amsterdam', 'Europe', now(), null),
('DE_HE', 33, 'de-he.cloud.thevpncompany.com.au', 'Frankfurt', 'Europe', now(), null),
('IN_CK', 33, 'in-ck.cloud.thevpncompany.com.au', 'Bangalore', 'India', now(), null)
;

--
-- Supported VPNs technologies
--
INSERT INTO vpns(name)
VALUES ('Openvpn');

--
-- Supported Devices
--
INSERT INTO devices(name)
VALUES
       ('Linux'),
       ('Windows'),
       ('Mac'),
       ('Android'),
       ('IOs')
;

INSERT INTO suppliers (name)
VALUES ('DigitalOcean'),
       ('Vultr');

INSERT INTO supplier_locations (supplier_id, location_id, id_external)
VALUES (2, 1, ''), -- Vultr Sydney
       (1, 2, 'sfo2'),
       (1, 3, 'nyc3'),
       (1, 4, 'tor1'),
       (1, 5, 'sgp1'),
       (1, 6, 'lon1'),
       (1, 7, 'ams3'),
       (1, 8, 'fra1'),
       (1, 9, 'blr1')
;

INSERT INTO servers ( user_id, supplier_location_id, ipv4, creation_request, creation_completed, deletion_protection)
VALUES (-1, 1, '149.28.162.96', now(), now(), now()),
       (-1, 2, '165.22.159.131', now(), now(), now());

UPDATE locations SET server_id = 1234 WHERE iso = 'AU_NSW';
UPDATE locations SET server_id = 1235 WHERE iso = 'US_CA';