package controller.rest;

import actions.mailing.MailTemplate;
import dto.MailTemplateDTO;
import service.MailService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/mail")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class MailController {

    @Inject
    private MailService mailService;

    @GET
    @Path("/templates")
    public Response getTemplates() {
        return Response.ok(this.mailService.getTemplates()).build();
    }

    @GET
    @Path("/templates/{id}")
    public Response getTemplate(@PathParam("id") String id) {
        Optional<MailTemplate> found = this.mailService.get(id);
        if (found.isPresent()) {
            return Response.ok(found.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/templates")
    public Response postTemplates(MailTemplateDTO dto) {
        return Response.ok(this.mailService.addTemplate(dto)).build();
    }

}
