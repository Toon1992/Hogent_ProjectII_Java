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
public abstract class ReservatieState
{
    private Reservatie reservatie;
   
    protected ReservatieState(Reservatie reservatie)
    {
        this.reservatie = reservatie;
    }
    
    public abstract void zetOpBlokkeer();
    public abstract void zetOpGereserveerd();
    public abstract void zetOpTelaat();
    public abstract void zetOpGehaald();
    public abstract void zetOpOverruled();
    
    public Reservatie getReservatie()
    {
        return reservatie;
    }

    public void setReservatie(Reservatie reservatie)
    {
        this.reservatie = reservatie;
    }
}
