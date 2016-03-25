/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Observable;
import java.util.Observer;

import domein.Materiaal;
import gui.LoaderSchermen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalDetailSchermController extends VBox implements Observer {

    @FXML
    private ImageView imgViewMateriaal;
    @FXML
    private TextArea txfOmschrijving;
    @FXML
    private TextField txfFirma;
    @FXML
    private TextField txfArtikelNummer;
    @FXML
    private TextField txfAantal;
    @FXML
    private TextField txfOnbeschikbaar;
    @FXML
    private TextField txfContactPersoon;
    @FXML
    private RadioButton radioStudent;
    @FXML
    private RadioButton radioLector;
    @FXML
    private TextField txfPlaats;
    @FXML
    private TextField txfPrijs;
    @FXML
    private Button btnOpslaan;
    @FXML
    private ListView<String> listDoelgroep;
    @FXML
    private ListView<String> listLeergbedied;
    @FXML
    private Label lblError;
    @FXML
    private TextField txfNaam;
    private MateriaalController mc;
    private Materiaal materiaal;
    private ToggleGroup group = new ToggleGroup();


    public MateriaalDetailSchermController(MateriaalController mc){
        LoaderSchermen.getInstance().setLocation("MateriaalDetailScherm.fxml",this);
        this.mc = mc;
    }

    @FXML
    private void wijzigFoto(MouseEvent event) {
    }

    @FXML
    private void materiaalWijzigen(ActionEvent event) {
        try{
            materiaal.setNaam(txfNaam.getText());
            materiaal.setOmschrijving(txfOmschrijving.getText());
            materiaal.setAantal(Integer.parseInt(txfAantal.getText()));
            materiaal.setAantalOnbeschikbaar(Integer.parseInt(txfOnbeschikbaar.getText()));
            materiaal.setPlaats(txfPlaats.getText());
            materiaal.setArtikelNr(Integer.parseInt(txfArtikelNummer.getText()));
            String prijs=txfPrijs.getText().replace(",", ".");
            materiaal.setPrijs(Double.valueOf(prijs));
            materiaal.getFirma().setEmailContact(txfContactPersoon.getText());
            materiaal.setUitleenbaarheid(radioStudent.isSelected());
            lblError.setText("");
            Alert succesvol=new Alert(Alert.AlertType.CONFIRMATION);
            succesvol.setTitle("Materiaal gewijzigd");
            succesvol.setHeaderText(materiaal.getNaam());
            succesvol.setContentText("Al uw wijzigingen zijn correct doorgevoerd!");
            succesvol.showAndWait();

        }
        catch(NumberFormatException ex){
            lblError.setText("Er werd een foute waarde ingegeven.");
        }
        catch(IllegalArgumentException ex){
            lblError.setText(ex.getMessage());
        }

    }
    @Override
    public void update(Observable o, Object obj) {
        materiaal = (Materiaal) obj;
        imgViewMateriaal.setImage(new Image(materiaal.getFoto()));
        txfAantal.setText(String.format("%d", materiaal.getAantal()));
        txfArtikelNummer.setText(String.format("%d", materiaal.getArtikelNr()));
        txfContactPersoon.setText(materiaal.getFirma().getEmailContact());
        listDoelgroep.setItems(mc.objectCollectionToObservableList(materiaal.getDoelgroepen()).sorted());
        listLeergbedied.setItems(mc.objectCollectionToObservableList(materiaal.getLeergebieden()).sorted());
        txfFirma.setText(materiaal.getFirma().getNaam());
        txfNaam.setText(materiaal.getNaam());
        txfOmschrijving.setText(materiaal.getOmschrijving());
        txfOnbeschikbaar.setText(String.format("%d", materiaal.getAantalOnbeschikbaar()));
        txfPlaats.setText(materiaal.getPlaats());
        txfPrijs.setText(String.format("%.2f", materiaal.getPrijs()));
        radioStudent.setToggleGroup(group);
        radioStudent.setSelected(materiaal.getUitleenbaarheid());
        radioLector.setSelected(!materiaal.getUitleenbaarheid());
        radioLector.setToggleGroup(group);

    }
    
}
