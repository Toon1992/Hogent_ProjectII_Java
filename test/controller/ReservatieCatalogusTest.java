/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import domein.Materiaal;
import domein.Reservatie;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import persistentie.ReservatieDao;
import domein.ReservatieCatalogus;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author donovandesmedt
 */
public class ReservatieCatalogusTest {
    private ReservatieData data;
    private ReservatieCatalogus repository;
    private List<Reservatie> reservaties;
    @Mock
    private ReservatieDao reservatieDao;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        data = new ReservatieData();
        trainDummy();
        repository = new ReservatieCatalogus(reservatieDao);
        reservaties = repository.geefReservaties();
    }

    public void trainDummy() {
        Mockito.when(reservatieDao.findAll()).thenReturn(data.getSortedReservaties());
    }

    @Test
    public void testZoekReservatieCorrecteZoektermGeeftReservatieTerug(){
        String zoekterm = "wereldbol";
        repository.Zoek(zoekterm);
        Assert.assertEquals(2, reservaties.size());
    }
    @Test
    public void testZoekReservatieLegeStringGeeftAlleReservatiesTerug(){
        String zoekterm = "";
        repository.Zoek(zoekterm);
        Assert.assertEquals(data.getSortedReservaties().size(), reservaties.size());
    }
    @Test
    public void testZoekReservatieSlechteZoektermGeeftGeenReservatieTerug(){
        String zoekterm = "blablabla";
        repository.Zoek(zoekterm);
        Assert.assertEquals(0, reservaties.size());
    }
    @Test
    public void testZoekReservatieOpCorrecteBeginDatumGeeftReservatieTerug(){
        Date beginDatum = data.getReservatieStudent1().getBeginDatum();
        repository.zoekOpBeginDatum(beginDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        Assert.assertEquals(1, reservaties.size());
    }
    @Test
    public void testZoekReservatieOpSlechteBeginDatumGeeftGeenReservatieTerug(){
        Date beginDatum = new Date(111, 3, 11);
        repository.zoekOpBeginDatum(beginDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        Assert.assertEquals(0, reservaties.size());
    }
    @Test
    public void geefReservatiesByCorrecteDatumGeeftReservatieTerug(){
        Reservatie reservatie = data.getReservatieStudent1();
        Date beginDatum = reservatie.getBeginDatum();
        Date eindDatum = reservatie.getEindDatum();
        Materiaal materiaal = reservatie.getMateriaal();
        Mockito.when(reservatieDao.getReservaties(beginDatum, eindDatum, reservatie.getMateriaal())).thenReturn(Arrays.asList(reservatie));

        Assert.assertEquals(1, repository.geefReservatiesByDatum(beginDatum, eindDatum, materiaal).size());
    }
    @Test
    public void geefReservatiesBySlechteDatumGeeftGeenReservatieTerug(){
        Reservatie reservatie = data.getReservatieStudent1();
        Date beginDatum = new Date(114, 1, 1);
        Date eindDatum = new Date(114, 1, 1);
        Materiaal materiaal = reservatie.getMateriaal();
        Mockito.when(reservatieDao.getReservaties(beginDatum, eindDatum, reservatie.getMateriaal())).thenReturn(Arrays.asList());
        Assert.assertEquals(0, repository.geefReservatiesByDatum(beginDatum, eindDatum, materiaal).size());
    }
    @Test
    public void berekenAantalBeschikbaar4ReservatieAantal0Return4BeschikbaarStudent(){
        Reservatie r1 = data.getReservatieStudent1();
        int[] data = berekenAantalBeschikbaar(Arrays.asList(), r1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];
        Assert.assertEquals(4, aantalBeschikbaar);
        Assert.assertEquals(0, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaar4ReservatieAantal4Returns0BeschikbaarStudent(){
        Reservatie r1 = data.getReservatieStudent1();
        int[] data = berekenAantalBeschikbaar(Arrays.asList(r1), r1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];
        Assert.assertEquals(2, aantalBeschikbaar);
        Assert.assertEquals(0, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaarStudent1_ReservatieAantal3_Returns4BeschikbaarLector_1OverruledStudent(){
        Reservatie reservatieLector1 = data.getReservatieLector1();
        Reservatie reservatieStudent1 = data.getReservatieStudent1();
        Reservatie reservatieStudent2 = data.getReservatieStudent2();

        int[] data = berekenAantalBeschikbaar(Arrays.asList(reservatieStudent1, reservatieStudent2), reservatieLector1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];

        Assert.assertEquals(4, aantalBeschikbaar);
        Assert.assertEquals(1, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaarStudent0_ReservatieAantal3_Returns4BeschikbaarLector_2OverruledStudent(){
        Reservatie reservatieLector1 = data.getReservatieLector1();
        Reservatie reservatieStudent1 = data.getReservatieStudent1();
        Reservatie reservatieStudent2 = data.getReservatieStudent2();
        reservatieStudent2.setAantalGereserveerd(2);

        int[] data = berekenAantalBeschikbaar(Arrays.asList(reservatieStudent1, reservatieStudent2), reservatieLector1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];

        Assert.assertEquals(4, aantalBeschikbaar);
        Assert.assertEquals(2, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaarLector1_ReservatieAantal3_Returns1BeschikbaarLector_0OverruledStudent(){
        Reservatie reservatieLector1 = data.getReservatieLector1();


        int[] data = berekenAantalBeschikbaar(Arrays.asList(reservatieLector1), reservatieLector1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];

        Assert.assertEquals(1, aantalBeschikbaar);
        Assert.assertEquals(2, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaarStudent2_ReservatieAantal2_Returns4BeschikbaarLector_0OverruledStudent(){
        Reservatie reservatieLector1 = data.getReservatieLector1();
        Reservatie reservatieStudent1 = data.getReservatieStudent1();

        int[] data = berekenAantalBeschikbaar(Arrays.asList(reservatieStudent1), reservatieLector1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];

        Assert.assertEquals(4, aantalBeschikbaar);
        Assert.assertEquals(0, aantalOverruled);
    }
    private int[] berekenAantalBeschikbaar(List<Reservatie> reservaties, Reservatie data){
        Date beginDatum = data.getBeginDatum();
        Date eindDatum = data.getEindDatum();
        Materiaal materiaal = data.getMateriaal();
        Mockito.when(reservatieDao.getReservaties(beginDatum, eindDatum, data.getMateriaal())).thenReturn(reservaties);
        return repository.berekenAantalbeschikbaarMateriaal(data.getGebruiker(), beginDatum, eindDatum, data.getMateriaal(), 2, 0);
    }
}
