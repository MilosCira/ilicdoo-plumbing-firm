
package main;


public class Usluge extends Klijenti {
    private int id;
    private String imeUsluge;
    private String cena;
    private String lokacija;

    public Usluge(int id, String ime, String prezime, String email, String password, String telefon, String adresa, int admin, String cena,String lokacija,String imeUsluge) {
        super(id, ime, prezime, email, password, telefon, adresa, admin);
       this.id=id;
       this.imeUsluge=imeUsluge;
       this.lokacija=lokacija;
       this.cena=cena;
       
        
    }

   

   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImeUsluge() {
        return imeUsluge;
    }

    public void setImeUsluge(String ime) {
        this.imeUsluge = ime;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
    
}
