package actions.mailing;

import actions.ActionConfiguration;
import model.fluid.FluidKeyValueModel;
import model.fluid.FluidModel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class MailingActionConfiguration extends ActionConfiguration {

    private static final String title = "Mail vesturen";
    private static final String description = "Deze actie verstuurt een mailtje.";
    private static final String type = "mailing";

    @OneToOne
    private FluidKeyValueModel inputType;

    private String emailField;

    @OneToOne(cascade = CascadeType.ALL)
    private MailTemplate template;

    public MailingActionConfiguration(FluidModel inputType, String emailField, MailTemplate template) {
        super(true);
        this.inputType = (FluidKeyValueModel) inputType;
        this.emailField = emailField;
        this.template = template;
    }

    public MailingActionConfiguration() {
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public FluidKeyValueModel getInputType() {
        return this.inputType;
    }

    public MailTemplate getTemplate() {
        return this.template;
    }

    public String getEmailField() {
        return this.emailField;
    }
}
