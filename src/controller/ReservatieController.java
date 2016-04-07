/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Gebruiker;
import domein.Materiaal;
import domein.Reservatie;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javafx.collections.transformation.SortedList;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import repository.ReservatieRepository;
import stateMachine.ReservatieStateEnum;

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
        return repository.geefReservaties();
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
    public void maakReservatie(Reservatie reservatie){
        repository.voegReservatieToe(reservatie);
    }
    public void wijzigReservatie(Reservatie reservatie, int aantal, Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, ReservatieStateEnum status){
        repository.wijzigReservatie(reservatie, aantal, gebruiker, startDate, endDate, materiaal, status);
    }
    public void verwijderReservatie(Reservatie reservatie)
     {
         repository.verwijderReservatue(reservatie);
     }
    public List<Reservatie> getReservatiesByDatum(Date startDatum, Date eindDatum, Materiaal materiaal){
        return repository.geefReservatiesByDatum(startDatum, eindDatum, materiaal);
    }
    public int[] berekenAantalBeschikbaar(Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, int aantal){
        return repository.berekenAantalbeschikbaarMateriaal(gebruiker, startDate, endDate, materiaal, aantal);
    }
    public void setFormatDatepicker(DatePicker dp){
        dp.setOnShowing(e-> Locale.setDefault(Locale.Category.FORMAT,Locale.FRANCE));
        String pattern = "dd-MM-yyy";
        dp.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
    });

}
}
