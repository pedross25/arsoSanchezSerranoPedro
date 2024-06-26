package alquileres.servicio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EstacionesRestClient {

	@GET("estacion/{idEstacion}/")
	Call<EstacionResumen> getEstacion(@Path("idEstacion") String idEstacion);

	@POST("bicicletas/estacionar/")
	Call<Void> situarBicicleta(@Query("idEstacion") String idEstacion, @Query("idBicicleta") String idBicicleta);

}
