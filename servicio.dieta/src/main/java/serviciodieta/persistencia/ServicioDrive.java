package serviciodieta.persistencia;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import serviciodietas.data.Cliente;

public class ServicioDrive {
	
	private static Drive driveService;
	private static String APPLICATION_NAME = "Google Drive";
	
	
	private static Credential authorize() throws IOException, GeneralSecurityException {
		InputStream in = Drive.class.getResourceAsStream("/credentials2.json");
		GoogleClientSecrets clienteSecret = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
	
	List<String> scopes = Arrays.asList(DriveScopes.DRIVE);
	
	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clienteSecret, scopes)
			.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens2")))
			.setAccessType("offline")
			.build();
	
	Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	
	return credential;
	
	}
	
	public static Drive getDriveService() throws GeneralSecurityException, IOException {
		
		Credential credencial = authorize();
		
		return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credencial).setApplicationName(APPLICATION_NAME).build();
		
	}
	
	public static void descargarArchivos(Cliente cliente) throws GeneralSecurityException, IOException {
		
		Drive drive = serviciodieta.persistencia.ServicioDrive.getDriveService();
		
		String folderId = "1CRP-rOzCNro8Bg0wHLHx5fJ9gS6j3SQI";
		
		System.out.println(drive.getApplicationName());
		
		

		// Descargar cada archivo y guardarlo en la carpeta local
		String localFolderPath = "C:\\ArchivosClientes"+cliente.getNombreC();
		java.io.File localFolder = new java.io.File(localFolderPath);
//		for (File file : fileList) {
//		    // Crear un archivo local con el mismo nombre que el archivo de Google Drive
//		    java.io.File localFile = new java.io.File(localFolderPath + java.io.File.separator + );
//		    OutputStream outputStream = new FileOutputStream(localFile);
//
//		    // Descargar el archivo de Google Drive y escribir los datos en el archivo local
//		    drive.files().get(file.getId())
//		            .executeMediaAndDownloadTo(outputStream);
//
//		    outputStream.close();
//		}
	}
}


