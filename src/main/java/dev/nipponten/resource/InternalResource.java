package dev.nipponten.resource;

import dev.nipponten.application.requests.InternalRequest;
import dev.nipponten.application.responses.InternalResponse;
import dev.nipponten.application.responses.InternalResponseMapper;
import dev.nipponten.application.services.InternalService;
import dev.nipponten.domain.models.Internal;
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

@Path("/internal")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InternalResource {

    @Inject InternalService service;

    @Inject InternalResponseMapper mapper;

    @POST
    public Response create(InternalRequest request) {
        Internal saved =
                service.create(
                        new Internal(
                                null,
                                request.userId(),
                                request.internalRoleId(),
                                request.name(),
                                request.lastName(),
                                request.cpf()));
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(saved)).build();
    }

    @GET
    @Path("/{id}")
    public InternalResponse getById(@PathParam("id") Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @GET
    public List<InternalResponse> getAll() {
        return service.getAll().stream().map(mapper::toResponse).toList();
    }

    @PUT
    @Path("/{id}")
    public InternalResponse update(@PathParam("id") Long id, InternalRequest request) {
        Internal updated =
                service.update(
                        id,
                        new Internal(
                                id,
                                request.userId(),
                                request.internalRoleId(),
                                request.name(),
                                request.lastName(),
                                request.cpf()));
        return mapper.toResponse(updated);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
