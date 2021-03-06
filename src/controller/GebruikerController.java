/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Observable;

import domein.*;
import javafx.collections.transformation.SortedList;
import repository.BeheerderRepository;

/**
 *
 * @author Thomas
 */
public class GebruikerController extends Observable{
    private Beheerder beheerder;
    private BeheerderRepository beheerderRepository;
    public GebruikerController(){
        setBeheerderRepository(new BeheerderRepository());
    }
    public void setBeheerderRepository(BeheerderRepository beheerderRepository){
        this.beheerderRepository = beheerderRepository;
    }
    public void registreer(String email, String password, String naam, boolean isHoofd){
        beheerderRepository.voegBeheerderToe(new Beheerder(email, isHoofd));
    }
    public void login(String email, String wachtwoord) throws Exception{
        beheerderRepository.login(email, wachtwoord);
    }
    public SortedList<Gebruiker> getGebruikers()
    {
        return beheerderRepository.getGebruikers().sorted();
    }
}
