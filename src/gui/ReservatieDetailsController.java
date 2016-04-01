/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.GebruikerController;
import controller.MateriaalController;
import controller.ReservatieController;
import controller.StartSchermController;
import domein.Materiaal;
import domein.Reservatie;
import java.time.ZoneId;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ToonDT
 */
public class ReservatieDetailsController extends GridPane
{

    @FXML
    private ListView<?> lvNamen;
    @FXML
    private DatePicker dtpOphaal;
    @FXML
    private DatePicker dtpTerugbreng;
    @FXML
    private Button btnTerug;

    private ReservatieController rc;
    private Reservatie reservatie;
    @FXML
    private ComboBox<String> cmbStatus;
    @FXML
    private ComboBox<String> cmbMateriaal;

    public ReservatieDetailsController(ReservatieController rc)
    {
        LoaderSchermen.getInstance().setLocation("ReservatieDetails.fxml", this);
        this.rc = rc;

        vulComboBoxStatus();
        vulCombBoxMateriaal();
    }

    private void vulComboBoxStatus()
    {
        cmbStatus.setItems(FXCollections.observableArrayList("Gereserveerd", "Geblokkeerd", "Opgehaald", "Overruled", "TeLaat"));
    }

    private void vulCombBoxMateriaal()
    {
        MateriaalController mc = new MateriaalController();
        SortedList<Materiaal> list = mc.getMateriaalFilterList();
        cmbMateriaal.setItems(FXCollections.observableArrayList(list.stream().map(Materiaal::getNaam).collect(Collectors.toList())));
    }

    @FXML
    private void terug(ActionEvent event)
    {
        BorderPane bp = (BorderPane) this.getParent();
        bp.setCenter(new StartSchermController(new GebruikerController()));
    }

    @FXML
    private void verwijderReservatie(ActionEvent event)
    {
        if (reservatie == null)
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Reservatie verwijderen", "Je moet eerst een reservatie selecteren", "Ok");
        } else
        {
            boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Reservatie verwijderen", "Ben je zeker dat je de reservatie wilt verwijderen?","Nee", "Ja");
            if (!isOk)
            {
                rc.verwijderReservatie(reservatie);
            }
        }
    }

    @FXML
    private void wijzigReservatie(ActionEvent event)
    {
    }

    public void setCurrenReservatie(Reservatie reservatie)
    {
        this.reservatie = reservatie;
        update();
    }

    public void update()
    {
        if (reservatie == null)
        {
            return;
        }

        dtpOphaal.setValue(reservatie.getBeginDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dtpTerugbreng.setValue(reservatie.getEindDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        cmbStatus.setValue(reservatie.getReservatieState().getClass().getSimpleName());
        cmbMateriaal.setValue(reservatie.getMateriaal().getNaam());
    }

}
