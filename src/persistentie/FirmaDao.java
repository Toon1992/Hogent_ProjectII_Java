/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Firma;

/**
 *
 * @author manu
 */
public interface FirmaDao extends GenericDao<Firma> {
    public Firma geefFirma(String naam);
}
