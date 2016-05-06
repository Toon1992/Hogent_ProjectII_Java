/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import repository.GebiedenRepository;

/**
 *
 * @author ToonDT
 */
public class GebiedenController<T>
{

    private GebiedenRepository repos;
    
    public GebiedenController()
    {
        repos = new GebiedenRepository();
    }

    public ObservableList<String> geefAlleGebieden(T naam)
    {
        return repos.geefAlleGebieden(naam);
    }

    public Set<T> geefGebiedenVoorNamen(List<String> namen, T naam)
    {
        return repos.geefGebiedenVoorNamen(namen, naam);
    }

    public void voegNieuwGebiedToe(String naam, T gebied)
    {
        repos.voegNieuwGebiedToe(naam, gebied);
    }
    
    public void deleteGebied(T gebied)
    {
        repos.deleteGebied(gebied);
    }
    
    public T geefGebied(T filter, String naam)
    {
        return (T) repos.geefGebied(filter, naam);
    }
    
}
