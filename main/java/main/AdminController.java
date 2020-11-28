package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    @Autowired
    private DriverManagerDataSource dataSource;

    @Autowired
    HttpSession session;

    /**
     * ***********
     * GRAD
    
    */
    
    private ArrayList<Grad> prikaziGradBaza() {
        ArrayList<Grad> grad = new ArrayList<>();
        try {
            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("SELECT * FROM grad inner join drzava using(drz_id)");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("grd_id");
                String ime = rs.getString("grd_ime");
                int idDrz = rs.getInt("drz_id");
                String naziv = rs.getString("drz_ime");

                grad.add(new Grad(id, naziv, ime, idDrz));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return grad;
    }

    @RequestMapping(value = "/prikazigrad")
    public String prikaziGrad(ModelMap m, HttpServletRequest request) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Grad> grad = prikaziGradBaza();
        m.addAttribute("grad", grad);
        return "grad";
    }

    @RequestMapping(value = "/dodajgrad", method = RequestMethod.POST)
    public String DodajGradBaza(@RequestParam String ime, @RequestParam("drzava") int drzId, ModelMap m) {
        try {
            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("INSERT INTO grad(grd_ime,drz_id) VALUE(?,?)");

            s.setString(1, ime);
            s.setInt(2, drzId);
            s.execute();
        } catch (Exception ex) {
            m.addAttribute("greska", "greska u dodavanju grada");
            return "dodajgrad";
        }
        return "redirect:/admin/prikazigrad";
    }

    @RequestMapping(value = "/prikazidodajgrad")
    public String DodajGrad(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Drzava> drzave = prikaziDrzave();
        m.addAttribute("drzave", drzave);
        return "dodajgrad";
    }

    @RequestMapping(value = "/obrisigrad{id}")
    public String ObrisiGrad(ModelMap model, @RequestParam Integer id) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("delete from  grad where grd_id=?");
            s.setInt(1, id);
            s.execute();
            return "redirect:/admin/prikazigrad";
        } catch (Exception e) {
            e.printStackTrace();
            return "/admin/prikazigrad";
        }

    }

    @RequestMapping(value = "/izmenigrad{id}")
    public String IzmenaGrad(@RequestParam("id") String id, ModelMap m) {

        try {

            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s = dataSource.getConnection().prepareStatement("select * from grad where grd_id=?");
            s.setInt(1, Integer.parseInt(id));
            ResultSet rs = s.executeQuery();
            Grad grad = new Grad(0, id, id, 0);
            rs.next();
            grad.setId(rs.getInt("grd_id"));
            grad.setIme(rs.getString("grd_ime"));
            m.addAttribute("grad", grad);
            m.addAttribute("izmena", true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "dodajgrad";
    }

    @RequestMapping(value = "/updategrad{id}")
    public String updateGrad(
            @RequestParam Integer id,
            @RequestParam String ime,
            ModelMap model) {
        try {

            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("update grad set grd_ime=? where grd_id=?");

            s.setString(1, ime);
            s.setInt(2, id);
            s.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "dodajgrad";
        }
        return "redirect:/admin/prikazigrad";

    }

  
    /**
     * ***********
     * Klijent
     */
    private ArrayList<Klijenti> podaciIzBazePodataka() {
        ArrayList<Klijenti> klijent = new ArrayList<>();
        try {
            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("SELECT * FROM klijenti");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("kli_id");
                String ime = rs.getString("kli_ime");
                String prezime = rs.getString("kli_prezime");
                String telefon = rs.getString("kli_telefon");
                String adresa = rs.getString("kli_adresa");
                String email = rs.getString("kli_email");
                String password = rs.getString("kli_password");
             
                klijent.add(new Klijenti(id, ime, prezime, email, password, telefon, adresa, id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return klijent;
    }

    @RequestMapping(value = "/prikazidodajklijenta")
    public String dodajklijenta() {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }
        return "dodajklijenta";
    }

    @RequestMapping(value = "/prikaziizmeniklijenta{id}")
    public String Izmenaklijenta(@RequestParam("id") String id, ModelMap m) {

        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s = dataSource.getConnection().prepareStatement("select * from klijenti where kli_id=?");
            s.setInt(1, Integer.parseInt(id));
            ResultSet rs = s.executeQuery();
            Klijenti klijent = new Klijenti(0, id, res, id, res, res, res, 0);
            rs.next();
            klijent.setId(rs.getInt("kli_id"));
            klijent.setIme(rs.getString("kli_ime"));
            klijent.setPrezime(rs.getString("kli_prezime"));
            klijent.setTelefon(rs.getString("kli_telefon"));
            klijent.setAdresa(rs.getString("kli_adresa"));
            klijent.setEmail(rs.getString("kli_email"));
            klijent.setPassword(rs.getString("kli_password"));
            
            m.addAttribute("klijent", klijent);
            m.addAttribute("edit", true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "dodajklijenta";
    }

    @RequestMapping(value = "/obrisiklijenta{id}")
    public String Obrisiklijenta(ModelMap model, @RequestParam Integer id) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("delete from klijenti where kli_id=?");
            s.setInt(1, id);
            s.executeUpdate();
            return "redirect:/admin/prikaziklijenta";
        } catch (Exception e) {
            e.printStackTrace();
            return "/admin/prikaziklijenta";
        }

    }

    @RequestMapping(value = "/prikaziklijenta")
    public String prikaziklijenta(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Klijenti> klijenti = podaciIzBazePodataka();
        m.addAttribute("klijenti", klijenti);
        return "klijenti";
    }

    @RequestMapping(value = "/updateklijenta{id}")
    public String updateklijenta(
            @RequestParam Integer id,
            @RequestParam String ime,
            @RequestParam String prezime,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String telefon,
            @RequestParam String adresa,
            ModelMap model) {

        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("update klijenti set kli_ime=?,kli_prezime=?, kli_email=?, kli_password=?, kli_telefon=?, kli_adresa=? where kli_id=?");

            s.setString(1, ime);
            s.setString(2, prezime);
            s.setString(3, email);
            s.setString(4, password);
            s.setString(5, telefon);
            s.setString(6, adresa);
            s.setInt(7, id);
            s.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "dodajklijenta";
        }
        return "redirect:/admin/prikaziklijenta";

    }

    @RequestMapping("/dodajklijenta")
    public String Dodajklijenta(
            @RequestParam String ime,
            @RequestParam String prezime,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String telefon,
            @RequestParam String adresa,
            ModelMap model) {

        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("INSERT INTO klijenti (kli_ime,kli_prezime,kli_email,kli_password,kli_telefon,kli_adresa) VALUE(?,?,?,?,?,?)");
            s.setString(1, ime);
            s.setString(2, prezime);
            s.setString(3, email);
            s.setString(4, password);
            s.setString(5, telefon);
            s.setString(6, adresa);
            s.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return "dodajklijenta";
        }
        return "redirect:/admin/prikaziklijenta";
    }

  

    /**
     * ***********
     * DRŽAVA
     **/
    private ArrayList<Drzava> prikaziDrzave() {
        ArrayList<Drzava> drzava = new ArrayList<>();
        try {
            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("SELECT * FROM drzava");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("drz_id");
                String ime = rs.getString("drz_ime");
                drzava.add(new Drzava(id, ime));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return drzava;
    }

    @RequestMapping(value = "/prikazidrzavu")
    public String prikaziDrzavu(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Drzava> drzava = prikaziDrzave();
        m.addAttribute("drzava", drzava);
        return "drzava";

    }

    @RequestMapping(value = "/dodajdrzavu")
    public String DodajDrzavu(@RequestParam String ime, ModelMap m) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("INSERT INTO drzava (drz_ime) VALUE(?)");
            s.setString(1, ime);
            s.execute();
        } catch (Exception ex) {
            m.addAttribute("greska", ex.getMessage());
            return "dodajdrzavu";
        }
        return "redirect:/admin/prikazidrzavu";
    }

    @RequestMapping(value = "/prikazidodajdrzavu")
    public String DodajDrzavu() {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }
        return "dodajdrzavu";
    }

    @RequestMapping(value = "/izmenidrzave{id}")
    public String IzmenaDrzave(@RequestParam("id") String id, ModelMap m) {

        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s = dataSource.getConnection().prepareStatement("select * from drzava where drz_id=?");
            s.setInt(1, Integer.parseInt(id));
            ResultSet rs = s.executeQuery();
            Drzava drzava = new Drzava(0, id);
            rs.next();
            drzava.setId(rs.getInt("drz_id"));
            drzava.setNaziv(rs.getString("drz_ime"));
            m.addAttribute("drzava", drzava);
            m.addAttribute("izmena", true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "dodajdrzavu";
    }

    @RequestMapping(value = "/updatedrzavu{id}")
    public String updateDrzavu(
            @RequestParam Integer id,
            @RequestParam String ime,
            ModelMap model) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("update drzava set drz_ime=? where drz_id=?");

            s.setString(1, ime);
            s.setInt(2, id);
            s.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "dodajdrzavu";
        }
        return "redirect:/admin/prikazidrzavu";

    }

    @RequestMapping(value = "/obrisidrzavu{id}")
    public String ObrisiDrzavu(ModelMap model, @RequestParam Integer id) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("delete from drzava where drz_id=?");
            s.setInt(1, id);
            s.execute();
            return "redirect:/admin/prikazidrzavu";
        } catch (Exception e) {
            e.printStackTrace();
            return "/admin/prikazidrzavu";
        }

    }

    /**
     * ***********
     * teren
     */
    private ArrayList<Slika> prikaziSlike() {
        ArrayList<Slika> slika = new ArrayList<>();
        try {
            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("SELECT slk_slika,rad_lokacija,rad_id,slk_id FROM slike\n" +
                                              "inner join radovi using(rad_id)");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("slk_id");
                String lokacija = rs.getString("rad_lokacija");
                Blob slike = rs.getBlob("slk_slika");
                Slika t = new Slika(id, lokacija, lokacija, lokacija, lokacija, lokacija, lokacija, lokacija, lokacija, lokacija, slike);
                slika.add(t);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return slika;
    }

    @RequestMapping(value = "/prikazislike")
    public String prikaziSliku(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Slika> slika = prikaziSlike();
        ArrayList<Slike> slike = new ArrayList<>();
        for (Slika t : slika) {

            try {
                ByteArrayOutputStream outputStream;
                String base64Image;

                if (t.getSlika()!= null) {

                    try (InputStream inputStream = t.getSlika().getBinaryStream()) {
                        outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[4096];
                        int bytesRead = -1;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        byte[] imageBytes = outputStream.toByteArray();
                        base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    }
                    outputStream.close();

                    slike.add(new Slike(t.getId(), t.getLokacija(), t.getLokacija(), t.getLokacija(), t.getLokacija(), t.getLokacija(), t.getLokacija(), t.getLokacija(), t.getLokacija(), t.getLokacija(), base64Image));
                }
            } catch (NullPointerException e) {

                e.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        m.addAttribute("slike", slike);
        return "slika";
    }

    @RequestMapping(value = "/dodajsliku")
    public String DodajSliku(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Radovi> radovi = prikaziRadoveBaza();
        m.addAttribute("radovi", radovi);
        return "dodajsliku";
    }

    @RequestMapping(value = "/dodajsliku", method = RequestMethod.POST)
    public String DodajTerene(@RequestParam("radId") int radId, @RequestParam("slika") MultipartFile file, ModelMap m) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            byte[] bytes = file.getBytes();
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("INSERT INTO slike (slk_slika,rad_id) VALUE(?,?)");
            s.setBlob(1, blob);
            s.setInt(2, radId);
            s.executeUpdate();      

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/admin/prikazislike";
    }

    @RequestMapping(value = "/obrisisliku{id}")
    public String ObrisiTeren(ModelMap model, @RequestParam Integer id) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("delete from slike where slk_id=?");
            s.setInt(1, id);
            s.executeUpdate();
            return "redirect:/admin/prikazislike";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/prikazislike";
        }

    }

   
    
    /**
     * ***********
     * USLUGE
     */
    private ArrayList<Usluge> prikaziUsluge() {
        ArrayList<Usluge> usluge = new ArrayList<>();
        try {
            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("SELECT kli_ime, kli_prezime,kli_telefon, kli_email,kli_adresa, usl_ime, usl_lokacija,usl_cena,kli_id,usl_id FROM usl_kli\n"
                                    + "inner join klijenti using(kli_id)\n"
                                    + "inner join usluge using(usl_id)  ");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("usl_id");
                String imeUsluge = rs.getString("usl_ime");
                String lokacija = rs.getString("usl_lokacija");
                String cena=rs.getString("usl_cena");
                String ime=rs.getString("kli_ime");
                String prezime=rs.getString("kli_prezime");
                String email=rs.getString("kli_email");

                Usluge t = new Usluge(id, ime, prezime, email, prezime, email, ime, id, cena, lokacija, imeUsluge);
                usluge.add(t);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return usluge;
    }

    @RequestMapping(value = "/prikaziusluge")
    public String prikaziDogovor(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Usluge> usluge = prikaziUsluge();
        m.addAttribute("usluge", usluge);
        return "usluga";
    }

    @RequestMapping(value = "/dodajuslugu")
    public String dodajDogovorGet(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        
        return "usluga";
    }

    @RequestMapping(value = "/dodajuslugu", method = RequestMethod.POST)
    public String DodajUslugu(
            @RequestParam String lokacija,
            @RequestParam String cena,           
            @RequestParam String ime,
            ModelMap m) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("insert into usluge (usl_lokacija, usl_ime, usl_cena) VALUE(?,?,?)", Statement.RETURN_GENERATED_KEYS);

            

            PreparedStatement s2
                    = dataSource.getConnection().
                            prepareStatement("select * from klijenti where kli_email=? ");
            String email = session.getAttribute("email").toString();
            s2.setString(1, email);
            ResultSet rs = s2.executeQuery();
            rs.next();
            int kliId = rs.getInt("kli_id");
            s.setString(1, lokacija);
            s.setString(2, ime);            
            s.setString(3, cena);
            s.executeUpdate();

            ResultSet rs1 = s.getGeneratedKeys();
            rs1.next();
            int uslId = rs1.getInt(1);

            PreparedStatement s3
                    = dataSource.getConnection().
                            prepareStatement("insert into usl_kli (usl_id,kli_id) VALUE(?,?)");
            s3.setInt(1, uslId);
            s3.setInt(2, kliId);
            s3.execute();

            return "redirect:/admin/prikaziusluge";
        } catch (Exception e) {
            e.printStackTrace();
           

        }
        return "redirect:/admin/prikaziusluge";
    }

    @RequestMapping(value = "/prikazidodajuslugu")
    public String DodajUslugu(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        
        return "dodajuslugu";
    }

    @RequestMapping(value = "/obrisiuslugu{id}")
    public String ObrisiUslugu(ModelMap model, @RequestParam Integer id) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("delete from usluge where usl_id=? ");
            s.setInt(1, id);
            s.executeUpdate();

            return "redirect:/admin/prikaziusluge";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/prikaziusluge";
        }

    }

    /**
     * ***********
     * KOMENTARI
     */
    private ArrayList<Komentari> prikaziKomentare() {
        ArrayList<Komentari> komentari = new ArrayList<>();
        try {
            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("SELECT kom_komentar,kli_ime,kli_prezime,kli_id,kom_id FROM komentari\n" +
                                                             "inner join klijenti using(kli_id)");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("kom_id");
                String komentar = rs.getString("kom_komentar");
                int kliId = rs.getInt("kli_id");
                String ime=rs.getString("kli_ime");
                String prezime=rs.getString("kli_prezime");
  
                Komentari t = new Komentari(id, ime, prezime, ime, prezime, ime, ime, kliId, komentar);
                komentari.add(t);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return komentari;
    }

    @RequestMapping(value = "/prikazikomentar")
    public String prikaziOcenu(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Komentari> komentari = prikaziKomentare();
        m.addAttribute("komentari", komentari);
        return "komentari";
    }

    @RequestMapping(value = "/obrisikomentar{id}")
    public String ObrisiOcenu(ModelMap model, @RequestParam Integer id) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("delete from komentari where kom_id=?");
            s.setInt(1, id);
            s.execute();
            return "redirect:/admin/prikazikomentar";
        } catch (Exception e) {
            e.printStackTrace();
            return "/admin/prikazikomentar";
        }

    }

  

   

    @RequestMapping(value = "/izmenikomentar{id}")
    public String IzmenaOcene(@RequestParam int id, ModelMap m) {

        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s = dataSource.getConnection().prepareStatement("select * from komentari where kom_id=?");
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
            Komentari komentari = new Komentari(id, res, res, res, res, res, res, id, res);
            rs.next();
            komentari.setId(rs.getInt("kom_id"));
            komentari.setKomentar(rs.getString("kom_komentar"));
            

            m.addAttribute("komentari", komentari);
            m.addAttribute("izmena", true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "dodajkomentar";
    }

    @RequestMapping(value = "/updatekomentar{id}")
    public String updateOcenu(
            @RequestParam Integer id,
            @RequestParam String komentar,
            ModelMap model) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("update komentari set kom_komentar=? where kom_id=?");

            s.setString(1, komentar);
            s.setInt(2, id);
            s.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "dodajkomentar";
        }
        return "redirect:/admin/prikazikomentar";

    }

    /**
     * ***********
     * RADOVI
     */
    private ArrayList<Radovi> prikaziRadoveBaza() {
        ArrayList<Radovi> radovi = new ArrayList<>();
        try {
            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("SELECT rad_lokacija,grd_ime,drz_ime, kli_ime, kli_email, kli_telefon,rad_id,kli_id,grd_id,drz_id FROM radovi\n" +
"                                                           inner join grad using(grd_id)\n" +
"                                                           inner join drzava using(drz_id)\n" +
"                                                           inner join klijenti using(kli_id)");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("rad_id");
                String lokacija = rs.getString("rad_lokacija");
                String ime = rs.getString("kli_ime");
                String telefon = rs.getString("kli_telefon");
                String grdIme=rs.getString("grd_ime");
                String drzIme=rs.getString("drz_ime");               
                String email = rs.getString("kli_email");

                radovi.add(new Radovi(id, ime, drzIme, email, telefon, telefon, drzIme, lokacija, grdIme, drzIme));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return radovi;
    }

    @RequestMapping(value = "/prikazirad")
    public String prikaziRad(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Radovi> radovi = prikaziRadoveBaza();
        m.addAttribute("radovi", radovi);
        return "radovi";
    }

    @RequestMapping(value = "/dodajrad")
    public String DodajRad(ModelMap m) {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }

        ArrayList<Grad> grad = prikaziGradBaza();
        ArrayList<Drzava> drzava=prikaziDrzave();
        ArrayList<Klijenti> klijent = podaciIzBazePodataka();
        m.addAttribute("grad", grad);
        m.addAttribute("drzava",drzava);
        m.addAttribute("klijent", klijent);

        return "dodajrad";
    }

    @RequestMapping(value = "/dodajrad", method = RequestMethod.POST)
    public String DodajRadBaza(@RequestParam String lokacija, @RequestParam int grdId, @RequestParam int kliId, ModelMap m) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }
            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("INSERT INTO radovi (rad_lokacija,grd_id,kli_id) VALUE(?,?,?)");

            s.setString(1, lokacija);
            s.setInt(2, grdId);
            s.setInt(3, kliId);
            s.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "dodajrad";
        }
        return "redirect:/admin/prikazirad";
    }

    @RequestMapping(value = "/obrisirad{id}")
    public String ObrisiRad(ModelMap model, @RequestParam Integer id) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("delete from  radovi where rad_id=?");
            s.setInt(1, id);
            s.executeUpdate();
            return "redirect:/admin/prikazirad";
        } catch (Exception e) {
            e.printStackTrace();
            return "/admin/prikazirad";
        }

    }

    /**
     * ***********
     * ADMIN
     */
    @RequestMapping(value = "/dodajadmina")
    public String dodajAdmina(
            @RequestParam String ime,
            @RequestParam String prezime,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String telefon,
            @RequestParam String adresa,
            ModelMap model) {
        try {
            String res = MainController.testPriviledges(session, true);
            if (res != null) {
                return res;
            }

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("INSERT INTO klijenti (kli_ime,kli_prezime,kli_email,kli_password,kli_telefon,kli_adresa,kli_admin) VALUE(?,?,?,?,?,?,?)");
            s.setString(1, ime);
            s.setString(2, prezime);
            s.setString(3, email);
            s.setString(4, password);
            s.setString(5, telefon);
            s.setString(6, adresa);
            s.setInt(7, 1);
            s.execute();
        } catch (Exception e) {
            model.addAttribute("mess", "GRESKA U DODAVANJU");
            return "adminadd";
        }
        return "redirect:/admin";
    }

    @RequestMapping(value = "/prikaziDodajAdmina")
    public String prikaziAdmina() {
        String res = MainController.testPriviledges(session, true);
        if (res != null) {
            return res;
        }
        return "adminadd";
    }
}
