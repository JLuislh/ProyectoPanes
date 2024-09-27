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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import java.sql.PreparedStatement;

/**
 *
 * @author jluis
 */
public class FELCobrosCF extends javax.swing.JFrame {

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
    int focoteclado = 0;
    int pago = 0;
    int para;
    int validarorden;
    DecimalFormat df = new DecimalFormat("#.00");

    /**
     * Creates new form FEL
     */
    public FELCobrosCF(int a, int b) {
        initComponents();
        String texto1 = "<html><center><body>TARJETA<br>EFECTIVO</body></center></html>";
        EYT.setText(texto1);
        Orden.setText(String.valueOf(a));
        this.id_orden = a;
        this.para = b;
        this.setLocationRelativeTo(null);
       
        TokenLocal();
        sumaTotal();
    }

    public void InsertarDatosComprador() {

        try {

            ObtenerProductosFactura p = new ObtenerProductosFactura();
            p.setNit(NIT.getText());
            p.setNombre(nombre.getText());
            ObtenerProductosFactura.InsertarNit(p);
            IdNitCliente = p.getIdNit();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void InsertarDatosFEL() {

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
            if (Usuario.equalsIgnoreCase("ENCUENTRO")) {
                imprimirEncuentro();
            } else if (Usuario.equalsIgnoreCase("ZONA4")) {
                imprimirZona4();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void TokenLocal() {
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

    public void NitLocal() {
        //  DecimalFormat df = new DecimalFormat("#.00");
        try {
            BDConexion conecta = new BDConexion();
            Connection cn = conecta.getConexion();
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery("select nombre,idNit from compradornit where nit ='" + NIT.getText() + "'");
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

    public void NitValidar() {
        //  DecimalFormat df = new DecimalFormat("#.00");
        try {
            BDConexion conecta = new BDConexion();
            Connection cn = conecta.getConexion();
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery("select count(nit) from compradornit where nit ='" + NIT.getText() + "'");
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

    public void sumaTotal() {
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

    private void Obtenernit() {

        /*String NitCeros = String.format("%12s", nit.getText()).replace(' ','0');
         NI = NitCeros;*/
        RestApiClient apiClient = new RestApiClient();

        try {
            String apiKey = "TAXID=000120011662&DATA1=SHARED_GETINFONITcom&DATA2=NIT%7C" + NIT.getText() + "&COUNTRY=GT&USERNAME=120011662";
            String accessToken = Token;
            String response = apiClient.get(apiKey, accessToken);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arrayObject = (JSONArray) jsonObject.get("RESPONSE");
            for (int i = 0; i < arrayObject.length(); i++) {
                if (i == 0) {
                    JSONObject object2 = (JSONObject) arrayObject.get(i);
                    NI = object2.get("NOMBRE").toString();
                    String nombrev = object2.get("NOMBRE").toString();
                    String nitv = object2.get("NIT").toString();
                    nombre.setText(nombrev);
                    NIT.setText(nitv);
                }
            }
            if (NI.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(null, "NIT INGRESADO ES INCORRECTO");
                nombre.setText("");
                NIT.setText("");
                NIT.requestFocus();
            } else {
                InsertarDatosComprador();
                facturar.requestFocus();
            }

        } catch (IOException ex) {
            Logger.getLogger(FELCobrosCF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Certificar() {
        FELclas apiClient = new FELclas();

        try {
            String apiKey = "TAXID=000120011662&FORMAT=''&USERNAME=120011662";//NIT DE NEGOCIO y USUARIO QUE DE DIGIFAC
            String accessToken = Token;
            //System.out.println("Token = "+Token);
            String response = apiClient.get(apiKey, accessToken);
            JSONObject jsonObject = new JSONObject(response);
            JSONObject object2 = (jsonObject);
            //System.out.println("JSON = "+jsonObject);
            autoriza = object2.get("authNumber").toString();
            serie = object2.get("batch").toString();
            numero = object2.get("serial").toString();
            FechaCerti = object2.get("enrolledTimeStamp").toString();
            
            if(autoriza.compareTo("")!=0 && serie.compareTo("")!=0 && numero.compareTo("")!=0 && FechaCerti.compareTo("")!=0)
            {
               InsertarDatosFEL();
            }else{
                JOptionPane.showMessageDialog(null, "NO SE GENERO LA FACTURA...");
            }
            
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
        }

    }

    private void crearXMLEncuentro() {

        NumeroLetras NumLetra = new NumeroLetras();
        String numero = total.getText();
        TotalLetras = (NumLetra.Convertir(numero, true));

        try {
            CrearXML_Encuentro ejemploXML = new CrearXML_Encuentro(nombre.getText(), NIT.getText(), Orden.getText(), grantotal, TotalLetras);
            Document documento = ejemploXML.crearDocumento();

            //System.out.println(ejemploXML.convertirString(documento));
            ejemploXML.escribirArchivo(documento, "C:\\Reportes\\XMLFel.xml");

        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, "Error de configuracion");
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            LOGGER.log(Level.SEVERE, "Error de transformacion XML a String");
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void crearXMLZona4() {

        NumeroLetras NumLetra = new NumeroLetras();
        String numero = total.getText();
        TotalLetras = (NumLetra.Convertir(numero, true));

        try {
            CrearXML_Zona4 XMLZona4 = new CrearXML_Zona4(nombre.getText(), NIT.getText(), Orden.getText(), grantotal, TotalLetras);
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

    private void imprimirEncuentro() {

        BDConexion con = new BDConexion();
        Connection conexion = con.getConexion();
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Reportes\\FEL\\FELElEncuentro.jasper");
            Map parametros = new HashMap();
            parametros.put("ID_ORDEN", id_orden);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexion);
            JasperPrintManager.printReport(print, true);
        } catch (Exception e) {
            System.out.println("F" + e);
            JOptionPane.showMessageDialog(null, "ERROR EJECUTAR REPORTES =  " + e);
        }
    }
    
     private void imprimirEncuentroSinValor() {

        BDConexion con = new BDConexion();
        Connection conexion = con.getConexion();
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Reportes\\FEL\\FELElEncuentroSinValor.jasper");
            Map parametros = new HashMap();
            parametros.put("ID_ORDEN", id_orden);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexion);
            JasperPrintManager.printReport(print, true);
        } catch (Exception e) {
            System.out.println("F" + e);
            JOptionPane.showMessageDialog(null, "ERROR EJECUTAR REPORTES =  " + e);
        }
    }

    private void imprimirZona4() {

        BDConexion con = new BDConexion();
        Connection conexion = con.getConexion();
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Reportes\\FEL\\FELZona4.jasper");
            Map parametros = new HashMap();
            parametros.put("ID_ORDEN", id_orden);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexion);
            JasperPrintManager.printReport(print, true);
        } catch (Exception e) {
            System.out.println("F" + e);
            JOptionPane.showMessageDialog(null, "ERROR EJECUTAR REPORTES =  " + e);
        }
    }
    private void imprimirZona4SinValor() {

        BDConexion con = new BDConexion();
        Connection conexion = con.getConexion();
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Reportes\\FEL\\FELZona4SinValor.jasper");
            Map parametros = new HashMap();
            parametros.put("ID_ORDEN", id_orden);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexion);
            JasperPrintManager.printReport(print, true);
        } catch (Exception e) {
            System.out.println("F" + e);
            JOptionPane.showMessageDialog(null, "ERROR EJECUTAR REPORTES =  " + e);
        }
    }

    private void borrar1() {
        String cadena;
        cadena = MONTOCAMBIO.getText();
        if (cadena.length() > 0) {
            cadena = cadena.substring(0, cadena.length() - 1);
        }
        MONTOCAMBIO.setText(cadena);
    }

    private void borrar2() {
        String cadena;
        cadena = EFECTIVO.getText();
        if (cadena.length() > 0) {
            cadena = cadena.substring(0, cadena.length() - 1);
        }
        EFECTIVO.setText(cadena);
    }

    private void borrar3() {
        String cadena;
        cadena = NIT.getText();
        if (cadena.length() > 0) {
            cadena = cadena.substring(0, cadena.length() - 1);
        }
        NIT.setText(cadena);
    }

    private void finalizar() {

        try {
            BDConexion conecta = new BDConexion();
            Connection con = conecta.getConexion();
            PreparedStatement smtp = null;
            if (para == 3) {
                smtp = con.prepareStatement("update PEDIDOS SET EFECTIVO = 0.00, TARJETA = 0.00 WHERE ID_PEDIDO =" + Orden.getText());
            } else {
                smtp = con.prepareStatement("update PEDIDOS SET EFECTIVO = " + EFECTIVO.getText() + ", TARJETA = " + TARJETA.getText() + " WHERE ID_PEDIDO =" + Orden.getText());
            }
            smtp.executeUpdate();
            con.close();
            smtp.close();
        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(null, ex);
        }
    }

    private void facturar() {
        ValidarOrden();
     if(validarorden <= 0){
        if (NIT.getText().compareTo("") != 0 && nombre.getText().compareTo("") != 0) {

            if (Usuario.equalsIgnoreCase("ENCUENTRO")) {
                crearXMLEncuentro();
                Certificar();
            } else if (Usuario.equalsIgnoreCase("ZONA4")) {

                crearXMLZona4();
                Certificar();
            }
        } else {
            JOptionPane.showMessageDialog(null, "INGRESE UN NIT O MARCAR CF");
        }
    } else{
     
        if (Usuario.equalsIgnoreCase("ENCUENTRO")) {
                imprimirEncuentro();
            } else if (Usuario.equalsIgnoreCase("ZONA4")) {
                imprimirZona4();
            }
     
     }
    }
     public void ValidarOrden() {
        //  DecimalFormat df = new DecimalFormat("#.00");
        try {
            BDConexion conecta = new BDConexion();
            Connection cn = conecta.getConexion();
            java.sql.Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery("select count(id_pedido) from fel where id_pedido =" + Orden.getText());
            while (rs.next()) {
                validarorden = rs.getInt(1);
            }
            rs.close();
            stmt.close();
            cn.close();
        } catch (Exception error) {
            System.out.print(error);
        }
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
        NIT = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        facturar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Orden = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        SIETE = new javax.swing.JButton();
        OCHO = new javax.swing.JButton();
        NUEVE = new javax.swing.JButton();
        SEIS = new javax.swing.JButton();
        CINCO = new javax.swing.JButton();
        TRES = new javax.swing.JButton();
        DOS = new javax.swing.JButton();
        CERO = new javax.swing.JButton();
        PUNTO = new javax.swing.JButton();
        BORRAR = new javax.swing.JButton();
        CUATRO = new javax.swing.JButton();
        UNO = new javax.swing.JButton();
        ENTER = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        EYT = new javax.swing.JButton();
        EFECTIVO = new javax.swing.JTextField();
        TARJETA = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        MONTOCAMBIO = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        VUELTO = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("NIT");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("NOMBRE");

        NIT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        NIT.setEnabled(false);
        NIT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NITMouseClicked(evt);
            }
        });
        NIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NITActionPerformed(evt);
            }
        });
        NIT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                NITKeyTyped(evt);
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

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton1.setText("NIT");
        jButton1.setPreferredSize(new java.awt.Dimension(72, 39));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 100, -1));

        jButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton2.setText("CF");
        jButton2.setPreferredSize(new java.awt.Dimension(72, 39));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 100, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("#ORDEN:");

        Orden.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Orden.setForeground(new java.awt.Color(255, 0, 0));
        Orden.setText("------------");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("TOTAL");

        total.setBackground(new java.awt.Color(255, 255, 255));
        total.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        total.setForeground(new java.awt.Color(255, 0, 0));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setText("0.00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Orden, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Orden, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(74, 183, 162));

        SIETE.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        SIETE.setText("7");
        SIETE.setPreferredSize(new java.awt.Dimension(60, 60));
        SIETE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIETEActionPerformed(evt);
            }
        });

        OCHO.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        OCHO.setText("8");
        OCHO.setPreferredSize(new java.awt.Dimension(60, 60));
        OCHO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OCHOActionPerformed(evt);
            }
        });

        NUEVE.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        NUEVE.setText("9");
        NUEVE.setPreferredSize(new java.awt.Dimension(60, 60));
        NUEVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NUEVEActionPerformed(evt);
            }
        });

        SEIS.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        SEIS.setText("6");
        SEIS.setPreferredSize(new java.awt.Dimension(60, 60));
        SEIS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SEISActionPerformed(evt);
            }
        });

        CINCO.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        CINCO.setText("5");
        CINCO.setPreferredSize(new java.awt.Dimension(60, 60));
        CINCO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CINCOActionPerformed(evt);
            }
        });

        TRES.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        TRES.setText("3");
        TRES.setPreferredSize(new java.awt.Dimension(60, 60));
        TRES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TRESActionPerformed(evt);
            }
        });

        DOS.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        DOS.setText("2");
        DOS.setPreferredSize(new java.awt.Dimension(60, 60));
        DOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DOSActionPerformed(evt);
            }
        });

        CERO.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        CERO.setText("0");
        CERO.setPreferredSize(new java.awt.Dimension(60, 60));
        CERO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CEROActionPerformed(evt);
            }
        });

        PUNTO.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        PUNTO.setText(".");
        PUNTO.setPreferredSize(new java.awt.Dimension(60, 60));
        PUNTO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PUNTOActionPerformed(evt);
            }
        });

        BORRAR.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        BORRAR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pngwing.com (1).png"))); // NOI18N
        BORRAR.setPreferredSize(new java.awt.Dimension(60, 60));
        BORRAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BORRARActionPerformed(evt);
            }
        });

        CUATRO.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        CUATRO.setText("4");
        CUATRO.setPreferredSize(new java.awt.Dimension(60, 60));
        CUATRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CUATROActionPerformed(evt);
            }
        });

        UNO.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        UNO.setText("1");
        UNO.setPreferredSize(new java.awt.Dimension(60, 60));
        UNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UNOActionPerformed(evt);
            }
        });

        ENTER.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        ENTER.setText("ENTER");
        ENTER.setPreferredSize(new java.awt.Dimension(60, 60));
        ENTER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ENTERActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(UNO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(SIETE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CUATRO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(OCHO, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CINCO, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NUEVE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SEIS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TRES, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(PUNTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CERO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(BORRAR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SIETE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OCHO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NUEVE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SEIS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CINCO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CUATRO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TRES, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UNO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BORRAR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CERO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PUNTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "FORMA DE PAGO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jButton4.setText("EFECTIVO");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("TARJETA");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        EYT.setText("PARCIAL");
        EYT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EYTActionPerformed(evt);
            }
        });

        EFECTIVO.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EFECTIVO.setEnabled(false);
        EFECTIVO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EFECTIVOMouseClicked(evt);
            }
        });
        EFECTIVO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                EFECTIVOKeyTyped(evt);
            }
        });

        TARJETA.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TARJETA.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("MONTO EN EFECTIVO");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("MONTO EN TARJETA");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EYT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(EFECTIVO, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(TARJETA, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(EFECTIVO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EYT, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TARJETA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jButton3.setBackground(new java.awt.Color(255, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton3.setText("SALIR");
        jButton3.setPreferredSize(new java.awt.Dimension(72, 39));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        MONTOCAMBIO.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        MONTOCAMBIO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        MONTOCAMBIO.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("MONTO");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("CAMBIO");

        VUELTO.setEditable(false);
        VUELTO.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        VUELTO.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MONTOCAMBIO)
                    .addComponent(VUELTO)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MONTOCAMBIO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(VUELTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                            .addComponent(nombre, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NIT, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(facturar, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(facturar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void facturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facturarActionPerformed

        if (EFECTIVO.getText().compareTo("") != 0) {
            finalizar();
            facturar();
            Entra F = new Entra();
            F.setVisible(true);
            this.dispose();
            
            //System.out.println("si factura");
        } else {

            JOptionPane.showMessageDialog(null, "LLENAR LA CANTIDAD DE EFECTIVO O SELECCIONAR OTRO METODO DE PAGO");

        }


    }//GEN-LAST:event_facturarActionPerformed

    private void NITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NITActionPerformed
        NitValidar();
        if (validarnit == 1) {
            NitLocal();
            facturar.requestFocus();
        } else {
            Obtenernit();
        }

    }//GEN-LAST:event_NITActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        NIT.setText("CF");
        nombre.setText("CONSUMIDOR FINAL");
        IdNitCliente = 1;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        NIT.setText("");
        nombre.setText("");
        NIT.setEnabled(true);
        NIT.requestFocus();
        focoteclado = 3;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        switch (pago) {
            case 0:
                JOptionPane.showMessageDialog(null, "SELECCIONAR METODO DE PAGO...");
                break;
            case 3:
                if (EFECTIVO.getText().compareTo("") != 0) {
                    finalizar();
                    
                     if (Usuario.equalsIgnoreCase("ENCUENTRO")) {
                        imprimirEncuentroSinValor();
                    } else if (Usuario.equalsIgnoreCase("ZONA4")) {
                        imprimirZona4SinValor();
            }
                    
                    Entra F = new Entra();
                    F.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "LLENAR LA CANTIDAD DE EFECTIVO O SELECCIONAR OTRO METODO DE PAGO");
                }
                break;
            default:
                finalizar();
                if (Usuario.equalsIgnoreCase("ENCUENTRO")) {
                        imprimirEncuentroSinValor();
                    } else if (Usuario.equalsIgnoreCase("ZONA4")) {
                        imprimirZona4SinValor();
                    }
                Entra F = new Entra();
                F.setVisible(true);
                this.dispose();
                break;
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void NITKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NITKeyTyped
        char c = evt.getKeyChar();
        if ((c < '0' || c > '9') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c != 'ñ') && (c != 'Ñ')) {
            evt.consume();
        }
    }//GEN-LAST:event_NITKeyTyped

    private void SIETEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIETEActionPerformed

        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "7");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "7");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "7");
            }
            default -> {
            }
        }

    }//GEN-LAST:event_SIETEActionPerformed

    private void OCHOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OCHOActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "8");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "8");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "8");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_OCHOActionPerformed

    private void NUEVEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NUEVEActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "9");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "9");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "9");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_NUEVEActionPerformed

    private void SEISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SEISActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "6");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "6");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "6");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_SEISActionPerformed

    private void CINCOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CINCOActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "5");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "5");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "5");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_CINCOActionPerformed

    private void TRESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TRESActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "3");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "3");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "3");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_TRESActionPerformed

    private void DOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DOSActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "2");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "2");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "2");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_DOSActionPerformed

    private void CEROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CEROActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "0");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "0");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "0");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_CEROActionPerformed

    private void PUNTOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PUNTOActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + ".");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + ".");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + ".");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_PUNTOActionPerformed

    private void BORRARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BORRARActionPerformed
        switch (focoteclado) {
            case 1 ->
                borrar1();
            case 2 ->
                borrar2();
            case 3 ->
                borrar3();
            default -> {
            }
        }
    }//GEN-LAST:event_BORRARActionPerformed

    private void CUATROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CUATROActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "4");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "4");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "4");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_CUATROActionPerformed

    private void UNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UNOActionPerformed
        switch (focoteclado) {
            case 1 -> {
                MONTOCAMBIO.requestFocus();
                MONTOCAMBIO.setText(MONTOCAMBIO.getText() + "1");
            }
            case 2 -> {
                EFECTIVO.requestFocus();
                EFECTIVO.setText(EFECTIVO.getText() + "1");
            }
            case 3 -> {
                NIT.requestFocus();
                NIT.setText(NIT.getText() + "1");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_UNOActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        EFECTIVO.setText(total.getText());
        TARJETA.setText("0.00");
        MONTOCAMBIO.setEnabled(true);
        EFECTIVO.setEnabled(false);
        MONTOCAMBIO.requestFocus();
        focoteclado = 1;
        pago = 1;
    }//GEN-LAST:event_jButton4ActionPerformed

    private void EFECTIVOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EFECTIVOMouseClicked
        EFECTIVO.requestFocus();
        focoteclado = 2;
    }//GEN-LAST:event_EFECTIVOMouseClicked

    private void NITMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NITMouseClicked
        NIT.requestFocus();
        focoteclado = 3;
    }//GEN-LAST:event_NITMouseClicked

    private void ENTERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ENTERActionPerformed
        if (focoteclado == 1) {
            Double a = Double.valueOf(MONTOCAMBIO.getText());
            Double b = Double.valueOf(EFECTIVO.getText());
            Double cambio = a - b;
            VUELTO.setText(String.valueOf(df.format(cambio)));
        } else if (focoteclado == 2) {
            double totale = Double.parseDouble(total.getText());
            double efectivo = Double.parseDouble(EFECTIVO.getText());
            double contarje = totale - efectivo;
            TARJETA.setText(String.valueOf(contarje));
        } else if (focoteclado == 3) {
            NitValidar();
            if (validarnit == 1) {
                NitLocal();
                facturar.requestFocus();
            } else {
                Obtenernit();
            }
        }
    }//GEN-LAST:event_ENTERActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        EFECTIVO.setText("0.00");
        EFECTIVO.setEnabled(false);
        TARJETA.setText(total.getText());
        MONTOCAMBIO.setEnabled(false);
        MONTOCAMBIO.setText("");
        VUELTO.setText("");
        pago = 2;
        focoteclado = 0;
    }//GEN-LAST:event_jButton5ActionPerformed

    private void EYTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EYTActionPerformed
        EFECTIVO.setText("");
        TARJETA.setText(total.getText());
        EFECTIVO.setEnabled(true);
        EFECTIVO.requestFocus();
        focoteclado = 2;
        MONTOCAMBIO.setEnabled(false);
        MONTOCAMBIO.setText("");
        VUELTO.setText("");
        pago = 3;
    }//GEN-LAST:event_EYTActionPerformed

    private void EFECTIVOKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EFECTIVOKeyTyped

    }//GEN-LAST:event_EFECTIVOKeyTyped

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
            java.util.logging.Logger.getLogger(FELCobrosCF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FELCobrosCF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FELCobrosCF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FELCobrosCF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
    private javax.swing.JButton BORRAR;
    private javax.swing.JButton CERO;
    private javax.swing.JButton CINCO;
    private javax.swing.JButton CUATRO;
    private javax.swing.JButton DOS;
    private javax.swing.JTextField EFECTIVO;
    private javax.swing.JButton ENTER;
    private javax.swing.JButton EYT;
    private javax.swing.JTextField MONTOCAMBIO;
    private javax.swing.JTextField NIT;
    private javax.swing.JButton NUEVE;
    private javax.swing.JButton OCHO;
    private javax.swing.JLabel Orden;
    private javax.swing.JButton PUNTO;
    private javax.swing.JButton SEIS;
    private javax.swing.JButton SIETE;
    private javax.swing.JTextField TARJETA;
    private javax.swing.JButton TRES;
    private javax.swing.JButton UNO;
    private javax.swing.JTextField VUELTO;
    private javax.swing.JButton facturar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField nombre;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
