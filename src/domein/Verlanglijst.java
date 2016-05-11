/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author ToonDT
 */
@Entity
@Table(name = "Verlanglijst")
public class Verlanglijst
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private long id;
    
    @Column(name = "GebruikerEmail")
    private String GebruikerEmail;

    @ManyToMany(mappedBy = "verlanglijsten")
    private Set<Materiaal> materialen;
    
    protected Verlanglijst()
    {}
    
    public Verlanglijst(String email)
    {
        this.GebruikerEmail = email;
    }

    public String getGebruikerEmail()
    {
        return GebruikerEmail;
    }

    public void setGebruikerEmail(String GebruikerEmail)
    {
        this.GebruikerEmail = GebruikerEmail;
    }

    public Set<Materiaal> getMaterialen()
    {
        return materialen;
    }

    public void setMaterialen(Set<Materiaal> materialen)
    {
        this.materialen = materialen;
    }
    
    
}
