package actions.mailing;

public class MailConfiguration {

    private String hostname;
    private int smtpPort;
    private String username;
    private String password;
    private boolean useSsl;
    private String email;

    public MailConfiguration(String hostname, int smtpPort, String username, String password, boolean useSsl, String email) {
        this.hostname = hostname;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;
        this.useSsl = useSsl;
        this.email = email;
    }

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
