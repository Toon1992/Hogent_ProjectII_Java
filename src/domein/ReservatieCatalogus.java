/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.sun.org.apache.regexp.internal.RE;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.ReservatieDao;
import persistentie.ReservatieDaoJpa;
import stateMachine.ReservatieStateEnum;

/**
 * @author ToonDT
 */
public class ReservatieCatalogus {

    private FilteredList<Reservatie> filterReservaties;
    private ReservatieDao reservatieDao;
    private ObservableList<Reservatie> filterReservatie;

    public ReservatieCatalogus(ReservatieDao reservatieDao) {
        setReservatieDao(reservatieDao);
    }

    public void setReservatieDao(ReservatieDao reservatieDao) {
        this.reservatieDao = reservatieDao;
    }

    public SortedList<Reservatie> geefReservaties() {
        if (filterReservaties == null) {
            filterReservatie = FXCollections.observableList(reservatieDao.findAll());
            filterReservaties = new FilteredList(filterReservatie, p -> true);
        }
        return new SortedList<>(filterReservaties);
    }

    public void Zoek(String zoekTerm) {
        filterReservaties.setPredicate(r
                ->
        {
            if (zoekTerm == null || zoekTerm.isEmpty()) {
                return true;
            }

            return r.getGebruiker().getNaam().toLowerCase().contains(zoekTerm.toLowerCase()) || r.getMateriaal().getNaam().toLowerCase().contains(zoekTerm.toLowerCase());
        });
    }

    public void zoekOpBeginDatum(LocalDate zoekTerm) {
        Date datum = HulpMethode.geefEersteDagVanDeWeek(zoekTerm);

        filterReservaties.setPredicate(r
                ->
        {
            if (zoekTerm == null) {
                return true;
            }

            return bevindtZichInDatumRange(datum, r, true);
        });
    }

    public void zoekOpEindDatum(LocalDate zoekTerm) {
        Date datum = HulpMethode.geefEersteDagVanDeWeek(zoekTerm);

        filterReservaties.setPredicate(r
                ->
        {
            if (zoekTerm == null) {
                return true;
            }

            return bevindtZichInDatumRange(datum, r, false);
        });
    }

    private boolean bevindtZichInDatumRange(Date datum, Reservatie reservatie, boolean opBeginDatum) {
        Date reservatieDatum;
        datum.setHours(0);
        datum.setMinutes(0);
        datum.setSeconds(0);

        if (opBeginDatum) {
            reservatieDatum = reservatie.getBeginDatum();
            reservatieDatum.setHours(0);
            reservatieDatum.setMinutes(0);
            reservatieDatum.setSeconds(0);
            if (reservatieDatum.equals(datum)) {
                return true;
            }
            return reservatieDatum.after(datum);
        } else {
            reservatieDatum = reservatie.getEindDatum();
            reservatieDatum.setHours(0);
            reservatieDatum.setMinutes(0);
            reservatieDatum.setSeconds(0);
            if (reservatieDatum.equals(datum)) {
                return true;
            }
            return reservatieDatum.before(datum);
        }
    }

    public List<Reservatie> geefReservatiesByDatum(Date startDatum, Date eindDatum, Materiaal materiaal) {
        return reservatieDao.getReservaties(startDatum, eindDatum, materiaal);
    }

