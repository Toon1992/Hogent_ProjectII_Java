/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    
    @OneToMany(mappedBy = "Verlanglijst")
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
