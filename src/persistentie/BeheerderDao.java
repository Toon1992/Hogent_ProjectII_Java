package persistentie;

import domein.Beheerder;
import java.util.List;

import javax.persistence.EntityNotFoundException;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public interface BeheerderDao extends GenericDao<Beheerder> {
     Beheerder getBeheerderByEmail(String email, String wachtwoord) throws EntityNotFoundException;
     List<Beheerder> getAlleBeheerders() throws EntityNotFoundException;
     
}
