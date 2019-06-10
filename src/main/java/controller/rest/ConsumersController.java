package controller.rest;

import events.EventConsumer;
import events.EventService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@Path("/consumers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ConsumersController {

    @Inject
    private EventService eventService;

    @GET
    public Response get() {

        Set<String> names = new HashSet<>();
        for (Class<? extends EventConsumer> consumer : this.eventService.scanEventConsumers()) {
            names.add(consumer.toString());
        }
        return Response.ok(names).build();

    }

}
