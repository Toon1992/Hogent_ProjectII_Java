/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author donovandesmedt
 */
public class EmailException extends IllegalArgumentException{
    public EmailException(){}
    public EmailException(String message){
        super(message);
    }
}
