package serviciodieta.persistencia;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import serviciodietas.data.Cliente;
import serviciodietas.data.Lesion;
import serviciodietas.data.Nivel;
import serviciodietas.data.Objetivo;
import serviciodietas.data.Peso;
import serviciodietas.data.Sexo;


public class ServicioSpreadSheets {
	
	private static final String SPREADSHEET_ID = "1Hgng61re-cVHEy_RF8v83v5256myqRRplYjn-RtNA7s";
	
	private static Sheets servicio;
	
	
	public static List<Cliente> cargarClientes() throws GeneralSecurityException, IOException{
		
		List<Cliente> listaClientes = new ArrayList<>();
		
		//CREAMOS EL SERVICIO SHEETS PARA CARGAR DATOS
		servicio = serviciodieta.persistencia.SpreadSheets.getSheetsService();
		
		int numRows = servicio.spreadsheets().values()
                .get(SPREADSHEET_ID, "clientes")
                .execute()
                .getValues()
                .size();
		
		String range = "clientes!A2:X"+numRows; 
		ValueRange response = servicio.spreadsheets().values()
				        .get(SPREADSHEET_ID, range)
				        .execute();
		
		List<List<Object>> values = response.getValues();
		
		for (List<Object> row : values) {
			
				String nombreC = String.format("%s %s", row.get(0), row.get(1));
				
				String numeroT = "+"+row.get(2);
				
				Sexo sexo= Sexo.hombre;

				if (row.get(19).toString().equals("MUJER")) {
					sexo= Sexo.mujer;
				}
				
				Peso peso = Peso.menos70;
				int kgs = Integer.parseInt(row.get(21).toString());
				
				if (kgs>=70 && kgs<=90) {
					peso = Peso.entre70y90;
				} else if (kgs>90) {
					peso = Peso.mas90;
				}
				
				String noGustos = row.get(7).toString();
				
				int diasentreno = Integer.parseInt(row.get(12).toString());
				
				int mesesentrenados = Integer.parseInt(row.get(18).toString());
				
				Nivel nivel = Nivel.principiante;
				if (row.get(11).toString().equals("Intermedio (1-3 años entrenando)")) {
					nivel = Nivel.intermedio;
				} else if (row.get(11).toString().equals("Avanzado (+3 años entrenando)")) {
					nivel = Nivel.avanzado;
				}
				
				Lesion lesion = Lesion.ninguna;
				if (row.get(13).toString().equals("HOMBRO (rotadores, me impide hacer ciertos ejercicios)")) {
					lesion = Lesion.hombro;
				} else if (row.get(13).toString().equals("LUMBAR (me impide hacer ciertos ejercicios)")) {
					lesion = Lesion.lumbar;
				} else if (row.get(13).toString().equals("RODILLA (me impide hacer ciertos ejercicios)")) {
					lesion = Lesion.rodilla;
				}
				
				Objetivo objetivo = Objetivo.definicion;
				if (row.get(6).toString().equals("Ganancia de músculo (sin grasa)")) {
					objetivo = Objetivo.volumen;
				}	
				
				String entrenador;
				if (row.get(17).toString().contains("%")) {
					entrenador = "Sin asignar";
				} else {
					entrenador=row.get(17).toString();
				}
				
				Cliente cliente = new Cliente(nombreC, numeroT, sexo, peso, noGustos, diasentreno, mesesentrenados, nivel, lesion, objetivo, entrenador);
				   
				listaClientes.add(cliente);
		}
		
//		for (Cliente c : listaClientes) {
//			System.out.println(c.toString());
//		}
		return listaClientes;
	}
	
	public static void modificarCliente(String nombreC, String sexo, int peso, String noGustos, int diasentreno, int mesesentrenados, String nivel, String lesion, String objetivo, String entrenador, String numT) throws IOException, GeneralSecurityException {
		
		//CREAMOS EL SERVICIO SHEETS PARA CARGAR DATOS
		servicio = serviciodieta.persistencia.SpreadSheets.getSheetsService();
		
		int numRows = servicio.spreadsheets().values()
                .get(SPREADSHEET_ID, "clientes")
                .execute()
                .getValues()
                .size();

		ValueRange result = servicio.spreadsheets().values()
		    .get(SPREADSHEET_ID, "clientes")
		    .execute();

		List<List<Object>> values = result.getValues();
		
		
		//BUSCAMOS EL INDICE DE LA FILA A MODIFICAR
		int filam = -1;
		
		for (int i = 1; i < numRows; i++) {    // EMPEZAMOS CONTANDO DESDE LA SEGUNDA FILA
			List<Object> row = values.get(i);
		    if (nombreC.contains(row.get(0).toString()) && numT.contains(row.get(2).toString())) {
		        filam = i+1;
		        break;
		    }
		}
		
		List<Object> newValues = Arrays.asList(null, null, null, null, null, null, objetivo, noGustos, null, null, null, nivel, diasentreno, lesion, null, null, null, entrenador, mesesentrenados, sexo, null, peso, null, null);
		
		String range = "clientes!A"+filam+":X"+filam;
		
		ValueRange response = servicio.spreadsheets().values()
			    .get(SPREADSHEET_ID, range)
			    .execute();
		List<List<Object>> currentValues = response.getValues();
		List<Object> rowValues = currentValues.get(0); // asumimos que solo hay una fila de valores
		for (int i = 0; i < newValues.size(); i++) {
		    if (newValues.get(i) != null) {
		        rowValues.set(i, newValues.get(i));
		    }
		}
		
		//ACTUALIZAMOS PAGINA
		ValueRange body = new ValueRange().setValues(currentValues);
		UpdateValuesResponse resultado = servicio.spreadsheets().values()
		    .update(SPREADSHEET_ID, range, body)
		    .setValueInputOption("USER_ENTERED")
		    .execute();
		
		
	}
}
