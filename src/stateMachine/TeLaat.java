/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateMachine;

import domein.Reservatie;

/**
 *
 * @author ToonDT
 */
public class TeLaat extends ReservatieState
{    
    public TeLaat(Reservatie reservatie)
    {
        super(reservatie);
    }   

    @Override
    public void zetOpBlokkeer()
    {
         getReservatie().toState(new Geblokkeerd(getReservatie()));
    }

    @Override
    public void zetOpGereserveerd()
    {
         getReservatie().toState(new Gereserveerd(getReservatie()));
    }

    @Override
    public void zetOpTelaat()
    {
         throw new IllegalArgumentException("Status staat al op Te laat");
    }

    @Override
    public void zetOpGehaald()
    {
        getReservatie().toState(new Opgehaald(getReservatie()));
    }

    @Override
    public void zetOpOverruled()
    {
       getReservatie().toState(new Overruled(getReservatie()));
    }
}
