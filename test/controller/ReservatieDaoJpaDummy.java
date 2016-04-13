import domein.Materiaal;
import domein.Reservatie;
import persistentie.ReservatieDao;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * Created by donovandesmedt on 09/04/16.
 */
public class ReservatieDaoJpaDummy implements ReservatieDao {
    @Override
    public List<Reservatie> getReservaties(Date startDatum, Date eindDatum, Materiaal materiaal) throws EntityNotFoundException {
        return null;
    }

    @Override
    public List<Reservatie> findAll() {
        return null;
    }

    @Override
    public Reservatie get(Long id) {
        return null;
    }

    @Override
    public Reservatie update(Reservatie object) {
        return null;
    }

    @Override
    public void delete(Reservatie object) {

    }

    @Override
    public void insert(Reservatie object) {

    }

    @Override
    public boolean exists(Long id) {
        return false;
    }
}
