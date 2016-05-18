/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author ToonDT
 */
@Entity
@Table(name = "Gebruiker")
public class Gebruiker
{

    @OneToMany(mappedBy = "gebruiker")
    private Set<Reservatie> reservaties;
    @Id
    @Column(name = "Email")
    private String email;

    @Column(name = "Naam")
    private String naam;

    @Column(name = "Type")
    private String type;
    
    @Column(name ="Faculteit")
    private String Faculteit;



    protected Gebruiker()
    {
    }

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

    public StringProperty typeProperty()
    {
        return new SimpleStringProperty(getType().toLowerCase().equals("st") ? "Student" : "Lector");
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

    public String getType()
    {
        return type;
    }

    private String getTypeGebruiker()
    {
        switch (getType().toLowerCase())
        {
            case "st":
                return "Student";
            case "le":
                return "Lector";
        }
        return "";
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return String.format("%s - %s", naam, getTypeGebruiker());
    }

    public String getFaculteit()
    {
        return Faculteit;
    }

    public void setFaculteit(String Faculteit)
    {
        this.Faculteit = Faculteit;
    }

    
}
