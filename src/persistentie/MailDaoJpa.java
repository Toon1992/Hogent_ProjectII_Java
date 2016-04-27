/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.MailTemplate;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author manu
 */
public class MailDaoJpa extends GenericDaoJpa<MailTemplate> implements MailDao {

    public MailDaoJpa(){
        super(MailTemplate.class);
    }
    @Override
    public MailTemplate geefMail(String onderwerp) {
        try{
            return em.createNamedQuery("MailTemplate.findbyOnderwerp",MailTemplate.class).setParameter("onderwerp", onderwerp).getSingleResult();
        }catch(Exception ex){
            throw new EntityNotFoundException();
        }
        
    }
    
}
