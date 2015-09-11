# jmeter-hmac
Hmac security for JMeter


Build
-----
mvn clean package

Deploy
------
cp target/hmac-1.0-SNAPSHOT.jar /usr/local/Cellar/jmeter/2.13/libexec/lib/ext/

mvn dependency:copy-dependencies
cp target/dependency/commons-codec-1.10.jar /usr/local/Cellar/jmeter/2.13/libexec/lib/ext/

Run
---
Use inside the JMeter Java Sampler. The result of this sampler is am HMAC that is written in a JMeter variable called hmac. You can use this variable to set HTTP/HTTPS request head.
