/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Firma;
import persistentie.FirmaDaoJpa;

import java.util.List;
import java.util.stream.Collectors;

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
    public void voegFirmaToe(String firma, String email, String website){
        firmaDao.startTransaction();
        firmaDao.insert(new Firma(firma, email, website));
        firmaDao.commitTransaction();

    }
    public List<String> geefAlleFirmas(){
        return firmaDao.findAll().stream().map(firma -> firma.getNaam()).collect(Collectors.toList());
    }
    
    public void wijzigFirma(Firma f){
        firmaDao.startTransaction();
        firmaDao.update(f);
        firmaDao.commitTransaction();
    }
    
    public void deleteFirma(Firma firma)
    {
        firmaDao.startTransaction();
        firmaDao.delete(firma);
        firmaDao.commitTransaction();
    }
}
