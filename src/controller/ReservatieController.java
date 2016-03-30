/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Materiaal;
import domein.Reservatie;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.transformation.SortedList;
import repository.ReservatieRepository;

/**
 *
 * @author ToonDT
 */
public class ReservatieController
{
    private ReservatieRepository repository;
    
    public ReservatieController()
    {
        repository = new ReservatieRepository();
    }
    
    public SortedList<Reservatie> getReservaties()
    {
        return repository.geefMaterialen();
    }
    
    public void zoek(String zoekterm)
    {
        repository.Zoek(zoekterm);
    }
    
    public void zoekOpBeginDatum(LocalDate datum)
    {
        repository.zoekOpBeginDatum(datum);
    }
    
    public void zoekOpEindDatum(LocalDate datum)
    {
        repository.zoekOpEindDatum(datum);
    }
    
     public void setCurrentReservatie(Reservatie reservatie){
//        setChanged();
//        notifyObservers(reservatie);
    }
    
     public void verwijderReservatie(Reservatie reservatie)
     {
         repository.verwijderReservatue(reservatie);
     }
}
