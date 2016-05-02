/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.imageio.ImageIO;
import javax.persistence.*;

/**
 *
 * @author Thomas
 */
@Entity
@NamedQueries(
        {
            @NamedQuery(name = "Materiaal.findAll", query = "Select a FROM Materiaal a")
        })
@Table(name = "Materiaal")
public class Materiaal
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MateriaalId")
    private int materiaalId;

    @Column(name = "Naam")
    private String naam;
    @Column(name = "Plaats")
    private String plaats;
    @Column(name = "Omschrijving")
    private String omschrijving;

    @Column(name = "ArtikelNr")
    private int artikelNr;
    @Column(name = "AantalOnbeschikbaar")
    private int aantalOnbeschikbaar;
    @Column(name = "AantalInCatalogus")
    private int aantalInCatalogus;

    @Column(name = "Prijs")
    private double prijs;

    @Column(name = "IsReserveerbaar")
    private boolean isReserveerbaar;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Firma firma;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "MateriaalDoelgroep", joinColumns = @JoinColumn(name = "ArtikelNr", referencedColumnName = "materiaalId"),
            inverseJoinColumns = @JoinColumn(name = "DoelgroepId", referencedColumnName = "doelgroepId"))
    Set<Doelgroep> doelgroepen;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "MateriaalLeergebied", joinColumns = @JoinColumn(name = "ArtikelNr", referencedColumnName = "materiaalId"),
            inverseJoinColumns = @JoinColumn(name = "LeergebiedId", referencedColumnName = "leergebiedId"))
    Set<Leergebied> leergebieden;

    @Lob
    @Column(name = "Foto")
    private byte[] foto;
    
    @ManyToOne
    private Verlanglijst verlanglijst;

    protected Materiaal()
    {

    }

    public String getNaam()
    {
        return naam;
    }

    public void setNaam(String naam)
    {
        if (naam.isEmpty())
        {
            throw new IllegalArgumentException("Naam moet ingevuld zijn!");
        }
        this.naam = naam;
    }

    public StringProperty naamProperty()
    {
        return new SimpleStringProperty(getNaam());
    }

    public void setPlaats(String plaats)
    {
        this.plaats = plaats;
    }

    public String getPlaats()
    {
        return plaats;
    }

    public StringProperty plaatsProperty()
    {
        return new SimpleStringProperty(getPlaats());
    }

    public void setIsReserveerbaar(boolean isReserveerbaar)
    {
        this.isReserveerbaar = isReserveerbaar;
    }

    public Boolean getIsReserveerbaar()
    {
        return isReserveerbaar;
    }

    public IntegerProperty artikelNummerProperty()
    {
        return new SimpleIntegerProperty(getArtikelNr());
    }

    public Materiaal(String foto, String naam, String omschrijving, String plaats, int artikelNr, int aantal, int aantalOnbeschikbaar, double prijs, boolean uitleenbaar, Firma firma, Set<Doelgroep> doelgroepen, Set<Leergebied> leergebieden)
    {
        setFoto(foto);
        setOmschrijving(omschrijving);
        setArtikelNr(artikelNr);
        setAantal(aantal);
        setAantalOnbeschikbaar(aantalOnbeschikbaar);
        setPrijs(prijs);
        setPlaats(plaats);
        setIsReserveerbaar(uitleenbaar);
        setFirma(firma);
        setDoelgroepen(doelgroepen);
        setLeergebieden(leergebieden);
        setNaam(naam);
    }

    public int getMateriaalId()
    {
        return materiaalId;
    }

    public void setMateriaalId(int materiaalId)
    {
        this.materiaalId = materiaalId;
    }

    public String getOmschrijving()
    {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving)
    {
        this.omschrijving = omschrijving;
    }

    public int getArtikelNr()
    {
        return artikelNr;
    }

    public void setArtikelNr(int artikelNr)
    {
        this.artikelNr = artikelNr;
    }

    public int getAantal()
    {
        return aantalInCatalogus;
    }

    public IntegerProperty aantalProperty()
    {
        return new SimpleIntegerProperty(getAantal() - getAantalOnbeschikbaar());
    }

    public void setAantal(int aantal)
    {
        if (aantal < 0)
        {
            throw new IllegalArgumentException("Aantal moet groter zijn dan 0");
        }
        this.aantalInCatalogus = aantal;
    }

    public int getAantalOnbeschikbaar()
    {
        return aantalOnbeschikbaar;
    }

    public void setAantalOnbeschikbaar(int aantalOnbeschikbaar)
    {
        this.aantalOnbeschikbaar = aantalOnbeschikbaar;
    }

    public double getPrijs()
    {
        return prijs;
    }

    public void setPrijs(double prijs)
    {
        this.prijs = prijs;
    }

    public boolean isUitleenbaar()
    {
        return isReserveerbaar;
    }

    public Firma getFirma()
    {
        return firma;
    }

    public void setFirma(Firma firma)
    {
        this.firma = firma;
    }

    public Set<Doelgroep> getDoelgroepen()
    {
        return doelgroepen;
    }

    public void setDoelgroepen(Set<Doelgroep> doelgroepen)
    {
        this.doelgroepen = doelgroepen;
    }

    public Set<Leergebied> getLeergebieden()
    {
        return leergebieden;
    }

    public void setLeergebieden(Set<Leergebied> leergebieden)
    {
        this.leergebieden = leergebieden;
    }

    public void setFoto(String url)
    {
        if (url.isEmpty())
        {
            this.foto = null;
        } else
        {
            File fi = new File(url);
            byte[] fileContent = null;
            try
            {
                fileContent = Files.readAllBytes(fi.toPath());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            this.foto = fileContent;
        }
    }

    public BufferedImage getFoto()
    {
        BufferedImage bufferedImage = null;
        try
        {
            if (foto == null)
            {
                bufferedImage = null;
            } else
            {
                bufferedImage = ImageIO.read(new ByteArrayInputStream(foto));
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    @Override
    public String toString()
    {
        return naam;
    }
}
