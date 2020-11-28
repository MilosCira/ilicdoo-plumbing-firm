
package main;


public class Grad extends Drzava {
    private int id;
    private String ime;

    public Grad(int id, String ime,String naziv, int drzId) {
        super(drzId, naziv);
        this.id=id;
        this.ime=ime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

   

   

   
    
    
}
