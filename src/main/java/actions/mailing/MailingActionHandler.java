package actions.mailing;

import actions.ActionHandler;
import model.fluid.FluidKeyValueModel;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotSupportedException;

@Singleton
public class MailingActionHandler implements ActionHandler<MailingActionConfiguration, FluidKeyValueModel> {

    @Inject
    private MailService service;

    @Override
    public void handle(MailingActionConfiguration configuration, FluidKeyValueModel value) {

        if (!configuration.getInputType().matches(value)) {
            throw new NotSupportedException("Given value is not supported for this configuration.");
        }

        try {
            configuration.getTemplate().seed(value.get());
            this.service.send(configuration.getConfiguration(), configuration.getTemplate(), (String) value.get(configuration.getEmailField(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean handlesAction(MailingActionConfiguration actionConfiguration) {
        return actionConfiguration.getClass() == MailingActionConfiguration.class;
    }

}
