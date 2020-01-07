# Introduction

  [TheVPNCompany](https://thevpncompany.com.au) provides an end to end solution to offer paid VPN Services.   
    
The objective is to increase the transparency on how the VPN Industry operates. [Users cannot trust the majority of the existing companies that offers VPN Services](https://www.top10vpn.com/free-vpn-app-investigation/). Unfortunately not all people has technical knowledge to be able to create, install and maintain their own VPN Infrastructure. Creating an end to end Open Source solution expects to increase the transparency on the VPN Market, allowing a new generation of open source and fully transparent VPN Companies.
    

This Open Source solution includes:   
    

- Frontend Website   
- API   
- Server Configurations   
- Orchestration Systems   
- Monitoring systems   
    
    

## Installation
    

 The installation process includes the following services.   
    

- `Frontend Services`, which are mandatory if you want to provide a website where users can sign up.   
 - You need to provide API keys for [DigitalOcean](https://digitalocean.com/) to manage the domains for the clients, or you can implement another provider using the interface of `DigitalOceanGateway.java` file.   
 - If you want to offer paid subscriptions, you need an API key from [Pinpayments](https://pinpayments.com/). Alternatively you can implement a new payment gateway using the interface of `PinNetGateway.java`    
 - If you want to be able to automatically create VPN end points, you need to configure [ManageaCloud](https://manageacloud.com/) or modify the script `consumer_orchestration.py` implementing the interface of `macapi.py`. If you decide to use Manageacloud to run VPN Services, please send an email to ruben at manageacloud dot com to waive all fees for your Manageacloud account.   
 - If you want to offer free subscriptions with a limited data usage, and you want the system to automatically disconnect all users that have use all data allowance, you need to implement the `Monitoring Systems` using [Zabbix](https://www.zabbix.com/).   
    

If you have question, problems or do you need additional help please create a ticket or drop an email to ruben at manageacloud dot com.   
    
    

### Frontend Services

`Frontend Services` includes the `Website`, `API` and the `Database`.   
    

1 - Sign up in the following third party services and configure `secrets.properties` accordingly.   
    

- DigitalOcean DNS. Please check the documentation to create an [API Token in Digital Ocean](https://www.digitalocean.com/docs/api/create-personal-access-token/).   
- [Pinpayments](https://pinpayments.com/) API keys. You will need to create tokens for any paid subscription that you want to offer.   
    

2 - Configure the database   
    

- Install Postgres. In Debian based systems you can execute `apt-get install postgres` - Create a database named `vpn` - Execute the files `resources/vpn-db/vpn-schema.sql` and `resources/vpn-db/vpn-data.sql` - Ensure that the user `postgres` can access via `127.0.0.1` to `vpn` database [without password](https://gist.github.com/groveriffic/4122225), or customize the credentials at `resources/vpn-db/db-config-vpn.properties`    

 3 - Compile and run the project.   
    
- Execute `make build` in the root of the project to generate the file `target/vpn-0.0.1-SNAPSHOT.jar`.   
    
- Execute `make run` to run the application at `http://localhost:8111`    
 
4 - You can configure system.d using the service file `scripts/systemd/vpn_frontend.service`    

5 - `Frontend Services` will send jobs to `queues`. It expects a [Beanstalkd](https://beanstalkd.github.io/) server listening in `localhost`      

### OpenVPN   

 The VPN Service that operates the VPN tunnel with the client's devices using OpenVPN.   
    

If you are using bash, change directory to   
``` cd vpn_server/openvpn ```    

 1 - Create the OpenVPN private certificate that will be operating your whole infrastructure.   
You will be using this certificate. You will need to create a password for the CA (aka CA_PASSWORD), which is needed to be able to create the `public certificates` when new users signs up.   
``` OPENVPN_SERVER_NAME=my.server.com make new-server-configuration ``` 

2 - This will generate the configuration files for the OpenVPN server at `/tmp/etc_openvpn`. Those configuration files needs to be accessible when running a OpenVPN server.    

3 - Install docker compose   
``` make install-docker-compose ```    

 4 - Run docker compose to run the OpenVPN Server (by default the server configuration is expected at `/tmp/etc_openvpn`)   
``` make run ```    

 5 - You should find a way a way to orchestrate automatically the OpenVPN servers. The files located at `/tmp/etc_openvpn` needs to be deployed as well on every new OpenVPN Server. For example:      

- Push `/etc/etc_openvpn` to a private repository in BitBucket   
- Create a ssh public/private key and add the [public key as a deployment key to Bitbucket private repository](https://confluence.atlassian.com/bitbucket/use-access-keys-294486051.html).   
``` ssh-keygen -N "" -f keys_openvpn_bitbucket ```    
- To orchestrate automatically a [OpenVPN Server](https://manageacloud.com/configuration/openvpn_28581) using [ManageaCloud-cli](https://manageacloud.com/article/orchestration/cli), you just need to execute the following command   
``` 
  mac instance create -c "openvpn_28581" -e "PRIVATE_KEY=\"`cat keys_openvpn_bitbucket`\"" "REPOSITORY=git@bitbucket.org:path_to/my_repository.git"  -l sgp1
 ```

 ### Queue 
There are two consumers for the queue, a process that creates the `public keys` for OpenVPN when new users signs up,  and the process that automatically creates new VPN end point servers in the cloud.      

If you are using bash, change directory to   
``` cd scripts ```    

 1 - The consumers are expecting jobs from [beanstalkd](https://beanstalkd.github.io/). In Debian based systems, you can install it executing `apt-get install beanstalkd`. The default configuration expects beanstalk in the same server as the application server.   
 
 2 - Prepare python environment `make rebuild`    
 
 3 - Start up the process that manages the user sign up    
 
- Generate the OpenVPN Certificate. You will need to assign a password for the private certificate generated in the OpenVPN section (CA_PASSWORD)   
- Run the script that process the public certificate of new sign ups   
``` ENV=dev CA_PASSWORD=private_cert_password make openvpn-create-cert-run ```    

 4 - Automate the Orchestration process, to create the OpenVPN Servers automatically when needed - this is only required if  you want a dynamic VPN Server Infrastructure which grows or it is reduced  automatically depending on the traffic conditions. If you  want to operate with a fixed number of VPN Servers, this is not required.   
    
- Sign up at [Manageacloud](https://manageacloud.com) (or rewrite the script that manages the orchestration).   
Configure the credentials for Digital Ocean and set it as default supplier.   
- Create your [ManageaCloud API Key](https://manageacloud.com/article/orchestration/api/authentication)    
- Run the server orchestration script   
``` ENV=dev MANAGEACLOUD_AUTH=user:api_key TVC_API_KEY=1234 make orchestration-run ```    

5 - Rather than using the development scripts provided by this repository, you can install the latest production version via [PyPI](https://pypi.org/project/thevpncompany/)    
```    
pip3 install thevpncompany    
```   
    

 ### Monitoring System   

 Install a monitoring system for all the VPN Servers. Additionally, you can configure the monitoring system   
to:   

- Grow or reduce the infrastructure automatically depending on the traffic conditions   
- Restrict access to users the that which are using the free plan and they reach the allowed monthly transfer limit   
- Monitor the monthly bandwidth usage   
    

1 - [Install zabbix server](https://www.zabbix.com/documentation/4.4/manual/installation/install_from_packages/debian_ubuntu) with postgres.   
    

- For Debian based systems, you can use the following command   
    

``` apt-get install -y zabbix-server-pgsql zabbix-frontend-php ```    

 - All the VPN Servers needs to install a zabbix client, for Debian based systems, you can execute the following command:   
``` apt-get install -y zabbix-agent ```    

 2 - Configure zabbix-client in all the servers that you want to be monitored (VPN Servers).   
- For Debian based systems, the file is located at `/etc/zabbix/zabbix_agentd.conf`. You can find a template at `scripts/zabbix/zabbix_agentd.conf` - Append the configuration to Zabbix Agent that allows to monitor OpenVPN. In Debian based systems, copy the files `openvpn.conf` and `vpn.conf` located at `scripts/zabbix` to the server path `/etc/zabbix/zabbix_agentd.d/`   

 3 - Import the file `scripts/zabbix.zbx_export_thevpncompany.xml` Zabbix Template to a Zabbix Server and [link it to the VPN Servers Host](https://www.zabbix.com/documentation/4.4/manual/config/templates/linking) configuration   
    

4 - Add the new client hosts.   
    

- Manually using [Zabbix Frontend](https://www.zabbix.com/documentation/3.4/manual/quickstart/host)    
    
- Automatically via Zabbix API executing from the VPN Server the script `scripts/zabbix /register_host_to_zabbix.sh`    
    

 # Support   
 If you have question, problems or do you need additional help please create a ticket or drop an email to ruben at manageacloud dot com.

