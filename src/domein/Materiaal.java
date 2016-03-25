/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
/**
 *
 * @author Thomas
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Materiaal.findAll", query = "Select a FROM Materiaal a" )
})
public class Materiaal{
    @Id
    private int materiaalId;
    private String naam, plaats, foto, omschrijving;
    private int artikelNr, aantal, aantalOnbeschikbaar;
    private double prijs;
    private boolean uitleenbaar;
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Firma firma;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    Set<Doelgroep> doelgroepen = new HashSet<>();
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    Set<Leergebied> leergebieden = new HashSet<>();
    protected Materiaal()
    {
        
    }
    public String getNaam(){
        return naam;
    }

    public void setNaam(String naam) {
        if(naam.isEmpty()){
            throw new IllegalArgumentException("Naam moet ingevuld zijn!");
        }
        this.naam = naam;
    }

    public StringProperty naamProperty() {
        return new SimpleStringProperty(getNaam());
    }
    public void setPlaats(String plaats){
        this.plaats = plaats;
    }
    public String getPlaats(){
        return plaats;
    }
    public StringProperty plaatsProperty() {
        return new SimpleStringProperty(getPlaats());
    }
    public void setUitleenbaarheid(boolean uitLeenbaar){
        this.uitleenbaar = uitLeenbaar;
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
        setFoto(foto);
        setOmschrijving(omschrijving);
        setArtikelNr(artikelNr);
        setAantal(aantal);
        setAantalOnbeschikbaar(aantalOnbeschikbaar);
        setPrijs(prijs);
        setPlaats(plaats);
        setUitleenbaarheid(uitleenbaar);
        setFirma(firma);
        setDoelgroepen(doelgroepen);
        setLeergebieden(leergebieden);
        setNaam(naam);
        setMateriaalId(artikelNr);
    }

    public int getMateriaalId() {
        return materiaalId;
    }

    public void setMateriaalId(int materiaalId) {
        this.materiaalId = materiaalId;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public int getArtikelNr() {
        return artikelNr;
    }

    public void setArtikelNr(int artikelNr) {
        this.artikelNr = artikelNr;
    }

    public int getAantal() {
        return aantal;
    }

    public void setAantal(int aantal) {
        if(aantal<0){
            throw new IllegalArgumentException("Aantal moet groter zijn dan 0");
        }
        this.aantal = aantal;
    }

    public int getAantalOnbeschikbaar() {
        return aantalOnbeschikbaar;
    }

    public void setAantalOnbeschikbaar(int aantalOnbeschikbaar) {
        this.aantalOnbeschikbaar = aantalOnbeschikbaar;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public boolean isUitleenbaar() {
        return uitleenbaar;
    }


    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public Set<Doelgroep> getDoelgroepen() {
        return doelgroepen;
    }

    public void setDoelgroepen(Set<Doelgroep> doelgroepen) {
        this.doelgroepen = doelgroepen;
    }

    public Set<Leergebied> getLeergebieden() {
        return leergebieden;
    }

    public void setLeergebieden(Set<Leergebied> leergebieden) {
        this.leergebieden = leergebieden;
    }
}
