/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.GebruikerController;
import domein.*;
import gui.LoginSchermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import persistentie.GenericDaoJpa;

import java.util.*;

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
        initializeDatabase();
        Scene scene = new Scene(new LoginSchermController(new GebruikerController()));
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

        jpa.insert(new Beheerder("admin@hogent.be", "admin", "admin"));
        //Firma
        Firma globe = new Firma("Globe atmosphere", "globe@atmosphere.com");
        Firma prisma = new Firma("Prisma", "helpdesk@prisma.com");
        Firma texas = new Firma("Texas Instruments", "helpdesk@texasinstrument.com");
        Firma kimax = new Firma("kimax", "contactpersoon@kimax.com");
        Firma wissner = new Firma("Wissner", "contactpersoon@wissner.com");

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
        jpa.insert(lager);
        jpa.insert(secundair);

        jpa.insert(aardrijkskunde);
        jpa.insert(techniek);
        jpa.insert(wiskunde);
        jpa.insert(fysica);
        jpa.insert(biologie);
        jpa.insert(wetenschap);
        jpa.insert(geschiedenis);
        jpa.insert(maatschappij);
        jpa.insert(mens);

        Materiaal wereldbol = new Materiaal("/images/wereldbol.png", "Wereldbol", "Globe met verlichting, boldoorsnede 26cm", "B2.13", 1234, 4, 0, 26.56, true, globe, new HashSet<>(Arrays.asList(lager, secundair)), new HashSet<>(Arrays.asList(aardrijkskunde,maatschappij,mens,geschiedenis)));
        jpa.insert(wereldbol);
        
        Materiaal rekenMachine = new Materiaal("/images/texas.jpg", "TI 84 plus", "Grafisch rekentoestel van Texas instrument", "B3.43", 2345, 10, 1, 116.99, true, texas, new HashSet<Doelgroep>(Arrays.asList(secundair)), new HashSet<Leergebied>(Arrays.asList(wiskunde, fysica, techniek)));     
        jpa.insert(rekenMachine);
        
        jpa.insert(new Materiaal("/images/erlenmeyer.jpg", "Erlenmeyer", "Starter pack contains one flask each of 50, 125, 250, 500, and 1000mL sizes.", "B1.00", 3445, 20, 0, 43.55, false, kimax, new HashSet<Doelgroep>(Arrays.asList(secundair)), new HashSet<Leergebied>(Arrays.asList(wetenschap,biologie))));
        jpa.insert(new Materiaal("/images/prisma.jpg", "Prisma Duits-Nederlands", "Pocketwoordenboek Duits-Nederlands Prisma", "B2.13", 4566, 22, 0, 9.50, true, prisma, new HashSet<Doelgroep>(Arrays.asList(lager, secundair)), new HashSet<Leergebied>(Arrays.asList(mens,maatschappij))));
        jpa.insert(new Materiaal("/images/geo.jpg", "Bordgeodriehoek", "Bordgeodriehoek Wissner 80cm", "B2.13", 5431, 4, 0, 26.15, true, wissner, new HashSet<Doelgroep>(Arrays.asList(lager, secundair)), new HashSet<Leergebied>(Arrays.asList(wiskunde, fysica, techniek))));

        
        jpa.insert(new Reservatie(1,2,new Date(2016, 3, 23), new Date(2016,4,1),1,new Gebruiker("Toon",25),wereldbol));
        jpa.insert(new Reservatie(5,10,new Date(2016, 1, 14), new Date(2016,2,5),1,new Gebruiker("De True",36),wereldbol));
        jpa.insert(new Reservatie(4,2,new Date(2016, 2, 23), new Date(2016,3,6),1,new Gebruiker("Toon",58),rekenMachine));
        
        jpa.commitTransaction();

    }
}
