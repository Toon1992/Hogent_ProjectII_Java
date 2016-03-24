/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import exceptions.EmailException;
import exceptions.WachtwoordException;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import persistentie.Mapping;

/**
 *
 * @author donovandesmedt
 */
public class BeheerderRepository{
    private Beheerder beheerder;
    public BeheerderRepository(){
        
    }
    


    public Beheerder getBeheerder() {
        return beheerder;
    }

    public void setBeheerder(Beheerder beheerder) {
        this.beheerder = beheerder;
    }

    public void login(String email, String wachtwoord){
        if(!Pattern.matches("\\w+(\\.\\w*)*@\\w+\\.\\w+(\\.\\w+)*", email)){
            throw new EmailException("Ongeldige email");
        }
        if(wachtwoord.isEmpty()){
            throw new WachtwoordException("Wachtwoord verplicht");
        }
        beheerder = Mapping.loginQuery(email, wachtwoord);
    }
    public void voegGebruikerToe(Beheerder beheerder){
        setBeheerder(beheerder);
        Mapping.persistObject(beheerder);
    }
}
