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
@Table(name = "Leergebied")
public class Leergebied {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leergebiedId;
    private String naam;
    
    protected Leergebied(){}
    
    public Leergebied(String naam)
    {
        this.naam = naam;
    }
}
