package alquileres.rest;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import alquileres.persistencia.dto.BicicletaRequest;
import alquileres.persistencia.dto.EstacionRequest;
import alquileres.persistencia.dto.LiberarBloqueoRequest;
import alquileres.servicio.IServicioAlquiler;
import alquileres.servicio.ServicioAlquileresException;
import io.jsonwebtoken.Claims;
import repositorio.EntidadNoEncontrada;
import repositorio.OperacionNoPermitida;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

@Path("alquileres")
public class AlquilerControladorRest {

	private IServicioAlquiler servicio = FactoriaServicios.getServicio(IServicioAlquiler.class);

	@Context
	private UriInfo uriInfo;
	
	@Context
	private HttpServletRequest servletRequest;

    //    curl -X POST http://localhost:8080/alquileres/reservar \
    //            -H "Content-Type: application/json" \
    //            -H "Authorization: Bearer token" \
    //            -d '{"idBicicleta": "bicicleta123"}'
    @POST
    @Path("/reservar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("usuario")
    public Response reservar(BicicletaRequest idBicicleta) throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida {
        servicio.reservar(getIdUsuario(), idBicicleta.getIdBicicleta());
        return Response.status(Response.Status.CREATED).build();
    }

    //    curl -X POST http://localhost:8080/alquileres/confirmar \
    //            -H "Content-Type: application/json" \
    //            -H "Authorization: Bearer token"
    @POST
    @Path("/confirmar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("usuario")
    public Response confirmarReserva() throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida {
        servicio.confirmarReserva(getIdUsuario());
        return Response.status(Response.Status.OK).build();
    }

    //    curl -X POST http://localhost:8080/alquileres/alquilar \
    //            -H "Content-Type: application/json" \
    //            -H "Authorization: Bearer token" \
    //            -d '{"idBicicleta": "bicicleta456"}'
    @POST
    @Path("/alquilar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("usuario")
    public Response alquilar(BicicletaRequest bicicleta) throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida {
         servicio.alquilar(getIdUsuario(), bicicleta.getIdBicicleta());
        return Response.status(Response.Status.CREATED).build();
    }

    //    curl -X GET http://localhost:8080/alquileres/historial \
    //            -H "Authorization: Bearer token"
    @GET
    @Path("/historial")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("usuario")
    public Response historialUsuario() throws RepositorioException, EntidadNoEncontrada {
        return Response.ok(servicio.hitorialUsuario(getIdUsuario())).build();
    }

    //    curl -X POST http://localhost:8080/alquileres/dejar \
    //            -H "Content-Type: application/json" \
    //            -H "Authorization: Bearer token" \
    //            -d '{"idEstacion": "estacion789"}'
    @POST
    @Path("/dejar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("usuario")
    public Response dejarBicicleta(EstacionRequest estacion) throws RepositorioException, EntidadNoEncontrada, ServicioAlquileresException {
        servicio.dejarBicicleta(getIdUsuario(), estacion.getIdEstacion());
        return Response.status(Response.Status.OK).build();
    }

    //    curl -X POST http://localhost:8080/alquileres/liberar \
    //            -H "Content-Type: application/json" \
    //            -H "Authorization: Bearer token" \
    //            -d '{"idUsuario": "usuario789"}'
    @POST
    @Path("/liberar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("gestor")
    public Response liberarBloqueo(LiberarBloqueoRequest liberarRequest) throws RepositorioException, EntidadNoEncontrada {
        servicio.liberarBloqueo(liberarRequest.getIdUsuario());
        return Response.status(Response.Status.OK).build();
    }

    private String getIdUsuario() {
        Claims claims = (Claims) servletRequest.getAttribute("claims");
        return claims.get("sub", String.class);
    }
}
