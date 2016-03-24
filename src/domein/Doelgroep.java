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
import javax.persistence.Table;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "Doelgroep")
public class Doelgroep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doelgroepId;
    private String naam;
    
    protected Doelgroep()
    {}
    
    public Doelgroep(String naam)
    {
        this.naam = naam;
    }

    public String getNaam() {
        return naam;
    }

    private void setNaam(String naam) {
        this.naam = naam;
    }
    @Override
    public String toString(){
        return naam;
    }
    
}
