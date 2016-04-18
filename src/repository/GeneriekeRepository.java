package repository;

import domein.Materiaal;
import javafx.collections.transformation.FilteredList;
import persistentie.GenericDaoJpa;

/**
 * Created by donovandesmedt on 17/04/16.
 */
public class GeneriekeRepository {
    private GenericDaoJpa<Object> objectDao;
    public GeneriekeRepository(){
        objectDao = new GenericDaoJpa<>(Object.class);
    }
    public <E> void saveObject(E element){
        objectDao.startTransaction();
        objectDao.insert(element);
        objectDao.commitTransaction();
    }

    public <E> void wijzigObject(E element)
    {
        objectDao.startTransaction();
        objectDao.update(element);
        objectDao.commitTransaction();
    }

    public <E> void verwijderObject(E element) {
        objectDao.startTransaction();
        objectDao.delete(element);
        objectDao.commitTransaction();
    }
}
