package serviciodietas.gui;

import java.util.Arrays;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import serviciodietas.data.Cliente;


public class ModeloTablaClientes extends DefaultTableModel {
	
private static final long serialVersionUID = 1L;
	
	private static final String RUTA_ICON_DESCARGA = "resources/iconoDescargar.png";
	private static final String RUTA_ICON_EDITAR = "resources/iconoEditar.png";

	private List<Cliente> clientes;
	private final List<String> headers = Arrays.asList(
			"NOMBRE", 
			"EDITAR", 
			"DESCARGAR");
	
	public ModeloTablaClientes(List<Cliente> clientes) {
		super();
		this.clientes = clientes;
	}
	
	@Override
	public String getColumnName(int column) {
		return headers.get(column);
	}

	@Override
	public int getRowCount() {
		if (clientes != null) {
			return clientes.size();
		} else { 
			return 0;
		}
	}

	@Override
	public int getColumnCount() {
		return headers.size(); 
	}
	
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 1 && columnIndex == 2);
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int column) {    	
    }
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Cliente cliente = clientes.get(rowIndex);
		
		switch (columnIndex) {
			case 0: return cliente.getNombreC();
			default: return null;
		}
	}
	
	
}
