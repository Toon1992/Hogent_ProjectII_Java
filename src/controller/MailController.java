/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.MailTemplate;
import java.util.List;
import repository.MailRepository;

/**
 *
 * @author manu
 */
public class MailController {
    private MailRepository mr;
    
    public MailController(){
        mr=new MailRepository();
    }
    
    public List<MailTemplate> geefAlleMails(){
        return mr.geefAlleMails();
    }
    
    public void wijzigMail(MailTemplate mail){
        mr.wijzigMail(mail);
    }
    
    public MailTemplate geefMail(String onderwerp){
        return mr.geefMail(onderwerp);
    }
    
    
}
