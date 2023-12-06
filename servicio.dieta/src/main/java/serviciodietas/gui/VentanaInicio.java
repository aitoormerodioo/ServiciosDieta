package serviciodietas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import serviciodietas.data.Cliente;
import serviciodietas.data.EntrenadorPrincipiante;
import serviciodietas.data.Usuario;

import java.awt.SystemColor;
import javax.swing.JButton;

public class VentanaInicio extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField textFieldUSU;
	private JTextField textFieldCONTRA;


	
	public VentanaInicio() throws IOException, GeneralSecurityException {
		inicializar();
		
	}
	
	public void inicializar() throws IOException, GeneralSecurityException{
		
		//DEFINIR VENTANA
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Servicio Nutrinafit");

		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setForeground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);

		textFieldUSU = new JTextField();
		textFieldUSU.setBounds(248, 137, 272, 28);
		contentPane.add(textFieldUSU);
		
		textFieldCONTRA = new JTextField();
		textFieldCONTRA.setBounds(248, 210, 272, 28);
		contentPane.add(textFieldCONTRA);
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("BIENVENIDO A NUTRINAFIT!");
		lblNewLabel.setForeground(SystemColor.infoText);
		lblNewLabel.setFont(new Font("Tempus Sans ITC", Font.BOLD, 23));
		lblNewLabel.setBounds(152, 44, 356, 62);
		contentPane.add(lblNewLabel);
		
		JButton btnInicioNormal = new JButton("Iniciar Sesion");
		btnInicioNormal.setBounds(517, 364, 136, 23);
		contentPane.add(btnInicioNormal);
		
		
		
		JLabel lblNewLabel_1 = new JLabel("Usuario:");
		lblNewLabel_1.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
		lblNewLabel_1.setBounds(116, 136, 122, 28);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Contraseña:");
		lblNewLabel_1_1.setFont(new Font("Tempus Sans ITC", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(116, 209, 122, 28);
		contentPane.add(lblNewLabel_1_1);
		
		
		String archivoCSV = "src/main/resources/entrenadoresPrincipiantes.csv";
		
		btnInicioNormal.addActionListener((i) -> {
//		try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
//		    String line;
			
			try {
		    String usuarioIngresado = textFieldUSU.getText();
			String contraseñaIngresada = textFieldCONTRA.getText();
//			
			if (usuarioIngresado.equals("admin") && contraseñaIngresada.equals("admin")) {
				System.out.println("Inicio de sesión exitoso");
	            Usuario usuario = new EntrenadorPrincipiante(usuarioIngresado,contraseñaIngresada);
	            
	            dispose();
				VentanaPrincipal ve = new VentanaPrincipal(usuario);
				ve.setVisible(true);
			} else if (usuarioIngresado.equals("jicaza") && contraseñaIngresada.equals("jicaza")) {
				System.out.println("Inicio de sesión exitoso");
	            Usuario usuario = new EntrenadorPrincipiante(usuarioIngresado,contraseñaIngresada);
	            
	            dispose();
				VentanaPrincipal ve = new VentanaPrincipal(usuario);
				ve.setVisible(true);
			} else if (usuarioIngresado.equals("asierm") && contraseñaIngresada.equals("asierm")) {
				System.out.println("Inicio de sesión exitoso");
	            Usuario usuario = new EntrenadorPrincipiante(usuarioIngresado,contraseñaIngresada);
	            
	            dispose();
				VentanaPrincipal ve = new VentanaPrincipal(usuario);
				ve.setVisible(true);
			} else if (usuarioIngresado.equals("mikel") && contraseñaIngresada.equals("mikel")) {
				System.out.println("Inicio de sesión exitoso");
	            Usuario usuario = new EntrenadorPrincipiante(usuarioIngresado,contraseñaIngresada);
	            
	            dispose();
				VentanaPrincipal ve = new VentanaPrincipal(usuario);
				ve.setVisible(true);
			}
			
		} catch (IOException | GeneralSecurityException e) {
		    e.printStackTrace();
		}
//		    while ((line = br.readLine()) != null) {
//		        String[] fields = line.split(";");
//		        String usuarioCSV = fields[0].trim();
//		        String contraseñaCSV = fields[1].trim();
//
//		        if (usuarioIngresado.equals(usuarioCSV) && contraseñaIngresada.equals(contraseñaCSV)) {
//		            // Iniciar sesión
//		            System.out.println("Inicio de sesión exitoso");
//		            Usuario usuario = new EntrenadorPrincipiante(usuarioIngresado,contraseñaIngresada);
//		            
//		            dispose();
//					VentanaPrincipal ve = new VentanaPrincipal(usuario);
//					ve.setVisible(true);
//					
//		        }
//		    }
//
//		    // Si se llega a este punto, no se encontró una coincidencia válida
//		    //System.out.println("Inicio de sesión fallido");
//		} catch (IOException | GeneralSecurityException e) {
//		    e.printStackTrace();
//		}
//		
		});
		
		//CREAMOS LA IMAGEN DE FONDO
		ImageIcon imagenFondo =  new ImageIcon(getClass().getResource("/fondo.jpg"));
		ImageIcon nuevoIcono = new ImageIcon(imagenFondo.getImage().getScaledInstance(700,450,Image.SCALE_AREA_AVERAGING));
				
		JLabel labelFondo = new JLabel("",nuevoIcono,JLabel.CENTER);
		labelFondo.setBounds(0, 0, 690, 440);

		contentPane.add(labelFondo);
	}
}
