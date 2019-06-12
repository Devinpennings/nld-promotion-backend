package controller.rest;

import events.ConsumerConfiguration;
import service.TriggerService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/triggers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TriggersController {

    @Inject
    private TriggerService triggerService;

    @GET
    public Response get() {
        return Response.ok(this.triggerService.get()).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        Optional<? extends ConsumerConfiguration> found = this.triggerService.get(id);
        if (found.isPresent()) {
            return Response.ok(found.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/configurations")
    public Response getConfigurations() {
        return Response.ok(this.triggerService.getEventConsumers()).build();
    }

    @GET
    @Path("/{id}/actions")
    public Response getActionsForTrigger(@PathParam("id") String id) {
        return Response.ok(this.triggerService.getActionsForTrigger(id)).build();
    }

}
