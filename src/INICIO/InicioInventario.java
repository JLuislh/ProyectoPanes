/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package INICIO;

import clas.BDConexion;
import clas.BDProductos;
import clas.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author jluis
 */
public class InicioInventario extends javax.swing.JFrame {
      int codigo1;
      String Fechain;
      String Fechaout;
      String Fecha;
      int existe;
      int existeFin;
      String FechaListar; 
    /**
     * Creates new form InicioInventario
     */
    public InicioInventario() {
        try {
             UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
             
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        //JOptionPane.showMessageDialog(null, "ENTRA1");
        try {
            initComponents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        JOptionPane.showMessageDialog(null, "ENTRA2");
        this.setLocationRelativeTo(null);
        ListarProductos();
        ListarHistorial();
        Guardar.setEnabled(false);
        
    }
    
    
     private void ListarHistorial() {
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         FechaListar = df.format(new Date());
        ArrayList<Productos> result = BDProductos.ListarProductosHistorialInventario(FechaListar);
        Lista(result);
    }

    private void Lista(ArrayList<Productos> list) {

        Object[][] dato = new Object[list.size()][5];
        int f = 0;
        for (Productos a : list) {
            dato[f][0] = a.getCodigo();
            dato[f][1] = a.getDescripcion();
            dato[f][2] = a.getCantidadinicial();
            dato[f][3] = a.getCantidadfinal();
            dato[f][4] = a.getCantidad2();
            f++;
        }
        Historial.setModel(new javax.swing.table.DefaultTableModel(
                dato,
                new String[]{
                    "CODIGO", "DESCRIPCION","CANTIDAD INICIAL","CANTIDAD FINAL","CANTIDAD"
                }) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                    return false;
                    }
                });
        
             TableColumn columna1 = Historial.getColumn("CODIGO");
             columna1.setPreferredWidth(10);
             TableColumn columna2 = Historial.getColumn("DESCRIPCION");
             columna2.setPreferredWidth(150);
              TableColumn columna3 = Historial.getColumn("CANTIDAD INICIAL");
             columna3.setPreferredWidth(50);
             TableColumn columna4 = Historial.getColumn("CANTIDAD FINAL");
             columna4.setPreferredWidth(50);
             TableColumn columna5 = Historial.getColumn("CANTIDAD");
             columna5.setPreferredWidth(30);
    }
    
    private void ListarProductos() {
        ArrayList<Productos> result = BDProductos.ListarProductosInventario();
        recagarTabla(result);
    }

