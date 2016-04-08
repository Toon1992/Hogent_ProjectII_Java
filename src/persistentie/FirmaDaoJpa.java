/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Firma;
import javax.persistence.NoResultException;

/**
 *
 * @author manu
 */
public class FirmaDaoJpa extends GenericDaoJpa<Firma> implements FirmaDao {
    
    public FirmaDaoJpa() {
        super(Firma.class);
    }

    @Override
    public Firma geefFirma(String naam) {
        try{
            return em.createNamedQuery("Firma.findByName",Firma.class).setParameter("Naam", naam).getSingleResult();
            
        }catch(NoResultException ex){
            throw new IllegalArgumentException("Firma is niet gevonden");
        }
    }
    
    
    
}
