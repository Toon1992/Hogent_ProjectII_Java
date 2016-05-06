/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import javafx.collections.ObservableList;

import java.util.List;
import java.util.Set;

/**
 *
 * @author manu
 */
public interface GeneriekeGebiedenInterface<T>{
    public Set<T> geefGebiedenVoorNamen(List<String> namen,T filter);
    public <T>void voegGebiedToe(String naam,T filter);
    public ObservableList<String> geefAlleGebieden(T filter);
    public void deleteGebied(T filter);
    public T geeftGebied(T filter, String naam);
            
            
}
