/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
    @ManyToMany(mappedBy = "leergebieden")
    Set<Materiaal> materialen;
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

    @Override
    public int hashCode() {
//        int hash = 7;
//        hash = 71 * hash + Objects.hashCode(this.naam);
//        return hash;
        return this.naam.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Leergebied other = (Leergebied) obj;
        if (!Objects.equals(this.naam, other.naam)) {
            return false;
        }
        return true;
    }
}
