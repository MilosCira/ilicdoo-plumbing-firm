
package main;


public class Komentari extends Klijenti {
    private int id;
    private String komentar;

    public Komentari(int id, String ime, String prezime, String email, String password, String telefon, String adresa, int admin,String komentar) {
        super(id, ime, prezime, email, password, telefon, adresa, admin);
        this.id=id;
        this.komentar=komentar;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }
    
}
