/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Beheerder;
import exceptions.EmailException;
import exceptions.WachtwoordException;
import java.util.regex.Pattern;

import persistentie.BeheerderDaoJpa;

/**
 *
 * @author donovandesmedt
 */
public class BeheerderRepository
{

    private Beheerder beheerder;
    private BeheerderDaoJpa beheerderDao;

    public BeheerderRepository()
    {
        beheerderDao = new BeheerderDaoJpa();
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

    public void voegGebruikerToe(Beheerder beheerder)
    {
        setBeheerder(beheerder);
        beheerderDao.startTransaction();
        beheerderDao.insert(beheerder);
        beheerderDao.commitTransaction();
    }
}
