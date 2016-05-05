package persistentie;

import domein.Materiaal;

import java.util.List;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public interface MateriaalDao extends GenericDao<Materiaal> {
    public List<Materiaal> getMaterialen();
}
