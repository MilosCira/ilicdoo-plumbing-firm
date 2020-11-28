
package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static reactor.util.concurrent.WaitStrategy.alert;

@Controller
@RequestMapping("/")
public class MainController {

    private void setCookie(String key, int value, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, String.valueOf(value));
        response.addCookie(cookie);
    }

    public static String testPriviledges(HttpSession session, boolean requireAdmin) {
        Object t = session.getAttribute("email");
        if (t == null) {
            return "redirect:/login";
        }
        if (requireAdmin) {
            t = session.getAttribute("admin");
            if (!(t instanceof Number) || ((Number) t).intValue() != 1) {
                return "redirect:/home";
            }
        }
        return null;
    }

    @Autowired
    HttpSession session;

    @Autowired
    DriverManagerDataSource dataSource;

    @RequestMapping(value = {"/", "/home", "/start"})
    public String homepage(HttpServletRequest request, ModelMap model) {
        boolean isAdmin = false;
        Cookie cookie = null;
        Cookie[] cookies = null;
        try {
            cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("admin") && cookie.getValue().equals("1")) {
                    isAdmin = true;
                }
            }
            model.addAttribute("isAdmin", isAdmin);
            return "home";
        } catch (NullPointerException e) {
            return "home";
        }

    }

    @RequestMapping(value = "/onama")
    public String onama(HttpServletRequest request, ModelMap model) {
        boolean isAdmin = false;
        Cookie cookie = null;
        Cookie[] cookies = null;
        try {
            cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("admin") && cookie.getValue().equals("1")) {
                    isAdmin = true;
                }

            }
            model.addAttribute("isAdmin", isAdmin);
            return "onama";
        } catch (NullPointerException e) {
            return "onama";
        }

    }

    @RequestMapping(value = "/admin")
    public String admin(HttpServletRequest request) {
        Cookie cookie = null;
        Cookie[] cookies = null;
        try {
            cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("admin") && cookie.getValue().equals("1")) {
                    return "admin";
                }

            }
        } catch (NullPointerException e) {
            return "login";
        }
        return "login";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(required = false) String email, @RequestParam(required = false) String password, ModelMap model) {

        return "login";

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String someMethod(@RequestParam("email") String email, @RequestParam("password") String password, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (email == null || password == null) {
                return "login";
            }

            PreparedStatement s = dataSource.getConnection().
                    prepareStatement("SELECT * FROM klijenti where kli_email=? and kli_password=?");

            s.setString(1, email);
            s.setString(2, password);
            ResultSet rs = s.executeQuery();
            rs.next();
            session.setAttribute("email", rs.getString("kli_email"));
            session.setAttribute("ime", rs.getString("kli_ime"));

            int admin = rs.getInt("kli_admin");
            if (admin == 1) {
                session.setAttribute("admin", 1);
                model.addAttribute("admin", 1);
                setCookie("admin", 1, response);
                return "redirect:/admin";
            }

            return "redirect:/home";

        } catch (Exception e) {
            model.addAttribute("poruka", "Pogresno korisničko ime ili loznika! Pokušajte ponovo");
            return "login";
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        setCookie("admin", 0, response);
        return "redirect:/home";
    }

    @RequestMapping("/registracija")
    public String registracija(
            @RequestParam String ime,
            @RequestParam String prezime,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String telefon,
            @RequestParam String adresa,
            ModelMap model) {

        try {

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
            return "redirect:/login";
        } catch (Exception e) {

            model.addAttribute("poruka", "greska prilikom registracije pokusajte ponovo");
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/usluga", method = RequestMethod.GET)
    public String usluge(ModelMap m) {
        try {

            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("SELECT kli_ime, kli_prezime,kli_telefon, kli_email,kli_adresa, usl_ime, usl_lokacija,usl_cena,kli_id,usl_id FROM usl_kli\n"
                                    + "inner join klijenti using(kli_id)\n"
                                    + "inner join usluge using(usl_id) ");

            ResultSet rs = s.executeQuery();
            ArrayList<Usluge> usluge = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("usl_id");
                String lokacija = rs.getString("usl_lokacija");
                String imeUsluge = rs.getString("usl_ime");
                String cena=rs.getString("usl_cena");
                String ime=rs.getString("kli_ime");
                String prezime=rs.getString("kli_prezime");
                String telefon=rs.getString("kli_telefon");
                String email=rs.getString("kli_email");
                String adresa=rs.getString("kli_adresa");
                Usluge usluga = new Usluge(id, ime, prezime, email, adresa, telefon, adresa, id, cena, lokacija, imeUsluge);

                
                usluge.add(usluga);
            }
            m.addAttribute("usluge", usluge);
        } catch (Exception ex) {
            ex.printStackTrace();
          
        }
        return "usluge";
    }
    
    
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
      
    @RequestMapping(value = "/radovi")
    public String prikaziSliku(ModelMap m) {
        

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
        return "nasiradovi";
    }
   
    
    @RequestMapping(value = "/dodajkomentar", method = RequestMethod.POST)
    public String DodajOcene(@RequestParam String komentar, ModelMap m) {
        try {

            PreparedStatement s1 = dataSource.getConnection().
                    prepareStatement("select * from klijenti where kli_email=? ");
            String email = session.getAttribute("email").toString();
            s1.setString(1, email);
            ResultSet rs1 = s1.executeQuery();
            rs1.next();
            int kliId = rs1.getInt("kli_id");
            PreparedStatement s
                    = dataSource.getConnection().
                            prepareStatement("INSERT INTO komentari (kom_komentar,kli_id) VALUE(?,?)");
            s.setString(1, komentar);
            s.setInt(2, kliId);
           
            s.execute();
           
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        return "redirect:/komentari";
    }
    
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

    @RequestMapping(value = "/komentari")
    public String prikaziOcenu(ModelMap m) {
        

        ArrayList<Komentari> komentari = prikaziKomentare();
        m.addAttribute("komentari", komentari);
        return "komentarifirme";
    }


}
