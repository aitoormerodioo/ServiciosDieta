package serviciodietas.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import serviciodietas.data.Cliente;


public class BorrarDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton jButtonConfirm = new JButton("Confirmar");
	private JButton jButtonCancel = new JButton("Cancelar");
		
	
	public BorrarDialog(Cliente cliente) {
		JPanel jPanelcliente = new JPanel();
		jPanelcliente.setBorder(new TitledBorder("Datos del Cliente"));
		jPanelcliente.setLayout(new GridLayout(5, 1));

		JLabel jLabelCliente = new JLabel(String.format("- %s", cliente.getNombreC()));
		
		jPanelcliente.add(jLabelCliente);
		
		JPanel jPanelPassengers = new JPanel();
		jPanelPassengers.setBorder(new TitledBorder("Datos personales"));
		jPanelPassengers.setLayout(new GridLayout(3, 1));
		
		//Eventos de los botones
		jButtonCancel.addActionListener((e) -> setVisible(false));
		jButtonConfirm.addActionListener((e) -> {
			eliminarCliente(cliente);
			setVisible(false);
		});
		
		JPanel jPanelButtons = new JPanel();
		jPanelButtons.add(jButtonCancel);
		jPanelButtons.add(jButtonConfirm);
		
		JPanel jPanelCenter = new JPanel();
		jPanelCenter.setLayout(new GridLayout(2, 1));
		jPanelCenter.add(jPanelcliente);
		jPanelCenter.add(jPanelPassengers);
		
		//this.setLayout(new BorderLayout(10, 10));
		add(new JPanel(), BorderLayout.NORTH);
		add(new JPanel(), BorderLayout.EAST);
		add(new JPanel(), BorderLayout.WEST);
		add(jPanelCenter, BorderLayout.CENTER);
		add(jPanelButtons, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle(String.format("Estas seguro que quieres borrar a %s de la Base de Datos?", cliente.getNombreC()));		
		setSize(500, 350);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Se actualiza la lista de nombres de personas a partir del combo de personas.
	 */
	private void eliminarCliente(Cliente cliente) {
		
		File inputFile = new File("data_files/clientes.csv");
		File outputFile = new File("data_files/clientes.csv");
		List<String[]> clientes = new ArrayList<String[]>();
		
		  try {
		      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		      BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		      
		      String[] fields;
		      String nombreBorrar = cliente.getNombreC();
		      String currentLine;

		    while((currentLine = reader.readLine()) != null) {                        
		    	fields = currentLine.split(";");
				String nombreC = fields[0];
		    	if(nombreC==nombreBorrar){ 
		            continue;
		        } else {
		        writer.write(currentLine + System.getProperty(";"));
		        }
		    }       
		    writer.close();
		    reader.close();
		    
		    JOptionPane.showMessageDialog(null, "Cliente borrado correctamente");
		    
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		
		VentanaPrincipal.actualizarTabla();
	}
	
}