    public int[] berekenAantalbeschikbaarMateriaal(Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, int aantal, int origineelAantal) {
        List<Reservatie> overschrijvendeReservaties = geefReservatiesByDatum(startDate, endDate, materiaal);
        int aantalStudent = 0;
        //Indien lector enkel de reservaties van lector opvragen
        if (gebruiker.getType().equals("LE")) {
            aantalStudent = overschrijvendeReservaties.stream().filter(r -> r.getGebruiker().getType().equals("ST")).mapToInt(r -> r.getAantalGereserveerd()).sum();
            overschrijvendeReservaties = overschrijvendeReservaties.stream().filter(r -> r.getGebruiker().getType().equals("LE")).collect(Collectors.toList());
        }
        //Aantal stuks dat reeds onbeschikbaar zijn voor de gebruiker (lector of student)
        int aantalGereserveerdeStuks = overschrijvendeReservaties.stream().mapToInt(r -> r.getAantalGereserveerd()).sum() - origineelAantal;
        aantalGereserveerdeStuks = aantalGereserveerdeStuks < 0 ? 0 : aantalGereserveerdeStuks;
        int aantalBeschikbaar = materiaal.getAantal() - materiaal.getAantalOnbeschikbaar() - aantalGereserveerdeStuks;
        int aantalOverruled = aantalStudent + aantalGereserveerdeStuks + aantal - materiaal.getAantal() - materiaal.getAantalOnbeschikbaar();
        aantalOverruled = aantalOverruled < 0 ? 0 : aantalOverruled;
        return new int[]
                {
                        aantalBeschikbaar, aantalOverruled
                };
    }

    public Set<Reservatie> overruleStudent(int aantalOverruled, Materiaal materiaal) {
        Set<Reservatie> teOverrulenReservatie = new HashSet<>();
        List<Reservatie> reservaties = geefReservaties().stream().filter(r -> r.getMateriaal().equals(materiaal)).collect(Collectors.toList());
        reservaties = reservaties.stream().filter(r -> r.getGebruiker().getType().equals("ST") && r.getReservatieStateEnum().equals(ReservatieStateEnum.Gereserveerd)).sorted(Comparator.comparing(Reservatie::getAanmaakDatum)).collect(Collectors.toList());
        boolean nogTeOverrulen = true;
        while (nogTeOverrulen) {
            Reservatie reservatie = reservaties.get(0);

            int aantal = reservatie.getAantalGereserveerd();

            reservatie.setReservatieStateEnum(ReservatieStateEnum.Overruled);
            wijzigReservatieObject(reservatie);

            //Indien er nog een aantal stuks overschieten na het overrulen, wordt er een nieuwe reservatie gemaakt
            if (aantal - aantalOverruled > 0) {
                teOverrulenReservatie.add(new Reservatie(aantal - aantalOverruled, 0, 0, reservatie.getBeginDatum(), reservatie.getEindDatum(), new Date(), null, ReservatieStateEnum.Gereserveerd, reservatie.getGebruiker(), reservatie.getMateriaal()));
            }

            aantalOverruled -= aantal;
            reservaties.remove(reservatie);

            if (aantalOverruled <= 0) {
                nogTeOverrulen = false;
            }
        }
        return teOverrulenReservatie;
    }

