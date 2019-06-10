package actions.mailing;

import actions.ActionConfiguration;
import model.fluid.FluidModel;

import javax.inject.Inject;

public class MailingActionConfiguration implements ActionConfiguration {

    private FluidModel inputType;
    private String emailField;
    private MailTemplate template;

    public MailingActionConfiguration(FluidModel inputType, String emailField, MailTemplate template) {
        this.inputType = inputType;
        this.emailField = emailField;
        this.template = template;
    }

    @Override
    public FluidModel getInputType() {
        return this.inputType;
    }

    public MailTemplate getTemplate() {
        return this.template;
    }

    public String getEmailField() {
        return this.emailField;
    }
}
