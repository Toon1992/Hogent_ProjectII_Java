/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Gebruiker;
import domein.GebruikerRepository;

/**
 *
 * @author donovandesmedt
 */
public class DomeinController {
    private GebruikerRepository gebruikerRepository;
    public DomeinController(){
        gebruikerRepository = new GebruikerRepository();
    }
    public void registreer(String email, String password){
        gebruikerRepository.voegGebruikerToe(new Gebruiker(email, password));
    }
}
