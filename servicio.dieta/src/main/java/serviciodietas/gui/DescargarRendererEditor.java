package serviciodietas.gui;

import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import serviciodietas.data.Cliente;


public class DescargarRendererEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	private Cliente cliente;
	private VentanaPrincipal mainWindow;
	
	public DescargarRendererEditor(VentanaPrincipal mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	private JButton prepare(JTable table, Object value, boolean isSelected, int row, int column) {
		cliente = (Cliente) value;
		
		JButton BotonDescargar = new JButton("");

		BotonDescargar.setEnabled(true);
		BotonDescargar.setToolTipText(String.format("Descargar dieta de - %s", cliente.getNombreC()));				
		BotonDescargar.setBackground(table.getBackground());
		
		ImageIcon imagenDescargar =  new ImageIcon(getClass().getResource("/iconoDescargar.png"));
		Icon i= new ImageIcon(imagenDescargar.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));
		
		BotonDescargar.setIcon(i);
		
		if (isSelected) {
			BotonDescargar.setBackground(table.getSelectionBackground());
		}
		
		BotonDescargar.addActionListener((e) -> {
			//DESCARGAR ARCHIVO EN...
			JOptionPane.showMessageDialog(null, "Descargando archivos");
			try {
				serviciodieta.persistencia.ServicioDrive.descargarArchivos(cliente);
			} catch (GeneralSecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		BotonDescargar.setOpaque(true);
		
		return BotonDescargar;
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return prepare(table, value, isSelected, row, column);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return prepare(table, value, isSelected, row, column);		
	}
	
	@Override
	public Object getCellEditorValue() {
		return cliente;
	}
	
    @Override
    public boolean shouldSelectCell(EventObject event) {
        return true;
    }
	
}
