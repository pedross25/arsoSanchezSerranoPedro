package alquileres.servicio;

import retrofit2.Response;

public class ServicioEstaciones implements IServicioEstaciones {

	EstacionesRestClient service = RetrofitClient.getRetrofitInstance().create(EstacionesRestClient.class);

	@Override
	public boolean isHuecoDisponible(String idEstacion) throws ServicioAlquileresException {
		Response<EstacionResumen> resultado;
		try {
			resultado = service.getEstacion(idEstacion).execute();
			if (resultado.isSuccessful()) {
				return resultado.body().isHuecosLibres();
			} else {
				String errorBody = resultado.errorBody().string();
				throw new ServicioAlquileresException(errorBody);
			}
		} catch (Exception e) {
			throw new ServicioAlquileresException(e.getMessage());
		}
	}

	@Override
	public boolean situarBicicleta(String idEstacion, String idBicicleta) throws ServicioAlquileresException {
		try {
			Response<Void> resultado = service.situarBicicleta(idEstacion, idBicicleta).execute();

			System.out.println(resultado);

			return resultado.isSuccessful();
		} catch (Exception e) {
			throw new ServicioAlquileresException("Ha habido un problema al comunicar con el servicio estaciones");
		}

	}

}
