/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Beheerder;
import java.util.List;
import javax.persistence.TypedQuery;
import persistentie.BeheerderDaoJpa;
import repository.BeheerderRepository;

/**
 *
 * @author ToonDT
 */
public class BeheerderController
{
    private BeheerderDaoJpa bd;
    private BeheerderRepository repo;
    
    public BeheerderController()
    {
        bd = new BeheerderDaoJpa();
        repo = new BeheerderRepository();
    }
    public List<Beheerder> getBeheerders()
    {
       return bd.findAll();
    }
    
    public void setBeheerder(Beheerder beheerder)
    {
        repo.setBeheerder(beheerder);
    }
    
    public void verwijderBeheerder(Beheerder beheerder)
    {
        repo.verwijderMateriaal(beheerder);
    }
    
    public void wijzigBeheerder(Beheerder beheerder)
    {
        repo.wijzigMateriaal(beheerder);
    }
    
    public void voegBeheerderToe(Beheerder beheerder)
    {
        repo.voegBeheerderToe(beheerder);
    }
    
    public Beheerder GetLoggedInBeheerder()
    {
        return repo.getLoggedInBeheerder();
    }
}
