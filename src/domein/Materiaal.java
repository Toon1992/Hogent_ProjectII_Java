/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Set;
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
public class Materiaal
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int materiaalId;
    private String naam, plaats, foto, omschrijving;
    private int artikelNr, aantalInCatalogus, aantalOnbeschikbaar;
    private double prijs;
    private boolean isReserveerbaar;
    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Firma firma;

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="MateriaalDoelgroep", joinColumns=@JoinColumn(name="ArtikelNr", referencedColumnName="materiaalId"),
     inverseJoinColumns=@JoinColumn(name="DoelgroepId", referencedColumnName="doelgroepId"))
    Set<Doelgroep> doelgroepen;
    
    @ManyToMany(fetch=FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinTable(name="MateriaalLeergebied", joinColumns=@JoinColumn(name="ArtikelNr", referencedColumnName="materiaalId"),
      inverseJoinColumns=@JoinColumn(name="LeergebiedId", referencedColumnName="leergebiedId")) 
    Set<Leergebied> leergebieden;

    @Lob
    private byte[] image;

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

    public StringProperty uitleenbaarProperty()
    {
        if (isReserveerbaar)
        {
            return new SimpleStringProperty("Student");
        } else
        {
            return new SimpleStringProperty("Lector");
        }
    }

    public Materiaal(String foto, String naam, String omschrijving, String plaats, int artikelNr, int aantal, int aantalOnbeschikbaar, double prijs, boolean uitleenbaar, Firma firma, Set<Doelgroep> doelgroepen, Set<Leergebied> leergebieden)
    {
        setFoto(foto);
        setImage(foto);
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

    public int getMateriaalId() {
        return materiaalId;
    }

    public void setMateriaalId(int materiaalId)
    {
        this.materiaalId = materiaalId;
    }

    public String getFoto()
    {
        return foto;
    }

    public void setFoto(String foto)
    {
        this.foto = foto;
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

    public void setImage(String url) {
        File fi = new File(url);
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(fi.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.image = fileContent;
    }
    public BufferedImage getImage(){
        BufferedImage bufferedImage=null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
}
