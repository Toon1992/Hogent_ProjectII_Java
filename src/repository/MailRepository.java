/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.MailTemplate;
import java.util.List;
import persistentie.MailDaoJpa;

/**
 *
 * @author manu
 */
public class MailRepository {
    
    private MailDaoJpa mailDao;
    
    public MailRepository(){
        mailDao=new MailDaoJpa();
    }
    
    public List<MailTemplate> geefAlleMails(){
        return mailDao.findAll();
    }
    
    public void wijzigMail(MailTemplate mail){
        mailDao.startTransaction();
        mailDao.update(mail);
        mailDao.commitTransaction();
    }
    
    public MailTemplate geefMail(String onderwerp){
        return mailDao.geefMail(onderwerp);
    }
    
    
}
