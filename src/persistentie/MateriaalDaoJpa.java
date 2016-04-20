package persistentie;

import domein.Materiaal;
import java.util.List;
import static persistentie.GenericDaoJpa.em;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public class MateriaalDaoJpa extends GenericDaoJpa<Materiaal> implements MateriaalDao {
    public MateriaalDaoJpa() {
        super(Materiaal.class);
    }
    
    public List<Materiaal> getMaterialen() {
        
            return em.createNamedQuery("Materiaal.findAll", Materiaal.class)
                    
                    .getResultList();
        
    }
}
