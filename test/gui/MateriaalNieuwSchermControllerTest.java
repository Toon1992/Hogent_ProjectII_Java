/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.MateriaalController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import domein.Firma;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Thomas
 */
@RunWith(Parameterized.class)
public class MateriaalNieuwSchermControllerTest {
    private String naam, aantal, aantalOnbeschikbaar, prijs, artikelNr;
    private MateriaalController materiaalController;
    
    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(
                new Object[][]{
                      {"test", "", "", "", ""} 
                    , {"", "0", "", "", ""},
                    {"test", "test", "", "", ""},
                    {"test", "0", "test", "", ""},
                    {"test", "0", "0", "test", ""},
                    {"test", "0", "0", "0", "test"}
                }
        );
    }
    
    public MateriaalNieuwSchermControllerTest(String naam, String aantal, String aantalOnbeschikbaar, String prijs, String artikelNr) {
        this.aantal = aantal;
        this.naam=naam;
        this.aantalOnbeschikbaar = aantalOnbeschikbaar;
        this.prijs = prijs;
        this.artikelNr = artikelNr;
    }
    
    @Before
    public void before()
    {
        materiaalController = new MateriaalController();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSlechteBeheerders() throws Exception {
        materiaalController.voegMateriaalToe("", naam, "", "", new Firma("",""), artikelNr, aantal, aantalOnbeschikbaar, prijs, true, null, null);
    }
    
}
