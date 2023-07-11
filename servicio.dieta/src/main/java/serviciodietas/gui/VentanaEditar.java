package serviciodietas.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import serviciodietas.data.Cliente;
import serviciodietas.data.Lesion;
import serviciodietas.data.Lugar;
import serviciodietas.data.Nivel;
import serviciodietas.data.Objetivo;
import serviciodietas.data.Peso;
import serviciodietas.data.Sexo;
import serviciodietas.data.Usuario;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import java.awt.SystemColor;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class VentanaEditar extends JFrame {

	private JPanel contentPane;
	private JRadioButton rdbtnM;
	private JRadioButton rdbtnH;
	private JComboBox comboBoxDIAS;
	private JRadioButton rdbtnDefinicion;
	private JRadioButton rdbtnSuperavit;
	private JRadioButton rdbtnRodilla;
	private JRadioButton rdbtnLumbar;
	private JRadioButton rdbtnHombro;
	private JRadioButton rdbtnNinguna;
	private JRadioButton rdbtnAvanzado;
	private JRadioButton rdbtnINTERMEDIO;
	private JRadioButton rdbtnPRINCIPIANTE;
	//private JComboBox comboBoxMESES;
	private JCheckBox chckbxVegan;
	private JCheckBox chckbxVegetarian;
	private JCheckBox chckbxPescado;
	private JCheckBox chckbxVerdura;
	private JCheckBox chckbxLactosa;
	private JCheckBox chckbxHUEVOS;
	private JCheckBox chckbxGLUTEN;
	private JCheckBox chckbxFRUTOSSECOS;
	private JCheckBox chckbxAGUACATE;
	private JCheckBox chckbxCLARAS;
	private JCheckBox chckbxNINGUNA;
	private JTextField textFieldEnt;
	private JTextField textFieldPESO;
	private JComboBox comboBoxNUMCOM;
	private JRadioButton rdbtnGYM;
	private JRadioButton rdbtnCASACON;
	private JRadioButton rdbtnCASASIN;
	private JPanel panel_1_1;


	public VentanaEditar(Cliente cliente, String busqueda, Usuario u) {
		
		//DEFINIR VENTANA
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Datos del cliente");
		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setForeground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelNombre = new JLabel("");
		labelNombre.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelNombre.setForeground(new Color(0, 0, 255));
		labelNombre.setBounds(21, 11, 182, 42);
		
		
		contentPane.add(labelNombre);
		labelNombre.setText(cliente.getNombreC());
		
		JButton btnCancelar = new JButton("Back");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JOptionPane.showConfirmDialog(null, "Los datos cambiados no se guardaran!", "Estas seguro?", JOptionPane.OK_CANCEL_OPTION);
				VentanaPrincipal vp = null;
				try {
					vp = new VentanaPrincipal(u);
					dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				vp.setVisible(true);
				
			}
		});
		btnCancelar.setBounds(10, 377, 73, 23);
		contentPane.add(btnCancelar);
		
		JButton btnConfirmar = new JButton("Guardar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				//RECOGEMOS LOS DATOS MODIFICADOS
				String nombreC = cliente.getNombreC();
				
				String numT = cliente.getNumeroT();
				
				String sexo;
				
				String entrenador= textFieldEnt.getText().toString();
				
				if (rdbtnM.isSelected()) {
					sexo = "MUJER";
				} else {
					sexo = "HOMBRE";
				}
				
				int peso = Integer.parseInt(textFieldPESO.getText().toString());
				
				int diasentreno= Integer.parseInt(comboBoxDIAS.getSelectedItem().toString());
				
//				int mesesentrenados = Integer.parseInt(comboBoxMESES.getSelectedItem().toString());
				
				int numerocomidas = Integer.parseInt(comboBoxNUMCOM.getSelectedItem().toString());
				
				String objetivo;
				
				if (rdbtnDefinicion.isSelected()) {
					objetivo = "Pérdida de grasa";
				} else {
					objetivo = "Ganancia de músculo (sin grasa)";
				}
					
				String nivel; 
				
				if (rdbtnPRINCIPIANTE.isSelected()) {
					nivel = "Principiante (0-1 años entrenando)";
				} else if (rdbtnINTERMEDIO.isSelected()) {
					nivel = "Intermedio (1-3 años entrenando)";
				} else {
					nivel = "Avanzado (+3 años entrenando)";
				}
				
				String lesiones;
				
				if (rdbtnHombro.isSelected()) {
					lesiones = "HOMBRO (rotadores, me impide hacer ciertos ejercicios)";
				} else if (rdbtnLumbar.isSelected()) {
					lesiones = "LUMBAR (me impide hacer ciertos ejercicios)";
				} else if (rdbtnRodilla.isSelected()){
					lesiones = "RODILLA (me impide hacer ciertos ejercicios)";
				} else {
					lesiones = "NINGUNA";
				}
				
				String lugar;
				
				if (rdbtnGYM.isSelected()) {
					lugar = "GYM";
				} else if (rdbtnCASACON.isSelected()) {
					lugar = "CASA (Tengo material)";
				} else {
					lugar = "CASA (No tengo material)";
				}
				
				String nogustos = "";
				
				if (chckbxNINGUNA.isSelected()) {
					nogustos=nogustos+"NINGUNA, ";
				}  
				
				if (chckbxAGUACATE.isSelected()) {
					nogustos=nogustos+"Aguacate, ";
				}  
				
				if (chckbxCLARAS.isSelected()) {
					nogustos=nogustos+"Claras, ";
				}  
				
				if (chckbxLactosa.isSelected()) {
					nogustos=nogustos+"Lactosa, ";
				}  
				
				if (chckbxFRUTOSSECOS.isSelected()) {
					nogustos=nogustos+"Frutos secos, ";
				}  
				
				if (chckbxGLUTEN.isSelected()) {
					nogustos=nogustos+"Gluten, ";
				} 
				
				if (chckbxHUEVOS.isSelected()) {
					nogustos=nogustos+"Huevos, ";
				} 
				
				if (chckbxVerdura.isSelected()) {
					nogustos=nogustos+"Verdura, ";
				} 
				
				if (chckbxPescado.isSelected()) {
					nogustos=nogustos+"Pescado, ";
				} 
				
				if (chckbxVegan.isSelected()) {
					nogustos=nogustos+"Soy VEGAN@, ";
				} 
				
				if (chckbxVegetarian.isSelected()) {
					nogustos=nogustos+"Soy VEGETARIAN@, ";
				}
				
				try {
					serviciodieta.persistencia.SpreadSheets.modificarCliente(nombreC, sexo, peso, nogustos, diasentreno, nivel, lesiones, objetivo, entrenador, numT, lugar, numerocomidas);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (GeneralSecurityException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				try {
					VentanaPrincipal vp = new VentanaPrincipal(u);
					vp.filtroNombre.setText(busqueda);
					vp.setVisible(true);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
				
				
			}
		});
		btnConfirmar.setBounds(590, 377, 84, 23);
		contentPane.add(btnConfirmar);
		
		JLabel lblSexo = new JLabel("SEXO:");
		lblSexo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSexo.setBounds(21, 69, 39, 14);
		contentPane.add(lblSexo);
		
		rdbtnM = new JRadioButton("MUJER");
		rdbtnM.setBounds(66, 65, 84, 23);
		contentPane.add(rdbtnM);
		
		rdbtnH = new JRadioButton("HOMBRE");
		rdbtnH.setBounds(166, 65, 89, 23);
		contentPane.add(rdbtnH);
		
		JLabel lblPeso = new JLabel("PESO:");
		lblPeso.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPeso.setBounds(21, 127, 46, 14);
		contentPane.add(lblPeso);
		
		JLabel lblnogustos = new JLabel("INTOLERANCIAS/NO GUSTOS:");
		lblnogustos.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblnogustos.setBounds(370, 28, 190, 14);
		contentPane.add(lblnogustos);
		
		chckbxNINGUNA = new JCheckBox("NINGUNA");
		chckbxNINGUNA.setBounds(343, 55, 97, 23);
		contentPane.add(chckbxNINGUNA);
		
		chckbxCLARAS = new JCheckBox("CLARAS");
		chckbxCLARAS.setBounds(451, 55, 89, 23);
		contentPane.add(chckbxCLARAS);
		
		chckbxAGUACATE = new JCheckBox("AGUACATE");
		chckbxAGUACATE.setBounds(542, 55, 119, 23);
		contentPane.add(chckbxAGUACATE);
		
		chckbxFRUTOSSECOS = new JCheckBox("FRUTOS SECOS");
		chckbxFRUTOSSECOS.setBounds(343, 88, 131, 23);
		contentPane.add(chckbxFRUTOSSECOS);
		
		chckbxGLUTEN = new JCheckBox("GLUTEN");
		chckbxGLUTEN.setBounds(476, 88, 84, 23);
		contentPane.add(chckbxGLUTEN);
		
		chckbxHUEVOS = new JCheckBox("HUEVOS");
		chckbxHUEVOS.setBounds(572, 88, 89, 23);
		contentPane.add(chckbxHUEVOS);
		
		chckbxLactosa = new JCheckBox("LACTOSA");
		chckbxLactosa.setBounds(343, 123, 97, 23);
		contentPane.add(chckbxLactosa);
		
		chckbxVerdura = new JCheckBox("VERDURA");
		chckbxVerdura.setBounds(457, 123, 89, 23);
		contentPane.add(chckbxVerdura);
		
		chckbxPescado = new JCheckBox("PESCADO");
		chckbxPescado.setBounds(559, 123, 102, 23);
		contentPane.add(chckbxPescado);
		
		chckbxVegetarian = new JCheckBox("VEGETARIAN@");
		chckbxVegetarian.setBounds(343, 157, 131, 23);
		contentPane.add(chckbxVegetarian);
		
		chckbxVegan = new JCheckBox("VEGAN@");
		chckbxVegan.setBounds(487, 157, 133, 23);
		contentPane.add(chckbxVegan);
		
		JLabel lblDiasEntreno = new JLabel("DIAS DE ENTRENO:");
		lblDiasEntreno.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiasEntreno.setBounds(21, 178, 117, 14);
		contentPane.add(lblDiasEntreno);
		
		comboBoxDIAS = new JComboBox();
		comboBoxDIAS.setMaximumRowCount(5);
		comboBoxDIAS.setBounds(144, 174, 73, 22);
		contentPane.add(comboBoxDIAS);
		
		JLabel lblNivel = new JLabel("NIVEL:");
		lblNivel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNivel.setBounds(21, 260, 46, 14);
		contentPane.add(lblNivel);
		
		rdbtnPRINCIPIANTE = new JRadioButton("PRINCIPIANTE");
		rdbtnPRINCIPIANTE.setBounds(53, 282, 110, 23);
		contentPane.add(rdbtnPRINCIPIANTE);
		
		rdbtnINTERMEDIO = new JRadioButton("INTERMEDIO");
		rdbtnINTERMEDIO.setBounds(53, 308, 110, 23);
		contentPane.add(rdbtnINTERMEDIO);
		
		rdbtnAvanzado = new JRadioButton("AVANZADO");
		rdbtnAvanzado.setBounds(53, 334, 110, 23);
		contentPane.add(rdbtnAvanzado);
		
		JLabel lblLesiones = new JLabel("LESIONES:");
		lblLesiones.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLesiones.setBounds(343, 260, 68, 14);
		contentPane.add(lblLesiones);
		
//		comboBoxMESES = new JComboBox();
//		comboBoxMESES.setMaximumRowCount(13);
//		comboBoxMESES.setBounds(487, 207, 73, 22);
//		contentPane.add(comboBoxMESES);
		
		comboBoxNUMCOM = new JComboBox();
		comboBoxNUMCOM.setMaximumRowCount(4);
		comboBoxNUMCOM.setBounds(240, 123, 68, 22);
		contentPane.add(comboBoxNUMCOM);
		
		rdbtnNinguna = new JRadioButton("NINGUNA");
		rdbtnNinguna.setBounds(417, 256, 117, 23);
		contentPane.add(rdbtnNinguna);
		
		rdbtnHombro = new JRadioButton("HOMBRO");
		rdbtnHombro.setBounds(417, 282, 117, 23);
		contentPane.add(rdbtnHombro);
		
		rdbtnLumbar = new JRadioButton("LUMBAR");
		rdbtnLumbar.setBounds(417, 308, 117, 23);
		contentPane.add(rdbtnLumbar);
		
		rdbtnRodilla = new JRadioButton("RODILLA");
		rdbtnRodilla.setBounds(417, 334, 117, 23);
		contentPane.add(rdbtnRodilla);
		
		JLabel lblObjetivo = new JLabel("OBJETIVO:");
		lblObjetivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblObjetivo.setBounds(21, 221, 68, 14);
		contentPane.add(lblObjetivo);
		
		rdbtnDefinicion = new JRadioButton("DEFINICION");
		rdbtnDefinicion.setBounds(95, 217, 102, 23);
		contentPane.add(rdbtnDefinicion);
		
		rdbtnSuperavit = new JRadioButton("SUPERAVIT");
		rdbtnSuperavit.setBounds(209, 217, 102, 23);
		contentPane.add(rdbtnSuperavit);
		
		
		//POR DEFECTO LOS DATOS DE LOS CLIENTES ESTARAN SELECCIONADOS
		if (cliente.getSexo().equals(Sexo.hombre)) {
			rdbtnH.setSelected(true);
		} else {
			rdbtnM.setSelected(true);
		}
		
		comboBoxDIAS.addItem("2");
		comboBoxDIAS.addItem("3");
		comboBoxDIAS.addItem("4");
		comboBoxDIAS.addItem("5");
		
		
		if (cliente.getDiasentreno() == 2) {
			comboBoxDIAS.setSelectedIndex(0);
		} else if (cliente.getDiasentreno() == 3) {
			comboBoxDIAS.setSelectedIndex(1);
		} else if (cliente.getDiasentreno() == 4){
			comboBoxDIAS.setSelectedIndex(2);
		} else {
			comboBoxDIAS.setSelectedIndex(3);
		}
		

		comboBoxNUMCOM.addItem("2");
		comboBoxNUMCOM.addItem("3");
		comboBoxNUMCOM.addItem("4");
		comboBoxNUMCOM.addItem("5");
		
		if (cliente.getNumerocomidas() == 2) {
			comboBoxNUMCOM.setSelectedIndex(0);
		} else if (cliente.getNumerocomidas() == 3) {
			comboBoxNUMCOM.setSelectedIndex(1);
		} else if (cliente.getNumerocomidas() == 4){
			comboBoxNUMCOM.setSelectedIndex(2);
		} else {
			comboBoxNUMCOM.setSelectedIndex(3);
		}
			
		if (cliente.getObjetivo().equals(Objetivo.definicion)) {
			rdbtnDefinicion.setSelected(true);
		} else {
			rdbtnSuperavit.setSelected(true);
		}
		
		
		if (cliente.getNivel().equals(Nivel.principiante)) {
			rdbtnPRINCIPIANTE.setSelected(true);
		} else if (cliente.getNivel().equals(Nivel.intermedio)) {
			rdbtnINTERMEDIO.setSelected(true);
		} else {
			rdbtnAvanzado.setSelected(true);
		}
		
		if (cliente.getNogustos().contains("NINGUNA")) {
			chckbxNINGUNA.setSelected(true);
		}  
		
		if (cliente.getNogustos().contains("Aguacate")) {
			chckbxAGUACATE.setSelected(true);
		}  
		
		if (cliente.getNogustos().contains("Claras")) {
			chckbxCLARAS.setSelected(true);
		}  
		
		if (cliente.getNogustos().contains("Lactosa")) {
			chckbxLactosa.setSelected(true);
		}  
		
		if (cliente.getNogustos().contains("Frutos secos")) {
			chckbxFRUTOSSECOS.setSelected(true);
		}  
		
		if (cliente.getNogustos().contains("Gluten")) {
			chckbxGLUTEN.setSelected(true);
		} 
		
		if (cliente.getNogustos().contains("Huevos")) {
			chckbxHUEVOS.setSelected(true);
		} 
		
		if (cliente.getNogustos().contains("Verdura")) {
			chckbxVerdura.setSelected(true);
		} 
		
		if (cliente.getNogustos().contains("Pescado")) {
			chckbxPescado.setSelected(true);
		} 
		
		if (cliente.getNogustos().contains("Soy VEGAN@")) {
			chckbxVegan.setSelected(true);
		} 
		
		if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
			chckbxVegetarian.setSelected(true);
		}
		
