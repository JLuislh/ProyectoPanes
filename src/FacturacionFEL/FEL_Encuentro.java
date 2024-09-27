/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package FacturacionFEL;

import FELclass.CrearXML;
import FELclass.CrearXML_Encuentro;
import FELclass.CrearXML_Zona4;
import FELclass.FELclas;
import FELclass.NumeroLetras;
import FELclass.ObtenerProductosFactura;
import FELclass.RestApiClient;
import INICIO.Cambio;
import INICIO.Entra;
import clas.BDConexion;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 *
 * @author jluis
 */
public class FEL_Encuentro extends javax.swing.JFrame {
     static final private Logger LOGGER = Logger.getLogger("mx.com.hash.pruebaxml.PruebaXML");
     String Token;
     String Usuario;
     String NI; //"000044653948";
     String grantotal;
     String TotalLetras;
     int IdNitCliente;
     int validarnit;
     int id_orden;
     String serie;
     String numero;
     String autoriza;
     String FechaCerti;
    /**
     * Creates new form FEL
     */
    public FEL_Encuentro(int a) {
        initComponents();
        Orden.setText(String.valueOf(a));
        this.id_orden = a;
        this.setLocationRelativeTo(null);
        sumaTotal();
        TokenLocal();
 
        
    }
    
    
     public void InsertarDatosComprador(){
    
     try {
            
            ObtenerProductosFactura p = new ObtenerProductosFactura();
            p.setNit(nit.getText());
            p.setNombre(nombre.getText());
            ObtenerProductosFactura.InsertarNit(p);
            IdNitCliente = p.getIdNit();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
     
     
   public void InsertarDatosFEL(){
    
     try {
            //serie,numero,autorizacion,fechacertificacion,id_pedido,idNit
            ObtenerProductosFactura p = new ObtenerProductosFactura();
            p.setSerie(serie);
            p.setNumero(numero);
            p.setAutorizacion(autoriza);
            p.setFechaCertifica(FechaCerti);
            p.setId_pedido(Integer.parseInt(Orden.getText()));
            p.setIdNit(IdNitCliente);
            ObtenerProductosFactura.InsertarFEL(p);
            if(Usuario.equalsIgnoreCase("ENCUENTRO")){imprimirEncuentro();}
            else if(Usuario.equalsIgnoreCase("ZONA4")){imprimirZona4();}
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }  

     public  void TokenLocal() {
      //  DecimalFormat df = new DecimalFormat("#.00");
            try {
                BDConexion conecta = new BDConexion();
                Connection cn = conecta.getConexion();
                java.sql.Statement stmt = cn.createStatement();
                ResultSet rs = stmt.executeQuery("select token,usuario  from TOKEN where idToken = 1");
                while (rs.next()) {
                      Token = rs.getString("token");
                      Usuario = rs.getString("usuario");
                }
                rs.close();
                stmt.close();
                cn.close();
            } catch (Exception error) {
                System.out.print(error);
            }
        }
    
    
    public  void NitLocal() {
      //  DecimalFormat df = new DecimalFormat("#.00");
            try {
                 BDConexion conecta = new BDConexion();
                Connection cn = conecta.getConexion();
                java.sql.Statement stmt = cn.createStatement();
                ResultSet rs = stmt.executeQuery("select nombre,idNit from compradornit where nit ='"+nit.getText()+"'");
                while (rs.next()) {
                       nombre.setText(rs.getString("nombre"));
                       IdNitCliente = rs.getInt("idNit");
                }
                rs.close();
                stmt.close();
                cn.close();
            } catch (Exception error) {
                System.out.print(error);
            }
        }
    
    public  void NitValidar() {
      //  DecimalFormat df = new DecimalFormat("#.00");
            try {
                 BDConexion conecta = new BDConexion();
                Connection cn = conecta.getConexion();
                java.sql.Statement stmt = cn.createStatement();
                ResultSet rs = stmt.executeQuery("select count(nit) from compradornit where nit ='"+nit.getText()+"'");
                while (rs.next()) {
                    validarnit = rs.getInt(1);
                }
                rs.close();
                stmt.close();
                cn.close();
            } catch (Exception error) {
                System.out.print(error);
            }
        }
    
    
    public  void sumaTotal() {
      //  DecimalFormat df = new DecimalFormat("#.00");
            try {
                 BDConexion conecta = new BDConexion();
                Connection cn = conecta.getConexion();
                java.sql.Statement stmt = cn.createStatement();
                ResultSet rs = stmt.executeQuery("select sum(precio) as Total from PRODUCTOS_PEDIDO where id_pedido =" + Orden.getText());
                while (rs.next()) {
                      grantotal = rs.getString(1);
                      total.setText(grantotal);
                }
                rs.close();
                stmt.close();
                cn.close();
            } catch (Exception error) {
                System.out.print(error);
            }
        }
    
    
    private void Obtenernit(){
        
     
   RestApiClient apiClient = new RestApiClient();
        
        try {
            String apiKey = "TAXID=000120011662&DATA1=SHARED_GETINFONITcom&DATA2=NIT%7C"+nit.getText()+"&COUNTRY=GT&USERNAME=120011662";
            String accessToken = Token;
            String response = apiClient.get( apiKey, accessToken);
            JSONObject  jsonObject = new JSONObject(response);
            JSONArray arrayObject = (JSONArray) jsonObject.get("RESPONSE");
             for (int i = 0; i < arrayObject.length(); i++) {
                if (i == 0) {
            JSONObject object2 = (JSONObject) arrayObject.get(i);
                  NI = object2.get("NOMBRE").toString();
                  String nombrev = object2.get("NOMBRE").toString();
                  String nitv = object2.get("NIT").toString();
            nombre.setText(nombrev);
            nit.setText(nitv);
                }
             }
             if(NI.equalsIgnoreCase(""))
             {JOptionPane.showMessageDialog(null, "NIT INGRESADO ES INCORRECTO");
             nombre.setText("");
             nit.setText("");
             nit.requestFocus();
             }
             else{ InsertarDatosComprador(); facturar.requestFocus();}
   
   }    catch (IOException ex) {
            Logger.getLogger(FEL_Encuentro.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROOOOOOOR "+ex);
        }
   }
    
    private void Certificar(){
    FELclas apiClient = new FELclas();
        
        try {
            String apiKey = "TAXID=000120011662&FORMAT=''&USERNAME=120011662";//NIT DE NEGOCIO y USUARIO QUE DE DIGIFAC
            String accessToken = Token;
            //System.out.println("Token = "+Token);
            String response = apiClient.get(apiKey, accessToken);
            JSONObject  jsonObject = new JSONObject(response);
            JSONObject object2 = (jsonObject); 
            //System.out.println("JSON = "+jsonObject);
            autoriza = object2.get("authNumber").toString();
            serie = object2.get("batch").toString();
            numero = object2.get("serial").toString();
            FechaCerti = object2.get("enrolledTimeStamp").toString();
            System.out.println("mensaje = "+object2.get("message").toString());
            InsertarDatosFEL();
            /*autorizacion.setText(auto);
            lote.setText(lot);
            seriee.setText(serie);
            fechaout.setText(fecha);
            System.out.println("No. Autorizacion = "+auto);
            System.out.println("No. Lote = "+lot);
            System.out.println("No. Serie = "+serie);
            System.out.println("Fecha = "+fecha);*/
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "ERROR DE COMUNICACION PARA EMITIR LA FACTURA INTENTAR DE NUEVO");
            System.out.println("FacturacionFEL.FEL_Encuentro.Certificar() "+e);
        }
        
    }
    
    private void crearXMLEncuentro(){
        
        NumeroLetras NumLetra  = new NumeroLetras();
        String numero = total.getText();
        TotalLetras =  (NumLetra .Convertir(numero,true));
    
         try {
             CrearXML_Encuentro ejemploXML = new CrearXML_Encuentro(nombre.getText(),nit.getText(),Orden.getText(),grantotal,TotalLetras);
            Document documento = ejemploXML.crearDocumento();
            
            //System.out.println(ejemploXML.convertirString(documento));
            
            ejemploXML.escribirArchivo(documento, "C:\\Reportes\\XMLFel.xml");            
            
        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, "Error de configuracion");
            LOGGER.log(Level.SEVERE, null, ex);
             System.out.println("FacturacionFEL.FEL_Encuentro.crearXMLEncuentro()" + ex);
        } catch (TransformerException ex) {
            LOGGER.log(Level.SEVERE, "Error de transformacion XML a String");
            LOGGER.log(Level.SEVERE, null, ex);
            System.out.println("FacturacionFEL.FEL_Encuentro.crearXMLEncuentro()" + ex);
        }
    }
    
    private void crearXMLZona4(){
        
        NumeroLetras NumLetra  = new NumeroLetras();
        String numero = total.getText();
        TotalLetras =  (NumLetra .Convertir(numero,true));
    
         try {
            CrearXML_Zona4 XMLZona4 = new CrearXML_Zona4(nombre.getText(),nit.getText(),Orden.getText(),grantotal,TotalLetras);
            Document documento = XMLZona4.crearDocumento();
            
            //System.out.println(ejemploXML.convertirString(documento));
            
            XMLZona4.escribirArchivo(documento, "C:\\Reportes\\XMLFel.xml");            
            
        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, "Error de configuracion");
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            LOGGER.log(Level.SEVERE, "Error de transformacion XML a String");
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void imprimirEncuentro(){
 
      BDConexion con= new BDConexion();
         Connection conexion= con.getConexion();
        try {
            JasperReport jasperReport=(JasperReport)JRLoader.loadObjectFromFile("C:\\Reportes\\FEL\\FELElEncuentro.jasper");
            Map parametros= new HashMap();
            parametros.put("ID_ORDEN", id_orden);
            JasperPrint print = JasperFillManager.fillReport(jasperReport,parametros, conexion);
            JasperPrintManager.printReport(print, true);
        } catch (Exception e) {System.out.println("F"+e);
           JOptionPane.showMessageDialog(null, "ERROR EJECUTAR REPORTES =  "+e);
        }
         Cambio F = new Cambio(Double.parseDouble(total.getText()));
         F.setVisible(true);
         this.dispose();
        
    }
    
    private void imprimirZona4(){
 
      BDConexion con= new BDConexion();
         Connection conexion= con.getConexion();
        try {
            JasperReport jasperReport=(JasperReport)JRLoader.loadObjectFromFile("C:\\Reportes\\FEL\\FELZona4.jasper");
            Map parametros= new HashMap();
            parametros.put("ID_ORDEN", id_orden);
            JasperPrint print = JasperFillManager.fillReport(jasperReport,parametros, conexion);
            JasperPrintManager.printReport(print, true);
        } catch (Exception e) {System.out.println("F"+e);
           JOptionPane.showMessageDialog(null, "ERROR EJECUTAR REPORTES =  "+e);
        }
         Cambio F = new Cambio(Double.parseDouble(total.getText()));
         F.setVisible(true);
         this.dispose();
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nit = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        facturar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        Orden = new javax.swing.JLabel();
        total = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("NIT");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("NOMBRE");

        nit.setEditable(false);
        nit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nitActionPerformed(evt);
            }
        });
        nit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nitKeyTyped(evt);
            }
        });

        nombre.setEditable(false);
        nombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        facturar.setBackground(new java.awt.Color(102, 255, 102));
        facturar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        facturar.setText("FACTURAR");
        facturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facturarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("#ORDEN:");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton1.setText("NIT");
        jButton1.setPreferredSize(new java.awt.Dimension(72, 39));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton2.setText("CF");
        jButton2.setPreferredSize(new java.awt.Dimension(72, 39));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, -1));

        jButton3.setPreferredSize(new java.awt.Dimension(72, 39));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("TOTAL");

        Orden.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Orden.setForeground(new java.awt.Color(255, 0, 0));

        total.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        total.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(29, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Orden, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(facturar, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(nit, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(Orden, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(facturar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void facturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facturarActionPerformed
        if(nit.getText().compareTo("")!=0 && nombre.getText().compareTo("")!=0){
            
            if(Usuario.equalsIgnoreCase("ENCUENTRO"))
            {
                crearXMLEncuentro();
                Certificar();
            }else if (Usuario.equalsIgnoreCase("ZONA4")){
                 
               crearXMLZona4();
               Certificar();
            }
        }else {JOptionPane.showMessageDialog(null, "INGRESE UN NIT O MARCAR CF");}
    }//GEN-LAST:event_facturarActionPerformed

    private void nitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nitActionPerformed
        NitValidar();
      if(validarnit==1){
          NitLocal();
          facturar.requestFocus();
      }else{  
        Obtenernit();
      }
        
    }//GEN-LAST:event_nitActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        nit.setText("CF");
        nombre.setText("CONSUMIDOR FINAL");
        IdNitCliente = 1;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        nit.setText("");
        nombre.setText("");
        nit.setEditable(true);
        nit.requestFocus();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
                  Cambio F = new Cambio(Double.parseDouble(total.getText()));
                  F.setVisible(true);
                  this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void nitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nitKeyTyped
        char c = evt.getKeyChar();
        if ((c < '0' || c > '9') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c !='ñ') && (c !='Ñ')) {
            evt.consume();
        }
    }//GEN-LAST:event_nitKeyTyped

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
            java.util.logging.Logger.getLogger(FEL_Encuentro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FEL_Encuentro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FEL_Encuentro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FEL_Encuentro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new FEL_Encuentro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Orden;
    private javax.swing.JButton facturar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField nit;
    private javax.swing.JTextField nombre;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
