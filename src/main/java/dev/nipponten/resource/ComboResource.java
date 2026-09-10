package dev.nipponten.resource;

import dev.nipponten.application.requests.ComboRequest;
import dev.nipponten.application.responses.ComboResponse;
import dev.nipponten.application.responses.ComboResponseMapper;
import dev.nipponten.application.services.ComboService;
import dev.nipponten.domain.models.Combo;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/combos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ComboResource {

    @Inject ComboService service;

    @Inject ComboResponseMapper mapper;

    @POST
    public Response create(ComboRequest request) {
        Combo saved =
                service.create(
                        new Combo(
                                null,
                                request.name(),
                                request.price(),
                                request.imageUrl(),
                                request.description(),
                                request.status()));
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(saved)).build();
    }

    @GET
    @Path("/{id}")
    public ComboResponse getById(@PathParam("id") Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @GET
    public List<ComboResponse> getAll() {
        return service.getAll().stream().map(mapper::toResponse).toList();
    }

    @PUT
    @Path("/{id}")
    public ComboResponse update(@PathParam("id") Long id, ComboRequest request) {
        Combo updated =
                service.update(
                        id,
                        new Combo(
                                id,
                                request.name(),
                                request.price(),
                                request.imageUrl(),
                                request.description(),
                                request.status()));
        return mapper.toResponse(updated);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
