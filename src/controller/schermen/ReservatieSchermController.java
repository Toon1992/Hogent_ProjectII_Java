/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.schermen;

import controller.ReservatieController;
import gui.LoaderSchermen;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author ToonDT
 */
public class ReservatieSchermController extends HBox
{

    @FXML
    private TextField txfZoek;
    @FXML
    private DatePicker datePickerBegin;
    @FXML
    private DatePicker datePickerEind;
    @FXML
    private TableColumn<?, ?> reservatieTable;
    @FXML
    private TableColumn<?, ?> materiaalColumn;
    @FXML
    private TableColumn<?, ?> aantalColumn;
    @FXML
    private TableColumn<?, ?> BeginDatumColumn;
    @FXML
    private TableColumn<?, ?> EindDatumColumn;
    
    private ReservatieController rc;

   public ReservatieSchermController(ReservatieController rc)
   {
       LoaderSchermen.getInstance().setLocation("ReservatieScherm.fxml", this);
       this.rc = rc;
   }
}
