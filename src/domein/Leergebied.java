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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Thomas
 */
@Entity
@NamedQueries(
{
    @NamedQuery(name = "Leergebied.findAll", query = "Select a FROM Leergebied a")
})
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

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
    @Override
    public String toString(){
        return naam;
    }
    
}
