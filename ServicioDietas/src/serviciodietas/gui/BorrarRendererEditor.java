package serviciodietas.gui;

import java.awt.Component;
import java.awt.Image;
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

public class BorrarRendererEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	private Cliente cliente;
	private VentanaPrincipal mainWindow;
	
	public BorrarRendererEditor(VentanaPrincipal mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	private JButton prepare(JTable table, Object value, boolean isSelected, int row, int column) {
		cliente = (Cliente) value;
		
		JButton BotonBorrar = new JButton("");

		BotonBorrar.setEnabled(true);
		BotonBorrar.setToolTipText(String.format("Borrar cliente: - %s", cliente.getNombreC()));				
		BotonBorrar.setBackground(table.getBackground());
		
		ImageIcon imagenDescargar =  new ImageIcon("resources/iconoBorrar.png");
		Icon i= new ImageIcon(imagenDescargar.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));
		
		BotonBorrar.setIcon(i);
		
		if (isSelected) {
			BotonBorrar.setBackground(table.getSelectionBackground());
		}
		
		BotonBorrar.addActionListener((e) -> {
			//DESCARGAR ARCHIVO EN...
			JOptionPane.showMessageDialog(null, "Borrando Cliente");
		});
		
		BotonBorrar.setOpaque(true);
		
		return BotonBorrar;
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
