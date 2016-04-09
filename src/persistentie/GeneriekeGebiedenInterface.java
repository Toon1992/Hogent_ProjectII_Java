/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Doelgroep;
import java.util.List;
import java.util.Set;

/**
 *
 * @author manu
 */
public interface GeneriekeGebiedenInterface<T>{
    public Set<T> geefGebieden(List<String> namen,T filter);
    public <T>void voegGebiedToe(String naam,T filter);
}
