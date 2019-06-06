package events;

import model.fluid.FluidModel;

public interface EventConfiguration {

    String getEventName();

    FluidModel getExpectedResponse();

}
