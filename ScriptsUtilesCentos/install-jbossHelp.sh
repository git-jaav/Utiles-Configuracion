$ sudo rpm -ihv jdk-8u91-linux-x64.rpm
$ java -version
java version "1.8.0_91"
Java(TM) SE Runtime Environment (build 1.8.0_91-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.91-b14, mixed mode)

$ javac -version
javac 1.8.0_91

$ sudo yum install unzip

$ wget http://download.jboss.org/jbossas/7.1/jboss-as-7.1.1.Final/jboss-as-7.1.1.Final.zip
$ sudo unzip jboss-as-7.1.1.Final.zip -d /opt/
$ cd /opt/
$ sudo ln -s jboss-as-7.1.1.Final/ jboss
$ sudo adduser jboss
$ sudo chown -R jboss. /opt/jboss-as-7.1.1.Final/ /opt/jboss

$ sudo su - jboss
$ cd /opt/jboss/bin
$ ./add-user.sh

What type of user do you wish to add?
 a) Management User (mgmt-users.properties)
 b) Application User (application-users.properties)
(a): a

Enter the details of the new user to add.
Realm (ManagementRealm) : [Enter]
Username : jboss
Password : [Password]
Re-enter Password : [Password]
About to add user 'jboss' for realm 'ManagementRealm'
Is this correct yes/no? yes
Added user 'jboss' to file '/opt/jboss-as-7.1.1.Final/standalone/configuration/mgmt-users.properties'
Added user 'jboss' to file '/opt/jboss-as-7.1.1.Final/domain/configuration/mgmt-users.properties'

$ ./standalone.sh -Djboss.bind.address=0.0.0.0 -Djboss.bind.address.management=0.0.0.0 &
http://<your ip address>:9990/
