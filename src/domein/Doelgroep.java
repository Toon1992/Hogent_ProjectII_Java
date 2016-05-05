/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
            @NamedQuery(name = "Doelgroep.findAll", query = "Select a FROM Doelgroep a")
        })
@Table(name = "Doelgroep")
public class Doelgroep
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DoelgroepId")
    private int doelgroepId;
    
     @Column(name = "Naam")
    private String naam;

    protected Doelgroep()
    {
    }
    @ManyToMany(mappedBy = "doelgroepen")
    Set<Materiaal> materialen;

    public Doelgroep(String naam)
    {
        this.naam = naam;
    }

    public String getNaam()
    {
        return naam;
    }

    private void setNaam(String naam)
    {
        this.naam = naam;
    }

    @Override
    public String toString()
    {
        return naam;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Doelgroep other = (Doelgroep) obj;
        if (!Objects.equals(this.naam, other.naam))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return this.naam.hashCode();
    }

}
