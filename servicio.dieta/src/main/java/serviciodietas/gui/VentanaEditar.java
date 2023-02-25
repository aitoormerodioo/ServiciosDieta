package serviciodietas.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import serviciodietas.data.Cliente;
import serviciodietas.data.Lesion;
import serviciodietas.data.Nivel;
import serviciodietas.data.Objetivo;
import serviciodietas.data.Peso;
import serviciodietas.data.Sexo;

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

public class VentanaEditar extends JFrame {

	private JPanel contentPane;
	private JRadioButton rdbtnM;
	private JRadioButton rdbtnH;
	private JRadioButton rdbtnmenos70;
	private JRadioButton rdbtnMas;
	private JComboBox comboBoxDIAS;
	private JRadioButton rdbtnEntre;
	private JRadioButton rdbtnDefinicion;
	private JRadioButton rdbtnSuperavit;
	private JRadioButton rdbtnRodilla;
	private JRadioButton rdbtnLumbar;
	private JRadioButton rdbtnHombro;
	private JRadioButton rdbtnNinguna;
	private JRadioButton rdbtnAvanzado;
	private JRadioButton rdbtnINTERMEDIO;
	private JRadioButton rdbtnPRINCIPIANTE;
	private JComboBox comboBoxMESES;
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

	public VentanaEditar(Cliente cliente) {
		
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
		labelNombre.setForeground(SystemColor.activeCaption);
		labelNombre.setBounds(21, 11, 309, 42);
		
		
		contentPane.add(labelNombre);
		labelNombre.setText(cliente.getNombreC());
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JOptionPane.showConfirmDialog(null, "Los datos cambiados no se guardaran!", "Estas seguro?", JOptionPane.OK_CANCEL_OPTION);
				VentanaPrincipal vp = null;
				try {
					vp = new VentanaPrincipal();
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
		btnCancelar.setBounds(27, 377, 102, 23);
		contentPane.add(btnCancelar);
		
		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//RECOGEMOS LOS DATOS MODIFICADOS
				String nombreC = cliente.getNombreC();
				
				String sexo;
				
				if (rdbtnM.isSelected()) {
					sexo = "MUJER";
				} else {
					sexo = "HOMBRE";
				}
				
				int peso;
				
				if (rdbtnmenos70.isSelected()) {
					peso= 65;
				} else if (rdbtnEntre.isSelected()) {
					peso = 80;
				} else {
					peso = 95;
				}
				
				int diasentreno= Integer.parseInt(comboBoxDIAS.getSelectedItem().toString());
				
				int mesesentrenados = Integer.parseInt(comboBoxMESES.getSelectedItem().toString());
				
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
					nogustos=nogustos+"Soy Vegan@, ";
				} 
				
				if (chckbxVegetarian.isSelected()) {
					nogustos=nogustos+"Soy Vegetarian@, ";
				}
				
				try {
					serviciodieta.persistencia.ServicioSpreadSheets.modificarCliente(nombreC,sexo,peso,nogustos,diasentreno,mesesentrenados,nivel,lesiones,objetivo);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (GeneralSecurityException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				try {
					VentanaPrincipal vp = new VentanaPrincipal();
					vp.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnConfirmar.setBounds(542, 377, 102, 23);
		contentPane.add(btnConfirmar);
		
		JLabel lblSexo = new JLabel("SEXO:");
		lblSexo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSexo.setBounds(21, 69, 39, 14);
		contentPane.add(lblSexo);
		
		rdbtnM = new JRadioButton("MUJER");
		rdbtnM.setBounds(82, 65, 68, 23);
		contentPane.add(rdbtnM);
		
		rdbtnH = new JRadioButton("HOMBRE");
		rdbtnH.setBounds(166, 65, 85, 23);
		contentPane.add(rdbtnH);
		
		JLabel lblPeso = new JLabel("PESO:");
		lblPeso.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPeso.setBounds(21, 127, 46, 14);
		contentPane.add(lblPeso);
		
		rdbtnmenos70 = new JRadioButton("-70KG");
		rdbtnmenos70.setBounds(67, 123, 62, 23);
		contentPane.add(rdbtnmenos70);
		
		rdbtnEntre = new JRadioButton("70-90KG");
		rdbtnEntre.setBounds(144, 123, 76, 23);
		contentPane.add(rdbtnEntre);
		
		rdbtnMas = new JRadioButton("+90KG");
		rdbtnMas.setBounds(230, 123, 68, 23);
		contentPane.add(rdbtnMas);
		
		JLabel lblnogustos = new JLabel("INTOLERANCIAS/NO GUSTOS:");
		lblnogustos.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblnogustos.setBounds(370, 28, 190, 14);
		contentPane.add(lblnogustos);
		
		chckbxNINGUNA = new JCheckBox("NINGUNA");
		chckbxNINGUNA.setBounds(343, 55, 82, 23);
		contentPane.add(chckbxNINGUNA);
		
		chckbxCLARAS = new JCheckBox("CLARAS");
		chckbxCLARAS.setBounds(437, 55, 76, 23);
		contentPane.add(chckbxCLARAS);
		
		chckbxAGUACATE = new JCheckBox("AGUACATE");
		chckbxAGUACATE.setBounds(527, 55, 89, 23);
		contentPane.add(chckbxAGUACATE);
		
		chckbxFRUTOSSECOS = new JCheckBox("FRUTOS SECOS");
		chckbxFRUTOSSECOS.setBounds(343, 88, 117, 23);
		contentPane.add(chckbxFRUTOSSECOS);
		
		chckbxGLUTEN = new JCheckBox("GLUTEN");
		chckbxGLUTEN.setBounds(468, 88, 82, 23);
		contentPane.add(chckbxGLUTEN);
		
		chckbxHUEVOS = new JCheckBox("HUEVOS");
		chckbxHUEVOS.setBounds(554, 88, 76, 23);
		contentPane.add(chckbxHUEVOS);
		
		chckbxLactosa = new JCheckBox("LACTOSA");
		chckbxLactosa.setBounds(343, 123, 82, 23);
		contentPane.add(chckbxLactosa);
		
		chckbxVerdura = new JCheckBox("VERDURA");
		chckbxVerdura.setBounds(431, 123, 82, 23);
		contentPane.add(chckbxVerdura);
		
		chckbxPescado = new JCheckBox("PESCADO");
		chckbxPescado.setBounds(522, 123, 82, 23);
		contentPane.add(chckbxPescado);
		
		chckbxVegetarian = new JCheckBox("VEGETARIAN@");
		chckbxVegetarian.setBounds(343, 157, 117, 23);
		contentPane.add(chckbxVegetarian);
		
		chckbxVegan = new JCheckBox("VEGAN@");
		chckbxVegan.setBounds(468, 157, 117, 23);
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
		rdbtnPRINCIPIANTE.setBounds(67, 271, 109, 23);
		contentPane.add(rdbtnPRINCIPIANTE);
		
		rdbtnINTERMEDIO = new JRadioButton("INTERMEDIO");
		rdbtnINTERMEDIO.setBounds(67, 297, 109, 23);
		contentPane.add(rdbtnINTERMEDIO);
		
		rdbtnAvanzado = new JRadioButton("AVANZADO");
		rdbtnAvanzado.setBounds(67, 323, 109, 23);
		contentPane.add(rdbtnAvanzado);
		
		JLabel lblLesiones = new JLabel("LESIONES:");
		lblLesiones.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLesiones.setBounds(343, 260, 68, 14);
		contentPane.add(lblLesiones);
		
		JLabel lblMesesEntrenados = new JLabel("MESES ENTRENADOS:");
		lblMesesEntrenados.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMesesEntrenados.setBounds(343, 211, 134, 14);
		contentPane.add(lblMesesEntrenados);
		
		comboBoxMESES = new JComboBox();
		comboBoxMESES.setMaximumRowCount(13);
		comboBoxMESES.setBounds(487, 207, 73, 22);
		contentPane.add(comboBoxMESES);
		
		rdbtnNinguna = new JRadioButton("NINGUNA");
		rdbtnNinguna.setBounds(417, 256, 109, 23);
		contentPane.add(rdbtnNinguna);
		
		rdbtnHombro = new JRadioButton("HOMBRO");
		rdbtnHombro.setBounds(417, 282, 109, 23);
		contentPane.add(rdbtnHombro);
		
		rdbtnLumbar = new JRadioButton("LUMBAR");
		rdbtnLumbar.setBounds(417, 308, 109, 23);
		contentPane.add(rdbtnLumbar);
		
		rdbtnRodilla = new JRadioButton("RODILLA");
		rdbtnRodilla.setBounds(417, 334, 109, 23);
		contentPane.add(rdbtnRodilla);
		
		JLabel lblObjetivo = new JLabel("OBJETIVO:");
		lblObjetivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblObjetivo.setBounds(21, 221, 68, 14);
		contentPane.add(lblObjetivo);
		
		rdbtnDefinicion = new JRadioButton("DEFINICION");
		rdbtnDefinicion.setBounds(103, 217, 89, 23);
		contentPane.add(rdbtnDefinicion);
		
		rdbtnSuperavit = new JRadioButton("SUPERAVIT");
		rdbtnSuperavit.setBounds(209, 217, 96, 23);
		contentPane.add(rdbtnSuperavit);
		
		
		//POR DEFECTO LOS DATOS DE LOS CLIENTES ESTARAN SELECCIONADOS
		if (cliente.getSexo().equals(Sexo.hombre)) {
			rdbtnH.setSelected(true);
		} else {
			rdbtnM.setSelected(true);
		}
		
		if (cliente.getPeso().equals(Peso.menos70)) {
			rdbtnmenos70.setSelected(true);
		} else if (cliente.getPeso().equals(Peso.mas90)) {
			rdbtnMas.setSelected(true);
		} else {
			rdbtnEntre.setSelected(true);
		}
		
		comboBoxDIAS.addItem("2");
		comboBoxDIAS.addItem("3");
		comboBoxDIAS.addItem("4");
		comboBoxDIAS.addItem("5");
		comboBoxDIAS.addItem("6");
		
		if (cliente.getDiasentreno() == 2) {
			comboBoxDIAS.setSelectedIndex(0);
		} else if (cliente.getDiasentreno() == 3) {
			comboBoxDIAS.setSelectedIndex(1);
		} else if (cliente.getDiasentreno() == 4){
			comboBoxDIAS.setSelectedIndex(2);
		} else if (cliente.getDiasentreno() == 5){
			comboBoxDIAS.setSelectedIndex(3);
		} else {
			comboBoxDIAS.setSelectedIndex(4);
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
		
		if (cliente.getNogustos().contains("Soy Vegan@")) {
			chckbxVegan.setSelected(true);
		} 
		
		if (cliente.getNogustos().contains("Soy Vegetarian@")) {
			chckbxVegetarian.setSelected(true);
		}
		
		comboBoxMESES.addItem("0");
		comboBoxMESES.addItem("1");
		comboBoxMESES.addItem("2");
		comboBoxMESES.addItem("3");
		comboBoxMESES.addItem("4");
		comboBoxMESES.addItem("5");
		comboBoxMESES.addItem("6");
		comboBoxMESES.addItem("7");
		comboBoxMESES.addItem("8");
		comboBoxMESES.addItem("9");
		comboBoxMESES.addItem("10");
		comboBoxMESES.addItem("11");
		comboBoxMESES.addItem("12");
		
		if (cliente.getMesesentrenados() == 0) {
			comboBoxMESES.setSelectedIndex(0);
		} else if (cliente.getMesesentrenados() == 1) {
			comboBoxMESES.setSelectedIndex(1);
		} else if (cliente.getMesesentrenados() == 2) {
			comboBoxMESES.setSelectedIndex(2);
		} else if (cliente.getMesesentrenados() == 3) {
			comboBoxMESES.setSelectedIndex(3);
		} else if (cliente.getMesesentrenados() == 4) {
			comboBoxMESES.setSelectedIndex(4);
		} else if (cliente.getMesesentrenados() == 5) {
			comboBoxMESES.setSelectedIndex(5);
		} else if (cliente.getMesesentrenados() == 6) {
			comboBoxMESES.setSelectedIndex(6);
		} else if (cliente.getMesesentrenados() == 7) {
			comboBoxMESES.setSelectedIndex(7);
		} else if (cliente.getMesesentrenados() == 8) {
			comboBoxMESES.setSelectedIndex(8);
		} else if (cliente.getMesesentrenados() == 9) {
			comboBoxMESES.setSelectedIndex(9);
		} else if (cliente.getMesesentrenados() == 10) {
			comboBoxMESES.setSelectedIndex(10);
		} else if (cliente.getMesesentrenados() == 11) {
			comboBoxMESES.setSelectedIndex(11);
		} else {
			comboBoxMESES.setSelectedIndex(12);
		}
		
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
		grupoPeso.add(rdbtnMas);
		grupoPeso.add(rdbtnEntre);
		grupoPeso.add(rdbtnmenos70);
		
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
		
	}
}
