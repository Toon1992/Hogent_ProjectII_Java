/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.Mapping;

/**
 *
 * @author donovandesmedt
 */
public class MateriaalRepository {
    private List<Materiaal> materialen;
    public MateriaalRepository(){
        
    }
    public SortedList<Materiaal> geefMaterialen(){
        ObservableList<Materiaal> filterMateriaal = FXCollections.observableList(Mapping.getMaterialen());
        return new SortedList<>(new FilteredList(filterMateriaal, p -> true));
    }
}
