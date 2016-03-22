/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.BeheerderRepository;
import domein.HoofdBeheerder;

/**
 *
 * @author donovandesmedt
 */
public class DomeinController {
    private BeheerderRepository beheerderRepository;
    public DomeinController(){
        beheerderRepository = new BeheerderRepository();
    }
    public void registreer(String email, String password, String naam){
        beheerderRepository.voegGebruikerToe(new HoofdBeheerder(email, password, naam));
        
    }
}
