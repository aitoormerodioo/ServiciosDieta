package serviciodietas.gui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Color;

import serviciodietas.data.*;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.CellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class VentanaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private List<Cliente> clientes = new ArrayList<>();
	
	Cliente cliente1 = new Cliente("Jon Lasa");
	Cliente cliente2 = new Cliente("Ane Lasa");
	
	private JPanel contentPane;
	private JTable tablaClientes = new JTable();
	JTextField filtroNombre;

	public VentanaPrincipal() {
		inicializar();
		
	}
	
	public void inicializar(){
		
		//DEFINIR VENTANA
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setForeground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		
		//INICIALIZAR DATOS A LIST-CLIENTES
		clientes.add(cliente1);
		clientes.add(cliente2);
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		filtroNombre = new JTextField();
		filtroNombre.setColumns(15);
		
		tablaClientes.setRowHeight(30);
		tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		((DefaultTableCellRenderer) tablaClientes.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		tablaClientes.setModel(new ModeloTablaClientes(clientes));
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBounds(51, 75, 574, 292);
		panelTabla.setLayout(new BorderLayout());
		
		//CAMBIOS DE ANCHURA
		tablaClientes.getColumnModel().getColumn(0).setMinWidth(95);
		tablaClientes.getColumnModel().getColumn(1).setMaxWidth(55);
		tablaClientes.getColumnModel().getColumn(2).setMaxWidth(55);
		tablaClientes.getColumnModel().getColumn(3).setMaxWidth(55);
		
		//AÑADIR EDITORES DE LAS CELDAS
		tablaClientes.getColumnModel().getColumn(1).setCellRenderer(new DescargarRendererEditor(this));
		tablaClientes.getColumnModel().getColumn(1).setCellEditor(new DescargarRendererEditor(this));
		
		tablaClientes.getColumnModel().getColumn(2).setCellRenderer(new EditarRendererEditor(this));
		tablaClientes.getColumnModel().getColumn(2).setCellEditor(new EditarRendererEditor(this));
		
		tablaClientes.getColumnModel().getColumn(3).setCellRenderer(new BorrarRendererEditor(this));
		tablaClientes.getColumnModel().getColumn(3).setCellEditor(new BorrarRendererEditor(this));
		
		panelTabla.add(tablaClientes);
		
		//AÑADIR COMPONENTES
		JScrollPane scroll = new JScrollPane(tablaClientes,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelTabla.add(scroll, BorderLayout.CENTER);
		contentPane.add(panelTabla);
		
		JPanel panelFiltro = new JPanel();
		panelFiltro.setBounds(51, 34, 282, 30);
		contentPane.add(panelFiltro);
		
		JLabel LabelInfo = new JLabel("Buscar Cliente:");
		LabelInfo.setFont(new Font("Sitka Small", Font.PLAIN, 12));
		panelFiltro.add(LabelInfo);
		
		panelFiltro.add(filtroNombre);
		
		JButton BotonActualizar = new JButton("");
		BotonActualizar.setBackground(Color.WHITE);
		BotonActualizar.setBounds(592, 34, 33, 30);
		
		ImageIcon imagenActualizar =  new ImageIcon("resources/iconoActualizar.png");
		Icon i= new ImageIcon(imagenActualizar.getImage().getScaledInstance(BotonActualizar.getWidth(), BotonActualizar.getHeight(), Image.SCALE_SMOOTH));
		
		BotonActualizar.setIcon(i);
		contentPane.add(BotonActualizar);
		
		
	}
}
