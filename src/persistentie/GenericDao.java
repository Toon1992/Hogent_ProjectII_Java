package persistentie;

import java.util.List;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public interface GenericDao<T> {
    public List<T> findAll();
    public T get(Long id);
    public T update(T object);
    public void delete(T object);
    public void insert(T object);
    public boolean exists(Long id);
}
