/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Doelgroep;
import domein.Leergebied;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author manu
 * @param <T>
 * @param <Leergebied>
 */
public class GeneriekeGebieden<T>  implements GeneriekeGebiedenInterface<T>{

    public GeneriekeGebieden(){
        
    }
    @Override
    public Set<T> geefGebieden(List<String> namen, T filter) {
        Set<T> filters=new HashSet<>();
        List<T> lijst=new ArrayList<>();
        try{
           if(filter.getClass()==Doelgroep.class){
               GenericDaoJpa<Doelgroep> jpa = new GenericDaoJpa<>(Doelgroep.class);
               lijst=(List<T>) jpa.findAll();
                for(Doelgroep gebied:(List<Doelgroep>)lijst){
                    namen.stream().filter((naam) -> (gebied.getNaam().equals(naam))).forEach((_item) -> {
                        filters.add((T)gebied);
                   });
               }
           
               
           }
           if(filter.getClass()== Leergebied.class){
               GenericDaoJpa<Leergebied> jpa=new GenericDaoJpa<>(Leergebied.class);
               lijst=(List<T>) jpa.findAll();
               for(Leergebied gebied:(List<Leergebied>)lijst){
                   namen.stream().filter((naam) -> (gebied.getNaam().equals(naam))).forEach((_item) -> {
                       filters.add((T)gebied);
                   });
                        
                   
               }
           }     
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return filters;
    }
    
    
    
    
}

    

