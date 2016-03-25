package persistentie;

import domein.Beheerder;

import javax.persistence.EntityNotFoundException;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public interface BeheerderDao extends GenericDao<Beheerder> {
    public Beheerder getBeheerderByEmail(String email, String wachtwoord) throws EntityNotFoundException;
}
