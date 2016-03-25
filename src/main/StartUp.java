/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.GebruikerController;
import domein.*;
import controller.LoginSchermController;
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
public class StartUp extends Application{
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
    private void initializeDatabase(){
        //Gebruiker toevoegen
        GenericDaoJpa<Object> jpa = new GenericDaoJpa<>(Object.class);
        jpa.startTransaction();

        jpa.insert(new Beheerder("admin@hogent.be", "admin", "admin"));
        //Firma
        Firma globe = new Firma("Globe atmosphere", "globe@atmosphere.com");
        Firma prisma = new Firma("Prisma", "helpdesk@prisma.com");
        Firma texas = new Firma("Texas Instruments", "helpdesk@texasinstrument.com");
        Firma kimax = new Firma("kimax", "contactpersoon@kimax.com");
        Firma wissner  = new Firma("Wissner", "contactpersoon@wissner.com");


        Doelgroep kleuter = new Doelgroep("Kleuter onderwijs");
        Doelgroep lager = new Doelgroep("Lager onderwijs");
        Doelgroep secundair = new Doelgroep("Secundair onderwijs");

        Leergebied aardrijkskunde = new Leergebied("Aardrijkskunde");
        Leergebied duits  = new Leergebied("Duits");
        Leergebied wiskunde = new Leergebied("Wiskunde");
        Leergebied fysica = new Leergebied("Fysica");
        Leergebied chemie = new Leergebied("Chemie");

        jpa.insert(globe);
        jpa.insert(prisma);
        jpa.insert(texas);
        jpa.insert(kimax);
        jpa.insert(wissner);

        jpa.insert(kleuter);
        jpa.insert(lager);
        jpa.insert(secundair);

        jpa.insert(aardrijkskunde);
        jpa.insert(duits);
        jpa.insert(wiskunde);
        jpa.insert(fysica);
        jpa.insert(chemie);

        jpa.insert(new Materiaal("/images/wereldbol.png", "Werelbol","Globe met verlichting, boldoorsnede 26cm","B2.13", 1234, 4, 0, 26.56, true, globe,new HashSet<Doelgroep>(Arrays.asList(lager, secundair)) , new HashSet<Leergebied>(Arrays.asList(aardrijkskunde))));
        jpa.insert(new Materiaal("/images/texas.jpg", "TI 84 plus","Grafisch rekentoestel van Texas instrument","B3.43", 2345, 10, 1, 116.99, true, texas, new HashSet<Doelgroep>(Arrays.asList(secundair)),  new HashSet<Leergebied>(Arrays.asList(wiskunde, fysica, chemie))));
        jpa.insert(new Materiaal("/images/erlenmeyer.jpg", "Erlenmeyer","Starter pack contains one flask each of 50, 125, 250, 500, and 1000mL sizes.","B1.00", 3445, 20, 0, 43.55, false, kimax, new HashSet<Doelgroep>(Arrays.asList(secundair)),  new HashSet<Leergebied>(Arrays.asList(chemie))));
        jpa.insert(new Materiaal("/images/prisma.jpg", "Prisma Duits-Nederlands","Pocketwoordenboek Duits-Nederlands Prisma","B2.13", 4566, 22, 0, 9.50, true, prisma, new HashSet<Doelgroep>(Arrays.asList(lager, secundair)),  new HashSet<Leergebied>(Arrays.asList(duits))));
        jpa.insert(new Materiaal("/images/geo.jpg", "Bordgeodriehoek","Bordgeodriehoek Wissner 80cm","B2.13", 5431, 4, 0, 26.15, true, wissner,new HashSet<Doelgroep>(Arrays.asList(lager, secundair)),  new HashSet<Leergebied>(Arrays.asList(wiskunde, fysica, chemie))));

        jpa.commitTransaction();

    }
}
