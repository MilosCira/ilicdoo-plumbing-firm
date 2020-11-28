
package main;


public class Slike extends Radovi {
    private int id;
    private String slika;
    private String lokacija;

    public Slike(int id, String ime, String prezime, String email, String password, String telefon, String adresa,String lokacija,String drzIme,String grdIme,String slika) {
        super(id, ime, prezime, email, password, telefon, adresa ,lokacija,drzIme,grdIme);
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

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    
    
}
