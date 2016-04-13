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

/**
 *
 * @author ToonDT
 */
public class BeheerderController
{
    private BeheerderDaoJpa bd;
    
    public BeheerderController()
    {
        bd = new BeheerderDaoJpa();
    }
    public List<Beheerder> getBeheerders()
    {
       return bd.findAll();
    }
}
