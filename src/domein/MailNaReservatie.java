/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Entity;

/**
 *
 * @author manu
 */
@Entity
public class MailNaReservatie extends MailTemplate{

    protected MailNaReservatie() {
    }

    public MailNaReservatie(String onderwerp, String body) {
        super(onderwerp, body);
    }
    
    
}
