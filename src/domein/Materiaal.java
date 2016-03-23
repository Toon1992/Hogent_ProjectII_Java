/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Thomas
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Materiaal.findAll", query = "Select a FROM Materiaal a" )
})
public class Materiaal implements Serializable{
    @Id
    private int materiaalId;
    private String naam, plaats, foto, omschrijving;
    private int artikelNr, aantal, aantalOnbeschikbaar;
    private double prijs;
    private boolean uitleenbaar;
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Firma firma;
    
    @ManyToMany
    Set<Doelgroep> doelgroepen = new HashSet<>();
    
    @ManyToMany
    Set<Leergebied> leergebieden = new HashSet<>();
    protected Materiaal()
    {
        
    }
    public String getNaam(){
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public StringProperty naamProperty() {
        return new SimpleStringProperty(getNaam());
    }
    public String getPlaats(){
        return plaats;
    }
    public StringProperty plaatsProperty() {
        return new SimpleStringProperty(getPlaats());
    }
    public Boolean getUitleenbaarheid(){
        return uitleenbaar;
    }
    public StringProperty uitleenbaarProperty() {
        if(uitleenbaar){
            return new SimpleStringProperty("Student");
        }
        else{
            return new SimpleStringProperty("Lector");
        }
    }
    public Materiaal(String foto, String naam, String omschrijving, String plaats, int artikelNr, int aantal, int aantalOnbeschikbaar, double prijs, boolean uitleenbaar, Firma firma, Set<Doelgroep> doelgroepen, Set<Leergebied> leergebieden)
    {
        this.foto = foto;
        this.omschrijving = omschrijving;
        this.plaats = plaats;
        this.artikelNr = artikelNr;
        this.aantalOnbeschikbaar = aantalOnbeschikbaar;
        this.prijs = prijs;
        this.uitleenbaar = uitleenbaar;
        this.firma = firma;
        this.doelgroepen = doelgroepen;
        this.leergebieden = leergebieden;
        this.naam = naam;
        this.aantal = aantal;
    }
}
