# aws-weather-station #

Simple weather web service running under an Amazon Linux image on AWS EC2.
* Backend: Apache and Tomcat, Java Spring Boot, MongoDB(?)
* Frontend: JavaScript React + Redux, Bootstrap, D3.js(?), JWT Token(?)
* Weather data come from:
  * Local Raspberry Pi, coding with Python (https://www.raspberryweather.com/)
  * Finnish Meteorological Institute, open data: https://ilmatieteenlaitos.fi/avoin-data

### Setting up a development environment ###
* My dev env: Linux Debian 9 virtual machine
* Required tools:
  * OpenJDK 8 - Open Java Development Kit
  * Maven - Java software project management and comprehension tool
  * Node.js 8 and npm (https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions)
* Useful tools:  
  * Curl - Command line tool for transferring data with URL syntax
  * Browser LiveReload extension: http://livereload.com/extensions/
* Install:
  ```
  $ git clone git@github.com:galactux/aws-weather-station.git
  $ cd aws-weather-station/frontend/
  $ npm install
  $ cd ..
  ```
* Build:
  ```
  $ ./build.sh
  ```

### Setting up an AWS EC2 instance ###
* Create an Amazon Linux AMI 2017.03.1 virtual server
* Security Group, Inbound:
  ```
  Type              Protocol   Port Range
  HTTP              TCP        80
  Custom TCP Rule   TCP        8080
  SSH               TCP        22
  ```
* `$ ssh -i <identity_file> ec2-user@<ec2_public_dns>`
  ```
  $ sudo su
  # yum update
  # yum install httpd
  # yum list tomcat*
  # yum install tomcat8
  # yum install tomcat8-webapps tomcat8-admin-webapps tomcat8-docs-webapp tomcat8-javadoc
  # nano /usr/share/tomcat8/conf/tomcat-users.xml
  <user username="admin" password="********" roles="manager-gui,admin-gui"/>
  # service tomcat8 start
  # chkconfig tomcat8 on
  # nano /etc/httpd/conf.d/virtualhost.conf
  <VirtualHost *:80>
      ServerAdmin webmaster
      ServerName foo.example.com

      ProxyPreserveHost on
      ProxyRequests off
      ProxyPass        / http://localhost:8080/weather-station/
      ProxyPassReverse / http://localhost:8080/weather-station/
  </VirtualHost>
  # service httpd start
  # chkconfig httpd on
  # exit
  ```
* Upload and deploy WAR file (`backend/target/weather-station.war`)
  * http://<ec2_ipv4_public_ip>:8080/manager/html
