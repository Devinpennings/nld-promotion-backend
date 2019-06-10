package actions.mailing;

import util.properties.Property;

import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class MailConfiguration {

    @Inject
    @Property("mail.hostname")
    private String hostname;

    @Inject
    @Property("mail.smtp")
    private int smtpPort;

    @Inject
    @Property("mail.username")
    private String username;

    @Inject
    @Property("mail.password")
    private String password;

    @Inject
    @Property("mail.ssl")
    private boolean useSsl;

    @Inject
    @Property("mail.email")
    private String email;

    public String getHostname() {
        return this.hostname;
    }

    public int getSmtpPort() {
        return this.smtpPort;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean usesSsl() {
        return this.useSsl;
    }

    public String getEmail() {
        return this.email;
    }
}
