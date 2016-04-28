package domein;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import domein.Firma;
import domein.Materiaal;
import domein.MateriaalCatalogus;
import domein.MateriaalCatalogus.MateriaalFilter;
import domein.MateriaalData;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.mockito.Matchers.anyString;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import persistentie.FirmaDao;
import persistentie.MateriaalDao;

/**
 *
 * @author donovandesmedt
 */
public class MateriaalCatalogusTest {

    @Mock
    private MateriaalDao materiaalDao;
    @Mock
    private FirmaDao firmaDao;
    private List<Materiaal> materialen;
    private MateriaalCatalogus catalogus;
    private MateriaalData materiaalData;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        materiaalData = new MateriaalData();
        trainDummy();
        catalogus = new MateriaalCatalogus(materiaalDao, firmaDao);
    }

    public void trainDummy() {
        Mockito.when(materiaalDao.findAll()).thenReturn(materiaalData.geefMaterialen());
        Mockito.when(firmaDao.findAll()).thenReturn(materiaalData.geefFirmas());
        Mockito.when(materiaalDao.getMaterialen()).thenReturn(materiaalData.geefMaterialen());
        Mockito.when(firmaDao.geefFirma("Prisma")).thenReturn(materiaalData.geefPrisma());
    }

    @Test
    public void geefFirmaGeeftJuisteFirmaTerug() {
        Firma f = catalogus.geefFirma("Prisma", "helpdesk@prisma.com");
        Assert.assertEquals("Prisma", f.getNaam());
        Assert.assertEquals("helpdesk@prisma.com", f.getEmailContact());
    }

    @Test(expected = IllegalArgumentException.class)
    public void geefFoutAlsMateriaalBestaatMetNaam() {
        materialen = catalogus.geefMaterialen();
        catalogus.controleerUniekheidMateriaalnaam(null,"Wereldbol");
    }

//    @Test
//    public void verwijderMateriaalTest() {
//        catalogus.geefMaterialen();
//        Materiaal m = materiaalData.getMateriaalRekenmachine();
//        catalogus.verwijderMateriaal(m);
//        materialen = catalogus.geefMaterialen();
//        Assert.assertEquals(1, materialen.size());
//    }

//    @Test
//    public void testZoekMateriaalCorrecteZoektermGeeftMateriaalTerug() {
//        catalogus.geefMaterialen();
//        Set<String> zoekTermen = new HashSet<>();
//        zoekTermen.add("Wereldbol");
//        catalogus.zoek(zoekTermen);
//        Assert.assertEquals(1, catalogus.geefMaterialen().size());
//    }

    @Test
    public void testZoekMateriaalParameterNullGeeftAlleMaterialenTerug() {
        materialen = catalogus.geefMaterialen();
        catalogus.zoek(null);
        Assert.assertEquals(2, materialen.size());
    }

    @Test
    public void testZoekMateriaalLegeSetGeeftAlleMaterialenTerug() {
        materialen = catalogus.geefMaterialen();
        catalogus.zoek(new HashSet<>());
        Assert.assertEquals(2, materialen.size());
    }

    @Test
    public void testZoekMateriaalOnbestaandeZoektermGeeftGeenMaterialenTerug() {
        materialen = catalogus.geefMaterialen();
        Set<String> zoekTermen = new HashSet<>();
        zoekTermen.add("blablabla");
        catalogus.zoek(zoekTermen);
        Assert.assertEquals(0, materialen.size());
    }

//    @Test
//    public void testFilterMateriaalMetEenZoektermGeeftAlleMaterialenTerugVanSoort() {
//        materialen = catalogus.geefMaterialen();
//        Set<String> zoekTermen = new HashSet<>();
//        zoekTermen.add("Lager onderwijs");
//        catalogus.filterMaterialen(MateriaalFilter.DOELGROEP, zoekTermen);
//        Assert.assertEquals(1, materialen.size());
//    }
//
//    @Test
//    public void testFilterMateriaalMetMeerdereZoektermGeeftAlleMaterialenTerugVanSoort() {
//        materialen = catalogus.geefMaterialen();
//        Set<String> zoekTermen = new HashSet<>();
//        zoekTermen.add("Lager onderwijs");
//        zoekTermen.add("Secundair onderwijs");
//        catalogus.filterMaterialen(MateriaalFilter.DOELGROEP, zoekTermen);
//        Assert.assertEquals(2, materialen.size());
//    }

}
