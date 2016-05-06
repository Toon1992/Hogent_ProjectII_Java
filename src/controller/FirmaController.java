/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Firma;
import java.util.List;
import repository.FirmaRepository;

/**
 *
 * @author ToonDT
 */
public class FirmaController
{
    private FirmaRepository repos;
    
    public FirmaController()
    {
        repos = new FirmaRepository();
    }
    
    public List<String> geefAlleFirmas()
    {
        return repos.geefAlleFirmas();
    }
    
    public Firma geefFirma(String naam)
    {
        return repos.geefFirma(naam);
    }
    
     public void voegFirmaToe(String firma, String email)
     {
         repos.voegFirmaToe(firma, email);
     }
     
     public void deleteFirma(Firma firma)
     {
         repos.deleteFirma(firma);
     }
     
}
