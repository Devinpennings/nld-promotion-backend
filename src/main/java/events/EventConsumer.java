package events;

import model.fluid.FluidModel;
import util.Callback;

public interface EventConsumer {

    void start();

    void stop();

    void listen(EventConfiguration configuration, Callback<FluidModel> callback);

    void ignore(EventConfiguration configuration);

    boolean handlesConfiguration(EventConfiguration configuration);

}
