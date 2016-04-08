/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import java.util.Set;
import persistentie.GeneriekeGebieden;

/**
 *
 * @author manu
 * @param <T>
 */
public class GebiedenRepository<T> {
    private GeneriekeGebieden filter;
    
    public GebiedenRepository(){
        filter=new GeneriekeGebieden();
    }
    public Set<T> geefGebiedenVoorNamen(List<String> namen,T naam){
        return filter.geefGebieden(namen, naam);
    }
}
