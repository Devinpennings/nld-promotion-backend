package actions.mailing;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;

import javax.inject.Named;

@Named
public class MailService {

    public void send(MailConfiguration config, MailTemplate template, String... receivers) {

        try {

            ImageHtmlEmail email = new ImageHtmlEmail();
            email.setHostName(config.getHostname());
            email.setSmtpPort(config.getSmtpPort());
            email.setAuthenticator(new DefaultAuthenticator(config.getUsername(), config.getPassword()));
            email.setSSLOnConnect(config.usesSsl());
            email.setFrom(config.getEmail());

            email.setSubject(template.getSubject());
            email.setMsg("Please open this email in an HTML email client.");
            email.setHtmlMsg(template.getHtmlFormatted());
            for (String receiver : receivers) {
                email.addTo(receiver);
            }
            email.send();

        } catch (EmailException e) {
            e.printStackTrace();
        }

    }

}
