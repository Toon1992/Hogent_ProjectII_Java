/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import domein.Beheerder;
import domein.BeheerderRepository;
import domein.Doelgroep;
import domein.DomeinController;
import domein.Firma;
import domein.HoofdBeheerder;
import domein.Leergebied;
import domein.Materiaal;
import gui.LoginSchermController;
import gui.MateriaalSchermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Material;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import persistentie.Mapping;
import gui.RegistreerSchermController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Mapping.persistObject(new HoofdBeheerder("admin@hogent.be", "admin", "admin"));
        Firma globe = new Firma("Globe atmosphere", "globe@atmosphere.com");
        Set<Doelgroep> doelgroepen = new HashSet<>();
        Set<Leergebied> leergebieden = new HashSet<>();
        Doelgroep lager = new Doelgroep("Lager onderwijs");
        Doelgroep secundair = new Doelgroep("Secundair onderwijs");
        Leergebied aardrijkskunde = new Leergebied("AardrijksKunde");
        leergebieden.add(aardrijkskunde);
        doelgroepen.add(lager);
        doelgroepen.add(secundair);
        Mapping.persistObject(globe);
        Mapping.persistObject(lager);
        Mapping.persistObject(secundair);
        Mapping.persistObject(aardrijkskunde);
        Mapping.persistObject(new Materiaal("/images/wereldbol.png", "Werelbol","Globe met verlichting, boldoorsnede 26cm","B2.13", 1234, 4, 0, 26.56, true, globe, doelgroepen, leergebieden));
        Scene scene = new Scene(new LoginSchermController(new DomeinController()));
        primaryStage.setScene(scene);

        // The stage will not get smaller than its preferred (initial) size.
        primaryStage.setOnShown((WindowEvent t) -> {
            primaryStage.setMinWidth(primaryStage.getWidth());
            primaryStage.setMinHeight(primaryStage.getHeight());
        });
        primaryStage.show();
        
    }
}
