/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Reservatie;
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
}
