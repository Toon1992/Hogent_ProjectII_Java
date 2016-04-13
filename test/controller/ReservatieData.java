import domein.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import repository.ReservatieRepository;
import stateMachine.ReservatieStateEnum;

import java.util.*;

/**
 * Created by donovandesmedt on 09/04/16.
 */
public class ReservatieData {
    private List<Reservatie> reservaties = new ArrayList<>();
    private SortedList<Reservatie> sortedReservaties;
    private Firma globe, prisma, texas, kimax, wissner;
    private Doelgroep kleuter, lager, secundair;
    private Leergebied aardrijkskunde, techniek, wiskunde, fysica, biologie, wetenschap, geschiedenis, maatschappij, mens;
    private Materiaal wereldbol, rekenMachine;
    private Reservatie reservatieStudent1, reservatieStudent2, reservatieStudent3,  reservatieLector1;
    public ReservatieData() {
        initializeData();
        reservaties.add(reservatieStudent1);
        reservaties.add(reservatieStudent2);
        sortedReservaties = new SortedList<Reservatie>(FXCollections.observableArrayList(reservaties));
    }
    public SortedList<Reservatie> getSortedReservaties(){
        return sortedReservaties;
    }
    public Reservatie getReservatieStudent1(){
        return reservatieStudent1;
    }
    public Reservatie getReservatieStudent2(){
        return reservatieStudent2;
    }
    public Reservatie getReservatieLector1(){return reservatieLector1;}
    private void initializeData() {
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

        wereldbol = new Materiaal("/Users/donovandesmedt/NetBeansProjects/groep06Java/groep06Java/src/images/wereldbol.png", "Wereldbol", "Globe met verlichting, boldoorsnede 26cm", "B2.13", 1234, 4, 0, 26.56, true, globe, new HashSet<>(Arrays.asList(lager, secundair)), new HashSet<>(Arrays.asList(aardrijkskunde, maatschappij, mens, geschiedenis)));
        rekenMachine = new Materiaal("/Users/donovandesmedt/NetBeansProjects/groep06Java/groep06Java/src/images/texas.jpg", "TI 84 plus", "Grafisch rekentoestel van Texas instrument", "B3.43", 2345, 10, 1, 116.99, true, texas, new HashSet<>(Arrays.asList(secundair)), new HashSet<>(Arrays.asList(wiskunde, fysica, techniek)));

        reservatieStudent1 = new Reservatie(2, 0, new Date(116, 3, 11), new Date(116, 3, 15), new Date(), new HashSet<Dag>(), ReservatieStateEnum.Gereserveerd, new Gebruiker("Toon", "toondetrue@gmail.com", "ST"), wereldbol);
        reservatieStudent2 = new Reservatie(1, 0, new Date(116, 4, 11), new Date(116, 4, 15), new Date(), new HashSet<Dag>(), ReservatieStateEnum.Gereserveerd, new Gebruiker("Donovan", "donovandesmedt@gmail.com", "ST"), wereldbol);
        reservatieStudent3 = new Reservatie(1, 0, new Date(116, 4, 11), new Date(116, 4, 15), new Date(), new HashSet<Dag>(), ReservatieStateEnum.Gereserveerd, new Gebruiker("Donovan", "donovandesmedt@gmail.com", "ST"), rekenMachine);

        reservatieLector1 = new Reservatie(3, 0, new Date(116, 3, 11), new Date(116, 3, 15), new Date(), new HashSet<Dag>(), ReservatieStateEnum.Gereserveerd, new Gebruiker("Lector", "lector@hogent.be", "LE"), wereldbol);
    }
}
