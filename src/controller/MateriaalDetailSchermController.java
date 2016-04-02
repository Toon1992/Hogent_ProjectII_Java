/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import domein.Materiaal;
import gui.LoaderSchermen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalDetailSchermController extends VBox
{

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
    private TextField txfNaam;
    @FXML
    private Button btnTerug;
    private MateriaalController mc;
    private Materiaal materiaal;
    private ToggleGroup group = new ToggleGroup();
    @FXML
    private Label lblErrorMessage;

    public MateriaalDetailSchermController(MateriaalController mc, Materiaal materiaal)
    {
        LoaderSchermen.getInstance().setLocation("MateriaalDetailScherm.fxml", this);
        this.materiaal = materiaal;
        this.mc = mc;
        update(materiaal);
    }

    @FXML
    private void wijzigFoto(MouseEvent event)
    {
    }

    @FXML
    private void materiaalWijzigen(ActionEvent event)
    {
        try
        {
            materiaal.setNaam(txfNaam.getText());
            materiaal.setOmschrijving(txfOmschrijving.getText());
            materiaal.setAantal(Integer.parseInt(txfAantal.getText()));
            materiaal.setAantalOnbeschikbaar(Integer.parseInt(txfOnbeschikbaar.getText()));
            materiaal.setPlaats(txfPlaats.getText());
            materiaal.setArtikelNr(Integer.parseInt(txfArtikelNummer.getText()));
            String prijs = txfPrijs.getText().replace(",", ".");
            materiaal.setPrijs(Double.valueOf(prijs));
            materiaal.getFirma().setEmailContact(txfContactPersoon.getText());

            materiaal.setUitleenbaarheid(radioStudent.isSelected());
            lblErrorMessage.setText("");

            materiaal.setIsReserveerbaar(radioStudent.isSelected());
            lblErrorMessage.setText("");

            Alert succesvol = new Alert(Alert.AlertType.CONFIRMATION);
            succesvol.setTitle("Materiaal gewijzigd");
            succesvol.setHeaderText(materiaal.getNaam());
            succesvol.setContentText("Al uw wijzigingen zijn correct doorgevoerd!");
            succesvol.showAndWait();

        } catch (NumberFormatException ex)
        {
            lblErrorMessage.setText("Er werd een foute waarde ingegeven.");
        } catch (IllegalArgumentException ex)
        {
            lblErrorMessage.setText(ex.getMessage());
        }

    }

    public void update(Materiaal materiaal)
    {
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
        radioStudent.setSelected(materiaal.getIsReserveerbaar());
        radioLector.setSelected(!materiaal.getIsReserveerbaar());
        radioLector.setToggleGroup(group);

    }

    @FXML
    private void terugNaarOverzicht(ActionEvent event)
    {
        BorderPane bp = (BorderPane) this.getParent();
        LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, (HBox) LoaderSchermen.getInstance().getNode());
        //LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, new MateriaalOverzichtSchermController(mc));
    }
}
