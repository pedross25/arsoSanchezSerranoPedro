package estaciones.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import estaciones.repositorio.EntidadNoEncontrada;
import estaciones.repositorio.RepositorioException;
import estaciones.rest.dto.BicicletaDTO;
import estaciones.rest.dto.EstacionDTO;
import estaciones.servicio.IServicioEstaciones;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/estaciones")
@Tag(name = "Estaciones", description = "Aplicación de estaciones")
public class EstacionesController {

	private IServicioEstaciones servicio;

	@Autowired
	private PagedResourcesAssembler<EstacionDTO> pagedResourcesAssemblerEstacion;

	@Autowired
	private PagedResourcesAssembler<BicicletaDTO> pagedResourcesAssemblerBicicleta;

	@Autowired
	public EstacionesController(IServicioEstaciones servicio) {
		this.servicio = servicio;
	}


	// FUNCIONALIDAD GESTOR

	@PreAuthorize("hasAuthority('gestor')")
	@Operation(summary = "Dar de alta una estación", description = "Crea una nueva estación en el sistema")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Estación creada con éxito"),
			@ApiResponse(responseCode = "400", description = "Datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping
	public String altaEstacion(@Valid @RequestBody EstacionDTO estacionDTO) throws RepositorioException {
		return servicio.altaEstacion(estacionDTO.getNombre(), estacionDTO.getPuestos(), estacionDTO.getDireccion(),
				estacionDTO.getLatitud(), estacionDTO.getLongitud());
	}

	@PreAuthorize("hasAuthority('gestor')")
	@Operation(summary = "Dar de alta una bicicleta en una estación", description = "Agrega una nueva bicicleta a una estación específica")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Bicicleta agregada con éxito"),
			@ApiResponse(responseCode = "400", description = "Datos inválidos"),
			@ApiResponse(responseCode = "404", description = "Estación no encontrada"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping("/{idEstacion}/bicicletas")
	public String altaBicicleta(@PathVariable String idEstacion, @Valid @RequestBody BicicletaDTO bicicletaDTO)
			throws RepositorioException, EntidadNoEncontrada {
		return servicio.altaBicicleta(bicicletaDTO.getModelo(), idEstacion);
	}

	@PreAuthorize("hasAuthority('gestor')")
	@Operation(summary = "Dar de baja una bicicleta", description = "Marca una bicicleta como no disponible y especifica el motivo")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Bicicleta dada de baja con éxito"),
			@ApiResponse(responseCode = "404", description = "Bicicleta no encontrada"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PutMapping("/dardebaja/{idBicicleta}")
	public ResponseEntity<Void> bajaBicicleta(@PathVariable String idBicicleta, @RequestParam String motivo)
			throws RepositorioException, EntidadNoEncontrada {
		servicio.bajaBicicleta(idBicicleta, motivo);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAuthority('gestor')")
	@Operation(summary = "Listar bicicletas en una estación", description = "Obtiene una lista paginada de todas las bicicletas en una estación específica")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Listado de bicicletas obtenido con éxito"),
			@ApiResponse(responseCode = "404", description = "Estación no encontrada"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@GetMapping("/{idEstacion}/bicicletas")
	public PagedModel<EntityModel<BicicletaDTO>> listarBicicletasEstacion(@PathVariable String idEstacion,
																		  @Parameter(description = "Número de página") @RequestParam int page,
																		  @Parameter(description = "Tamaño de página") @RequestParam int size)
			throws RepositorioException, EntidadNoEncontrada {

		Pageable paginacion = PageRequest.of(page, size, Sort.by("modelo").ascending());
		Page<BicicletaDTO> resultado = this.servicio.getBicicletasEstacionPaginado(idEstacion, paginacion);
		return this.pagedResourcesAssemblerBicicleta.toModel(resultado);
	}





	// FUNCIONALIDAD USUARIO

	@PreAuthorize("hasAuthority('usuario')")
	@Operation(summary = "Obtener detalles de una estación", description = "Obtiene los detalles de una estación específica por su ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Detalles de la estación obtenidos con éxito"),
			@ApiResponse(responseCode = "404", description = "Estación no encontrada") })
	@GetMapping("/estacion/{idEstacion}")
	public ResponseEntity<Object> obtenerEstacion(@PathVariable String idEstacion) {
		System.out.println("REST ESTACIONES OBTENERESTACION");
        try {
			return ResponseEntity.ok(servicio.obtenerEstacion(idEstacion));
		} catch (EntidadNoEncontrada e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (RepositorioException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
		}
    }

	@PreAuthorize("hasAuthority('usuario')")
	@Operation(summary = "Listar estaciones", description = "Obtiene una lista paginada de todas las estaciones")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Listado de estaciones obtenido con éxito"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@GetMapping
	public PagedModel<EntityModel<EstacionDTO>> listarEstaciones(
			@Parameter(description = "Número de página") @RequestParam int page,
			@Parameter(description = "Tamaño de página") @RequestParam int size) throws Exception {

		Pageable paginacion = PageRequest.of(page, size, Sort.by("nombre").ascending());
		Page<EstacionDTO> resultado = this.servicio.listarEstaciones(paginacion);
		return this.pagedResourcesAssemblerEstacion.toModel(resultado);
	}

	@PreAuthorize("hasAuthority('usuario')")
	@Operation(summary = "Estacionar una bicicleta en una estación", description = "Asigna una bicicleta a una estación específica")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Bicicleta estacionada con éxito"),
			@ApiResponse(responseCode = "404", description = "Estación o bicicleta no encontrada"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping("/bicicletas/estacionar")
	public ResponseEntity<String> estacionarBicicleta(@RequestParam String idEstacion, @RequestParam String idBicicleta) {
		try {
			System.out.println("ESTACIONAR BICICLETA REST");
			servicio.estacionarBicicleta(idEstacion, idBicicleta);
			return ResponseEntity.ok("Bicicleta estacionada con éxito");
		} catch (EntidadNoEncontrada e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (RepositorioException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
		}
	}

	@PreAuthorize("hasAuthority('usuario')")
	@Operation(summary = "Listar bicicletas disponibles en una estación", description = "Obtiene una lista paginada de todas las bicicletas disponibles en una estación específica")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Listado de bicicletas disponibles obtenido con éxito"),
			@ApiResponse(responseCode = "404", description = "Estación no encontrada"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@GetMapping("/{idEstacion}/bicicletas/disponibles")
	public PagedModel<EntityModel<BicicletaDTO>> listarBicicletasDisponiblesEstacion(@PathVariable String idEstacion,
			@Parameter(description = "Número de página") @RequestParam int page,
			@Parameter(description = "Tamaño de página") @RequestParam int size)
			throws RepositorioException, EntidadNoEncontrada {

		Pageable paginacion = PageRequest.of(page, size, Sort.by("modelo").ascending());

		Page<BicicletaDTO> resultado = this.servicio.getBicicletasEstacionPaginado(idEstacion, paginacion);

		return this.pagedResourcesAssemblerBicicleta.toModel(resultado, bicicleta -> {
			EntityModel<BicicletaDTO> model = EntityModel.of(bicicleta);
			try {

				String urlDarDeBaja = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EstacionesController.class)
						.bajaBicicleta(bicicleta.getId(), "Escribir motivo")).toUri().toString();

				model.add(Link.of(urlDarDeBaja, "dar de baja"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return model;
		});

	}

}
