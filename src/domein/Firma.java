/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import exceptions.EmailException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Thomas
 */
@Entity
@NamedQueries(
        {
            @NamedQuery(name = "Firma.findByName", query = "Select a FROM Firma a WHERE a.naam= :Naam")
        })
@Table(name = "Firma")
public class Firma
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FirmaId")
    private int firmaId;

    @Column(name = "Naam")
    private String naam;

    @Column(name = "Email")
    private String email;

    @Column(name = "Adres")
    private String adres;

    @Column(name= "ContactPersoon")
    private String contactPersoon;

    @Column(name= "Website")
    private String website;

    protected Firma()
    {
    }

    public Firma(String naam, String emailContact)
    {
        this.naam = naam;
        this.email = emailContact;
    }

    public String getNaam()
    {
        return naam;
    }

    public void setNaam(String naam)
    {
        this.naam = naam;
    }

    public String getEmailContact()
    {
        return email;
    }

    public void setEmailContact(String emailContact)
    {
        if(!emailContact.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
            throw new EmailException("Email is niet correct.");
        }
        this.email = emailContact;
    }

}
