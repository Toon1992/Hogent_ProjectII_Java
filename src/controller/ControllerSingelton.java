/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author ToonDT
 */
public class ControllerSingelton
{
    private static BeheerderController bhc;
    private static GebruikerController gbc;
    private static MateriaalController mtc;
    private static ReservatieController rsc;
    private static MailController mlc;
    
    private ControllerSingelton()
    {}
    
    public static BeheerderController getBeheerderControllerInstance()
    {
        if(bhc == null)
            bhc = new BeheerderController();
        
        return bhc;
    }
    
    public static GebruikerController getGebruikerControllerInstance()
    {
         if(gbc == null)
            gbc = new GebruikerController();
        
        return gbc;
    }
    
    public static MateriaalController getMateriaalControllerInstance()
    {
        if(mtc == null)
            mtc = new MateriaalController();
        
        return mtc;
    }
    
    public static ReservatieController getReservatieControllerInstance()
    {
            if(rsc == null)
            rsc = new ReservatieController();
        
        return rsc;
    }
    
    public static MailController getMailControllerInstance(){
        if(mlc==null){
            mlc=new MailController();
        }
        return mlc;
    }
    
}
