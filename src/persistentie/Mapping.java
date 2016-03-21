/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Gebruiker;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author donovandesmedt
 */
public class Mapping {
    private static final String PERSISTENCE_UNIT_NAME = "DidactischeLeermiddelenPU";
    private static EntityManager em;
    private static EntityManagerFactory emf;
    public static void openPersistentie(){
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
    }
    public static void closePersistentie(){
        em.close();
        emf.close();
    }
    public static <E> void persistObject(E object){
        openPersistentie();
        em.persist(object);
        em.getTransaction().commit();
        closePersistentie();
    }
}
