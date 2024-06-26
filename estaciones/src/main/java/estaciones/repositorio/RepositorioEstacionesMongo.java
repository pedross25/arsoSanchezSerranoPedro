package estaciones.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;

import estaciones.modelo.Estacion;
import org.springframework.data.mongodb.repository.Query;

public interface RepositorioEstacionesMongo extends RepositorioEstaciones, MongoRepository<Estacion, String> {

    @Query(value = "{ 'bicicletas._id' : ?0 }")
    Estacion findByBicicletaId(String idBicicleta);


}
