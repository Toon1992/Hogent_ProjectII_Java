/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Reservatie;

/**
 *
 * @author ToonDT
 */
public class ReservatieDaoJpa extends GenericDaoJpa<Reservatie>
{   
    public ReservatieDaoJpa()
    {
        super(Reservatie.class);
    }
    
}