    public Reservatie maakReservatieObject(int aantal, int aantalUitgeleend, int aantalTerug, Date startDate, Date endDate, ReservatieStateEnum status, Gebruiker gebruiker, Materiaal materiaal) {
        SortedSet<Dag> dagen = new TreeSet<>();
        if (gebruiker.getType().equals("LE")) {
            Date maandag = HulpMethode.geefEersteDagVanDeWeek(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            Date vrijdag = HulpMethode.convertLocalDateToDate(maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));
            //alle dagen tussen begin en einddatum in de list plaatsen
            Date beginDatum = startDate;
            while (beginDatum.before(endDate)) {
                dagen.add(new Dag(beginDatum));
                beginDatum = HulpMethode.convertLocalDateToDate(beginDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1));
            }
            dagen.add(new Dag(endDate));
        }
        return new Reservatie(aantal, aantalUitgeleend, aantalTerug, startDate, endDate, new Date(), dagen, status, gebruiker, materiaal);
    }

    public Set<Reservatie> verwijderVervallenReservaties() {
        LocalDate thismomentLastWeek = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date deadline = new Date();
        deadline.setHours(17);
        deadline.setMinutes(1);

        Date vorigeWeek = HulpMethode.convertLocalDateToDate(thismomentLastWeek.minusWeeks(1));

        // De reservaties waarbij niet alle stuks tijdig werden teruggebracht worden een week bewaard na de deadline om het nodige administratieve werk te doen
        Set<Reservatie> reservaties = geefReservaties().stream().filter(reservatie
                -> reservatie.getEindDatum().before(vorigeWeek) && (reservatie.getAantalTeruggebracht() < reservatie.getAantalUitgeleend()) && reservatie.getAantalTeruggebracht() > 0
        ).collect(Collectors.toSet());

        // De reservaties waarvan alle stuks werden teruggebracht worden instant verwijderd wanneer de deadline verlopen is.
        reservaties.addAll(geefReservaties().stream().filter(reservatie -> reservatie.getEindDatum().before(deadline) && reservatie.getAantalUitgeleend() == reservatie.getAantalTeruggebracht()).collect(Collectors.toList()));
        return reservaties;
    }

    public Set<Reservatie> aanpassenTeLaatTerugGebrachteReservaties() {
        Date deadline = new Date();
        deadline.setHours(17);
        deadline.setMinutes(1);

        //Alle reservaties die niet tijdig zijn teruggebracht.
        Set<Reservatie> reservatiesTeLaat = geefReservaties().stream().filter(reservatie -> reservatie.getEindDatum().before(deadline) && reservatie.getAantalTeruggebracht() == 0 && reservatie.getAantalUitgeleend() > 0).collect(Collectors.toSet());
        reservatiesTeLaat.forEach(reservatie -> reservatie.setReservatieStateEnum(ReservatieStateEnum.TeLaat));

        //Alle reservaties die hierop volgen moeten ook op status te laat worden gezet.
        Set<Reservatie> aangepasteReservaties = aanpassenOpeenvolgendeReservaties(reservatiesTeLaat);
        reservatiesTeLaat.addAll(aangepasteReservaties);

        return reservatiesTeLaat;
    }

    public Set<Reservatie> aanpassenOpeenvolgendeReservaties(Set<Reservatie> reservaties) {
        int week, aantal, aantalBenodigdeStuks, huidigBeschikbaar;
        Reservatie reser;
        //Materiaal materiaal;
        List<Reservatie> reservatiePool = geefReservaties();
        Set<Reservatie> aangepasteReservaties = new HashSet<>();
        Map<Materiaal, Map<Integer, Integer>> aantalBeschikbaar = berekenAantalBeschikbaarPerMateriaalPerWeek(reservaties);
        Map<Integer, Integer> aantallenPerWeek;
        for (Reservatie reservatie : reservaties) {

            //Variabelen
            Materiaal materiaal = reservatie.getMateriaal();
            aantal = reservatie.getAantalUitgeleend();
            week = HulpMethode.getWeekOfDate(reservatie.getEindDatum()) + 1;
            Date maandag = HulpMethode.getFirstDayOfWeek(week);
            Date vrijdag = HulpMethode.convertLocalDateToDate(maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));

            //Alle reservaties van hetzelfde materiaal die gepland staan voor de opeenvolgende week
            reservatiePool = reservatiePool.stream().filter(r -> r.getMateriaal().equals(materiaal) && r.getBeginDatum().before(vrijdag) && r.getEindDatum().after(maandag) && (r.getReservatieStateEnum().equals(ReservatieStateEnum.Gereserveerd) || r.getReservatieStateEnum().equals(ReservatieStateEnum.Geblokkeerd))).sorted(Comparator.comparing(Reservatie::getAanmaakDatum)).collect(Collectors.toList());
            //De reservaties worden slechts op te laat geplaatst indien er niet meer voldoende stuks beschikbaar zijn om te reserveren.
            huidigBeschikbaar = aantalBeschikbaar.get(materiaal).get(week);

            while (aantal > 0 && reservatiePool.size() > 0) {
                reser = reservatiePool.get(0);
                if (reser.getAantalGereserveerd() <= huidigBeschikbaar) {
                    huidigBeschikbaar -= reser.getAantalGereserveerd();
                } else {
                    reser.setReservatieStateEnum(ReservatieStateEnum.TeLaat);
                    aangepasteReservaties.add(reser);
                }
                aantal -= reser.getAantalGereserveerd();
                reservatiePool.remove(reser);
            }
            //De gewijzigde aantallen updaten in de betreffende week in de map met aantallen per week
            //Deze map wordt geupdate in de map aantalBeschikbaar
            aantallenPerWeek = aantalBeschikbaar.get(materiaal);
            aantallenPerWeek.put(week, huidigBeschikbaar);
            aantalBeschikbaar.put(materiaal, aantallenPerWeek);
        }
        return aangepasteReservaties;
    }

    public Set<Reservatie> checkAlleReservatiesBeschikbaar() {
        // Kijken of de reservaties van vorige week op TeLaat staan, indien ja, de opeenvolgende reservaties ook op te laat zetten.
        Date maandag, vrijdag;
        maandag = HulpMethode.geefEersteDagVanDeWeek(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        vrijdag = HulpMethode.convertLocalDateToDate(maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));
        Set<Reservatie> reservaties = geefReservaties().stream().filter(reservatie -> reservatie.getReservatieStateEnum().equals(ReservatieStateEnum.TeLaat) && reservatie.getBeginDatum().before(vrijdag) && reservatie.getEindDatum().after(maandag)).collect(Collectors.toSet());
        Set<Reservatie> aangepasteReservaties = aanpassenOpeenvolgendeReservaties(reservaties);
        return aangepasteReservaties;
    }

    public Set<Reservatie> wijzigLaatbinnenGebrachteReservatie(Reservatie reservatie) {
        Set<Reservatie> aangepasteReservaties = new HashSet<>();
        int orgAantal = reservatie.getAantalTeruggebracht() == 0 ? reservatie.getAantalUitgeleend() : reservatie.getAantalTeruggebracht();

        Materiaal materiaal = reservatie.getMateriaal();
        //De week na de week van de binnengebrachte reservatie
        int orgWeek = HulpMethode.getWeekOfDate(reservatie.getEindDatum()) + 1;
        //Een week na de huidige datum
        int volgendeWeek = HulpMethode.getWeekOfDate(new Date()) + 1;

        //Als een reservatie te laat binnen wordt gebracht, moeten alle reservaties die door deze reservatie de status te laat kregen terug op gereserveerd komen
        //dit betreft alle reservaties tot 1 week na de huidige datum
        while (orgWeek != (volgendeWeek + 1)) {
            int aantal = orgAantal;
            Date beginDatum = HulpMethode.getFirstDayOfWeek(orgWeek);
            Date eindDatum = HulpMethode.convertLocalDateToDate(beginDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));
            List<Reservatie> reservatiePool = geefReservaties().stream().filter(r -> r.getReservatieStateEnum().equals(ReservatieStateEnum.TeLaat) && r.getMateriaal().equals(materiaal) && r.getBeginDatum().before(eindDatum) && r.getEindDatum().after(beginDatum)).collect(Collectors.toList());
            ;
            //Indien aantal niet op 0 wordt gezet wanneer de reservatiepool leeg is, ontstaat er een oneindige lus in de while loop.
            if (reservatiePool.isEmpty()) {
                aantal = 0;
            }
            while (aantal > 0 && !reservatiePool.isEmpty()) {
                Reservatie changeRes = reservatiePool.get(0);
                changeRes.setReservatieStateEnum(ReservatieStateEnum.Gereserveerd);
                reservatiePool.remove(changeRes);
                aangepasteReservaties.add(changeRes);
                aantal -= changeRes.getAantalGereserveerd();
            }
            orgWeek++;
        }
        return aangepasteReservaties;
    }

    public ObservableList<Reservatie> geefConflictReservaties() {
        List<Reservatie> conflicts = new ArrayList<>();
        Set<Reservatie> reservaties = geefReservaties().stream().collect(Collectors.toSet());
        Map<Materiaal, Map<Integer, Integer>> aantalBeschikbaar = new HashMap<>();

        //Map maken van reservaties gegroupeerd per materiaal
        Map<Materiaal, Set<Reservatie>> aantalGereserveerd = convertSetReservatiesToMap(reservaties);
        //Voor elke reservatie van een materiaal een map maken die het aantal gereserveerde stuks per week bijhoudt.
        aantalGereserveerd.entrySet().stream().forEach(e -> {
            aantalBeschikbaar.put(e.getKey(), berekenGereserveerdPerWeek(e.getValue(), e.getKey()));
        });

        //elk materiaal overlopen en per week checken of er meer gereserveerd zijn dan beschikbaar, zoja dan wordt die row in rood aangekleurd
        aantalBeschikbaar.entrySet().stream().forEach(materiaal -> {
            Materiaal mat = materiaal.getKey();
            materiaal.getValue().entrySet().stream().forEach(week -> {
                int weekNr = week.getKey();
                int aantal = week.getValue();
                //Indien het aantal gereserveerde stuks groter is dan het aantal beschikbare stuks is er een conflict
                if (aantal > mat.getAantal()) {
                    reservaties.stream().filter(reservatie ->
                            HulpMethode.getWeekOfDate(reservatie.getBeginDatum()) == weekNr &&
                                    reservatie.getMateriaal().equals(mat)
                    ).forEach(reservatie -> conflicts.add(reservatie));
                }
            });
        });
        return FXCollections.observableArrayList(conflicts);
    }

    public void voegReservatieToe(Reservatie reservatie) {
        filterReservatie.remove(reservatie);
        filterReservatie.add(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }

    public void wijzigReservatie(Reservatie reservatie, int aantal, int aantalUit, int aantalTerug, Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, ReservatieStateEnum status) {
        Reservatie oldReservatie = reservatie;
        //De parameters setten
        reservatie.setAantalGereserveerd(aantal);
        if (aantalUit != -1) {
            reservatie.setAantalUitgeleend(aantalUit);
        }
        if (aantalTerug != -1) {
            reservatie.setAantalTeruggebracht(aantalTerug);
        }
        reservatie.setGebruiker(gebruiker);
        reservatie.setStartDatum(startDate);
        reservatie.setEindDatum(endDate);
        reservatie.setMateriaal(materiaal);
        if (status.equals(ReservatieStateEnum.TeLaat) && aantalTerug > 0) {
            reservatie.setReservatieStateEnum(gebruiker.getType().equals("ST") ? ReservatieStateEnum.Gereserveerd : ReservatieStateEnum.Geblokkeerd);
            wijzigLaatbinnenGebrachteReservatie(reservatie);
        } else {
            reservatie.setReservatieStateEnum(status);
        }
        if (aantalUit > aantalTerug && aantalTerug == 0) {
            reservatie.setReservatieStateEnum(ReservatieStateEnum.TeLaat);
            aanpassenTeLaatTerugGebrachteReservaties();
        }

        wijzigReservatieObject(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }

    private void wijzigReservatieObject(Reservatie reservatie) {
        ReservatieDaoJpa jpa = (ReservatieDaoJpa) reservatieDao;
        jpa.startTransaction();
        jpa.update(reservatie);
        jpa.commitTransaction();
    }

    public void verwijderReservatue(Reservatie reservatie) {
        filterReservatie.remove(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }

    /**
     * ##################################################################################################################
     * Methoden om de reservaties om te vormen tot een map key: materiaal value: per week het aantal gereserveerde stuks
     * ##################################################################################################################
     */
    private Map<Materiaal, Map<Integer, Integer>> berekenAantalBeschikbaarPerMateriaalPerWeek(Set<Reservatie> reservaties) {
        Map<Materiaal, Map<Integer, Integer>> beschikbaarheden = new HashMap<>();
        // Op basis van de reservaties een map maken met per materiaal en set van reservaties.
        Map<Materiaal, Set<Reservatie>> reservatiesMateriaal = convertSetReservatiesToMap(reservaties);

        // Deze map overlopen en een map maken met key: het materiaal en value: een map die per week het aantal gereserveerde stuks bevat
        reservatiesMateriaal.entrySet().stream().forEach(e
                ->
        {
            beschikbaarheden.put(e.getKey(), berekenAantalBeschikbaarMateriaalPerWeek(e.getValue()));
        });

        return beschikbaarheden;
    }

    private Map<Materiaal, Set<Reservatie>> convertSetReservatiesToMap(Set<Reservatie> reservaties) {
        Map<Materiaal, Set<Reservatie>> reservatiesMateriaal = new HashMap<>();
        reservaties.forEach(reservatie
                ->
        {
            if (reservatiesMateriaal.containsKey(reservatie.getMateriaal())) {
                Set<Reservatie> prevReservateies = reservatiesMateriaal.get(reservatie.getMateriaal());
                prevReservateies.add(reservatie);
                reservatiesMateriaal.put(reservatie.getMateriaal(), prevReservateies);
            } else {
                reservatiesMateriaal.put(reservatie.getMateriaal(), new HashSet<Reservatie>(Arrays.asList(reservatie)));
            }
        });
        return reservatiesMateriaal;
    }
    private Map<Integer, Integer> berekenGereserveerdPerWeek(Set<Reservatie> reservaties, Materiaal materiaal){
        Map<Integer, Integer> beschikbaarheden = new HashMap<>();
        reservaties.stream().filter(reservatie -> reservatie.getMateriaal().equals(materiaal)).forEach(reservatie -> {
            int week = HulpMethode.getWeekOfDate(reservatie.getBeginDatum());
            if(beschikbaarheden.containsKey(week)){
                int aantal = beschikbaarheden.get(week);
                aantal += reservatie.getAantalGereserveerd();
                beschikbaarheden.put(week, aantal);
            }
            else{
                beschikbaarheden.put(week, reservatie.getAantalGereserveerd());
            }
        });
        return beschikbaarheden;
    }
    private Map<Integer, Integer> berekenAantalBeschikbaarMateriaalPerWeek(Set<Reservatie> reservaties) {
        Map<Integer, Integer> aantalBeschikbaar = new HashMap<>();
        int huidigBeschikbaar, week;

        // De beschikbaarheden berekenen voor de week die volgt op de week van de reservatie
        for (Reservatie reservatie : reservaties) {
            //Indien er reeds voor een week reeds stuks gereserveerd staan, wordt het nieuwe aantal van het huidige beschikbaar afgetrokken.
            week = HulpMethode.getWeekOfDate(reservatie.getEindDatum()) + 1;
            Date maandag = HulpMethode.getFirstDayOfWeek(week);
            Date vrijdag = HulpMethode.convertLocalDateToDate(maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));
            Materiaal materiaal = reservatie.getMateriaal();

            if (aantalBeschikbaar.containsKey(week)) {
                huidigBeschikbaar = aantalBeschikbaar.get(week);
                huidigBeschikbaar -= getAantalGereserveerdVoorMateriaalInRange(maandag, vrijdag, materiaal);
            } else {
                huidigBeschikbaar = materiaal.getAantal() - materiaal.getAantalOnbeschikbaar() - getAantalGereserveerdVoorMateriaalInRange(maandag, vrijdag, materiaal);
            }

            aantalBeschikbaar.put(week, huidigBeschikbaar);

        }
        return aantalBeschikbaar;
    }

    private int getAantalGereserveerdVoorMateriaalInRange(Date startDate, Date endDate, Materiaal materiaal) {
        return geefReservaties()
                .stream()
                .filter(r
                        -> r.getEindDatum().after(startDate)
                        && r.getBeginDatum().before(endDate)
                        && r.getMateriaal().equals(materiaal)
                        && (r.getReservatieStateEnum().equals(ReservatieStateEnum.Gereserveerd) || r.getReservatieStateEnum().equals(ReservatieStateEnum.Geblokkeerd)))
                .mapToInt(res -> res.getAantalGereserveerd()).sum();
    }
}
