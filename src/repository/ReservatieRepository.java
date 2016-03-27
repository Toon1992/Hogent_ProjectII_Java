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

    public SortedList<Reservatie> geefMaterialen()
    {
        if (filterReservaties == null)
        {
            filterReservatie = FXCollections.observableList(reservatieDao.findAll());
            filterReservaties = new FilteredList(filterReservatie, p -> true);
        }
        return new SortedList<>(filterReservaties);
    }

    public void Zoek(String zoekTerm)
    {
        filterReservaties.setPredicate(r -> 
                {
                    if (zoekTerm == null || zoekTerm.isEmpty())
                    {
                        return true;
                    }
                    
                    return r.getGebruiker().getNaam().toLowerCase().contains(zoekTerm.toLowerCase()) || r.getMateriaal().getNaam().toLowerCase().contains(zoekTerm.toLowerCase());
        });
    }
}
