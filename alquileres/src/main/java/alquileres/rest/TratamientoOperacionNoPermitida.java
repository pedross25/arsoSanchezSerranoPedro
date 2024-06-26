package alquileres.rest;

import repositorio.OperacionNoPermitida;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TratamientoOperacionNoPermitida implements ExceptionMapper<OperacionNoPermitida> {

    @Override
    public Response toResponse(OperacionNoPermitida arg0) {

        return Response.status(Response.Status.FORBIDDEN).entity(arg0.getMessage()).build();
    }
}