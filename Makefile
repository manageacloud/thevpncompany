#
# Start tomcat
# ENV=prod JAVA_HOME=/opt/jdk-12/ /opt/apache-tomcat-9.0.21/bin/startup.sh
#
requirements:
	apt-get install -y expect

build:
	mvn clean package -DskipTests

#  Hot deployment example
deploy: build
	rsync -uz --progress target/vpn-0.0.1-SNAPSHOT.jar root@thevpncompany.com.au:/var/thevpncompany/
	ssh root@thevpncompany.com.au -C "systemctl restart thevpncompany"

make run: build
	java -Djava.security.egd=file:/dev/./urandom -jar target/vpn-0.0.1-SNAPSHOT.jar
		
