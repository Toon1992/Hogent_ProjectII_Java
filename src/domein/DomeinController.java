/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;

/**
 *
 * @author Thomas
 */
public class DomeinController {
    private Beheerder beheerder;
    private BeheerderRepository beheerderRepository;
    public DomeinController(){
        setBeheerderRepository(new BeheerderRepository());
    }
    public void setBeheerderRepository(BeheerderRepository beheerderRepository){
        this.beheerderRepository = beheerderRepository;
    }
    public void registreer(String email, String password, String naam){
        beheerderRepository.voegGebruikerToe(new HoofdBeheerder(email, password, naam));
    }
    public void login(String email, String wachtwoord) throws Exception{
        beheerderRepository.login(email, wachtwoord);
    }
    public void voegMateriaalToe(String foto, String naam, String omschrijving, String plaats, int artikelNr, int aantal, int aantalOnbeschikbaar, double prijs, boolean uitleenbaar, Firma firma, List<Doelgroep> doelgroepen, List<Leergebied> leergebieden)
    {
        beheerder.voegMateriaalToe(foto, naam, omschrijving, plaats, artikelNr, aantal, aantalOnbeschikbaar, prijs, uitleenbaar, firma, doelgroepen, leergebieden);
    }
}
