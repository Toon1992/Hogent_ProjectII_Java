/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Beheerder;
import domein.Gebruiker;
import exceptions.EmailException;
import exceptions.WachtwoordException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.BeheerderDaoJpa;
import persistentie.GenericDaoJpa;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import javax.persistence.EntityNotFoundException;

/**
 *
 * @author donovandesmedt
 */
public class BeheerderRepository
{

    private static Beheerder beheerder;
    private BeheerderDaoJpa beheerderDao;
    private GenericDaoJpa<Gebruiker> gebruikerDoa;
    public BeheerderRepository()
    {
        beheerderDao = new BeheerderDaoJpa();
        gebruikerDoa = new GenericDaoJpa<>(Gebruiker.class);
    }

    public Beheerder getLoggedInBeheerder()
    {
        return beheerder;
    }

    public void setBeheerder(Beheerder beheerder)
    {
        this.beheerder = beheerder;
    }

    public void login(String email, String wachtwoord)
    {
        //AANPASSEN
        //AANPASSEN
        if(email.isEmpty() && wachtwoord.isEmpty()){
            beheerder = new Beheerder("sdf",true);
        }
        //AANPASSEN
        //AANPASSEN
        //AANPASSEN
        //AANPASSEN
        // AANPASSEN

        else{


            if (!Pattern.matches("\\w+(\\.\\w*)*@\\w+\\.\\w+(\\.\\w+)*", email))
            {
                throw new EmailException("Ongeldige email");
            }
            if (wachtwoord.isEmpty())
            {
                throw new WachtwoordException("Wachtwoord verplicht");
            }
            beheerder = beheerderDao.getBeheerderByEmail(email);
            String url = "https://studservice.hogent.be/auth" + "/" + email + "/" + encryptSHA256(wachtwoord);
            String json = getJSON(url);
            login("["+json+"]");
            //Mapping.loginQuery(email, wachtwoord);
        }
    }
    private String encryptSHA256(String base){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public void login(String jsonString){
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                System.out.println(jsonObject.get("VOORNAAM"));
                System.out.println(jsonObject.get("TYPE"));

        } catch (JSONException e) {
            beheerder = null;
            throw new EntityNotFoundException();
        }
    }
    public String getJSON(String myURL){
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(myURL);
            urlConn = url.openConnection();
            if (urlConn != null)
                urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:"+ myURL, e);
        }

        return sb.toString();
    }
    public void voegBeheerderToe(Beheerder beheerder)
    {
        setBeheerder(beheerder);
        beheerderDao.startTransaction();
        beheerderDao.insert(beheerder);
        beheerderDao.commitTransaction();
    }
    
    public void verwijderBeheerder(Beheerder beheerder) {
        beheerderDao.startTransaction();
        beheerderDao.delete(beheerder);
        beheerderDao.commitTransaction();
    }
    
    public void wijzigBeheerder(Beheerder beheerder)
    {
        beheerderDao.startTransaction();
        beheerderDao.update(beheerder);
        beheerderDao.commitTransaction();
    }
    
    public SortedList<Gebruiker> getGebruikers(){
        return new SortedList<Gebruiker>(new FilteredList<Gebruiker>(FXCollections.observableArrayList(gebruikerDoa.findAll())));
    }
}