//		comboBoxMESES.addItem("0");
//		comboBoxMESES.addItem("1");
//		comboBoxMESES.addItem("2");
//		comboBoxMESES.addItem("3");
//		comboBoxMESES.addItem("4");
//		comboBoxMESES.addItem("5");
//		comboBoxMESES.addItem("6");
//		comboBoxMESES.addItem("7");
//		comboBoxMESES.addItem("8");
//		comboBoxMESES.addItem("9");
//		comboBoxMESES.addItem("10");
//		comboBoxMESES.addItem("11");
//		comboBoxMESES.addItem("12");
		
//		if (cliente.getMesesentrenados() == 0) {
//			comboBoxMESES.setSelectedIndex(0);
//		} else if (cliente.getMesesentrenados() == 1) {
//			comboBoxMESES.setSelectedIndex(1);
//		} else if (cliente.getMesesentrenados() == 2) {
//			comboBoxMESES.setSelectedIndex(2);
//		} else if (cliente.getMesesentrenados() == 3) {
//			comboBoxMESES.setSelectedIndex(3);
//		} else if (cliente.getMesesentrenados() == 4) {
//			comboBoxMESES.setSelectedIndex(4);
//		} else if (cliente.getMesesentrenados() == 5) {
//			comboBoxMESES.setSelectedIndex(5);
//		} else if (cliente.getMesesentrenados() == 6) {
//			comboBoxMESES.setSelectedIndex(6);
//		} else if (cliente.getMesesentrenados() == 7) {
//			comboBoxMESES.setSelectedIndex(7);
//		} else if (cliente.getMesesentrenados() == 8) {
//			comboBoxMESES.setSelectedIndex(8);
//		} else if (cliente.getMesesentrenados() == 9) {
//			comboBoxMESES.setSelectedIndex(9);
//		} else if (cliente.getMesesentrenados() == 10) {
//			comboBoxMESES.setSelectedIndex(10);
//		} else if (cliente.getMesesentrenados() == 11) {
//			comboBoxMESES.setSelectedIndex(11);
//		} else {
//			comboBoxMESES.setSelectedIndex(12);
//		}
		
		if (cliente.getLesion().equals(Lesion.hombro)) {
			rdbtnHombro.setSelected(true);
		} else if (cliente.getLesion().equals(Lesion.lumbar)) {
			rdbtnLumbar.setSelected(true);
		} else if (cliente.getLesion().equals(Lesion.rodilla)) {
			rdbtnRodilla.setSelected(true);
		} else {
			rdbtnNinguna.setSelected(true);
		}
		
		//CONDICIONES PARA QUE SOLO ESTE UN RADIO BUTTON SELECCIONADO - CREACION DE GRUPOS
		ButtonGroup grupoSexo = new ButtonGroup();
		grupoSexo.add(rdbtnH);
		grupoSexo.add(rdbtnM);
		
		ButtonGroup grupoPeso = new ButtonGroup();
		
		ButtonGroup grupoObjetivo = new ButtonGroup();
		grupoObjetivo.add(rdbtnSuperavit);
		grupoObjetivo.add(rdbtnDefinicion);
		
		ButtonGroup grupoNivel = new ButtonGroup();
		grupoNivel.add(rdbtnPRINCIPIANTE);
		grupoNivel.add(rdbtnINTERMEDIO);
		grupoNivel.add(rdbtnAvanzado);
		
		ButtonGroup grupoLesiones = new ButtonGroup();
		grupoLesiones.add(rdbtnRodilla);
		grupoLesiones.add(rdbtnHombro);
		grupoLesiones.add(rdbtnLumbar);
		grupoLesiones.add(rdbtnNinguna);
		
		
		JLabel lblInfoT = new JLabel("Contacto:");
		lblInfoT.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblInfoT.setBounds(93, 386, 53, 14);
		contentPane.add(lblInfoT);
		
		JLabel lblNumeroT = new JLabel(cliente.getNumeroT());
		lblNumeroT.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNumeroT.setBounds(316, 384, 113, 18);
		contentPane.add(lblNumeroT);
		
		JLabel lblemail = new JLabel(cliente.getEmail());
		lblemail.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblemail.setBounds(149, 384, 169, 18);
		contentPane.add(lblemail);
		
		JLabel lblEntrenador = new JLabel("ENTRENADOR:");
		lblEntrenador.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEntrenador.setBounds(343, 217, 109, 14);
		contentPane.add(lblEntrenador);
		
		textFieldEnt = new JTextField(cliente.getEntrenador());
		textFieldEnt.setBounds(439, 215, 163, 20);
		contentPane.add(textFieldEnt);
		
		
		textFieldPESO = new JTextField(String.valueOf(cliente.getKilos()));
		textFieldPESO.setBounds(66, 124, 62, 20);
		contentPane.add(textFieldPESO);
		
		JLabel lblcomidas = new JLabel("COMIDAS:");
		lblcomidas.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblcomidas.setBounds(166, 127, 68, 14);
		contentPane.add(lblcomidas);
		
		JLabel lblLugar = new JLabel("LUGAR:");
		lblLugar.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLugar.setBounds(171, 260, 46, 14);
		contentPane.add(lblLugar);
		
		rdbtnGYM = new JRadioButton("GYM");
		rdbtnGYM.setBounds(202, 282, 116, 23);
		contentPane.add(rdbtnGYM);
		
		rdbtnCASACON = new JRadioButton("CASA(con MAT)");
		rdbtnCASACON.setBounds(202, 308, 116, 23);
		contentPane.add(rdbtnCASACON);
		
		rdbtnCASASIN = new JRadioButton("CASA(Sin MAT)");
		rdbtnCASASIN.setBounds(202, 334, 116, 23);
		contentPane.add(rdbtnCASASIN);
		
		ButtonGroup grupoLugar = new ButtonGroup();
		grupoLesiones.add(rdbtnGYM);
		grupoLesiones.add(rdbtnCASACON);
		grupoLesiones.add(rdbtnCASASIN);
		
		//INICIALIZAR LUGAR
		if (cliente.getLugar().equals(Lugar.gym)) {
			rdbtnGYM.setSelected(true);
		} else if (cliente.getLugar().equals(Lugar.casa_material)){
			rdbtnCASACON.setSelected(true);
		} else {
			rdbtnCASASIN.setSelected(true);
		}
		
		JLabel lblTMB = new JLabel("TMB: " + cliente.getTMB()+" KCal");  //INICIALIZAR TMB
		lblTMB.setForeground(Color.RED);
		lblTMB.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTMB.setBounds(542, 308, 132, 32);
		contentPane.add(lblTMB);
		
		JLabel lblInstagram = new JLabel("IG: "+cliente.getInsta());
		lblInstagram.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblInstagram.setBounds(439, 384, 141, 18);
		contentPane.add(lblInstagram);
		
		panel_1_1 = new JPanel();
		panel_1_1.setBackground(new Color(255, 255, 240));
		panel_1_1.setBounds(10, 11, 318, 362);
		contentPane.add(panel_1_1);
		
		JPanel panel_1_1_1 = new JPanel();
		panel_1_1_1.setBackground(new Color(255, 255, 240));
		panel_1_1_1.setBounds(335, 11, 339, 362);
		contentPane.add(panel_1_1_1);
		
		
	}
}
