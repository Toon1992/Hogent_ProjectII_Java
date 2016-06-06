/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import gui.LayoutFrameController;
import domein.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import persistentie.GenericDaoJpa;

import java.util.*;
import stateMachine.ReservatieStateEnum;

/**
 *
 * @author donovandesmedt
 */
public class StartUp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //initializeDatabase();
        Scene scene = new Scene(new LayoutFrameController());
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);

        // The stage will not get smaller than its preferred (initial) size.
        primaryStage.setOnShown((WindowEvent t) -> {
            primaryStage.setMinWidth(primaryStage.getWidth());
            primaryStage.setMinHeight(primaryStage.getHeight());
        });
        primaryStage.show();

    }
    
    private void initializeDatabase() {
        //Gebruiker toevoegen
        GenericDaoJpa<Object> jpa = new GenericDaoJpa<>(Object.class);
        jpa.startTransaction();
        //jpa.insert(new Gebruiker("Lector","jan.pers@hogent.be","LE"));

        Firma globe = new Firma("Globe atmosphere", "globe@atmosphere.com","http://www.atmosphere-newworld.com/");
        Firma prisma = new Firma("Prisma", "helpdesk@prisma.com","http://www.prisma.nl/");
        Firma texas = new Firma("Texas Instruments", "helpdesk@texasinstrument.com", "http://www.ti.com/");
        Firma kimax = new Firma("kimax", "contactpersoon@kimax.com","http://www.kimble-chase.com/");
        Firma wissner = new Firma("Wissner", "contactpersoon@wissner.com", "http://www.karl-wissner.com/wandtafelzeichenger%C3%A4te-blackboard-drawing-instruments/geo-zeichendreieck-geometry-square/");

        jpa.insert(new Beheerder("toon.detrue.v7134@student.hogent.be", false));
        jpa.insert(new Beheerder("donovan.desmedt.v3759@student.hogent.be", true));
//
//
        Doelgroep kleuter = new Doelgroep("Kleuter onderwijs");
        Doelgroep lager = new Doelgroep("Lager onderwijs");
        Doelgroep secundair = new Doelgroep("Secundair onderwijs");

        Leergebied aardrijkskunde = new Leergebied("Aardrijkskunde");
        Leergebied techniek = new Leergebied("Techniek");
        Leergebied wiskunde = new Leergebied("Wiskunde");
        Leergebied fysica = new Leergebied("Fysica");
        Leergebied biologie = new Leergebied("Biologie");
        Leergebied wetenschap = new Leergebied("Wetenschap");
        Leergebied geschiedenis = new Leergebied("Geschiedenis");
        Leergebied maatschappij = new Leergebied("Maatschappij");
        Leergebied mens = new Leergebied("Mens");
        jpa.insert(globe);
        jpa.insert(prisma);
        jpa.insert(texas);
        jpa.insert(kimax);
        jpa.insert(wissner);

        jpa.insert(kleuter);
//        jpa.insert(lager);
//        jpa.insert(secundair);

        jpa.insert(aardrijkskunde);
        jpa.insert(techniek);
        jpa.insert(wiskunde);
        jpa.insert(fysica);
        jpa.insert(biologie);
        jpa.insert(wetenschap);
        jpa.insert(geschiedenis);
        jpa.insert(maatschappij);
        jpa.insert(mens);
//



        Materiaal wereldbol = new Materiaal("/Users/donovandesmedt/NetBeansProjects/groep06Java/groep06Java/src/images/wereldbol.png", "Wereldbol", "Globe met verlichting, boldoorsnede 26cm", "B2.13", 1234, 4, 0, 26.56, true, globe, new HashSet<>(Arrays.asList(lager, secundair)), new HashSet<>(Arrays.asList(aardrijkskunde,maatschappij,mens,geschiedenis)));

        jpa.insert(wereldbol);


        Materiaal rekenMachine = new Materiaal("/Users/donovandesmedt/NetBeansProjects/groep06Java/groep06Java/src/images/texas.jpg", "TI 84 plus", "Grafisch rekentoestel van Texas instrument", "B3.43", 2345, 10, 1, 116.99, true, texas, new HashSet<>(Arrays.asList(secundair)), new HashSet<>(Arrays.asList(wiskunde, fysica, techniek)));

        jpa.insert(rekenMachine);
//
        jpa.insert(new Materiaal("/Users/donovandesmedt/NetBeansProjects/groep06Java/groep06Java/src/images/erlenmeyer.jpg", "Erlenmeyer", "geschiedenis pack contains one flask each of 50, 125, 250, 500, and 1000mL sizes.", "B1.00", 3445, 20, 0, 43.55, false, kimax, new HashSet<Doelgroep>(Arrays.asList(secundair)), new HashSet<Leergebied>(Arrays.asList(wetenschap,biologie))));
        jpa.insert(new Materiaal("/Users/donovandesmedt/NetBeansProjects/groep06Java/groep06Java/src/images/prisma.jpg", "Prisma Duits-Nederlands", "Pocketwoordenboek Duits-Nederlands Prisma", "B2.13", 4566, 22, 0, 9.50, true, prisma, new HashSet<Doelgroep>(Arrays.asList(lager, secundair)), new HashSet<Leergebied>(Arrays.asList(mens,maatschappij))));
        jpa.insert(new Materiaal("/Users/donovandesmedt/NetBeansProjects/groep06Java/groep06Java/src/images/geo.jpg", "Bordgeodriehoek", "Bordgeodriehoek Wissner 80cm", "B2.13", 5431, 4, 0, 26.15, true, wissner, new HashSet<Doelgroep>(Arrays.asList(lager, secundair)), new HashSet<Leergebied>(Arrays.asList(wiskunde, fysica, techniek))));

        jpa.insert(new Reservatie(2,2,0,new Date(116, 4, 9), new Date(116,4,11),new Date(),new HashSet<Dag>(),ReservatieStateEnum.Gereserveerd,new Gebruiker("Toon","toondetrue@gmail.com","ST"),wereldbol));
        jpa.insert(new Reservatie(1,0,0,new Date(116, 4, 9), new Date(116,4,11),new Date(),new HashSet<Dag>(),ReservatieStateEnum.Gereserveerd,new Gebruiker("Donovan","donovan.desmedt.v3759@student.hogent.be","ST"),wereldbol));
        jpa.insert(new Reservatie(3,0,0,new Date(116, 4, 16), new Date(116,4,20),new Date(),new HashSet<Dag>(),ReservatieStateEnum.Gereserveerd,new Gebruiker("Obama","obamam@us.gov","ST"),wereldbol));
        jpa.insert(new Reservatie(2,0,0,new Date(116, 4, 23), new Date(116,4,27),new Date(),new HashSet<Dag>(),ReservatieStateEnum.Gereserveerd,new Gebruiker("Manu","manuschoenmakers@gmail.com","ST"),rekenMachine));
        jpa.insert(new Reservatie(3,0,0,new Date(116, 4, 23), new Date(116,4,27),new Date(),new HashSet<Dag>(),ReservatieStateEnum.Gereserveerd,new Gebruiker("Thomas","thomasledoux@gmail.com","ST"),rekenMachine));

        MailTemplate mail1 = new MailNaReservatie("Bevestiging reservatie", "<p>Dag _NAAM</p>"
                + "Je reservatie loopt van _STARTDATUM tot _EINDDATUM"
                + "<p> Hieronder vind je terug wat je zonet reserveerde: </p>"
                + "<ul>"
                + "_ITEMS"
                + "</ul> ");

        MailTemplate mail2 = new MailNaBlokkeringLector("Blokkering", "<p>Dag _NAAM</p>"
                + "U heeft volgende materialen gereserveerd op _DATUMS :"
                + "<ul>"
                + "_ITEMS"
                + "</ul>");

        MailTemplate mail3 = new MailNaBlokkeringStudent("Reservatie gewijzigd", "<p>Dag _NAAM</p>"
                + "Uw reservatie van volgend materiaal in de week van _STARTDATUM is geblokkeerd:"
                + "<ul>"
                + "_ITEMS"
                + "</ul>");


        jpa.insert(mail1);
        jpa.insert(mail2);
        jpa.insert(mail3);
        jpa.commitTransaction();



    }
}
