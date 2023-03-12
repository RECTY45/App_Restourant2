    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DashboardKasir;

import Config.Koneksi;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class transaksi extends javax.swing.JFrame {
      private DefaultTableModel model = null;
     private PreparedStatement stat;
     private ResultSet rs;
     Koneksi k = new Koneksi();
    /**
     * Creates new form transaksi
     */
    public transaksi() {
        initComponents();
        k.connect();
        recordTable();
       refreshCombo();
    }
 
     public class trans extends transaksi{
            int id_transaksi,id_masakan,harga,jumlah_beli,total_bayar;
            String nama_pelanggan,tanggal,nama_masaskan;
        public trans() {
            this.nama_pelanggan = inputNamapelanggan.getText();
            String combo = idcombo.getSelectedItem().toString();
            String[] arr = combo.split(":");
            this.id_masakan = Integer.parseInt(arr[0]);
            try {
                Date date = DateInput.getDate();
                DateFormat dateformat =  new SimpleDateFormat("YYYY-MM-dd");
                this.tanggal = dateformat.format(date);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            this.nama_masaskan = arr[1];
            this.harga = Integer.parseInt(arr[2]);
            this.jumlah_beli = Integer.parseInt(jumlahbeli.getText());
            this.total_bayar = this.harga*this.jumlah_beli;
        }
        
    }
    
    public void handleDelete(){
         if(inputId.getText().isEmpty()){
           JOptionPane.showMessageDialog(this,"DATA GAGAL DI HAPUS SILAHKAN KLIK DATA PADA TABEL YANG INGIN DI HAPUS !","Error",JOptionPane.ERROR_MESSAGE);
       }else{
        try {
          trans t = new trans();
            t.id_transaksi = Integer.parseInt(inputId.getText());
            this.stat = k.getCon().prepareStatement("DELETE FROM tbl_transaksi WHERE id_transaksi=?");
            this.stat.setInt(1, t.id_transaksi);
            this.stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "DATA BERHASIL DI HAPUS !");
            recordTable();
        } catch (Exception e) {
        }
         }
    }
     
    public void handleUpdate(){
           if(inputId.getText().isEmpty() || inputNamapelanggan.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"DATA TIDAK BOLEH KOSONG BRO!","Warning",JOptionPane.WARNING_MESSAGE);
        }else{
        try {
        trans t = new trans();
            t.id_transaksi = Integer.parseInt(inputId.getText());
            this.stat = k.getCon().prepareStatement("UPDATE tbl_transaksi SET nama_pelanggan=?,"
                    + "id_masakan=?,tanggal=?,jumlah=?,total=? WHERE id_transaksi=?");
            this.stat.setString(1, t.nama_pelanggan);
            this.stat.setInt(2, t.id_masakan);
            this.stat.setString(3, t.tanggal);
            this.stat.setInt(4, t.jumlah_beli);
            this.stat.setInt(5, t.total_bayar);
            this.stat.setInt(6, t.id_transaksi);
            this.stat.executeUpdate();
            recordTable();
            JOptionPane.showMessageDialog(this, "DATA BERHASIL DI UBAH !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
           }
    }
    
    public void handleCreate(){
            if(inputNamapelanggan.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"DATA TIDAK BOLEH KOSONG BRO !","Warning",JOptionPane.WARNING_MESSAGE);
        }else{
        try {
              PreparedStatement validate = k.getCon().prepareStatement("SELECT * FROM tbl_transaksi WHERE nama_pelanggan = '"+inputNamapelanggan.getText()+"'");
            this.rs = validate.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(this, "DATA TRANSAKSI INI SUDAH ADA SILAHKAN BIKIN YANG BARU BRO !","Error",JOptionPane.ERROR_MESSAGE);
            }else{
            trans t = new trans();
            totalBayar.setText(""+t.total_bayar);
            this.stat = k.getCon().prepareStatement("insert into tbl_transaksi values (?,?,?,?,?,?)");
            this.stat.setInt(1, 0);
            this.stat.setString(2,inputNamapelanggan.getText());
            this.stat.setInt(3, t.id_masakan);
            this.stat.setInt(4, t.jumlah_beli);
            this.stat.setString(5, t.tanggal);
            this.stat.setInt(6, t.total_bayar);
               int pilihan = JOptionPane.showConfirmDialog(null
                    , "Tanggal : " +t.tanggal+
                         "\nNama_Pelanggan " +t.nama_pelanggan+
                          "\nPembelian : " + t.jumlah_beli + " "+t.nama_masaskan+
                   "\nTotal Bayar : "+t.total_bayar+"\n" ,
                    "Apakah Ingin Tambah Transaksi ?",
                   JOptionPane.YES_NO_OPTION);
            if (pilihan == JOptionPane.YES_OPTION) {
                this.stat.executeUpdate();
                recordTable();
            } else if(pilihan == JOptionPane.YES_NO_OPTION){
                recordTable();
            }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
         }
    } 
    
       public void refreshCombo(){
          try {
              this.stat = k.getCon().prepareStatement("select*from tbl_menu "
                      + "where status = 'Tersedia'");
              this.rs = this.stat.executeQuery();
              while(rs.next()){
                  idcombo.addItem(rs.getString("id_masakan")+":"+
                          rs.getString("nama_masakan")+":"+rs.getString("harga")+":"+rs.getString("kategori"));
              }
              
          } catch (Exception e) {
              JOptionPane.showMessageDialog(null, e.getMessage());
          }
      }
       
      public void handleClickTable(){
           inputId.setText(model.getValueAt(tblTransaksi.getSelectedRow(), 0).toString());
        inputNamapelanggan.setText(model.getValueAt(tblTransaksi.getSelectedRow(), 1).toString());
        jumlahbeli.setText(model.getValueAt(tblTransaksi.getSelectedRow(), 4).toString());
        totalBayar.setText(model.getValueAt(tblTransaksi.getSelectedRow(), 6).toString());
       }
      
          public void recordTable(){
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("NAMA PELANGGAN");
        model.addColumn("KATEGORI MENU");
        model.addColumn("NAMA MASAKAN");
        model.addColumn("JUMLAH PEMESANAN");
        model.addColumn("TANGGAL PEMESANAN");
        model.addColumn("TOTAL HARGA PEMESANAN");
        this.tblTransaksi.setModel(model);
        try {
           stat = k.getCon().prepareStatement("SELECT * FROM viewtransaksi WHERE status='tersedia'");
           rs = stat.executeQuery();
           while(rs.next()){
               Object[] data = {
                   rs.getString(1),
                   rs.getString(2),
                   rs.getString(3),
                   rs.getString(4),
                   rs.getString(5),
                   rs.getString(6),
                   rs.getString(7),
               };
            model.addRow(data);
        }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        inputId.setText("");
        inputNamapelanggan.setText("");
        jumlahbeli.setText("");
        totalBayar.setText("");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        inputId = new javax.swing.JTextField();
        inputNamapelanggan = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jumlahbeli = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        totalBayar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();
        btnHapus = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnrekam = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        idcombo = new javax.swing.JComboBox<>();
        DateInput = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1142, 823));
        getContentPane().setLayout(null);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Id Transaksi");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(-30, 100, 190, 20);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("TRANSAKSI");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(280, 10, 230, 30);

        inputId.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        inputId.setEnabled(false);
        getContentPane().add(inputId);
        inputId.setBounds(50, 130, 710, 30);

        inputNamapelanggan.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        getContentPane().add(inputNamapelanggan);
        inputNamapelanggan.setBounds(50, 190, 710, 30);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Nama Pelanggan");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(-20, 160, 190, 20);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Kategori Menu");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(-20, 240, 190, 20);

        jumlahbeli.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        getContentPane().add(jumlahbeli);
        jumlahbeli.setBounds(50, 370, 710, 30);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Jumlah Beli");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(-30, 340, 190, 20);

        totalBayar.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        totalBayar.setEnabled(false);
        getContentPane().add(totalBayar);
        totalBayar.setBounds(50, 430, 710, 30);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Total Bayar");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(-30, 400, 190, 20);

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id Transaksi", "Nama Pelanggan", "Kategori", "Menun Pilihan", "Jumlah Pemesanan"
            }
        ));
        tblTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTransaksi);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 470, 1140, 260);

        btnHapus.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnHapus.setText("HAPUS");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        getContentPane().add(btnHapus);
        btnHapus.setBounds(640, 70, 130, 40);

        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jButton3.setText("KEMBALI");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(10, 10, 130, 40);

        btnrekam.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnrekam.setText("REKAM");
        btnrekam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrekamActionPerformed(evt);
            }
        });
        getContentPane().add(btnrekam);
        btnrekam.setBounds(360, 70, 130, 40);

        btnedit.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnedit.setText("EDIT");
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });
        getContentPane().add(btnedit);
        btnedit.setBounds(500, 70, 130, 40);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Tanggal Pemesanan");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(-10, 280, 190, 20);

        getContentPane().add(idcombo);
        idcombo.setBounds(150, 240, 580, 30);
        getContentPane().add(DateInput);
        DateInput.setBounds(100, 310, 700, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showOptionDialog(this,
            "Ingin Keluar?",
            "Keluar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (jawab == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Anda Berhasil Di Keluarkan");
            this.dispose();
            this.setVisible(false);
            new menu_Kasir().setVisible(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
    handleDelete();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnrekamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrekamActionPerformed
     handleCreate();
    }//GEN-LAST:event_btnrekamActionPerformed

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
        handleUpdate();
    }//GEN-LAST:event_btneditActionPerformed

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransaksiMouseClicked
     handleClickTable();
    }//GEN-LAST:event_tblTransaksiMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DateInput;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnrekam;
    private javax.swing.JComboBox<String> idcombo;
    private javax.swing.JTextField inputId;
    private javax.swing.JTextField inputNamapelanggan;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jumlahbeli;
    private javax.swing.JTable tblTransaksi;
    private javax.swing.JTextField totalBayar;
    // End of variables declaration//GEN-END:variables
}
