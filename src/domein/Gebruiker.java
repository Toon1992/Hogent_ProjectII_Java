/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author ToonDT
 */
@Entity
public class Gebruiker
{

    @OneToOne(mappedBy = "gebruiker")
    private Reservatie reservatie;
    @Id
    private String email;
    
    private String naam;
    private String type;
    
    protected Gebruiker(){}

    public Gebruiker(String naam, String email, String type)
    {
        setNaam(naam);
        setEmail(email);
        setType(type);
    }
    
    public StringProperty naamProperty()
    {
        return new SimpleStringProperty(getNaam());
    }
    
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getNaam()
    {
        return naam;
    }

    public void setNaam(String naam)
    {
        this.naam = naam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
}
