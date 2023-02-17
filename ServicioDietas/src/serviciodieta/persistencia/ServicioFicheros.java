package serviciodieta.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import serviciodietas.data.*;

public class ServicioFicheros {
	
	public static List<Cliente> cargarClientes(){
		
		List<Cliente> listaClientes = new ArrayList<>();
	   
		try (BufferedReader reader = new BufferedReader(new FileReader("data_files/clientes.csv"))) {
			
			String line = reader.readLine();
			String[] fields;
			Cliente cliente;

			while ((line = reader.readLine()) != null) {
				fields = line.split(";");
				String nombreC = fields[0];
				String vegano = fields[1];
	           
	           cliente = new Cliente(nombreC.toUpperCase(), vegano);
	           listaClientes.add(cliente);
	           
	    	 }
	    	 reader.close();
	         
	      } catch (Exception e) {
	         System.out.println(String.format("Error al leer csv: %s", e.getMessage()));;
	      }
		
		return listaClientes;
	}
}
