package serviciodieta.persistencia;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import serviciodietas.data.Cliente;
import serviciodietas.data.Lesion;
import serviciodietas.data.Nivel;
import serviciodietas.data.Objetivo;
import serviciodietas.data.Peso;
import serviciodietas.data.Sexo;


public class ServicioSpreadSheets {
	
	private static final String SPREADSHEET_ID = "1Hgng61re-cVHEy_RF8v83v5256myqRRplYjn-RtNA7s";
	
	public static List<Cliente> cargarClientes() throws GeneralSecurityException, IOException{
		
		List<Cliente> listaClientes = new ArrayList<>();
		
		//CREAMOS EL SERVICIO SHEETS PARA CARGAR DATOS
		Sheets servicio = serviciodieta.persistencia.SpreadSheets.getSheetsService();
		
		int numRows = servicio.spreadsheets().values()
                .get(SPREADSHEET_ID, "clientes")
                .execute()
                .getValues()
                .size();
		
		String range = "clientes!A2:V"+numRows; 
		ValueRange response = servicio.spreadsheets().values()
				        .get(SPREADSHEET_ID, range)
				        .execute();
		
		List<List<Object>> values = response.getValues();
		
		for (List<Object> row : values) {
				String nombreC = String.format("%s %s", row.get(0), row.get(1));
				
				Sexo sexo= Sexo.hombre;

				if (row.get(21).toString().equals("MUJER")) {
					sexo= Sexo.mujer;
				}
				
				Peso peso = Peso.menos70;
				int kgs = Integer.parseInt(row.get(6).toString());
				
				if (kgs>=70 && kgs<=90) {
					peso = Peso.entre70y90;
				} else if (kgs>90) {
					peso = Peso.mas90;
				}
				
				String noGustos = row.get(9).toString();
				
				int diasentreno = Integer.parseInt(row.get(14).toString());
				
				int mesesentrenados = Integer.parseInt(row.get(20).toString());
				
				Nivel nivel = Nivel.principiante;
				if (row.get(13).toString().equals("Intermedio (1-3 años entrenando)")) {
					nivel = Nivel.intermedio;
				} else if (row.get(13).toString().equals("Avanzado (+3 años entrenando)")) {
					nivel = Nivel.avanzado;
				}
				
				Lesion lesion = Lesion.ninguna;
				if (row.get(15).toString().equals("HOMBRO (rotadores, me impide hacer ciertos ejercicios)")) {
					lesion = Lesion.hombro;
				} else if (row.get(15).toString().equals("LUMBAR (me impide hacer ciertos ejercicios)")) {
					lesion = Lesion.lumbar;
				} else if (row.get(15).toString().equals("RODILLA (me impide hacer ciertos ejercicios)")) {
					lesion = Lesion.rodilla;
				}
				
				Objetivo objetivo = Objetivo.definicion;
				if (row.get(8).toString().equals("Ganancia de músculo (sin grasa)")) {
					objetivo = Objetivo.volumen;
				}	
					
				Cliente cliente = new Cliente(nombreC, sexo, peso, noGustos, diasentreno, mesesentrenados, nivel, lesion, objetivo);
				   
				listaClientes.add(cliente);
		}
		
//		for (Cliente c : listaClientes) {
//			System.out.println(c.toString());
//		}
		return listaClientes;
	}
}