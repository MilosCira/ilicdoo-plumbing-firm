/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


public class Radovi extends Klijenti {
    private  int id;
    private String lokacija;
    private int grdId;
    private String grdIme;
    private int drzId;
    private String drzIme;

    public Radovi(int id, String ime, String prezime, String email, String password, String telefon, String adresa,String lokacija,String grdIme,String drzIme) {
        super(id, ime, prezime, email, password, telefon, adresa,0);
        this.id=id;
        this.lokacija=lokacija;
        this.grdId=grdId;
        this.grdIme=grdIme;
        this.drzId=drzId;
        this.drzIme=drzIme;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }


    public int getGrdId() {
        return grdId;
    }

    public void setGrdId(int grdId) {
        this.grdId = grdId;
    }

    public String getGrdIme() {
        return grdIme;
    }

    public void setGrdIme(String grdIme) {
        this.grdIme = grdIme;
    }

    public int getDrzId() {
        return drzId;
    }

    public void setDrzId(int drzId) {
        this.drzId = drzId;
    }

    public String getDrzIme() {
        return drzIme;
    }

    public void setDrzIme(String drzIme) {
        this.drzIme = drzIme;
    }

 
    
}
