package actions.mailing;

import actions.ActionConfiguration;
import model.fluid.FluidModel;

public class MailingActionConfiguration implements ActionConfiguration {

    private FluidModel inputType;
    private String emailField;
    private MailConfiguration configuration;
    private MailTemplate template;

    public MailingActionConfiguration(FluidModel inputType, String emailField, MailConfiguration configuration, MailTemplate template) {
        this.inputType = inputType;
        this.emailField = emailField;
        this.configuration = configuration;
        this.template = template;
    }

    @Override
    public FluidModel getInputType() {
        return this.inputType;
    }

    public MailConfiguration getConfiguration() {
        return this.configuration;
    }

    public MailTemplate getTemplate() {
        return this.template;
    }

    public String getEmailField() {
        return this.emailField;
    }
}
