
--
-- plan
--

DROP TABLE IF EXISTS plans;
CREATE TABLE plans (
  id serial primary key,
  name varchar[],
  price money,
  price_discounted money,
  data int, -- data included in the plan in Megabits
  created timestamp with time zone not null default now(),
  deleted timestamp with time zone null
);




--
-- Users
--

DROP TABLE IF EXISTS user_roles;
CREATE TABLE user_roles (
  id serial primary key,
  user_id numeric,
  username varchar,
  role varchar,
  created TIMESTAMP default CURRENT_TIMESTAMP,
  deleted TIMESTAMP null
);

DROP TABLE IF EXISTS users;
CREATE TABLE users (
   id serial primary key ,
   name varchar,
   username varchar,
   password varchar,
   login_token varchar,
   email varchar unique ,
   phone varchar,
   cookie varchar,
   autopass TIMESTAMP null,
   ip varchar,
   created TIMESTAMP not null default CURRENT_TIMESTAMP,
   deleted TIMESTAMP null
);

DROP TABLE IF EXISTS log_transactions;
CREATE TABLE log_transactions (
   id serial primary key,
   user_id numeric,
   subscription_id numeric,
   code varchar,
   text varchar,
   txtid  varchar,
   ip varchar,
   dtype varchar,
   created TIMESTAMP not null default CURRENT_TIMESTAMP,
   deleted TIMESTAMP null
);
-- ALTER TABLE log_transactions ADD COLUMN subscription_id numeric;

DROP TABLE IF EXISTS subscriptions;
CREATE TABLE subscriptions (
  id serial primary key,
  user_id numeric,
  plan_id numeric,
  id_external varchar,
  enabled TIMESTAMP null,
  disabled_request TIMESTAMP null,
  disabled_confirmed TIMESTAMP null,
  used TIMESTAMP null,
  created TIMESTAMP not null default CURRENT_TIMESTAMP,
  deleted TIMESTAMP null
);

-- ALTER TABLE subscriptions ADD COLUMN used TIMESTAMP null;

CREATE UNIQUE INDEX idx_enabled_per_user ON subscriptions (user_id, (enabled is not null)) WHERE disabled_request is null;

DROP TABLE IF EXISTS tokens;
CREATE TABLE tokens (
   id serial primary key,
   user_id numeric,
   email varchar,
   type varchar,
   token varchar,
   used TIMESTAMP null,
   created TIMESTAMP not null default CURRENT_TIMESTAMP,
   deleted TIMESTAMP null
);


--
-- Available server locations
--

DROP TABLE IF EXISTS locations;
CREATE TABLE locations (
   id serial primary key,
   server_id numeric,
   iso varchar,
   name varchar,
   parent_region varchar,
   sort numeric,
   hostname varchar,
   enabled TIMESTAMP null,
   standard TIMESTAMP null,
   created TIMESTAMP not null default CURRENT_TIMESTAMP,
   deleted TIMESTAMP null
);

DROP TABLE IF EXISTS user_locations;
CREATE TABLE user_locations (
   id serial primary key,
   user_id numeric UNIQUE ,
   location_id numeric,
   external_cname varchar,
   hostname varchar,
   created TIMESTAMP not null default CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS vpns;
CREATE TABLE vpns (
    id serial primary key,
    name varchar,
    created TIMESTAMP not null default CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS devices;
CREATE TABLE devices (
  id serial primary key,
  name varchar,
  created TIMESTAMP not null default CURRENT_TIMESTAMP
);


DROP TABLE IF EXISTS vpn_user_certs;
CREATE TABLE vpn_user_certs (
    id serial primary key,
    vpn_id numeric,
    user_id numeric,
    private varchar,
    public varchar,
    ca varchar,
    static_key varchar,
    revoked TIMESTAMP null,
    created TIMESTAMP not null default CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS vpn_devices_configurations;
CREATE TABLE vpn_devices_configurations (
   id serial primary key,
   user_id numeric ,
   vpn_id numeric ,
   device_id numeric ,
   configuration varchar,
   created TIMESTAMP not null default CURRENT_TIMESTAMP,
   deleted TIMESTAMP null
);

CREATE UNIQUE INDEX idx_unique_configuration ON vpn_devices_configurations(user_id, vpn_id, device_id);

DROP TABLE IF EXISTS suppliers;
CREATE TABLE suppliers (
    id serial primary key,
    name varchar,
    id_external varchar,
    created TIMESTAMP not null default CURRENT_TIMESTAMP,
    deleted TIMESTAMP null
);

DROP TABLE IF EXISTS supplier_locations;
CREATE TABLE supplier_locations (
   id serial primary key,
   supplier_id numeric,
   location_id numeric,
   id_external varchar,
   created TIMESTAMP not null default CURRENT_TIMESTAMP,
   deleted TIMESTAMP null
);

DROP TABLE IF EXISTS servers;
CREATE TABLE servers (
   id serial primary key,
   user_id numeric, -- user who makes the creation request
   supplier_location_id numeric,
   id_external varchar,
   ipv4 varchar,
   deletion_protection TIMESTAMP null, -- if this instance can be deleted
   creation_request TIMESTAMP null,
   creation_completed TIMESTAMP null,
   creation_failed TIMESTAMP null,
   deletion_request TIMESTAMP null,
   deletion_completed TIMESTAMP null,
   deletion_failed TIMESTAMP null,
   created TIMESTAMP not null default CURRENT_TIMESTAMP,
   deleted TIMESTAMP null
);
ALTER SEQUENCE servers_id_seq RESTART WITH 1234;