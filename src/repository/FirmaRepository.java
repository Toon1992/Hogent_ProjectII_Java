/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Firma;
import persistentie.FirmaDaoJpa;

/**
 *
 * @author manu
 */
public class FirmaRepository {
    
    private FirmaDaoJpa firmaDao;
    public FirmaRepository(){
        firmaDao=new FirmaDaoJpa();
    }
    
    public Firma geefFirma(String naam){
        return firmaDao.geefFirma(naam);
    }
    
    public void wijzigFirma(Firma f){
        firmaDao.startTransaction();
        firmaDao.update(f);
        firmaDao.commitTransaction();
    }
}
