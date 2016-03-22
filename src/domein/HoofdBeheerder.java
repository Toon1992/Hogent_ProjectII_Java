/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "HoofdBeheerder")
public class HoofdBeheerder extends Beheerder{
    private String bijnaam;
    protected HoofdBeheerder(){}
    
    public HoofdBeheerder(String email, String wachtwoord, String naam){
        super(email, naam, wachtwoord);
    }
}
