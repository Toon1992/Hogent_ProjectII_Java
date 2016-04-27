/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

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
    private int firmaId;
    private String naam, email, adres;

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
        this.email = emailContact;
    }

}
