package serviciodietas.gui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import serviciodieta.persistencia.SpreadSheets;

import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Color;

import serviciodietas.data.*;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.swing.CellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public static List<Cliente> clientes = new ArrayList<>();
	
	private JPanel contentPane;
	private static JTable tablaClientes = new JTable();
	JTextField filtroNombre;
	private TableRowSorter filtro;


	
	public VentanaPrincipal() throws IOException, GeneralSecurityException {
		inicializar();
		
	}
	
	public void inicializar() throws IOException, GeneralSecurityException{
		
		//DEFINIR VENTANA
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Servicio Nutrinafit");
		//SE ORDENA LA TABLA AL ABRIR
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                int columnaTabla = 0;
                filtro.setRowFilter(RowFilter.regexFilter(filtroNombre.getText(), columnaTabla));
                //tablaClientes.getRowSorter().toggleSortOrder(columnaTabla);
            }
        });
		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setForeground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		
		//INICIALIZAR DATOS A LIST-CLIENTES
		clientes = serviciodieta.persistencia.ServicioSpreadSheets.cargarClientes();

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tablaClientes.setRowHeight(30);
		tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		((DefaultTableCellRenderer) tablaClientes.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		tablaClientes.setModel(new ModeloTablaClientes(clientes));
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBounds(51, 75, 574, 292);
		panelTabla.setLayout(new BorderLayout());
		
		//CAMBIOS DE ANCHURA
		tablaClientes.getColumnModel().getColumn(0).setMaxWidth(360);
		tablaClientes.getColumnModel().getColumn(1).setMaxWidth(100);
		tablaClientes.getColumnModel().getColumn(2).setMaxWidth(55);
		tablaClientes.getColumnModel().getColumn(3).setMaxWidth(55);
		
		//AÑADIR EDITORES DE LAS CELDAS
		tablaClientes.getColumnModel().getColumn(2).setCellRenderer(new DescargarRendererEditor(this));
		tablaClientes.getColumnModel().getColumn(2).setCellEditor(new DescargarRendererEditor(this));
		
		tablaClientes.getColumnModel().getColumn(3).setCellRenderer(new EditarRendererEditor(this));
		tablaClientes.getColumnModel().getColumn(3).setCellEditor(new EditarRendererEditor(this));
		
		tablaClientes.getColumnModel().getColumn(1).setCellRenderer(new RendererTablaClientes());
        
		panelTabla.add(tablaClientes);
		
		//AÑADIR COMPONENTES
		JScrollPane scroll = new JScrollPane(tablaClientes,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelTabla.add(scroll, BorderLayout.CENTER);
		contentPane.add(panelTabla);
		
		ImageIcon imagenActualizar =  new ImageIcon(getClass().getResource("/iconoActualizar.png"));
		
		filtro = new TableRowSorter(tablaClientes.getModel());
		tablaClientes.setRowSorter(filtro);
		
		filtroNombre = new JTextField();
		filtroNombre.setBounds(134, 39, 176, 26);
		contentPane.add(filtroNombre);
		filtroNombre.setColumns(15);
		
		JLabel lblNewLabel = new JLabel("Buscar:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel.setBounds(56, 39, 68, 25);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 228, 181));
		panel.setBounds(51, 33, 282, 38);
		contentPane.add(panel);
		
		filtro();
		
		//AÑADIR FILTRO
		filtroNombre.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				String cadena = filtroNombre.getText().toUpperCase();
				filtroNombre.setText(cadena);
				filtro();
			}
		});
		
		
        
		
	}
	

	
	//FUNCIONES DEL FILTRO	
	public void filtro() {
		int columnaTabla = 0;
		filtro.setRowFilter(RowFilter.regexFilter(filtroNombre.getText(), columnaTabla));
		
	}
}
