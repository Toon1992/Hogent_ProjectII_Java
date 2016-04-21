/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author ToonDT
 */
public class MultiException extends IllegalArgumentException
{

    public MultiException()
    {
    }

    public MultiException(String message)
    {
        super(message);
    }
}
