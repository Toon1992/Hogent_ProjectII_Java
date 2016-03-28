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
public class Geblokkeerd extends ReservatieState
{
    
    public Geblokkeerd(Reservatie reservatie)
    {
        super(reservatie);
    }

    @Override
    public void zetOpBlokkeer()
    {
        throw new IllegalArgumentException("status staat al op geblokkeerd");
    }

    @Override
    public void zetOpGereserveerd()
    {
        getReservatie().toState(new Gereserveerd(getReservatie()));
    }

    @Override
    public void zetOpTelaat()
    {
         getReservatie().toState(new TeLaat(getReservatie()));
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
