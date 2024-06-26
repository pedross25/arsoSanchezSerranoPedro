package alquileres.persistencia;

import alquileres.persistencia.jpa.EntityManagerHelper;
import alquileres.persistencia.jpa.IRepositorioJpaUsuarios;
import alquileres.persistencia.jpa.RepositorioJpa;
import repositorio.Identificable;
import repositorio.RepositorioException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class RepositorioJpaUsuarios<T extends Identificable> extends RepositorioJpa<T> implements IRepositorioJpaUsuarios<T> {

    public RepositorioJpaUsuarios(Class<T> persistedClass) {
        super(persistedClass);
    }

    public List<T> findByUsuarioId(String usuarioId) {
        EntityManager entityManager = EntityManagerHelper.getEntityManager();
        try {
            String queryString = "SELECT model FROM " + persistedClass.getSimpleName() + " model WHERE model.usuarioId = :usuarioId";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("usuarioId", usuarioId);

            @SuppressWarnings("unchecked")
            List<T> resultList = query.getResultList();
            return resultList;
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public void deleteByUsuarioId(String usuarioId) throws RepositorioException {
        EntityManager entityManager = EntityManagerHelper.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            String queryString = "DELETE FROM " + persistedClass.getSimpleName() + " model WHERE model.usuarioId = :usuarioId";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("usuarioId", usuarioId);
            int deletedCount = query.executeUpdate();
            transaction.commit();
            System.out.println("Se eliminaron " + deletedCount + " registros para el usuario con ID: " + usuarioId);
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RepositorioException("Error al eliminar entidades por usuarioId", e);
        } finally {
            entityManager.close();
        }
    }
}
