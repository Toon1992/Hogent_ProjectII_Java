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
public abstract class  Beheerder {
    @Id
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
