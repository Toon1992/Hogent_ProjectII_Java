/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import domein.DomeinController;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


/**
 *
 * @author donovandesmedt
 */
@RunWith(value = Parameterized.class)
public class LoginSchermControllerTest {
    private String email,wachtwoord;
    private DomeinController dc;
    
    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(
                new Object[][]{
                      {"admin@","addmin"} 
                    , {"", "admin"} 
                    , {"admin@hogentbe", "amdin"}
                    , {"admin@hogent.be", ""}
                    , {"", ""}
                    , {"4", ""}
                    ,{"aadmfin.",""}    
                }
        );
    }
    public LoginSchermControllerTest(String email, String wachtwoord){
        this.email = email;
        this.wachtwoord = wachtwoord;
    }
    @Before
    public void before(){
        dc = new DomeinController();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSlechteBeheerders() throws Exception {
        dc.login(email, wachtwoord);
    }
    
}
