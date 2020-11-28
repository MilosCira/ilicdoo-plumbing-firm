/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Blob;

/**
 *
 * @author Milos
 */
public class Slika extends Radovi {
    private int id;
    private Blob slika;
    private String lokacija;

    public Slika(int id, String ime, String prezime, String email, String password, String telefon, String adresa, String lokacija, String grdIme, String drzIme,Blob slika) {
        super(id, ime, prezime, email, password, telefon, adresa, lokacija, grdIme, drzIme);
        this.id=id;
        this.slika=slika;
        this.lokacija=lokacija;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getSlika() {
        return slika;
    }

    public void setSlika(Blob slika) {
        this.slika = slika;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
    
}
