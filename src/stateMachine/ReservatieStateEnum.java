/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateMachine;

/**
 *
 * @author ToonDT
 */
public enum ReservatieStateEnum
{
    Gereserveerd,
    Geblokkeerd,
    TeLaat,
    Opgehaald,
    Overruled;
    @Override
    public String toString() {
        switch(this) {
            case Gereserveerd: return "Gereserveerd";
            case Geblokkeerd: return "Geblokkeerd";
            case TeLaat: return "Te laat";
            case Opgehaald: return "Opgehaald";
            case Overruled: return "Overruled";
            default: throw new IllegalArgumentException();
        }
    }
}
