package events;

import model.fluid.FluidModel;

public interface ConsumerConfiguration {

    String getEventName();

    FluidModel getExpectedResponse();

}
