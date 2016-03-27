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
    private int gebruikerID;
    
    private String naam;
    
    protected Gebruiker(){}

    public Gebruiker(String naam)
    {
        setNaam(naam);
    }
    
    public StringProperty naamProperty()
    {
        return new SimpleStringProperty(getNaam());
    }
    
    public int getGebruikerID()
    {
        return gebruikerID;
    }

    public void setGebruikerID(int gebruikerID)
    {
        this.gebruikerID = gebruikerID;
    }

    public String getNaam()
    {
        return naam;
    }

    public void setNaam(String naam)
    {
        this.naam = naam;
    }
    
    
}
