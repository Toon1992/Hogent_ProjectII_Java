/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
    private FilteredList<Materiaal> filteredmaterialen;
    public MateriaalRepository(){
        
    }
    public SortedList<Materiaal> geefMaterialen(){
        ObservableList<Materiaal> filterMateriaal = FXCollections.observableList(Mapping.getMaterialen());
        filteredmaterialen = new FilteredList(filterMateriaal, p -> true);
        return new SortedList<>(filteredmaterialen);
    }
    public <E> ObservableList<String> objectCollectionToObservableList(Collection<E> list){
        List<String> stringLijst = list.stream().map(e -> e.toString()).collect(Collectors.toList());
        return FXCollections.observableArrayList(stringLijst);
    }
    public void zoek(String zoekterm){
        filteredmaterialen.setPredicate(m -> {
            if(zoekterm == null || zoekterm.isEmpty())
                return true;
            if(m.getNaam().toLowerCase().contains(zoekterm.toLowerCase()) || m.getOmschrijving().toLowerCase().contains(zoekterm.toLowerCase())){
                return true;
            }
            if(m.leergebieden.stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm.toLowerCase()))){
                return true;
            }
            if(m.doelgroepen.stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm.toLowerCase()))){
                return true;
            }
            return false;
        });

    }
}
