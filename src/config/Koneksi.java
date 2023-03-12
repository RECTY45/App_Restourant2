package Config;

import java.sql.*;
import javax.swing.JOptionPane;

public class Koneksi {
 private String url="jdbc:mysql://localhost/db_resto2";
 private String user = "root";
 private String pass= "";
 private Connection con;
 
 
 public void connect(){
     try {
         con = DriverManager.getConnection(url, user, pass);
         System.out.println("Databases Connected Success Full");
     } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e.getMessage());
     }
 
 }

    public Connection getCon() {
        return con;
    }
    
    
    public static void main(String args []){
        Koneksi k = new Koneksi();
        
        k.connect();
    }
    
    
}