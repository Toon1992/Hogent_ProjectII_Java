/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Materiaal;
import domein.Reservatie;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
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
        filterReservaties.setPredicate(r
                -> 
                {
                    if (zoekTerm == null || zoekTerm.isEmpty())
                    {
                        return true;
                    }

                    return r.getGebruiker().getNaam().toLowerCase().contains(zoekTerm.toLowerCase()) || r.getMateriaal().getNaam().toLowerCase().contains(zoekTerm.toLowerCase());
        });
    }

    public void zoekOpBeginDatum(LocalDate zoekTerm)
    {
        Date datum = geefEersteDagVanDeWeek(zoekTerm);

        filterReservaties.setPredicate(r -> 
                {
                    if (zoekTerm == null)
                    {
                        return true;
                    }

                    return bevindtZichInDatumRange(datum, r, true);
        });
    }

    public void zoekOpEindDatum(LocalDate zoekTerm)
    {
        Date datum = geefEersteDagVanDeWeek(zoekTerm);

        filterReservaties.setPredicate(r -> 
                {
                    if (zoekTerm == null)
                    {
                        return true;
                    }

                    return bevindtZichInDatumRange(datum, r, false);
        });
    }

    private boolean bevindtZichInDatumRange(Date datum, Reservatie reservatie, boolean opBeginDatum)
    {
        Date reservatieDatum;
        if (opBeginDatum)
        {
            reservatieDatum = reservatie.getBeginDatum();
        } else
        {
            reservatieDatum = reservatie.getEindDatum();
        }

        if (reservatieDatum.getYear() >= datum.getYear())
        {
            if (reservatieDatum.getMonth() >= datum.getMonth())
            {
                return reservatieDatum.getDay() >= datum.getDay();
            }
        }

        return false;
    }

    private Date geefEersteDagVanDeWeek(LocalDate datum)
    {
        switch (datum.getDayOfWeek())
        {
            case TUESDAY:
                datum = datum.minusDays(1);
                break;
            case WEDNESDAY:
                datum = datum.minusDays(2);
                break;
            case THURSDAY:
                datum = datum.minusDays(3);
                break;
            case FRIDAY:
                datum = datum.minusDays(4);
                break;
            case SATURDAY:
                datum = datum.minusDays(5);
                break;
            case SUNDAY:
                datum = datum.minusDays(6);
                break;
            default:
        }

        return convertLocalDateToDate(datum);
    }

    private Date convertLocalDateToDate(LocalDate datum)
    {
        Instant instant = Instant.from(datum.atStartOfDay(ZoneId.of("GMT")));
        return Date.from(instant);
    }

    public void verwijderReservatue(Reservatie reservatie)
    {
        reservatieDao.startTransaction();
        reservatieDao.delete(reservatie);
        reservatieDao.commitTransaction();
        filterReservatie.remove(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }
}
