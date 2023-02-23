package serviciodietas.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import serviciodietas.data.Cliente;
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
				
				//modificarCliente()
			}
		});
		btnConfirmar.setBounds(542, 377, 102, 23);
		contentPane.add(btnConfirmar);
		
		JLabel lblSexo = new JLabel("SEXO:");
		lblSexo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSexo.setBounds(21, 69, 39, 14);
		contentPane.add(lblSexo);
		
		JRadioButton rdbtnM = new JRadioButton("MUJER");
		rdbtnM.setBounds(82, 65, 68, 23);
		contentPane.add(rdbtnM);
		
		JRadioButton rdbtnH = new JRadioButton("HOMBRE");
		rdbtnH.setBounds(166, 65, 85, 23);
		contentPane.add(rdbtnH);
		
		JLabel lblPeso = new JLabel("PESO:");
		lblPeso.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPeso.setBounds(21, 127, 46, 14);
		contentPane.add(lblPeso);
		
		JRadioButton rdbtnmenos70 = new JRadioButton("-70KG");
		rdbtnmenos70.setBounds(67, 123, 62, 23);
		contentPane.add(rdbtnmenos70);
		
		JRadioButton rdbtnEntre = new JRadioButton("70-90KG");
		rdbtnEntre.setBounds(144, 123, 76, 23);
		contentPane.add(rdbtnEntre);
		
		JRadioButton rdbtnMas = new JRadioButton("+90KG");
		rdbtnMas.setBounds(230, 123, 68, 23);
		contentPane.add(rdbtnMas);
		
		JLabel lblnogustos = new JLabel("INTOLERANCIAS/NO GUSTOS:");
		lblnogustos.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblnogustos.setBounds(370, 28, 190, 14);
		contentPane.add(lblnogustos);
		
		JCheckBox chckbxNINGUNA = new JCheckBox("NINGUNA");
		chckbxNINGUNA.setBounds(343, 55, 82, 23);
		contentPane.add(chckbxNINGUNA);
		
		JCheckBox chckbxCLARAS = new JCheckBox("CLARAS");
		chckbxCLARAS.setBounds(437, 55, 76, 23);
		contentPane.add(chckbxCLARAS);
		
		JCheckBox chckbxAGUACATE = new JCheckBox("AGUACATE");
		chckbxAGUACATE.setBounds(527, 55, 89, 23);
		contentPane.add(chckbxAGUACATE);
		
		JCheckBox chckbxFRUTOSSECOS = new JCheckBox("FRUTOS SECOS");
		chckbxFRUTOSSECOS.setBounds(343, 88, 117, 23);
		contentPane.add(chckbxFRUTOSSECOS);
		
		JCheckBox chckbxGLUTEN = new JCheckBox("GLUTEN");
		chckbxGLUTEN.setBounds(468, 88, 82, 23);
		contentPane.add(chckbxGLUTEN);
		
		JCheckBox chckbxHUEVOS = new JCheckBox("HUEVOS");
		chckbxHUEVOS.setBounds(554, 88, 76, 23);
		contentPane.add(chckbxHUEVOS);
		
		JCheckBox chckbxLactosa = new JCheckBox("LACTOSA");
		chckbxLactosa.setBounds(343, 123, 82, 23);
		contentPane.add(chckbxLactosa);
		
		JCheckBox chckbxVerdura = new JCheckBox("VERDURA");
		chckbxVerdura.setBounds(431, 123, 82, 23);
		contentPane.add(chckbxVerdura);
		
		JCheckBox chckbxPescado = new JCheckBox("PESCADO");
		chckbxPescado.setBounds(522, 123, 82, 23);
		contentPane.add(chckbxPescado);
		
		JCheckBox chckbxVegetarian = new JCheckBox("VEGETARIAN@");
		chckbxVegetarian.setBounds(343, 157, 117, 23);
		contentPane.add(chckbxVegetarian);
		
		JCheckBox chckbxVegan = new JCheckBox("VEGAN@");
		chckbxVegan.setBounds(468, 157, 117, 23);
		contentPane.add(chckbxVegan);
		
		JLabel lblDiasEntreno = new JLabel("DIAS DE ENTRENO:");
		lblDiasEntreno.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiasEntreno.setBounds(21, 178, 117, 14);
		contentPane.add(lblDiasEntreno);
		
		JComboBox comboBoxDIAS = new JComboBox();
		comboBoxDIAS.setMaximumRowCount(5);
		comboBoxDIAS.setBounds(144, 174, 73, 22);
		contentPane.add(comboBoxDIAS);
		
		JLabel lblNivel = new JLabel("NIVEL:");
		lblNivel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNivel.setBounds(21, 260, 46, 14);
		contentPane.add(lblNivel);
		
		JRadioButton rdbtnPRINCIPIANTE = new JRadioButton("PRINCIPIANTE");
		rdbtnPRINCIPIANTE.setBounds(67, 271, 109, 23);
		contentPane.add(rdbtnPRINCIPIANTE);
		
		JRadioButton rdbtnINTERMEDIO = new JRadioButton("INTERMEDIO");
		rdbtnINTERMEDIO.setBounds(67, 297, 109, 23);
		contentPane.add(rdbtnINTERMEDIO);
		
		JRadioButton rdbtnAvanzado = new JRadioButton("AVANZADO");
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
		
		JComboBox comboBoxMESES = new JComboBox();
		comboBoxMESES.setMaximumRowCount(13);
		comboBoxMESES.setBounds(487, 207, 73, 22);
		contentPane.add(comboBoxMESES);
		
		JRadioButton rdbtnNinguna = new JRadioButton("NINGUNA");
		rdbtnNinguna.setBounds(417, 256, 109, 23);
		contentPane.add(rdbtnNinguna);
		
		JRadioButton rdbtnHombro = new JRadioButton("HOMBRO");
		rdbtnHombro.setBounds(417, 282, 109, 23);
		contentPane.add(rdbtnHombro);
		
		JRadioButton rdbtnLumbar = new JRadioButton("LUMBAR");
		rdbtnLumbar.setBounds(417, 308, 109, 23);
		contentPane.add(rdbtnLumbar);
		
		JRadioButton rdbtnRodilla = new JRadioButton("RODILLA");
		rdbtnRodilla.setBounds(417, 334, 109, 23);
		contentPane.add(rdbtnRodilla);
		
		JLabel lblObjetivo = new JLabel("OBJETIVO:");
		lblObjetivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblObjetivo.setBounds(21, 221, 68, 14);
		contentPane.add(lblObjetivo);
		
		JRadioButton rdbtnDefinicion = new JRadioButton("DEFINICION");
		rdbtnDefinicion.setBounds(103, 217, 89, 23);
		contentPane.add(rdbtnDefinicion);
		
		JRadioButton rdbtnSuperavit = new JRadioButton("SUPERAVIT");
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
		
	}
}
