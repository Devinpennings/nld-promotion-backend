package actions;

import model.fluid.FluidModel;

public interface ActionHandler<C extends ActionConfiguration, V extends FluidModel> {

    void handle(C configuration, V value);

    boolean handlesAction(C actionConfiguration);

}
