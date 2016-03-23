/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import domein.Beheerder;
import domein.BeheerderRepository;
import domein.DomeinController;
import domein.Firma;
import domein.HoofdBeheerder;
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
