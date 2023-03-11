package serviciodietas.gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class RendererTablaClientes implements TableCellRenderer{
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = new JLabel();
		label.setBackground(table.getBackground());
		
		if (column == 0 ) {
			label.setText(value.toString());
			label.setHorizontalAlignment(JLabel.LEFT);
		}
		
		if (column == 1 ) {
			label.setText(value.toString());
			label.setHorizontalAlignment(JLabel.CENTER);
			if (value.toString()=="Sin asignar") {
				Font font = new Font("Arial", Font.ITALIC, 11);
	            label.setFont(font);
			} else {
				Font font = new Font("Arial", Font.PLAIN, 12);
	            label.setFont(font);
			}
			
		}

		if (isSelected) {
			label.setBackground(table.getSelectionBackground());
			label.setForeground(table.getSelectionForeground());
		}
		
		label.setOpaque(true);
		return label;
	}
	
}
