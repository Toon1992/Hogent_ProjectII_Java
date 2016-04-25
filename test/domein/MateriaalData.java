/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author Thomas
 */
public class MateriaalData {
    private List<Materiaal> materialen = new ArrayList<>();
    private List<Firma> firmas = new ArrayList<>();
    private Doelgroep kleuter, lager, secundair;
    private Leergebied aardrijkskunde, techniek, wiskunde, fysica, biologie, wetenschap, geschiedenis, maatschappij, mens;
    private Materiaal wereldbol, rekenMachine;
    private Firma globe, prisma, texas, kimax, wissner;
    private SortedList<Materiaal> sortedMaterialen;
    
    public MateriaalData()
    {
        initializeData();
        materialen.add(wereldbol);
        materialen.add(rekenMachine);
        firmas.add(globe);
        firmas.add(prisma);
        firmas.add(texas);
        sortedMaterialen = new SortedList<Materiaal>(FXCollections.observableArrayList(materialen));
    }
    
    public Materiaal getMateriaalWereldbol()
    {
        return wereldbol;
    }
    
    public Materiaal getMateriaalRekenmachine()
    {
        return rekenMachine;
    }
    
    public SortedList<Materiaal> geefMaterialen()
    {
        return sortedMaterialen;
    }
    
    public List<Firma> geefFirmas()
    {
        return firmas;
    }
    
    
    
    public void initializeData()
    {
        globe = new Firma("Globe atmosphere", "globe@atmosphere.com");
        prisma = new Firma("Prisma", "helpdesk@prisma.com");
        texas = new Firma("Texas Instruments", "helpdesk@texasinstrument.com");
        kimax = new Firma("kimax", "contactpersoon@kimax.com");
        wissner = new Firma("Wissner", "contactpersoon@wissner.com");

        kleuter = new Doelgroep("Kleuter onderwijs");
        lager = new Doelgroep("Lager onderwijs");
        secundair = new Doelgroep("Secundair onderwijs");

        aardrijkskunde = new Leergebied("Aardrijkskunde");
        techniek = new Leergebied("Techniek");
        wiskunde = new Leergebied("Wiskunde");
        fysica = new Leergebied("Fysica");
        biologie = new Leergebied("Biologie");
        wetenschap = new Leergebied("Wetenschap");
        geschiedenis = new Leergebied("Geschiedenis");
        maatschappij = new Leergebied("Maatschappij");
        mens = new Leergebied("Mens");

        wereldbol = new Materiaal("C:\\School\\Semester II\\Project II\\groep06Java\\src\\images\\wereldbol.png", "Wereldbol", "Globe met verlichting, boldoorsnede 26cm", "B2.13", 1234, 4, 0, 26.56, true, globe, new HashSet<>(Arrays.asList(lager, secundair)), new HashSet<>(Arrays.asList(aardrijkskunde, maatschappij, mens, geschiedenis)));
        rekenMachine = new Materiaal("C:\\School\\Semester II\\Project II\\groep06Java\\src\\images\\texas.jpg", "TI 84 plus", "Grafisch rekentoestel van Texas instrument", "B3.43", 2345, 10, 1, 116.99, true, texas, new HashSet<>(Arrays.asList(secundair)), new HashSet<>(Arrays.asList(wiskunde, fysica, techniek)));

    }
}
