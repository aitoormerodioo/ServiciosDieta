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
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

public class SpreadSheets {
	
	private static Sheets sheetsService;
	private static String APPLICATION_NAME = "Google Sheets";
	
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
	
	Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	
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
		
		Credential credencial = authorize();
		
		credencial.refreshToken();
		
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credencial).setApplicationName(APPLICATION_NAME).build();
		
	}
}
