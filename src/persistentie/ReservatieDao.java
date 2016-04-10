package persistentie;

import domein.Materiaal;
import domein.Reservatie;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * Created by donovandesmedt on 06/04/16.
 */
public interface ReservatieDao extends GenericDao<Reservatie> {
    public List<Reservatie> getReservaties(Date startDatum, Date eindDatum, Materiaal materiaal) throws EntityNotFoundException;
}
