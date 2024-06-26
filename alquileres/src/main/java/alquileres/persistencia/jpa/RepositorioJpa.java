package alquileres.persistencia.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import repositorio.EntidadNoEncontrada;
import repositorio.Identificable;
import repositorio.RepositorioException;
import repositorio.RepositorioString;
import utils.Utils;

public class RepositorioJpa<T extends Identificable> implements RepositorioString<T> {
	protected Class<T> persistedClass;
	protected String name;

	public RepositorioJpa(Class<T> persistedClass) {
		this.persistedClass = persistedClass;
		name = persistedClass.getName().substring(persistedClass.getName().lastIndexOf(".") + 1);
	}

	@Override
	public String add(T entity) throws RepositorioException {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
			return entity.getId();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			throw new RepositorioException("Error al añadir la entidad " + entity.getClass().getSimpleName(), e);
		} finally {
			entityManager.close();
		}
	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
		EntityManager em = EntityManagerHelper.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new RepositorioException("Error al actualizar la entidad AlquilerEntidad", e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();

			T current = entity;
			if (!entityManager.contains(entity)) {
				current = entityManager.merge(entity);
			}

			entityManager.remove(current);

			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			throw new RepositorioException("Error al eliminar la entidad " + entity.getClass().getSimpleName(), e);
		} finally {
			entityManager.close();
		}
	}

//		EntityManager entityManager = EntityManagerHelper.getEntityManager();
//        EntityTransaction transaction = entityManager.getTransaction();
//        try {
//            transaction.begin();
//			entityManager.merge(entity);
////            if (!entityManager.contains(entity)) {
////                throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
////            }
//            entityManager.remove(entity);
//            transaction.commit();
//        } catch (RuntimeException e) {
//            if (transaction.isActive()) {
//                transaction.rollback();
//            }
//            throw e;
//        } finally {
//            entityManager.close();
//        }
	//}

	@Override
	public T getById(String id) throws RepositorioException, EntidadNoEncontrada {
		try {
			T instance = EntityManagerHelper.getEntityManager().find(persistedClass, id);
			if (instance != null) {
				EntityManagerHelper.getEntityManager().refresh(instance);
			} else
				throw new EntidadNoEncontrada(id + " no existe en el repositorio"); 
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<T> getAll() throws RepositorioException {
		try {
            final String queryString = " SELECT model from " + name + " model ";
            Query query = EntityManagerHelper.getEntityManager().createQuery(queryString);
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            return query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
	}

	@Override
    public List<String> getIds() throws RepositorioException {
        EntityManager entityManager = EntityManagerHelper.getEntityManager();
        try {
            String queryString = "SELECT model.id FROM " + persistedClass.getSimpleName() + " model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException e) {
            throw new RepositorioException("Error al obtener los IDs de las entidades", e);
        } finally {
            entityManager.close();
        }
    }

}
