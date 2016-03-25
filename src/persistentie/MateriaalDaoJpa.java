package persistentie;

import domein.Materiaal;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public class MateriaalDaoJpa extends GenericDaoJpa<Materiaal> implements MateriaalDao {
    public MateriaalDaoJpa() {
        super(Materiaal.class);
    }
}
