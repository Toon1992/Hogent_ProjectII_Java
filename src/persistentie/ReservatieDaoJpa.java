/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Materiaal;
import domein.Reservatie;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ToonDT
 */
public class ReservatieDaoJpa extends GenericDaoJpa<Reservatie> implements ReservatieDao
{   
    public ReservatieDaoJpa()
    {
        super(Reservatie.class);
    }

    @Override
    public List<Reservatie> getReservaties(Date startDatum, Date eindDatum, Materiaal materiaal) throws EntityNotFoundException {
        try {
            return em.createNamedQuery("Reservatie.findBydatum", Reservatie.class)
                    .setParameter("StartDatum", startDatum)
                    .setParameter("EindDatum", eindDatum)
                    .setParameter("Materiaal", materiaal)
                    .getResultList();
        } catch (NoResultException ex) {
            throw new EntityNotFoundException();
        }
    }
}
