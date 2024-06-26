package alquileres.persistencia.jpa;

import repositorio.Identificable;
import repositorio.RepositorioException;
import repositorio.RepositorioString;

import java.util.List;

public interface IRepositorioJpaUsuarios<T extends Identificable> extends RepositorioString<T> {
    List<T> findByUsuarioId(String usuarioId);

    public void deleteByUsuarioId(String usuarioId) throws RepositorioException;
}