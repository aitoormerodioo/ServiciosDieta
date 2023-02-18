package serviciodieta.persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

public class ServicioDrive {
	
	private static Drive driveService;
	private static String APPLICATION_NAME = "Google Drive";
	
	
	private static Credential authorize() throws IOException, GeneralSecurityException {
		InputStream in = SpreadSheets.class.getResourceAsStream("/credentials.json");
		GoogleClientSecrets clienteSecret = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
	
	List<String> scopes = Arrays.asList(DriveScopes.DRIVE);
	
	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clienteSecret, scopes)
			.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
			.setAccessType("offline")
			.build();
	
	Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	
	return credential;
	
	}
	
	public static Drive getSheetsService() throws GeneralSecurityException, IOException {
		
		Credential credencial = authorize();
		
		return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credencial).setApplicationName(APPLICATION_NAME).build();
		
	}
}


