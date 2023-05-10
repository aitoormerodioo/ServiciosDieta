package serviciodieta.persistencia;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import serviciodietas.data.Cliente;
import serviciodietas.data.Lesion;
import serviciodietas.data.Lugar;
import serviciodietas.data.Nivel;
import serviciodietas.data.Objetivo;
import serviciodietas.data.Peso;
import serviciodietas.data.Sexo;

public class SpreadSheets {
	
	private static Sheets sheetsService;
	private static String APPLICATION_NAME = "Google Sheets";
	private static Credential credential;
	private static final String SPREADSHEET_ID = "1Hgng61re-cVHEy_RF8v83v5256myqRRplYjn-RtNA7s";
	
	private static Credential authorize() throws IOException, GeneralSecurityException {
	
		InputStream in = SpreadSheets.class.getResourceAsStream("/credentials.json");
		GoogleClientSecrets clienteSecret = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
	
	List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
		
	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clienteSecret, scopes)
			.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
			.setAccessType("offline")
			.build();
	
	credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	
	String accessToken = credential.getAccessToken();
	String refreshToken = credential.getRefreshToken();
	
	GoogleCredential googleCredential = new GoogleCredential.Builder()
	        .setTransport(GoogleNetHttpTransport.newTrustedTransport())
	        .setJsonFactory(JacksonFactory.getDefaultInstance())
	        .setClientSecrets(clienteSecret)
	        .build();
	     
    googleCredential.setAccessToken(accessToken);
    googleCredential.setRefreshToken(refreshToken);
	       

	// Configura la biblioteca de autenticación de Google para Java para renovar automáticamente el token de acceso
	if (googleCredential.getExpiresInSeconds() != null && googleCredential.getExpiresInSeconds() <= 0) {
        // If the access token has expired or will expire within 1 minute, then refresh it
        googleCredential.refreshToken();
    }
	return googleCredential;
	
	
	}
	
	public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
		
		if (sheetsService == null) {
            credential = authorize();
            sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
        }
        return sheetsService;
		
		
		//return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
		
	}
	
public static List<Cliente> cargarClientes() throws GeneralSecurityException, IOException{
		
		try{List<Cliente> listaClientes = new ArrayList<>();
		
		//CREAMOS EL SERVICIO SHEETS PARA CARGAR DATOS
		sheetsService = serviciodieta.persistencia.SpreadSheets.getSheetsService();
		
		int numRows = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, "clientes")
                .execute()
                .getValues()
                .size();
		
		String range = "clientes!A2:Y"+numRows; 
		ValueRange response = sheetsService.spreadsheets().values()
				        .get(SPREADSHEET_ID, range)
				        .execute();
		
		List<List<Object>> values = response.getValues();
		
		for (List<Object> row : values) {
			
				String nombreC = String.format("%s %s", row.get(0), row.get(1));
				
				String numeroT = "+"+row.get(2);
				
				String email = row.get(3).toString();
				
				Sexo sexo= Sexo.hombre;

				if (row.get(19).toString().equals("MUJER")) {
					sexo= Sexo.mujer;
				}
				
				Peso peso = Peso.menos70;
				int kilos = Integer.parseInt(row.get(21).toString());
				
				if (kilos>=70 && kilos<=90) {
					peso = Peso.entre70y90;
				} else if (kilos>90) {
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
				
				Lugar lugar;
				if (row.get(23).toString().contains("GYM")) {
					lugar=Lugar.gym;
				} else if (row.get(23).toString().contains("CASA (Tengo material)")){
					lugar=Lugar.casa_material;
				} else {
					lugar=Lugar.casa_sin_material;
				}
				
				int numerocomidas= Integer.parseInt(row.get(24).toString());
				
				Cliente cliente = new Cliente(nombreC, numeroT, email, sexo, peso, kilos, noGustos, diasentreno, mesesentrenados, nivel, lesion, objetivo, entrenador, lugar, numerocomidas);
				   
				listaClientes.add(cliente);
		}
		
//		for (Cliente c : listaClientes) {
//			System.out.println(c.toString());
//		}
		return listaClientes;
		
		} finally {
			sheetsService=null;
			if (credential != null) {
				credential=null;
			}
		}
	}
	
	public static void modificarCliente(String nombreC, String sexo, int peso, String noGustos, int diasentreno, int mesesentrenados, String nivel, String lesion, String objetivo, String entrenador, String numT, String lugar, int numerocomidas) throws IOException, GeneralSecurityException {
		
		//CREAMOS EL SERVICIO SHEETS PARA CARGAR DATOS
		try{sheetsService = serviciodieta.persistencia.SpreadSheets.getSheetsService();
		
		int numRows = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, "clientes")
                .execute()
                .getValues()
                .size();

		ValueRange result = sheetsService.spreadsheets().values()
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
		
		List<Object> newValues = Arrays.asList(null, null, null, null, null, null, objetivo, noGustos, null, null, null, nivel, diasentreno, lesion, null, null, null, entrenador, mesesentrenados, sexo, null, peso, null, lugar, numerocomidas);
		
		String range = "clientes!A"+filam+":Y"+filam;
		
		ValueRange response = sheetsService.spreadsheets().values()
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
		UpdateValuesResponse resultado = sheetsService.spreadsheets().values()
		    .update(SPREADSHEET_ID, range, body)
		    .setValueInputOption("USER_ENTERED")
		    .execute();
		} finally {
		sheetsService=null;
		if (credential != null) {
			credential=null;
		}
	}
	
	}
}
