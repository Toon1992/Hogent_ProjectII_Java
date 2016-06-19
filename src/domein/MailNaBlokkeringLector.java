/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Entity;

/**
 *
 * @author manu
 */
@Entity
public class MailNaBlokkeringLector extends MailTemplate {
    
    protected MailNaBlokkeringLector(){
        
    }
    public MailNaBlokkeringLector(String onderwerp,String body){
        super(onderwerp, body);
    }
}