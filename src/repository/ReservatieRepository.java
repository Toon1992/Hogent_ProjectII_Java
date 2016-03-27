/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Materiaal;
import domein.Reservatie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.ReservatieDaoJpa;

/**
 *
 * @author ToonDT
 */
public class ReservatieRepository
{
    private FilteredList<Reservatie> filterReservaties;
    private ReservatieDaoJpa reservatieDao;
    private ObservableList<Reservatie> filterReservatie;
    
    public ReservatieRepository()
    {
        reservatieDao = new ReservatieDaoJpa();
    }
    
     public SortedList<Reservatie> geefMaterialen() {
        if (filterReservaties == null) {
            filterReservatie = FXCollections.observableList(reservatieDao.findAll());
            filterReservaties = new FilteredList(filterReservatie, p -> true);
        }
        return new SortedList<>(filterReservaties);
    }
}
