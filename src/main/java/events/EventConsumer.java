package events;

import model.fluid.FluidModel;
import util.Callback;

public interface EventConsumer {

    void start();

    void stop();

    void listen(ConsumerConfiguration configuration, Callback<FluidModel> callback);

    void ignore(ConsumerConfiguration configuration);

    Class<? extends ConsumerConfiguration> getConfigurationType();

    boolean handlesConfiguration(ConsumerConfiguration configuration);

    String getId();

    String getTitle();

    String getDescription();

}
