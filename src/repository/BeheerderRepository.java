/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Beheerder;
import domein.Gebruiker;
import domein.Materiaal;
import exceptions.EmailException;
import exceptions.WachtwoordException;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.BeheerderDaoJpa;
import persistentie.GenericDaoJpa;

/**
 *
 * @author donovandesmedt
 */
public class BeheerderRepository
{

    private Beheerder beheerder;
    private BeheerderDaoJpa beheerderDao;
    private GenericDaoJpa<Gebruiker> gebruikerDoa;
    public BeheerderRepository()
    {
        beheerderDao = new BeheerderDaoJpa();
        gebruikerDoa = new GenericDaoJpa<>(Gebruiker.class);
    }

    public Beheerder getBeheerder()
    {
        return beheerder;
    }

    public void setBeheerder(Beheerder beheerder)
    {
        this.beheerder = beheerder;
    }

    public void login(String email, String wachtwoord)
    {
        if (!Pattern.matches("\\w+(\\.\\w*)*@\\w+\\.\\w+(\\.\\w+)*", email))
        {
            throw new EmailException("Ongeldige email");
        }
        if (wachtwoord.isEmpty())
        {
            throw new WachtwoordException("Wachtwoord verplicht");
        }
        beheerder = beheerderDao.getBeheerderByEmail(email, wachtwoord);//Mapping.loginQuery(email, wachtwoord);
    }

    public void voegBeheerderToe(Beheerder beheerder)
    {
        setBeheerder(beheerder);
        beheerderDao.startTransaction();
        beheerderDao.insert(beheerder);
        beheerderDao.commitTransaction();
    }
    
    public void verwijderMateriaal(Beheerder beheerder) {
        beheerderDao.startTransaction();
        beheerderDao.delete(beheerder);
        beheerderDao.commitTransaction();
    }
    
    public void wijzigMateriaal(Beheerder beheerder)
    {
        beheerderDao.startTransaction();
        beheerderDao.update(beheerder);
        beheerderDao.commitTransaction();
    }
    
    public SortedList<Gebruiker> getGebruikers(){
        return new SortedList<Gebruiker>(new FilteredList<Gebruiker>(FXCollections.observableArrayList(gebruikerDoa.findAll())));
    }
}
