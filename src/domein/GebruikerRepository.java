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
public class GebruikerRepository {
    private Gebruiker gebruiker;
    public GebruikerRepository(){
        
    }
    public Gebruiker getGebruiker() {
        return gebruiker;
    }
    private void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }
    public void voegGebruikerToe(Gebruiker gebruiker){
        setGebruiker(gebruiker);
        Mapping.persistObject(gebruiker);
    }
}