    private void recagarTabla(ArrayList<Productos> list) {

        Object[][] dato = new Object[list.size()][3];
        int f = 0;
        for (Productos a : list) {
            dato[f][0] = a.getCodigo();
            dato[f][1] = a.getDescripcion();
            dato[f][2] = a.getCantidad2();
            f++;
        }
        Inventario.setModel(new javax.swing.table.DefaultTableModel(
                dato,
                new String[]{
                    "CODIGO","DESCRIPCION","CANTIDAD"
                }) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                    return false;
                    }
                });
        
             TableColumn columna1 = Inventario.getColumn("CODIGO");
             columna1.setPreferredWidth(0);
             TableColumn columna2 = Inventario.getColumn("DESCRIPCION");
             columna2.setPreferredWidth(275);
              TableColumn columna3 = Inventario.getColumn("CANTIDAD");
             columna3.setPreferredWidth(50);
    }
    private void BuscarProducto() {
    
     try {
            Productos ca = BDProductos.BuscarProducto(Integer.parseInt(String.valueOf(Inventario.getModel().getValueAt(Inventario.getSelectedRow(), 0))));
            //LaDescrip.setText(ca.getDescripcion());
            //unidadMedida.setText(ca.getPresentacion());
            codigo.setText(String.valueOf(ca.getCodigo()));
            descripcion.setText(ca.getDescripcion());
            cantidad.setText(String.valueOf(ca.getCantidad2()));
          
        } catch (Exception e) {
            System.out.println("ERROR REPORTE AL ADMINISTRADOR DE SISTEMA" + e);
        }
    }
    
    private void Limpiar(){
    
    codigo.setText("");
    descripcion.setText("");
    cantidad.setText("");
    cantidadCarga.setText("");
    ListarProductos();
    ListarProductosIngresados();
    
    }
    
    private void ListarProductosIngresados() {
        ArrayList<Productos> result = BDProductos.ListarProductosInventarioIngresados(codigo1);
        ingresos(result);
    }

    private void ingresos(ArrayList<Productos> list) {

        Object[][] dato = new Object[list.size()][2];
        int f = 0;
        for (Productos a : list) {
            dato[f][0] = a.getFecha();
            dato[f][1] = a.getCantidad2();
            f++;
        }
        ingresos.setModel(new javax.swing.table.DefaultTableModel(
                dato,
                new String[]{
                    "FECHA","CANTIDAD"
                }) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                    return false;
                    }
                });
        
             TableColumn columna1 = ingresos.getColumn("FECHA");
             columna1.setPreferredWidth(50);
             TableColumn columna2 = ingresos.getColumn("CANTIDAD");
             columna2.setPreferredWidth(50);
    }
    
    
    
    
    
    public  void ObtenerExistecian() {
         Date Fecha = new Date();
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         Fechain = df.format(Fecha);
            try {
                 BDConexion conecta = new BDConexion();
                Connection cn = conecta.getConexion();
                java.sql.Statement stmt = cn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as Existe FROM consumos WHERE FECHA = '"+ Fechain +"'");
                while (rs.next()) {
                    existe = rs.getInt(1);
                }
                rs.close();
                stmt.close();
                cn.close();
                
            } catch (Exception error) {
                System.out.print(error);
            }
            System.out.println("Existe = "+existe);
        }
    
    
    
    public void actualizarCantidadHistorial(){
        
         Date Fe = new Date();
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         Fecha = df.format(Fe);
        if(cantidadCarga.getText().compareTo("")!=0){       
        BDConexion conecta = new BDConexion();
        Connection con = conecta.getConexion();
        PreparedStatement s = null;
        try {
            s = con.prepareStatement("{call IngresoInventarioHistorial("+codigo.getText()+","+cantidadCarga.getText()+",'"+Fecha+"')}");
            s.executeUpdate();
            con.close();
            JOptionPane.showMessageDialog(null, "INGRESO AGREGADO");
            Limpiar();
        } catch (SQLException ex) {
            System.out.println("ERROR ="+ex);
            //Logger.getLogger(CargaProductos_Encuentro.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{JOptionPane.showMessageDialog(null, "NO SE CARGA NINGUNA CANTIDAD");}


}
    
        private void IniciarInventario() {
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         Fechain = df.format(new Date());
          //System.out.println("FECHA = "+Fecha); 
       BDConexion conecta = new BDConexion();
        Connection con = conecta.getConexion();
        PreparedStatement sm = null;
        try {
            sm = con.prepareStatement("insert into consumos(codigo,cantidadInicio,fecha,estado) (select codigo,cantidad,'"+Fechain+"',1 from productos_inventario)");
            sm.executeUpdate();
            con.close();
            sm.close();
            JOptionPane.showMessageDialog(null, "INICIO DE INVENTARIO AGREGADO");
        } catch (SQLException ex) {
            System.out.println("ERROR ="+ex);
            //Logger.getLogger(CargaProductos_Encuentro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


 private void FinalizarInventario() {
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Fechaout = df.format(new Date());
          //System.out.println("FECHA = "+Fecha); 
       BDConexion conecta = new BDConexion();
        Connection con = conecta.getConexion();
        PreparedStatement sm = null;
        try {
            sm = con.prepareStatement("update consumos c,productos_inventario p set c.CantidadFinal = p.cantidad,estado = 2 where c.Codigo = p.codigo and c.fecha = '"+Fechaout+"'");
            sm.executeUpdate();
            con.close();
            sm.close();
            JOptionPane.showMessageDialog(null, "INICIO DE INVENTARIO AGREGADO");
        } catch (SQLException ex) {
            System.out.println("ERROR ="+ex);
            //Logger.getLogger(CargaProductos_Encuentro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 
  public  void ObtenerExistecianFinalizar() {
        
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         Fechaout = df.format(new Date());
          //System.out.println("FECHA = "+Fecha);
            try {
                 BDConexion conecta = new BDConexion();
                Connection cn = conecta.getConexion();
                java.sql.Statement stmt = cn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as Existe FROM consumos WHERE ESTADO = 1 and FECHA = '"+ Fechaout +"'");
                while (rs.next()) {
                      existeFin = rs.getInt(1);
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

        Inicio = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Inventario = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        ingresos = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cantidadCarga = new javax.swing.JTextField();
        Guardar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        codigo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        descripcion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        Historial = new javax.swing.JTable();
        Guardar1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Inventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "DESCRIPCION"
            }
        ));
        Inventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InventarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Inventario);

        ingresos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "FECHA", "CANTIDAD"
            }
        ));
        jScrollPane2.setViewportView(ingresos);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CANTIDAD INGRESO");

        cantidadCarga.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cantidadCarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadCargaActionPerformed(evt);
            }
        });

        Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/save2.png"))); // NOI18N
        Guardar.setText("GUARDAR");
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/ComponenteImagenes/Print.png"))); // NOI18N
        jButton1.setText("IMPRIMIR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(cantidadCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cantidadCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("CODIGO");

        jLabel6.setText("DESCRIPCION");

        descripcion.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        descripcion.setForeground(new java.awt.Color(0, 102, 255));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel2.setText("CANTIDAD");

        cantidad.setEditable(false);
        cantidad.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        cantidad.setForeground(new java.awt.Color(255, 0, 0));
        cantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cantidad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(25, 25, 25)))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(5, 5, 5)
                .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        Historial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "DESCRIPCION", "CANTIDAD INICIAL", "CANTIDAD FINAL", "FECHA"
            }
        ));
        jScrollPane3.setViewportView(Historial);

        Guardar1.setText("INICIAR INVENTARIO");
        Guardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Guardar1ActionPerformed(evt);
            }
        });

        jButton4.setText("IMPRIMIR");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setText("CERRAR INVENTARIO");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout InicioLayout = new javax.swing.GroupLayout(Inicio);
        Inicio.setLayout(InicioLayout);
        InicioLayout.setHorizontalGroup(
            InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InicioLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InicioLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(InicioLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(InicioLayout.createSequentialGroup()
                        .addComponent(Guardar1)
                        .addGap(11, 11, 11)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        InicioLayout.setVerticalGroup(
            InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InicioLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InicioLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Guardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(InicioLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(InicioLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Inicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(Inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void InventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventarioMouseClicked
        codigo1 = Integer.parseInt(String.valueOf(Inventario.getModel().getValueAt(Inventario.getSelectedRow(), 0)));
        BuscarProducto();
        ListarProductosIngresados();
        cantidadCarga.requestFocus();
        Guardar.setEnabled(true);
    }//GEN-LAST:event_InventarioMouseClicked

    private void cantidadCargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadCargaActionPerformed
        Guardar.requestFocus();
    }//GEN-LAST:event_cantidadCargaActionPerformed

    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarActionPerformed

        if(cantidadCarga.getText().compareTo("")!=0){

            ObtenerExistecian();
            if(existe == 0 ){

                BDConexion conecta = new BDConexion();
                Connection con = conecta.getConexion();
                PreparedStatement sm = null;
                try {
                    sm = con.prepareStatement("{call IngresoInventario("+codigo.getText()+","+cantidadCarga.getText()+")}"); //,"+fecha.getDateFormatString()+"
                    sm.executeUpdate();
                    con.close();
                    sm.close();
                    JOptionPane.showMessageDialog(null, "INGRESO AGREGADO");
                    Limpiar();
                } catch (SQLException ex) {
                    System.out.println("ERROR ="+ex);
                }
                /////////////////////////////////////////////////////////////
            }else{JOptionPane.showMessageDialog(null, "NO SE CARGA NINGUNA CANTIDAD PORQUE YA SE A INICIADO EL INVENTARIO");}

        }else{ JOptionPane.showMessageDialog(null, "INGRESE CANTIDAD");}

    }//GEN-LAST:event_GuardarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        BDConexion con= new BDConexion();
        Connection conexion= con.getConexion();
        try {
            JasperReport jasperReport=(JasperReport)JRLoader.loadObjectFromFile("C:\\Reportes\\Inventario.jasper");
            //JasperReport jasperReport2=(JasperReport)JRLoader.loadObjectFromFile("\\\\SRVANATEK\\Bases de Datos\\Sistema\\Recursos Humanos\\Reportes\\EvaluacionDesempeñoImprime2.jasper");
            Map parametros= new HashMap();
            JasperPrint print = JasperFillManager.fillReport(jasperReport,parametros, conexion);
            //JasperPrint print2 = JasperFillManager.fillReport(jasperReport2,parametros, conexion);
            JasperPrintManager.printReport(print, true);
            //view2.setVisible(true);
        } catch (Exception e) {System.out.println("F"+e);
            JOptionPane.showMessageDialog(null, "ERROR EJECUTAR REPORTES =  "+e);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void Guardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Guardar1ActionPerformed

        ObtenerExistecian();
        System.out.println("EXISTENCIA = "+existe);
        if(existe == 0)
        {
            //JOptionPane.showMessageDialog(null, "INVENTARIO INICIADO");
            IniciarInventario();
            ListarHistorial();
        }else{
            JOptionPane.showMessageDialog(null, "INVENTARIO YA INICIADO");
        }

    }//GEN-LAST:event_Guardar1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        BDConexion con= new BDConexion();
        Connection conexion= con.getConexion();
        try {
            JasperReport jasperReport=(JasperReport)JRLoader.loadObjectFromFile("C:\\Reportes\\InventarioFinal.jasper");
            //JasperReport jasperReport2=(JasperReport)JRLoader.loadObjectFromFile("\\\\SRVANATEK\\Bases de Datos\\Sistema\\Recursos Humanos\\Reportes\\EvaluacionDesempeñoImprime2.jasper");
            Map parametros= new HashMap();
            JasperPrint print = JasperFillManager.fillReport(jasperReport,parametros, conexion);
            //JasperPrint print2 = JasperFillManager.fillReport(jasperReport2,parametros, conexion);
            JasperPrintManager.printReport(print, true);
            //view2.setVisible(true);
        } catch (Exception e) {System.out.println("F"+e);
            JOptionPane.showMessageDialog(null, "ERROR EJECUTAR REPORTES =  "+e);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ObtenerExistecianFinalizar();
        System.out.println("EXISTENCIA = "+existe);
        if(existeFin > 0)
        {
            FinalizarInventario();
            ListarHistorial();
        }else{
            JOptionPane.showMessageDialog(null, "INVENTARIO YA SE A FINALIZADO PARA ESTA FECHA");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(InicioInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InicioInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InicioInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InicioInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InicioInventario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Guardar;
    private javax.swing.JButton Guardar1;
    private javax.swing.JTable Historial;
    private javax.swing.JPanel Inicio;
    private javax.swing.JTable Inventario;
    private javax.swing.JTextField cantidad;
    private javax.swing.JTextField cantidadCarga;
    private javax.swing.JTextField codigo;
    private javax.swing.JTextField descripcion;
    private javax.swing.JTable ingresos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
