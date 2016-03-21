/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistentie.Mapping;

/**
 *
 * @author donovandesmedt
 */
public class BeheerderRepository {
    private Beheerder beheerder;
    public BeheerderRepository(){
        
    }

    public Beheerder getBeheerder() {
        return beheerder;
    }

    public void setBeheerder(Beheerder beheerder) {
        this.beheerder = beheerder;
    }
    
    public void voegGebruikerToe(Beheerder beheerder){
        setBeheerder(beheerder);
        Mapping.persistObject(beheerder);
    }
}
