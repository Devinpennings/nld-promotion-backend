package data.jpa;

import data.interfaces.DAO;
import model.Model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Optional;

public abstract class ModelJPADAO<T extends Model> implements DAO<T> {
    private Class<T> entityClass;

    @PersistenceContext(name = "PU", unitName = "PU")
    protected EntityManager entityManager;

    public ModelJPADAO(){
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<T> get() {
        return entityManager.createQuery("Select t from " + this.entityClass.getSimpleName() + " t").getResultList();
    }

    @Override
    public Optional<T> get(String id) {
        T o = this.entityManager.find(this.entityClass, id);
        return Optional.ofNullable(o);
    }

    @Override
    public T add(T item) {
        this.entityManager.persist(item);
        return item;
    }

    @Override
    public Collection<T> add(Collection<T> items) {
        items.forEach(this::add);
        return items;
    }

    @Override
    public Optional<T> update(T item) {

        T existing = item.getId() == null ? null : this.entityManager.find(this.entityClass, item.getId());

        if (existing != null) {
            this.entityManager.merge(item);
        } else {
            this.entityManager.persist(item);
        }

        if (existing == null) {
            return Optional.of(item);
        }

        return Optional.empty();

    }

    @Override
    public void delete(T item) {
        this.entityManager.remove(item);
    }

}
