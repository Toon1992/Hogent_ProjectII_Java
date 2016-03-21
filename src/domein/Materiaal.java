/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Thomas
 */
@Entity
public class Materiaal {
    @Id
    private int materiaalId;
    private String foto, naam, omschrijving, plaats;
    private int artikelNr, aantal, aantalOnbeschikbaar;
    private double prijs;
    private boolean uitleenbaar;
    private Firma firma;
    private List<Doelgroep> doelgroepen;
    private List<Leergebied> leergebieden;
    
    protected Materiaal()
    {
        
    }
    
    public Materiaal(String foto, String naam, String omschrijving, String plaats, int artikelNr, int aantal, int aantalOnbeschikbaar, double prijs, boolean uitleenbaar, Firma firma, List<Doelgroep> doelgroepen, List<Leergebied> leergebieden)
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
