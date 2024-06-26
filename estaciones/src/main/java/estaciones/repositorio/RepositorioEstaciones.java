package estaciones.repositorio;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import estaciones.modelo.Estacion;

@NoRepositoryBean
public interface RepositorioEstaciones extends PagingAndSortingRepository<Estacion, String>{
    Estacion findByBicicletasId(String idBicicleta);

}
