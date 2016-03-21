/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Thomas
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class  Beheerder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gebruikersId;
    private String email, naam, wachtwoord;
    
    protected Beheerder(){}
    
    public Beheerder(String email, String naam, String wachtwoord)
    {
        this.email = email;
        this.naam = naam;
        this.wachtwoord = wachtwoord;
    }
    
    public void voegMateriaalToe(String foto, String naam, String omschrijving, String plaats, int artikelNr, int aantal, int aantalOnbeschikbaar, double prijs, boolean uitleenbaar, Firma firma, List<Doelgroep> doelgroepen, List<Leergebied> leergebieden)
    {
        Materiaal m = new Materiaal(foto, naam, omschrijving, plaats, artikelNr, aantal, aantalOnbeschikbaar, prijs, uitleenbaar, firma, doelgroepen, leergebieden);
        
    }
}
