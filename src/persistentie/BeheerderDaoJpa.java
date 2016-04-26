package persistentie;

import domein.Beheerder;
import java.util.List;
import repository.BeheerderRepository;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public class BeheerderDaoJpa extends GenericDaoJpa<Beheerder> implements BeheerderDao {
    public BeheerderDaoJpa() {
        super(Beheerder.class);
    }

    @Override
    public Beheerder getBeheerderByEmail(String email) throws EntityNotFoundException {
        try {
            return em.createNamedQuery("Beheerder.findByEmail", Beheerder.class)
                    .setParameter("Email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Beheerder> getAlleBeheerders() throws EntityNotFoundException
    {
       try
       {
           return em.createNamedQuery("Beheerder.FindAll",Beheerder.class).getResultList();
       }
       catch(NoResultException ex)
       {
           throw new EntityNotFoundException();
       }
    }

}
