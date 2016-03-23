/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Thomas
 */
@Entity
public class Firma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int firmaId;
    private String naam, emailContact;
    
    protected Firma()
    {}
    
    public Firma(String naam, String emailContact)
    {
        this.naam = naam;
        this.emailContact = emailContact;
    }
}
