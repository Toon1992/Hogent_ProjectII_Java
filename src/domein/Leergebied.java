/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Thomas
 */
@Entity
public class Leergebied {
    @Id
    private int leergebiedId;
    private String naam;
    
    protected Leergebied(){}
    
    public Leergebied(String naam)
    {
        this.naam = naam;
    }
}
