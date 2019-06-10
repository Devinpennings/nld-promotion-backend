package data.interfaces;

import java.util.Collection;
import java.util.Optional;

public interface DAO<T> {

        Collection<T> get();

        Optional<T> get(String id);

        T add(T item);

        Collection<T> add(Collection<T> items);

        Optional<T> update(T item);

        void delete(T item);

}
