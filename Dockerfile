FROM jboss/wildfly
ADD target/mail-service-1.0.war /opt/jboss/wildfly/standalone/deployments/