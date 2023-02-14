package serviciodietas.gui;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
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
		
		JButton button = new JButton("Reservar");
		button.setEnabled(true);
		button.setToolTipText(String.format("Descargar dieta de - %s", cliente.getNombreC()));				
		button.setBackground(table.getBackground());
		
		if (isSelected) {
			button.setBackground(table.getSelectionBackground());
		}
		
		button.addActionListener((e) -> {
			//DESCARGAR ARCHIVO EN...
			JOptionPane.showMessageDialog(null, "Descargando dieta");
		});
		
		button.setOpaque(true);
		
		return button;
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
