/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Doelgroep;
import domein.Leergebied;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author manu
 * @param <T>
 */
public class GeneriekeGebieden<T> implements GeneriekeGebiedenInterface<T>
{

    public GeneriekeGebieden()
    {

    }

    @Override
    public Set<T> geefGebiedenVoorNamen(List<String> namen, T filter)
    {
        Set<T> filters = new HashSet<>();
        List<T> lijst = new ArrayList<>();
        try
        {
            if (filter.getClass() == Doelgroep.class)
            {
                GenericDaoJpa<Doelgroep> jpa = new GenericDaoJpa<>(Doelgroep.class);
                lijst = (List<T>) jpa.findAll();
                for (Doelgroep gebied : (List<Doelgroep>) lijst)
                {
                    namen.stream().filter((naam) -> (gebied.getNaam().equals(naam))).forEach((_item)
                            -> 
                            {
                                filters.add((T) gebied);
                    });
                }

            }
            if (filter.getClass() == Leergebied.class)
            {
                GenericDaoJpa<Leergebied> jpa = new GenericDaoJpa<>(Leergebied.class);
                lijst = (List<T>) jpa.findAll();
                for (Leergebied gebied : (List<Leergebied>) lijst)
                {
                    namen.stream().filter((naam) -> (gebied.getNaam().equals(naam))).forEach((_item)
                            -> 
                            {
                                filters.add((T) gebied);
                    });

                }
            }
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return filters;
    }

    @Override
    public <T> void voegGebiedToe(String naam, T filter)
    {
        if (filter.getClass() == Doelgroep.class)
        {
            Doelgroep d = new Doelgroep(naam);
            GenericDaoJpa<Doelgroep> jpa = new GenericDaoJpa<>(Doelgroep.class);
            jpa.startTransaction();
            jpa.insert(d);
            jpa.commitTransaction();
        }

        if (filter.getClass() == Leergebied.class)
        {
            Leergebied l = new Leergebied(naam);
            GenericDaoJpa<Leergebied> jpa = new GenericDaoJpa<>(Leergebied.class);
            jpa.startTransaction();
            jpa.insert(l);
            jpa.commitTransaction();

        }
    }

    @Override
    public ObservableList<String> geefAlleGebieden(T filter)
    {
        if (filter.getClass() == Doelgroep.class)
        {
            GenericDaoJpa<Doelgroep> jpa = new GenericDaoJpa<>(Doelgroep.class);
            List<String> namen = new ArrayList<>();
            for (Doelgroep d : jpa.findAll())
            {
                namen.add(d.getNaam());
            }
            return FXCollections.observableList(namen);
        }

        if (filter.getClass() == Leergebied.class)
        {
            GenericDaoJpa<Leergebied> jpa = new GenericDaoJpa<>(Leergebied.class);
            List<String> namen = new ArrayList<>();
            for (Leergebied d : jpa.findAll())
            {
                namen.add(d.getNaam());
            }
            return FXCollections.observableList(namen);
        }

        return null;
    }

    @Override
    public void deleteGebied(T filter)
    {
        if (filter.getClass() == Doelgroep.class)
        {
            GenericDaoJpa<Doelgroep> jpa = new GenericDaoJpa<>(Doelgroep.class);
            jpa.startTransaction();
            jpa.delete((Doelgroep) filter);
            jpa.commitTransaction();
        } else if (filter.getClass() == Leergebied.class)
        {
            GenericDaoJpa<Leergebied> jpa = new GenericDaoJpa<>(Leergebied.class);
            jpa.startTransaction();
            jpa.delete((Leergebied) filter);
            jpa.commitTransaction();
        }

    }

    @Override
    public T geeftGebied(T filter, String naam)
    {

        if (filter.getClass() == Doelgroep.class)
        {
            List<Doelgroep> lijst = new ArrayList<>();
            GenericDaoJpa<Doelgroep> jpa = new GenericDaoJpa<>(Doelgroep.class);
            lijst = (List<Doelgroep>) jpa.findAll();

            return (T) lijst.stream().filter(p -> p.getNaam().equals(naam)).findFirst().get();
        }

        if (filter.getClass() == Leergebied.class)
        {
            List<Leergebied> lijst = new ArrayList<>();
            GenericDaoJpa<Leergebied> jpa = new GenericDaoJpa<>(Leergebied.class);
            lijst = (List<Leergebied>) jpa.findAll();

            return (T) lijst.stream().filter(p -> p.getNaam().equals(naam)).findFirst().get();
        }

        return null;
    }

}
