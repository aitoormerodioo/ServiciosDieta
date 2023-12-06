package serviciodieta.persistencia;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.google.api.client.auth.oauth2.Credential;
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
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;


import serviciodietas.data.Cliente;
import serviciodietas.data.Lesion;
import serviciodietas.data.Lugar;
import serviciodietas.data.Nivel;
import serviciodietas.data.Objetivo;
import serviciodietas.data.Peso;
import serviciodietas.data.Sexo;


public class ServicioDrive {
	
	private static Drive driveService;
	private static String APPLICATION_NAME = "Google Drive";
	private static Credential credential;
	
	
	private static Credential authorize() throws IOException, GeneralSecurityException {
		InputStream in = Drive.class.getResourceAsStream("/credentials3.json");
		GoogleClientSecrets clienteSecret = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
	
	List<String> scopes = Arrays.asList(DriveScopes.DRIVE);
	
	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clienteSecret, scopes)
			.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens2")))
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
	     
	
	if (googleCredential.getExpiresInSeconds() != null && googleCredential.getExpiresInSeconds() <= 0) {
        // If the access token has expired or will expire within 1 minute, then refresh it
        googleCredential.refreshToken();
    }
	return googleCredential;
	
	
	}
	
	public static Drive getDriveService() throws GeneralSecurityException, IOException {
		
		if (driveService == null) {
            credential = authorize();
            driveService = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
        }
        return driveService;
		
	}
	
	public static void descargarArchivos(Cliente cliente) throws GeneralSecurityException, IOException {
		
		try{Drive drive = serviciodieta.persistencia.ServicioDrive.getDriveService();
		
		String clienteN= cliente.getNombreC().replaceAll("\\s+", "");
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccione la carpeta de descarga");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = fileChooser.showOpenDialog(null);
		
		java.io.File selectedFolder = null;
		if (option == JFileChooser.APPROVE_OPTION) {
		    selectedFolder = fileChooser.getSelectedFile();
		}
		
		//PRIMERO DESCARGAMOS LAS DIETAS NUEVAS
		String folderIdDie = "";
		
		if (cliente.getSexo().equals(Sexo.hombre)) {  /////////////////////////////// HOMBRE  (De momento basico)
			if (cliente.getNumerocomidas()==2) {  					// 2 COMIDAS
				if (cliente.getNogustos().contains("NINGUNA")) {
					folderIdDie="1wteDjh1O0tJPgdc9TNyI_VgmWTuQOnUr"; //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGANA GLUTEN
						folderIdDie="1V_wph_lpjT2wcidzPiGv37YtrcVIBSGz";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGANA LACTOSA
						folderIdDie="1NU_ke-zY0TITfjq4Tmp7IDu10wXlzwsm";
					} else {
						folderIdDie="1q7QRAu9mJUk1Oqfi-pWEj2Flx9_CFWR3"; //VEGANA ****
					}
					
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGETARIANA GLUTEN
						folderIdDie="1ADHv5egM1xBjXpPxtYIMpnQ7QrJ9BHSS";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGETARIANA LACTOSA
						folderIdDie="1kd7X4-cYaUeUeV-NBPvFT4lYE-IFVhl7";
					} else {
						folderIdDie="1QZYRM4zMViREH_im_MfgA1re7y40e0gl";//VEGETARIANO
					}
					
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {           //LACTOSA GLUTEN
						folderIdDie="1FYxbjQPwvT89NxhFTbhqbw5BIUq5NeCL";
					} else if(cliente.getNogustos().contains("Huevos")){		//LACTOSA HUEVO
						folderIdDie="1EoMl4CUnPKVswjD0kXxW3L_S-JQnmpaa";
					} else if(cliente.getNogustos().contains("Claras")){		//LACTOSA CLARAS
						folderIdDie="1ZGbYVwxcqy0FucBvL-nhhhzOIzYpxUbS";
					} else if(cliente.getNogustos().contains("Aguacate")){		//LACTOSA AGUACATE
						folderIdDie="1pSrIeV5o23WcCQfTxhxv6FaxPrdp4N7n";
					} else if(cliente.getNogustos().contains("Pescado")){		//LACTOSA PESCADO
						folderIdDie="1Pr4Fqm7G_GdX5Wp8rtaNMM3pHD3V884x";
					} else if(cliente.getNogustos().contains("Verdura")){		//LACTOSA VERDURA
						folderIdDie="1WITcHnGprrvXDBAkkCNsBllFNrnZ0XmF";
					} else {
						folderIdDie="17ADT9IIgSveDor27Kkjw_k7s2CZmcOxG"; //LACTOSA
					}
					
				} else if(cliente.getNogustos().contains("Gluten")){

					if (cliente.getNogustos().contains("Huevos")){		        //GLUTEN HUEVO
						folderIdDie="1-N9fqPYcnCccaaCezkXwt_pwveKe2Aao";
					} else if(cliente.getNogustos().contains("Claras")){		//GLUTEN CLARAS
						folderIdDie="1XZBlOLVehj-g5WglIMYG4oMWjTHnifcv";
					} else if(cliente.getNogustos().contains("Aguacate")){		//GLUTEN AGUACATE
						folderIdDie="1rye3dENnZoAnTBQQ5iXHzUdvPzH9onBP";
					} else if(cliente.getNogustos().contains("Pescado")){		//GLUTEN PESCADO
						folderIdDie="1XZBlOLVehj-g5WglIMYG4oMWjTHnifcv";
					} else if(cliente.getNogustos().contains("Verdura")){		//GLUTEN VERDURA
						folderIdDie="1rHx9xldoGT6NURtw4R5j0flQurmF7rnC";
					} else {
						folderIdDie="1u1Rkb8sOiRrWuUMaykWFVhq44RXH_hSD"; //GLUTEN
					}
					
				} else if(cliente.getNogustos().contains("Huevos")){		//SIN HUEVO
					folderIdDie="1CVZ2hDfti-ANgM34eEt41hJytgiL52GN";
				} else if(cliente.getNogustos().contains("Claras")){		//SIN CLARAS
					folderIdDie="1O295aGTmfN-xOO0yeA3eIxu65rA7bSUF";
				} else if(cliente.getNogustos().contains("Aguacate")){		//SIN AGUACATE
					folderIdDie="1FqTmVtvpvgz2tD5H7jauaBp-rDxIo2_W";
				} else if(cliente.getNogustos().contains("Pescado")){		//SIN PESCADO
					folderIdDie="1U8ChU8axm9MKsI0qpSzaBGERidchzW5d";
				} else {  												//SIN VERDURA
					folderIdDie="1XP3S7qC5TYaLSOV3fwgeXACdp18GzJgI";
				}
			} else if(cliente.getNumerocomidas()==3) {  			// 3 COMIDAS
				if (cliente.getNogustos().contains("NINGUNA")) {
					folderIdDie="1jf86LoUPHMaXRTH9xmPji7g5x2NtOGIN"; //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGANA GLUTEN
						folderIdDie="1O3wpiCTqvXLRmGUg1e77f0WXmtkSM9xH";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGANA LACTOSA
						folderIdDie="1SKdcjTfB-4N47XHFzY0ZAwBJzHAi1EWi";
					} else {
						folderIdDie="1NwBNVZipsVKpWg5Z2QskLgwJQtxNL2Ud"; //VEGANA ****
					}
					
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGETARIANA GLUTEN
						folderIdDie="1GWfvGJiBInm7jdlRecJu6vLSNh-WaR5j";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGETARIANA LACTOSA
						folderIdDie="1IOCfm2djUJllLFWupHA7NKHtNgCCka1B";
					} else {
						folderIdDie="1Tqa8X1Sl6pDe2kkQI2V3JjnRYA-tIddX";//VEGETARIANO
					}
					
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {           //LACTOSA GLUTEN
						folderIdDie="1WnGMe2qiAgS1be4Vc41bmbhT07bhvHjz";
					} else if(cliente.getNogustos().contains("Huevos")){		//LACTOSA HUEVO
						folderIdDie="1GLyYtKQ6EY2pxnSH6uZTXvcfRON85OUv";
					} else if(cliente.getNogustos().contains("Claras")){		//LACTOSA CLARAS
						folderIdDie="1dTlTaBVF4sT3dRGWwbeBJl6fIU1DemyA";
					} else if(cliente.getNogustos().contains("Aguacate")){		//LACTOSA AGUACATE
						folderIdDie="19gpYbV2vtpXsfq8KdN-8nV7FAcjA1ocp";
					} else if(cliente.getNogustos().contains("Pescado")){		//LACTOSA PESCADO
						folderIdDie="1XCGRiYo6NHdDh_WBuIKl4QLdcGp0-DWJ";
					} else if(cliente.getNogustos().contains("Verdura")){		//LACTOSA VERDURA
						folderIdDie="1DMF6LF6vbW6vx6kJmjB4pj4-MZzhM44O";
					} else {
						folderIdDie="1ahFN8LO1e9H71vEBy-k_Sj0na_eVF6Ge"; //LACTOSA
					}
					
				} else if(cliente.getNogustos().contains("Gluten")){

					if (cliente.getNogustos().contains("Huevos")){		        //GLUTEN HUEVO
						folderIdDie="1IUaiP4BJrV1B2_YwQHtUChXsAGbO_hbL";
					} else if(cliente.getNogustos().contains("Claras")){		//GLUTEN CLARAS
						folderIdDie="1WLTwVietsFVbmgHuN2TPo6R-Us4-RY2K";
					} else if(cliente.getNogustos().contains("Aguacate")){		//GLUTEN AGUACATE
						folderIdDie="1YPGShadYA4-kOCBswCmxtbJFcFm-SyAe";
					} else if(cliente.getNogustos().contains("Pescado")){		//GLUTEN PESCADO
						folderIdDie="1-XgMaEhlUS2qw0P2ewUJhQt6jTjlw531";
					} else if(cliente.getNogustos().contains("Verdura")){		//GLUTEN VERDURA
						folderIdDie="10v6lVYLRk_GSTcw4yDo1zurIdT6xkulJ";
					} else {
						folderIdDie="15szI2ZTZQOAMGMSPnz9o7r4DL8KOT2UB"; //GLUTEN
					}
					
				} else if(cliente.getNogustos().contains("Huevos")){		//SIN HUEVO
					folderIdDie="1VGOfv7-h0TBa6MSL78cukvbN7mlY95FO";
				} else if(cliente.getNogustos().contains("Claras")){		//SIN CLARAS
					folderIdDie="1J-KpsxHu52GFzN2db9hGB1oX2M-srd1u";
				} else if(cliente.getNogustos().contains("Aguacate")){		//SIN AGUACATE
					folderIdDie="17R-a4zVhgaElUav4I_sfRINJ6mujodPU";
				} else if(cliente.getNogustos().contains("Pescado")){		//SIN PESCADO
					folderIdDie="1af-C8B83G-ErnEScydxT4LS4kON4Ug7B";
				} else {  												//SIN VERDURA
					folderIdDie="1RJDRVkgM8sFNmINzpbk5PV_iJPgaz1g5";
				} 
			} else if(cliente.getNumerocomidas()==4) {  			// 4 COMIDAS
				if (cliente.getNogustos().contains("NINGUNA")) {
					folderIdDie="1bhDWQasVNgMeewY3nl6mMjqKR3t9h8c0"; //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGANA GLUTEN
						folderIdDie="1bRpKpXzDIMmpxwHSn7MOH4V_DDd29Qed";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGANA LACTOSA
						folderIdDie="1NFc3TMutxBOTlj7MuVeBIIoAsMhC3qhu";
					} else {
						folderIdDie="1Ocgeth3tPVagc6iMtVU7qsd3PtguPemG"; //VEGANA ****
					}
					
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGETARIANA GLUTEN
						folderIdDie="12gqXO8eVcTXbBVudMysKXdIND6HonFA-";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGETARIANA LACTOSA
						folderIdDie="19gASazzpNgbNVrHc_fnFC4LnUjjFt_r_";
					} else {
						folderIdDie="1RmZTy0DPDnxs0QXGv9x6JxTWvCuP2E_9";//VEGETARIANO
					}
					
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {           //LACTOSA GLUTEN
						folderIdDie="14GH1S4d9w6CCAP83UEeDG1UBbgzJhECT";
					} else if(cliente.getNogustos().contains("Huevos")){		//LACTOSA HUEVO
						folderIdDie="1pqNuV-VURIE0eV08SZh86hbY0wSll7OE";
					} else if(cliente.getNogustos().contains("Claras")){		//LACTOSA CLARAS
						folderIdDie="1nAYMTHP8-jOQtaWcjY7FKzV20B1pVekW";
					} else if(cliente.getNogustos().contains("Aguacate")){		//LACTOSA AGUACATE
						folderIdDie="1FArNcK3s7xB78WJg2fxfXjUiYuvWWCVG";
					} else if(cliente.getNogustos().contains("Pescado")){		//LACTOSA PESCADO
						folderIdDie="1f329T7iqxSnMHHEVO-XowEOuHEC4XjLT";
					} else if(cliente.getNogustos().contains("Verdura")){		//LACTOSA VERDURA
						folderIdDie="1WSCasRNxW9F1SXHiuwY_g2VTjZOLq_4A";
					} else {
						folderIdDie="1yd55VZrqQ2PnazUkJ9IVRQxjpnAbosrp"; //LACTOSA
					}
					
				} else if(cliente.getNogustos().contains("Gluten")){

					if (cliente.getNogustos().contains("Huevos")){		        //GLUTEN HUEVO
						folderIdDie="1ZJr_xHagoe68jm3g-92VJhs73pnnrK18";
					} else if(cliente.getNogustos().contains("Claras")){		//GLUTEN CLARAS
						folderIdDie="1BNU9slonXK7srfTfIuCuR43Aw4wyaQ28";
					} else if(cliente.getNogustos().contains("Aguacate")){		//GLUTEN AGUACATE
						folderIdDie="1vBp6W0kAAGZY1GuLY7HfVKW512Tvw7oa";
					} else if(cliente.getNogustos().contains("Pescado")){		//GLUTEN PESCADO
						folderIdDie="1SeEiNDrVns2VuU6L2kq5r6TMqJMWfkVk";
					} else if(cliente.getNogustos().contains("Verdura")){		//GLUTEN VERDURA
						folderIdDie="1Olelo49C6yxtAn-guF6Y9wOiq8tdCE34";
					} else {
						folderIdDie="1AoJYhZQb6yFs173Q_YCPDZ2RuydwOC-j"; //GLUTEN
					}
					
				} else if(cliente.getNogustos().contains("Huevos")){		//SIN HUEVO
					folderIdDie="1kbsBM-Eu30S04YV9XDdDYFPHnUsVZt5r";
				} else if(cliente.getNogustos().contains("Claras")){		//SIN CLARAS
					folderIdDie="1wl1EvG3ndGvvPkcgB_2HNVAzhQK4RUkG";
				} else if(cliente.getNogustos().contains("Aguacate")){		//SIN AGUACATE
					folderIdDie="1tOu7EVUB1qA4C8iMY4_WUzOuxhBCIYRP";
				} else if(cliente.getNogustos().contains("Pescado")){		//SIN PESCADO
					folderIdDie="1BaLMSACOSagmMOOiExVnGBye_B_-fEs_";
				} else {  												//SIN VERDURA
					folderIdDie="1sMCy_KoB74qV-ogktBpbczSDpeesLLfA";
				}
			} else {  												// 5 COMIDAS
				if (cliente.getNogustos().contains("NINGUNA")) {
					folderIdDie="1NiSZvOuF9ZrmtNp7lFYUd_JkJEe2TV06"; //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGANA GLUTEN
						folderIdDie="1loF4rAtRjO59u25IW0_I5H-qNYp9FOms";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGANA LACTOSA
						folderIdDie="1px9BSbvORD80X7c3zAdt7acwo1G0VbhS";
					} else {
						folderIdDie="1KK9I9Ec-Ntd_AawFG75UjA7BJyuqaXrs"; //VEGANA ****
					}
					
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGETARIANA GLUTEN
						folderIdDie="1pj4k-cj5Squ4YtR-1riNKroy-Obt2yfG";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGETARIANA LACTOSA
						folderIdDie="1mwv7jSfQ8PRE2j04sPmgGn0GhxIlarlY";
					} else {
						folderIdDie="1GJrw52ZbnBRVlD5uBv66msisw10zhAKI";//VEGETARIANO
					}
					
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {           //LACTOSA GLUTEN
						folderIdDie="1MrE_5qu-iZrOzjPhrIa2qQZpDLx6WwLw";
					} else if(cliente.getNogustos().contains("Huevos")){		//LACTOSA HUEVO
						folderIdDie="1Gx7yVwqgvA2XrKbRpP7K93qwQqGhki9F";
					} else if(cliente.getNogustos().contains("Claras")){		//LACTOSA CLARAS
						folderIdDie="1L-SFq-EdRseDv8eXUTBNuHuX99ocToag";
					} else if(cliente.getNogustos().contains("Aguacate")){		//LACTOSA AGUACATE
						folderIdDie="1l9aE3ElEiU6L8wPxyrovejDo91s1vp9e";
					} else if(cliente.getNogustos().contains("Pescado")){		//LACTOSA PESCADO
						folderIdDie="1KPHJWPuySH1RezSJk6Qr7Go_8iA3sWQh";
					} else if(cliente.getNogustos().contains("Verdura")){		//LACTOSA VERDURA
						folderIdDie="1AcOWx95Ix2f__r7kL54cM2N2AVllLxu3";
					} else {
						folderIdDie="1ukPYU2FhA9fkPgbnyKSaWZbL2qB0Oz-n"; //LACTOSA
					}
					
				} else if(cliente.getNogustos().contains("Gluten")){

					if (cliente.getNogustos().contains("Huevos")){		        //GLUTEN HUEVO
						folderIdDie="1VswrYqgDY-9peodF9o9SwoMseIYNLLl4";
					} else if(cliente.getNogustos().contains("Claras")){		//GLUTEN CLARAS
						folderIdDie="1-QhXKBxV3jtHeB65FCrTTkCF8rpJDov1";
					} else if(cliente.getNogustos().contains("Aguacate")){		//GLUTEN AGUACATE
						folderIdDie="19kOwCDTgsvgRH9O3ChTNXRbGT9BFslDf";
					} else if(cliente.getNogustos().contains("Pescado")){		//GLUTEN PESCADO
						folderIdDie="19boUNrrtoJn-ifVlRucJo8Pyev8yB2XM";
					} else if(cliente.getNogustos().contains("Verdura")){		//GLUTEN VERDURA
						folderIdDie="1l9guMIqXQ_qIoBOPN2-GLTONpnLX6NDd";
					} else {
						folderIdDie="1GijUb-tUGfiLOXAhLNpLhv9PFBm6kq6D"; //GLUTEN
					}
					
				} else if(cliente.getNogustos().contains("Huevos")){		//SIN HUEVO
					folderIdDie="1bH_US8VDCJJ-tzWSHX3nbBwQ5nNC93n5";
				} else if(cliente.getNogustos().contains("Claras")){		//SIN CLARAS
					folderIdDie="1mSkVd9dJWqzqqF9UoLBPWkBlzuI80fBK";
				} else if(cliente.getNogustos().contains("Aguacate")){		//SIN AGUACATE
					folderIdDie="1_bNnV2sOOLN3VvaZEN61cfVRvy7T4zrt";
				} else if(cliente.getNogustos().contains("Pescado")){		//SIN PESCADO
					folderIdDie="15vx1PR55l1XVDkWJW21UdFBzAmN_wFh_";
				} else {  												//SIN VERDURA
					folderIdDie="12t_zT87k0Yc9vEPsDRrlnad8n_1udUfO";
				}
			}
		 } else {   ////////////////////////////////////////////////////////////////// MUJER
			if (cliente.getNumerocomidas()==2) {  					// 2 COMIDAS
				if (cliente.getNogustos().contains("NINGUNA")) {
					folderIdDie="1o-BxZy3XXTGYHYm2qn5VZAnhi6Kssgm9"; //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGANA GLUTEN
						folderIdDie="1AzF7Q2SRl4BONuFFXjczAleCK88Cn2dF";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGANA LACTOSA
						folderIdDie="14k3yiybWhx-TgNJ8zBmZKKB78ZmHKd6o";
					} else {
						folderIdDie="13wppJDZVKdys24m4xS4IFOCZRKOjEzaa"; //VEGANA ****
					}
					
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGETARIANA GLUTEN
						folderIdDie="1ozTujXpo39HI8T-LlykdDl5vpVdFBrMc";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGETARIANA LACTOSA
						folderIdDie="171K99SsikHLJaTD1zDCfwd1ET-Yp9Bnn";
					} else {
						folderIdDie="1qjYyIlQr9x7lsf2usTT1p6N8kJiwXrPc";//VEGETARIANO
					}
					
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {           //LACTOSA GLUTEN
						folderIdDie="1j0vb3S6tGkg16insjuy6iLel2C5g2HvP";
					} else if(cliente.getNogustos().contains("Huevos")){		//LACTOSA HUEVO
						folderIdDie="1wemDPcIWKKvZFN7EM6rCuLRPHVWM0y1a";
					} else if(cliente.getNogustos().contains("Claras")){		//LACTOSA CLARAS
						folderIdDie="13WMuze29BTmRz7Qf_wRWywUeXE1ehegR";
					} else if(cliente.getNogustos().contains("Aguacate")){		//LACTOSA AGUACATE
						folderIdDie="1FFwtWkjrQXG0mPsiz6Cz9TrIs4d8TUFx";
					} else if(cliente.getNogustos().contains("Pescado")){		//LACTOSA PESCADO
						folderIdDie="1w_dhoD2hwTe9HRoCRTAq1HuBGSGsjPSX";
					} else if(cliente.getNogustos().contains("Verdura")){		//LACTOSA VERDURA
						folderIdDie="1TxCTgefTbD45GX7D0bPXovF02xTLCrzk";
					} else {
						folderIdDie="1KlChmJEJGWmapWiJzImON5k5HmjWTzTn"; //LACTOSA
					}
					
				} else if(cliente.getNogustos().contains("Gluten")){

					if (cliente.getNogustos().contains("Huevos")){		        //GLUTEN HUEVO
						folderIdDie="1jj2SMbg_bxvYiQPSh70A1_liNML7UtLe";
					} else if(cliente.getNogustos().contains("Claras")){		//GLUTEN CLARAS
						folderIdDie="1pRIThrjHHLLLswHonoTWNOEsQ0fwAeJi";
					} else if(cliente.getNogustos().contains("Aguacate")){		//GLUTEN AGUACATE
						folderIdDie="1_AOoBn9K6sBEI7kjUg5qwvytF6TGbkNH";
					} else if(cliente.getNogustos().contains("Pescado")){		//GLUTEN PESCADO
						folderIdDie="1naYOz3R-ZQIzgHw0chaHP_sCNlEQZI9Q";
					} else if(cliente.getNogustos().contains("Verdura")){		//GLUTEN VERDURA
						folderIdDie="1LWOieu1twe4tSFBUkHMYpQMeK8Ry4j01";
					} else {
						folderIdDie="1x74sOevykdvog0gH7_FDCWKEOWLevOyP"; //GLUTEN
					}
					
				} else if(cliente.getNogustos().contains("Huevos")){		//SIN HUEVO
					folderIdDie="1VEfCiai_OHtyStfY-dGnPVNWOo95dXkj";
				} else if(cliente.getNogustos().contains("Claras")){		//SIN CLARAS
					folderIdDie="1Db067FJRi4SEnpTK0uPs4UNW3epY2yaJ";
				} else if(cliente.getNogustos().contains("Aguacate")){		//SIN AGUACATE
					folderIdDie="1DHMs6r1xRzXmSIQ6eyUKEP_eItwMbMmq";
				} else if(cliente.getNogustos().contains("Pescado")){		//SIN PESCADO
					folderIdDie="1Y2pJRKY1bJZXJ6mbe0NJW2tFSYycLopK";
				} else {  												//SIN VERDURA
					folderIdDie="1yHPWUF9b9ORS7qfej4Z_dmIkV5WeVFC-";
				}
			} else if(cliente.getNumerocomidas()==3) {  			// 3 COMIDAS
				if (cliente.getNogustos().contains("NINGUNA")) {
					folderIdDie="1Fu1V5cf9zi69qjI8Wf3rfj8rOaEc8T-M"; //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGANA GLUTEN
						folderIdDie="1XUmYweU_kSCQ7UZu75-Q8Y64tKMFuBbC";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGANA LACTOSA
						folderIdDie="1N4Hyme6jTZ6QNRQ_q4Ii_l7l7Mwu14k-";
					} else {
						folderIdDie="1IATpoQseXdKmFw9OPCG8c1QnAbADdZFB"; //VEGANA ****
					}
					
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGETARIANA GLUTEN
						folderIdDie="18teUTT8_hHJWfFckvE8SiuI1OhazKTH0";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGETARIANA LACTOSA
						folderIdDie="1dU075_4kXMt3iQzfsI302qkLZb6dGnth";
					} else {
						folderIdDie="1cNBg8UXzKAIn4pdGs020blhOVjewJFQN";//VEGETARIANO
					}
					
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {           //LACTOSA GLUTEN
						folderIdDie="1UXpA9py54filoYMdmgrf9LT4q0c1vaIi";
					} else if(cliente.getNogustos().contains("Huevos")){		//LACTOSA HUEVO
						folderIdDie="1LCOyoo6wSi87kMf1FeuHYAquBfAaIj1J";
					} else if(cliente.getNogustos().contains("Claras")){		//LACTOSA CLARAS
						folderIdDie="17lztpOexqDBkZ2sw-aFLcdDVkN9u_URN";
					} else if(cliente.getNogustos().contains("Aguacate")){		//LACTOSA AGUACATE
						folderIdDie="1dOieGgQdIVa2ZvOsFyEPHF_VtXMEse_O";
					} else if(cliente.getNogustos().contains("Pescado")){		//LACTOSA PESCADO
						folderIdDie="1f94BP3bemltnZb1YLMIAdHKGj7U6pALO";
					} else if(cliente.getNogustos().contains("Verdura")){		//LACTOSA VERDURA
						folderIdDie="1NK1fL5TJIlcOsEPZ_EBhsxTXL_qvLL0M";
					} else {
						folderIdDie="1B7_8QMM9kU8LzBCSyhiwzxkzca05znzI"; //LACTOSA
					}
					
				} else if(cliente.getNogustos().contains("Gluten")){

					if (cliente.getNogustos().contains("Huevos")){		        //GLUTEN HUEVO
						folderIdDie="1Dftm2kW8XyoOa2QEMMLz6aN2j8mkr8FY";
					} else if(cliente.getNogustos().contains("Claras")){		//GLUTEN CLARAS
						folderIdDie="1xgwsOp9oASdzSH3zJKtY2qJOHKJkOIEo";
					} else if(cliente.getNogustos().contains("Aguacate")){		//GLUTEN AGUACATE
						folderIdDie="1I0ktLaXYuGxXZWp6Iq-csp_YZE65IDgi";
					} else if(cliente.getNogustos().contains("Pescado")){		//GLUTEN PESCADO
						folderIdDie="1_aPmS_nfXWIk1cvvQ_hZ1u5VFTWXdL0O";
					} else if(cliente.getNogustos().contains("Verdura")){		//GLUTEN VERDURA
						folderIdDie="1MC0L5SzGLespqT_a040VANz_ox27ElJN";
					} else {
						folderIdDie="10-_EOlZLSqsz6xfiLl58i7Z4y8K7van3"; //GLUTEN
					}
					
				} else if(cliente.getNogustos().contains("Huevos")){		//SIN HUEVO
					folderIdDie="1_k-0-nOe_Q7bDzDb1Cj0GaeScPbt2rPM";
				} else if(cliente.getNogustos().contains("Claras")){		//SIN CLARAS
					folderIdDie="11AHA_oI_5vHvqOCiSekvcJVKdYh7suEw";
				} else if(cliente.getNogustos().contains("Aguacate")){		//SIN AGUACATE
					folderIdDie="1rznh_li8ZYNISY2fvcUsfpdJx90G3Tv6";
				} else if(cliente.getNogustos().contains("Pescado")){		//SIN PESCADO
					folderIdDie="1VT3fe2zqVuDBrfGDy9yCO1u8v8bgxu5Z";
				} else {  												//SIN VERDURA
					folderIdDie="16h-YbnwGckl_-DAceimHedW8MX0_Tewt";
				} 
			} else if(cliente.getNumerocomidas()==4) {  			// 4 COMIDAS
				if (cliente.getNogustos().contains("NINGUNA")) {
					folderIdDie="1yZ9YbX7Z2lZFxBJQ85K5EqN9TLbjGSVH"; //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGANA GLUTEN
						folderIdDie="1PoW6HveEw6DIVymyabs362puIDvWe2RH";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGANA LACTOSA
						folderIdDie="1YTM-PxMolgiN1Uya2cuH3qH0Zyieu_IB";
					} else {
						folderIdDie="1-FjBYcmA54iYf6lgiOMR31UmCi_XCWLb"; //VEGANA ****
					}
					
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGETARIANA GLUTEN
						folderIdDie="1RcNlxiKskv-1xeq01LoIpMsg9Z52j7QT";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGETARIANA LACTOSA
						folderIdDie="1bHFBlsZOH_PjPpiISZbwcKriVnSIAkWe";
					} else {
						folderIdDie="1Xud9RbMVW1CsUvcuGuOZg2D7_DAyuFzC";//VEGETARIANO
					}
					
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {           //LACTOSA GLUTEN
						folderIdDie="1o-XX8Lhp812cda5zp7DLS1jLwSNaLcPS";
					} else if(cliente.getNogustos().contains("Huevos")){		//LACTOSA HUEVO
						folderIdDie="1zCz8DHtCAS42SzEMVTczRaFQr1m46Y_k";
					} else if(cliente.getNogustos().contains("Claras")){		//LACTOSA CLARAS
						folderIdDie="1jXvzhlAZebllEYftiqyWBcGUHgAHv18d";
					} else if(cliente.getNogustos().contains("Aguacate")){		//LACTOSA AGUACATE
						folderIdDie="1pdBlajSZYHUGh1c_wcXJxWqR9avIthKU";
					} else if(cliente.getNogustos().contains("Pescado")){		//LACTOSA PESCADO
						folderIdDie="1je107u989aLJsS0GjONGAKrMMQAGagL_";
					} else if(cliente.getNogustos().contains("Verdura")){		//LACTOSA VERDURA
						folderIdDie="1p9KWvjPSSeGchkqz9HryRP1s3jSM7piX";
					} else {
						folderIdDie="1LImMynsE1I67IGGiAVlOwY5ViMx9cX6d"; //LACTOSA
					}
					
				} else if(cliente.getNogustos().contains("Gluten")){

					if (cliente.getNogustos().contains("Huevos")){		        //GLUTEN HUEVO
						folderIdDie="1Du_Jfd4vlvY509Fg3ekJ0KDXQA26_lAC";
					} else if(cliente.getNogustos().contains("Claras")){		//GLUTEN CLARAS
						folderIdDie="15iUHIFglxhpTFd405a9NunID8pjC2-6q";
					} else if(cliente.getNogustos().contains("Aguacate")){		//GLUTEN AGUACATE
						folderIdDie="1kbM0MEjgQigX6R2Mc_sIT3lFNN2WpXRF";
					} else if(cliente.getNogustos().contains("Pescado")){		//GLUTEN PESCADO
						folderIdDie="1e-n_3H8MPxHutKIbkN9pD6lxRJut_dEw";
					} else if(cliente.getNogustos().contains("Verdura")){		//GLUTEN VERDURA
						folderIdDie="1F-sQmGgK0RLyPaG8B4syXBvGPBWvGQ3f";
					} else {
						folderIdDie="15dg5_yEKHzqRl0Ay6lXrOwrgTJNRYZM3"; //GLUTEN
					}
					
				} else if(cliente.getNogustos().contains("Huevos")){		//SIN HUEVO
					folderIdDie="1r9iZ69qtRaWn07X2qGhxx4bq3cWZ0kVv";
				} else if(cliente.getNogustos().contains("Claras")){		//SIN CLARAS
					folderIdDie="1IrN5B7cdf3mgGvCYLU5luzMUdgIVJStH";
				} else if(cliente.getNogustos().contains("Aguacate")){		//SIN AGUACATE
					folderIdDie="1WhegN5bttI4dgooCqr0aSrZIMZMHpdlO";
				} else if(cliente.getNogustos().contains("Pescado")){		//SIN PESCADO
					folderIdDie="1yU9SEuPoRMjabSf6MIyN5PoSZQr39YzB";
				} else {  												//SIN VERDURA
					folderIdDie="1MYxzAQU8gl8ZS99hO_yjIQ_30N7ao4xP";
				}
			} else {  												// 5 COMIDAS
				if (cliente.getNogustos().contains("NINGUNA")) {
					folderIdDie="1F-ZvoVyo7eOYjyhbFylOxIMA6ZcUldTn"; //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGANA GLUTEN
						folderIdDie="1VNJPVr5VetkJa6a1OoLka3gfR51bY-Md";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGANA LACTOSA
						folderIdDie="18qaZCiunDGiMPUjDOx3jcrCoRnsRczMc";
					} else {
						folderIdDie="1Gk7VHAn17ldJ01SIDV_zo7vTaNSAEjrd"; //VEGANA ****
					}
					
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getNogustos().contains("Gluten")) {           //VEGETARIANA GLUTEN
						folderIdDie="1o4f1ZBlOPueZOu78HZhS-dKCy7CzhpED";
					} else if (cliente.getNogustos().contains("Lactosa")) {   //VEGETARIANA LACTOSA
						folderIdDie="1XxnOYikld7210D0kaYfObtc0nSiGZzZc";
					} else {
						folderIdDie="1jvClH_oEkkzC9NJXWo-IcslEGfGnDzhg";//VEGETARIANO
					}
					
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {           //LACTOSA GLUTEN
						folderIdDie="1UmXEDj4xa0gt0jIah_XPxAE7jmCiBLL_";
					} else if(cliente.getNogustos().contains("Huevos")){		//LACTOSA HUEVO
						folderIdDie="1QKD8i0okNsLEXRFNhHNFVIee3XwZsXL5";
					} else if(cliente.getNogustos().contains("Claras")){		//LACTOSA CLARAS
						folderIdDie="1m3CssZuxYPai3xG2xVeeRRZ3G40qICr1";
					} else if(cliente.getNogustos().contains("Aguacate")){		//LACTOSA AGUACATE
						folderIdDie="1I3z_IUm7daVmcCJd6v4yaMzYY5EHYE-D";
					} else if(cliente.getNogustos().contains("Pescado")){		//LACTOSA PESCADO
						folderIdDie="11qnGiuz3I1iyhJOJg39SatMdu0wCakAt";
					} else if(cliente.getNogustos().contains("Verdura")){		//LACTOSA VERDURA
						folderIdDie="15oAazG0Dv9TUSq32wvw3fWUEz_si3kIV";
					} else {
						folderIdDie="1UBJB99IJvt-27Pt2wOVM_xXuk7oMOtRS"; //LACTOSA
					}
					
				} else if(cliente.getNogustos().contains("Gluten")){

					if (cliente.getNogustos().contains("Huevos")){		        //GLUTEN HUEVO
						folderIdDie="1JaaOuTBs7XvOhCtu1XpmvG_Lf-274CQc";
					} else if(cliente.getNogustos().contains("Claras")){		//GLUTEN CLARAS
						folderIdDie="1dTsySYiFt46FZU5ADos85eUiwpmkGksb";
					} else if(cliente.getNogustos().contains("Aguacate")){		//GLUTEN AGUACATE
						folderIdDie="1i3nC2eoDacxatZIVhJrHqcX7XpW50opP";
					} else if(cliente.getNogustos().contains("Pescado")){		//GLUTEN PESCADO
						folderIdDie="15Du_y9deO-4kLcPbEfn3NHEINudz_Om6";
					} else if(cliente.getNogustos().contains("Verdura")){		//GLUTEN VERDURA
						folderIdDie="1dU0Z8BmeOXkFIBfLGrblfVsudSS-uCDe";
					} else {
						folderIdDie="1A6obWeAEyxrgcuQb3sr_dXOkZ91aTeDg"; //GLUTEN
					}
					
				} else if(cliente.getNogustos().contains("Huevos")){		//SIN HUEVO
					folderIdDie="1fzkGcqlWQI6SzPIAbWvNorwWvsRAG8__";
				} else if(cliente.getNogustos().contains("Claras")){		//SIN CLARAS
					folderIdDie="1AatIyLYGfQ6eA1WXj7hd5eo4Mw9cAglE";
				} else if(cliente.getNogustos().contains("Aguacate")){		//SIN AGUACATE
					folderIdDie="1blAteBem1JKBSc7-69xDT01h7oEg52N6";
				} else if(cliente.getNogustos().contains("Pescado")){		//SIN PESCADO
					folderIdDie="1LbEDJ8RpzFLiKO80iF7XCKPXyQJgW_F-";
				} else {  												//SIN VERDURA
					folderIdDie="1yrofVFKlm9D8Ut1QT3-kMX5_F2yOyKAb";
				}
			}
		}
		
		java.io.File destinationFolderA = new java.io.File(selectedFolder.getAbsolutePath() + "/"+clienteN+" Dieta-TMB_"+cliente.getTMB());
		if (!destinationFolderA.exists()) {
		    destinationFolderA.mkdirs();
		}
		
		if (destinationFolderA.exists()) {
		    // borrar todos los archivos de la carpeta
		    java.io.File[] archivos = destinationFolderA.listFiles();
		    for (java.io.File archivo : archivos) {
		        archivo.delete();
		    }
		}
		
//		String query;
//		if (cliente.getObjetivo().equals(Objetivo.definicion)) {
//			query = "mimeType='application/pdf' and '" + folderIdDie + "' in parents";
//		} else {
//			query = "mimeType='application/vnd.openxmlformats-officedocument.wordprocessingml.document' and '" + folderIdDie + "' in parents";
//		}
		
		String query=query = "mimeType='application/vnd.openxmlformats-officedocument.wordprocessingml.document' and '" + folderIdDie + "' in parents";
		
		Files.List request1 = drive.files().list().setQ(query).setOrderBy("title asc");
		
//		int archivosDD;
//		if (cliente.getObjetivo().equals(Objetivo.definicion)) {    //PARA DESCARGAR X DIETAS SEGUN LOS MESES
//			if (cliente.getMesesentrenados()>=12) {
//				archivosDD=0;
//			} else {
//				archivosDD=cliente.getMesesentrenados();
//			}
//		} else {
//			if (cliente.getMesesentrenados()>5) {
//				archivosDD=0;
//			} else {
//				archivosDD=cliente.getMesesentrenados();
//			}
//		}
		
		int ind = 1;
		try {
			int totalArchivos = request1.execute().getItems().size();
//		    HiloProgreso progressThread = new HiloProgreso(totalArchivos);
//		    progressThread.start();
		    
			for (File file : request1.execute().getItems()) {
			
//			if (archivosDD!=0) {  // para desdcargar archivo a partir de X
//				archivosDD--;
//				ind++;
//				continue;
//				
//			} else {
			
			String fileName = file.getTitle();
			String cabezerafile;
			
//			if (cliente.getObjetivo().equals(Objetivo.definicion)) {
//				int cantidad = 4;
//			    cabezerafile = fileName.substring(0, fileName.length() - cantidad);
//			    term = ".pdf";
//			} else {
//				int cantidad = 5;
//			    cabezerafile = fileName.substring(0, fileName.length() - cantidad);
//			    term = ".docx";
//			}
			
			String term=".docx";
			
		    OutputStream outputStream = new FileOutputStream(new java.io.File(destinationFolderA, fileName+"-"+cliente.getNombreC()+term));
//		    if (cliente.getObjetivo().equals(Objetivo.volumen)) {
//				outputStream=convertDocxToPdf(new FileInputStream(new java.io.File(destinationFolderA, "Dieta Mes "+ind+" "+cliente.getNombreC()+term)));
//			} else {
//
//			}
		    drive.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
		    outputStream.flush();
		    outputStream.close();
//		   	ind++;
//		   	progressThread.archivoDescargado();
			}
//			progressThread.join();
//			}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "El archivo de dieta no existe.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		System.out.println("Las DIETAS han sido descargadas con exito");
		
		//PRIMERO DESCARGAMOS LAS DIETAS VIEJAS
				String folderIdDie2 = "";
				
		if (cliente.getObjetivo().equals(Objetivo.definicion)) {
			if (cliente.getSexo().equals(Sexo.hombre)) {
				if (cliente.getNogustos().contains("NINGUNA")) {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie2="1DFQ8HefsXmLFk2UnNLTWrm-Nj485HIpT";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie2="1DFQ8HefsXmLFk2UnNLTWrm-Nj485HIpT";
					} else {
						folderIdDie2="1Asp20Am8JuvK4taRx2sJh-qwYmsXuuCI";
					} //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie2="1rjpG5jm39RishNFII7Rt2q9JLUA7VVrq";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie2="1iwukT6spXqVZ_kdyZj_vr3wmx4SdaRpN";
					} else {
						folderIdDie2="1lix_YH5OJgh7oFdaTSwj0yAid6Mf62P1";
					} //VEGANA
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie2="1sFjXSTcgcxFeAyx8fA9Mb9ZkeicsNW9H";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie2="1heGNYTi_-M82Uc6YDVEvW2U4AFGZ0dxg";
					} else {
						folderIdDie2="1CnlCZmrKGP89dFmuNBSpY_jEmaxuinqx";
					} //VEGETARIANO
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {
						if (cliente.getNogustos().contains("Verdura") || cliente.getNogustos().contains("Pescado")) {
							if (cliente.getPeso().equals(Peso.mas90)) {
								folderIdDie2="1KD6PS2N76zSJFa5hMiBKg7UiSCZvnHIQ";
							} else if (cliente.getPeso().equals(Peso.entre70y90)) {
								folderIdDie2="1QYbChg5w6s8Pqqsv6__kpsWCR-ocFeva";
							} else {
								folderIdDie2="1Y407j_PraGcoVbIxs6b_Ey990ulJq-Wc";
							} //NO LACTOSA NO GLUTEN NO VERDURA NO PESCADO
						} else {
							if (cliente.getPeso().equals(Peso.mas90)) {
								folderIdDie2="1dO4vLkCtzalHM1c0YOIFgsCTwXYca8hu";
							} else if (cliente.getPeso().equals(Peso.entre70y90)) {
								folderIdDie2="1MFlUOjs7xJRcwuqsAphDlisdZgf--FtD";
							} else {
								folderIdDie2="1-NNZiilGQmH-0bOAJYjK5AmmTj9MWIZ5";
							} //NO LACTOSA NO GLUTEN
						}
					} else if(cliente.getNogustos().contains("Claras")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="10762n1PKhW2biH8lxTAnpuvZnJHdC1WJ";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1QjQmypabVv_hUqeunU0bA4hPzgAqfmVy";
						} else {
							folderIdDie2="1jhIm_QBS-i1j7bT57yXMiUiLEBk6k0Un";
						} //NO LACTOSA NO CLARAS
					} else if(cliente.getNogustos().contains("Huevos")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1opTJIrE1h25nNYCuq0-AhjmTcgy7UIiB";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="10hMtWiAgn8W5I_mfzZ6yDOYhUEcSLkmx";
						} else {
							folderIdDie2="1eFTSV-5jZ4XoaVyGZyYzhBTX19ANJlhW";
						} //NO LACTOSA NO HUEVOS
					} else if(cliente.getNogustos().contains("Pescado")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="18Zn818bhGtLx28vgIQzFAfYC3Jxpg7Qj";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1d2endKvxoPQkTtVRdPuQb2tlVSRYT9BM";
						} else {
							folderIdDie2="1FeimjoWipSswnnMwFWjcRA4XHYxi_bdD";
						} //NO LACTOSA NO PESCADO
					} else if(cliente.getNogustos().contains("Verdura")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="14QKpK8HVNaR6tJX_YdqMtnedmV61OB7W";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1wXcMao0941RYgrRbUlvMfeIzxngJqB__";
						} else {
							folderIdDie2="1SrTmGyAT2pvTS4DS6SGzkQAtrKlik5uL";
						} //NO LACTOSA NO VERDURA
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1FR0Ytg3dQPpuh_-7VK-9xCXv0IJAV7Y6";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1-WpkFXFeTIGqe3xJedDoRxoyBabzTYzk";
						} else {
							folderIdDie2="1G3dBjdd_tEbjDVv1upt8q-rmhFSqCE4I";
						} //NO LACTOSA
					}
				} else if (cliente.getNogustos().contains("Frutos secos")) {
					if (cliente.getNogustos().contains("Claras")) {
						if (cliente.getNogustos().contains("Aguacate")) {
							if (cliente.getPeso().equals(Peso.mas90)) {
								folderIdDie2="11OOZVxv-NNyMyYAPME2qKEbyna-KTKuk";
							} else if (cliente.getPeso().equals(Peso.entre70y90)) {
								folderIdDie2="1v_PC5sE0sz4cIyItYLlYCbnh5M3eNGgP";
							} else {
								folderIdDie2="1aBDszvP213UND7NFZmzCOGdqdqnMVSNi";
							} //NO FRUTOS SECOS NO CLARAS NO AGUACATE
						} else {
							if (cliente.getPeso().equals(Peso.mas90)) {
								folderIdDie2="1HP5ceipxKz4viNbKJpdclp7fkI7Ogxnc";
							} else if (cliente.getPeso().equals(Peso.entre70y90)) {
								folderIdDie2="1VmgsF_EraVfHt--VC1H4hLgJrOs7g-Hg";
							} else {
								folderIdDie2="1zusjwpm1ecaaZdA-X9HsKrJWLFgfqJmj";
							} //NO FRUTOS SECOS NO CLARAS
						}
					} else if (cliente.getNogustos().contains("Lactosa")){
						if (cliente.getNogustos().contains("Claras")) {
							if (cliente.getPeso().equals(Peso.mas90)) {
								folderIdDie2="132A2nhRNmL3jNdztT3GCppufQDhOfq_r";
							} else if (cliente.getPeso().equals(Peso.entre70y90)) {
								folderIdDie2="1uV8Ik34M6ii7_naJd374hgOTUCRoJsDH";
							} else {
								folderIdDie2="1Z9NHHWyaOq8uBUun1VNL0rCnt21yXW32";
							} //NO FRUTOS SECOS NO LACTOSA NO CLARAS
						} else {
							if (cliente.getPeso().equals(Peso.mas90)) {
								folderIdDie2="1Ryul-j1aCdHFnM2A1OTxWJk_U8nS56Yp";
							} else if (cliente.getPeso().equals(Peso.entre70y90)) {
								folderIdDie2="1_DFZ6AgIhbsj70Gmoft68-jUF9QP0zof";
							} else {
								folderIdDie2="1EbRbbzWhVYB3p_qdGimU7MJZk8o5nOva";
							} //NO FRUTOS SECOS NO LACTOSA 
						}
					} else if (cliente.getNogustos().contains("Gluten")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1IWu6PzoEKFM9qC32tSW1NwYBIa_HXB0F";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1MHoYRyjTvKThJ7nPpSGuUawcR4uuhqTE";
						} else {
							folderIdDie2="119m97dh3Xgbb41HslqnCalArBgj9g75t";
						} // NO FRUTOS SECOS NO GLUTEN
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1IqzBOWb4CRtuAx1J3cFV0vM5FZMEX4Ro";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1xh9lhonPPWBtDG7ylLzSse9NzEhM8Ht9";
						} else {
							folderIdDie2="1mJUtNrSUhzO4uJRKXVkQk9vIZ_HuBsmV";
						} //NO FRUTOS SECOS
					}
				} else if (cliente.getNogustos().contains("Gluten")) {
					if (cliente.getNogustos().contains("Claras")) {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1kyCY1ZLSjsJdKpOuWbVnNtoHLx44zPZ6";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1ibnAdpluS4pG7_45Sm8f7DJGSnl-UVpP";
						} else {
							folderIdDie2="1NJ2s9kBEYxHLxj5TKZxaxvVY4byeC9pR";
						} // NO GLUTEN NO CLARAS
					} else if (cliente.getNogustos().contains("Aguacate")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="14C2s5LGnbcmVS0C0ivsKq-wduCvXaX6A";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1TmaikoY03Xuy6LlB0yOL3dNIBWrNDyej";
						} else {
							folderIdDie2="1GC4aj5HwQg9zvox8UlDkUSIAweeFrRVs";
						} // NO GLUTEN NO AGUACATE
					} else if (cliente.getNogustos().contains("Huevos")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="16wakVbCufe2ZYpM3UZRr6RQadOYzHLjL";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1sY27T9f5zxIm9u2A5KN3PSZzck7lCZCZ";
						} else {
							folderIdDie2="1XAKsdGUre8F7PC2JbI89oVd-Ejncrgv2";
						} // NO GLUTEN NO HUEVOS
					} else if (cliente.getNogustos().contains("Pescado")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1DOY0G3_GoZmzgI4ih3cq5uzwDIjXGZ9o";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1btrgCeYVAeL6zc2aNPBJPUcfzzMX48id";
						} else {
							folderIdDie2="1f6Og-pJSbNh_L1_M54F2QLoy2V0siEFC";
						} // NO GLUTEN NO PESCADO
					} else if (cliente.getNogustos().contains("Verdura")){
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1_0yOoqGAB1sSQq9jHUTxkos-1hjlymBD";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1x_XLht85Z9tnxmBNxgAhmwyqh-Svr7M4";
						} else {
							folderIdDie2="1Z1qeVNWv1g0dirDGME2uR5X6rLjEOClY";
						} // NO GLUTEN NO VERDURA
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1pUKSmLtuHThlOpA31B9gMkWC_CxCf_2U";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="10FOx7KRGphaPxvUiIwF5aq8e93iu2KSb";
						} else {
							folderIdDie2="1QEJuOhlxK9iaDqtDRmLY2J5mw_nJTzcC";
						} //NO GLUTEN
					}
				} else if (cliente.getNogustos().contains("Aguacate")) {
					if (cliente.getNogustos().contains("Claras")) {
						if (cliente.getNogustos().contains("Lactosa")) {
							if (cliente.getPeso().equals(Peso.mas90)) {
								folderIdDie2="1JCOGqc8R2m2O6Xqh-006qXj65mFI5lNC";
							} else if (cliente.getPeso().equals(Peso.entre70y90)) {
								folderIdDie2="1OM9q_6IydSHQ00Qu3z-6jk8zjM4pvAhZ";
							} else {
								folderIdDie2="1IScUY3iizs6N7XCWbnUUszsFNfHx7XkI";
							} //NO AGUACATE NO CLARAS NO LACTOSA
						} else {
							if (cliente.getPeso().equals(Peso.mas90)) {
								folderIdDie2="1L0hQk6PRqDBbkSQiHJfpmu3JrKSA7rka";
							} else if (cliente.getPeso().equals(Peso.entre70y90)) {
								folderIdDie2="1DD-LaVj6Lcl6jLa-X-hMNlQZ7EmqM_oy";
							} else {
								folderIdDie2="15BcdwY1sZlvAT_sO-huKrDf2imuuWjO_";
							} // NO AGUACATE NO CLARAS
						}
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1joedIbx6oxUwGzGLaoZkvWchgmlDcKan";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1sPxhjTpvtmaUjp_6m830cVfh6tqD92FR";
						} else {
							folderIdDie2="1LHcUxegwA7gKfDXJGsxNDkVJfL8Q4igb";
						} //NO AGUACATE
					}
				} else if (cliente.getNogustos().contains("Claras")){
					if (cliente.getNogustos().contains("Huevos")) {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1r76Xt-OIjL9wXr7nrAY76KPGwKWZ4WJi";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1sOYAyz0nkeseqGuJl4Ty3MRS-mB-WGsA";
						} else {
							folderIdDie2="1aK6r6TnEADFUWxK5q5GRHL1PlAd34fQC";
						} //NO CLARAS NO HUEVOS
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1jnHn67a2uureQ7zik6xlMZCScyRduulB";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1MXoBYahkPhaSLTMirJ5iz178YyppkU2Z";
						} else {
							folderIdDie2="1c3mB8VmEZqM8sRLRICNyR4H88HHRdA1c";
						} //NO CLARAS
					}
				} else if (cliente.getNogustos().contains("Pescado")) {
					if (cliente.getNogustos().contains("Verdura")) {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="1bm79POOiusDC7o_HRI29wfB59sbDE3x6";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1_kTRk7djrQ7txbgH1aYhmVoTJVtwqeDH";
						} else {
							folderIdDie2="1kJemvUf_TRAKGdFalUnA3YZtHriqmEAv";
						} //NO PESCADO NO VERDURA
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie2="13qmXEsdP9rVowt3q_4gePWCKxpo8hpAy";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie2="1uEnkdY7MnA1jQk1G4qXJVH9L32b02sFT";
						} else {
							folderIdDie2="1IvWQyRtm3PJpE7j8N_Sca_V0hxeLeg8u";
						} //NO PESCADO
					}
				} else if (cliente.getNogustos().contains("Verdura")) {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie2="1xJcr8F_XL0ZjsLDfKe0A5sCzjzg71C9f";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie2="17rBbZ3TaOpi8mybr0ljBk82hFjBl9KiR";
					} else {
						folderIdDie2="15dQhaBn2ae9vBGay3eECv7s-D_QbSAD9";
					} //NO VERDURA
				}
			} else {  //MUJER--------------------------------------------------------------------------------------------------------
				if (cliente.getNogustos().contains("NINGUNA")) {
					if (cliente.getPeso().equals(Peso.menos70)) {
						folderIdDie2="1pxJxFeGCrF3lz7c_2ssjjph-cY7zP9dC";
					} else {
						folderIdDie2="1J-TSbQxqSc0QkhYoFdjDy_n0H4Q5Vi9K";
					} //BASICA
				}  else if (cliente.getNogustos().contains("Soy VEGAN@")) {
					if (cliente.getPeso().equals(Peso.menos70)) {
						folderIdDie2="1GC6CYYfcD8685DM3v6TEDnHVAKpNHD7W";
					} else {
						folderIdDie2="1tits5lkPmRtJjRnCNkK1aBbUnAwgkolA";
					} //VEGANA
				} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
					if (cliente.getPeso().equals(Peso.menos70)) {
						folderIdDie2="13_A_PHZ5PBvWhlhqn3v40JB69lBK89kO";
					} else {
						folderIdDie2="15PJYJ9n9g5RkwZaiAg-ziTUUTGY4OE7p";
					} //VEGETARIANO
				} else if (cliente.getNogustos().contains("Lactosa")) {
					if (cliente.getNogustos().contains("Gluten")) {
						if (cliente.getNogustos().contains("Verdura")) {
							if (cliente.getPeso().equals(Peso.menos70)) {
								folderIdDie2="1epdiAc4AX0oxbUbj_NC9adpALd9kp0bL";
							} else {
								folderIdDie2="1fmxdGLxVxRk-AUst-nOf-SLgNbzAlUNt";
							} //NO LACTOSA NO GLUTEN NO VERDURA 
						} else {
							if (cliente.getPeso().equals(Peso.menos70)) {
								folderIdDie2="1ezCvNygcMHXz3PXp7uz5tk2R70eHb-rh";
							} else {
								folderIdDie2="1sr_233t7q5QAgqOeJ2rWxGUGL2dOxMKB";
							} //NO LACTOSA NO GLUTEN
						}
					} else if(cliente.getNogustos().contains("Claras")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="18WacdJRswcAcbvC7j0NQ-2PAvSxxdGlQ";
						} else {
							folderIdDie2="1A6IisZqnKQVfmwSA7hNK9woet5rkKHIk";
						} //NO LACTOSA NO CLARAS
					} else if(cliente.getNogustos().contains("Huevos")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="18-0pUeqG9UmragSV-D1inLynlpn-DzU3";
						} else {
							folderIdDie2="1_ThM-abYNyTa8xXCsADM7gq7SSFLD5_U";
						} //NO LACTOSA NO HUEVOS
					} else if(cliente.getNogustos().contains("Pescado")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="104WpIWbODRyGEwgGvGxUWF572cgSff7e";
						} else {
							folderIdDie2="1lR5pIRfIOR5D6lcXIEFl-mSuEOemD98H";
						} //NO LACTOSA NO PESCADO
					} else if(cliente.getNogustos().contains("Verdura")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1ZGTHMULMXqTHXO7dNEaj8I4CekpdG4S8";
						} else {
							folderIdDie2="1J1ZJIg45dxIyOownlvnSTZmdlkji02rk";
						} //NO LACTOSA NO VERDURA
					} else {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1Cx-HGfd9PC-S-M-SUVm7Wlab_98VmuYy";
						} else {
							folderIdDie2="1iDAeYxsiGM6PFu1DAdWSzvcKmqxUlU4f";
						} //NO LACTOSA
					}
				} else if (cliente.getNogustos().contains("Frutos secos")) {
					if (cliente.getNogustos().contains("Claras")) {
						if (cliente.getNogustos().contains("Aguacate")) {
							if (cliente.getPeso().equals(Peso.menos70)) {
								folderIdDie2="1zrpEPePgzXj6Jh_PyqzX8fbcJhYhOQ_q";
							} else {
								folderIdDie2="1t9iUyK02pozD43hOQ4jdROV_F4__xGSK";
							} //NO FRUTOS SECOS NO CLARAS NO AGUACATE
						} else {
							if (cliente.getPeso().equals(Peso.menos70)) {
								folderIdDie2="1OOuP7ygzt628IOAalYoxL_uDTB_otB_B";
							} else {
								folderIdDie2="1MorQ2MiiiL_xsYVt3BinP1PaZSxOAhxQ";
							} //NO FRUTOS SECOS NO CLARAS
						}
					} else if (cliente.getNogustos().contains("Lactosa")){
						if (cliente.getNogustos().contains("Claras")) {
							if (cliente.getPeso().equals(Peso.menos70)) {
								folderIdDie2="1-XovXtL2Uw6xUJOeMZbSRQ7H9d-_aQHQ";
							} else {
								folderIdDie2="13TSCj_Z1nzSQ7_xoHYq_Y-CqyYD4FTXS";
							} //NO FRUTOS SECOS NO LACTOSA NO CLARAS
						} else {
							if (cliente.getPeso().equals(Peso.menos70)) {
								folderIdDie2="1dZATwJ_P0EY8taqTkhHrjDtn9l-9Mhvi";
							} else {
								folderIdDie2="1kgXjltQFr4WBdiqab6fB2bNTPxGArX8Q";
							} //NO FRUTOS SECOS NO LACTOSA 
						}
					} else if (cliente.getNogustos().contains("Gluten")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="14TkpKxdr0NRxGlwaZ0opNfFRsPnjmr79";
						} else {
							folderIdDie2="19ZhrgsN9lcGfZuKNJiDZjmW-1ikULm8m";
						} // NO FRUTOS SECOS NO GLUTEN
					} else {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1NJLXXfB_HdI6ElkCJd5eaLm9BAPZTiL5";
						} else {
							folderIdDie2="1skfdK3StJWYhxXY6K15OCkEC2emxT8xG";
						} //NO FRUTOS SECOS
					}
				} else if (cliente.getNogustos().contains("Gluten")) {
					if (cliente.getNogustos().contains("Claras")) {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="15IYbzJ_feyWXH4vWqNmdpVyCgMmT9qNk";
						} else {
							folderIdDie2="1i9T3wgEAcWtlLs2KuVYzu3oc3HNXpc9i";
						} // NO GLUTEN NO CLARAS
					} else if (cliente.getNogustos().contains("Aguacate")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1VQ-psOd_G5_Ta2CxR1iLHT4Y35WStzrY";
						} else {
							folderIdDie="1p7e1kewxH7IrPdX47J7NcLhUfs__kToV";
						} // NO GLUTEN NO AGUACATE
					} else if (cliente.getNogustos().contains("Huevos")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1EL_02ZkcjdBQfwVHpHGT2kioZ6Rodw8s";
						} else {
							folderIdDie2="1s2WC03rgNhjYtOaK_BNVlHwt3C90mpjN";
						} // NO GLUTEN NO HUEVOS
					} else if (cliente.getNogustos().contains("Pescado")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1EbXTU07Gw1ERh1hnZ12uhCMWjmZ6bKUC";
						} else {
							folderIdDie2="1whLtIBKgErGrd10KOmgCFHxPuWOMULJT";
						} // NO GLUTEN NO PESCADO
					} else if (cliente.getNogustos().contains("Verdura")){
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1ORKDaDANyEmpk_O_NZSUEuOVZRHQwCNU";
						} else {
							folderIdDie2="1wCmJyv3MdRqXX2kQyhw_lHUM3kB1K7j7";
						} // NO GLUTEN NO VERDURA
					} else {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1Z-XdrcaWJcJYHCYmxLVkHtWlXH1muq-q";
						} else {
							folderIdDie2="1HMD5b6GJCUN4esFBDPyuTxzLMwt1gSyA";
						} //NO GLUTEN
					}
				} else if (cliente.getNogustos().contains("Aguacate")) {
					if (cliente.getNogustos().contains("Claras")) {
							if (cliente.getPeso().equals(Peso.menos70)) {
								folderIdDie2="1OJflSlZJkj3S-gHZvFxREcnK3Fxt_u1J";
							} else {
								folderIdDie2="16Z6Ulak1Lw9v70JNNYrYZeRwDGDBfG1D";
							} // NO AGUACATE NO CLARAS
						
					} else {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1nelrgeJOcghediX_N95IhqJticUQqWhR";
						} else {
							folderIdDie2="1MqpjYMskPyhYL4zh8M8mNYrlF9dbpHw0";
						} //NO AGUACATE
					}
				} else if (cliente.getNogustos().contains("Claras")){
					if (cliente.getNogustos().contains("Huevos")) {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1q1Dc1vB7NFl7E5bQKy0MdGYlxs1ZDCPo";
						} else {
							folderIdDie2="1PHi5XPrht3u4ZO852JAoqWtknBpOOd3k";
						} //NO CLARAS NO HUEVOS
					} else {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1IHy_j2pTwaUISn-fjMyoIyf3uoebyrBG";
						} else {
							folderIdDie2="1m-5_iJgsQ_ENpuzDQs10oaJE8GgHwNjO";
						} //NO CLARAS
					}
				} else if (cliente.getNogustos().contains("Pescado")) {
					if (cliente.getNogustos().contains("Verdura")) {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1NMfwZNFAi99ACSZCrVp88FsPIqdAoQjS";
						} else {
							folderIdDie2="1l-M1YLuoutIooKGYfAQw2MYx1YvI688O";
						} //NO PESCADO NO VERDURA
					} else {
						if (cliente.getPeso().equals(Peso.menos70)) {
							folderIdDie2="1K1vNAs5AqId5YyYldYrTImqDYiOo2ACw";
						} else {
							folderIdDie2="1-XLs05KyrD5SOW0FsGdjbH0whWaFzXnR";
						} //NO PESCADO
					}
				} else if (cliente.getNogustos().contains("Verdura")) {
					if (cliente.getPeso().equals(Peso.menos70)) {
						folderIdDie2="1V16gs3eoPW8ahkcS-nKbkxO7f-UK5SmZ";
					} else {
						folderIdDie2="1bsyN9BpdGoTvT0phv_phpxNJJeSGKTar";
					} //NO VERDURA
				}
			} 
		} else { //SUPERAVIT-----------------------------------------------------------------------
			
			if (cliente.getNogustos().contains("NINGUNA")) {
				folderIdDie2="1l2DveIYmvh_jAfkBlMPVxNnFIj_Yzb79"; //BASICA
			} else if (cliente.getNogustos().contains("Aguacate")) {
				if (cliente.getNogustos().contains("Claras")) {
					if (cliente.getNogustos().contains("Lactosa")) {
						folderIdDie2="1l2cwFwA9hEQkVdfv2tqHPyZ-_Z46QbKw"; //NO AGUACATE NO CLARAS NO LACTOSA
					} else {
						folderIdDie2="1nT7sIm5igxvqkX1NJRaPEqaBmNR7JASA"; // NO AGUACATE NO CLARAS
					}
				} else {
					folderIdDie2="1G8ByVmPw_3_Ch9st72j4uQ1CedTyj6Xb"; //NO AGUACATE
				}
			} else if (cliente.getNogustos().contains("Soy VEGAN@")) {
				folderIdDie2="1sMmIIM_gbq4hEpgT6q6sXOaWEKH29-tu"; //VEGANA
			} else if (cliente.getNogustos().contains("Soy VEGETARIAN@")) {
				folderIdDie2="1gMXMlPpTcRtgR2XF3Fc7nBM3PEZx2KhS"; //VEGETARIANO
			} else if (cliente.getNogustos().contains("Lactosa")) {
				if (cliente.getNogustos().contains("Gluten")) {
					if (cliente.getNogustos().contains("Verdura") || cliente.getNogustos().contains("Pescado")) {
						folderIdDie2="1WTM2BBRkmUM_bOJheNWO-8JhyRoMYN7I"; //NO LACTOSA NO GLUTEN NO VERDURA NO PESCADO
					} else {
						folderIdDie2="1url--W-H2bBlvGzL-TpNZdOGxjyj8YC4"; //NO LACTOSA NO GLUTEN
					}
				} else {
					folderIdDie2="1mT84QCLHHTMAFUlA7yoimGyqeU4xBfXy"; //NO LACTOSA
				}
			} else if (cliente.getNogustos().contains("Frutos secos")) {
				if (cliente.getNogustos().contains("Claras")) {
					if (cliente.getNogustos().contains("Aguacate")) {
						folderIdDie2="1xGKSjZnZGA4dTO1cJLDBr39n1UzpMwcJ"; //NO FRUTOS SECOS NO CLARAS NO AGUACATE
					} else {
						folderIdDie2="1jJ1h4mkDpeaNb65kqYL-JNWa8KVaqnQE"; //NO FRUTOS SECOS NO CLARAS
					}
				} else if (cliente.getNogustos().contains("Lactosa")){
					if (cliente.getNogustos().contains("Claras")) {
						folderIdDie2="1R1dulbv0QzJAyY05uFof52GyEhlYdV-r"; //NO FRUTOS SECOS NO LACTOSA NO CLARAS
					} else {
						folderIdDie2="1jveLSysA-e6T_Td4qjjAwQgUm4-OiffH"; //NO FRUTOS SECOS NO LACTOSA 
					}
				} else {
					folderIdDie2="1feIcM_-m5Xq2558QOn2gouF-_8nmbQAb"; //NO FRUTOS SECOS
				}
			} else if (cliente.getNogustos().contains("Gluten")) {
				folderIdDie2="1LPn5R1y-TKYcqP6ylQdaHUudN57oXZJf"; //NO GLUTEN
			} else if (cliente.getNogustos().contains("Claras")){
				if (cliente.getNogustos().contains("Huevos")) {
					folderIdDie2="1xsKypiM7BLCMotHITEdofcLmuEYGpLOA"; //NO CLARAS NO HUEVOS
				} else {
					folderIdDie2="1o3_VZ5_8qftzHnh1aWjRF4v6RhpuKx-K"; //NO CLARAS
				}
			} else if (cliente.getNogustos().contains("Pescado")) {
				if (cliente.getNogustos().contains("Verdura")) {
					folderIdDie2="15A4oeHrOFeNms5NS6HZK64xItnaaylBd"; //NO PESCADO NO VERDURA
				} else {
					folderIdDie2="19soDHErdKuIPRhTvzrI1NchJxkQZhef7"; //NO PESCADO
				}
			} else if (cliente.getNogustos().contains("Verdura")) {
				folderIdDie2="1cZj1y0zrXvMmHxIh5CVDLWs95kQuyOqm"; //NO VERDURA
			}
		}
		
		java.io.File destinationFolderA2 = new java.io.File(selectedFolder.getAbsolutePath() + "/"+clienteN+" Dieta 12 MESES");
		if (!destinationFolderA2.exists()) {
		    destinationFolderA2.mkdirs();
		}
		
		if (destinationFolderA2.exists()) {
		    // borrar todos los archivos de la carpeta
		    java.io.File[] archivos2 = destinationFolderA2.listFiles();
		    for (java.io.File archivo : archivos2) {
		        archivo.delete();
		    }
		}
		
		String query2;
		if (cliente.getObjetivo().equals(Objetivo.definicion)) {
			query2 = "mimeType='application/pdf' and '" + folderIdDie2 + "' in parents";
		} else {
			query2 = "mimeType='application/vnd.openxmlformats-officedocument.wordprocessingml.document' and '" + folderIdDie2 + "' in parents";
		}
		
		Files.List request2 = drive.files().list().setQ(query2).setOrderBy("title asc");
		
//		int archivosDD;
//		if (cliente.getObjetivo().equals(Objetivo.definicion)) {    //PARA DESCARGAR X DIETAS SEGUN LOS MESES
//			if (cliente.getMesesentrenados()>=12) {
//				archivosDD=0;
//			} else {
//				archivosDD=cliente.getMesesentrenados();
//			}
//		} else {
//			if (cliente.getMesesentrenados()>5) {
//				archivosDD=0;
//			} else {
//				archivosDD=cliente.getMesesentrenados();
//			}
//		}
		
		int ind2 = 1;
		try {
			int totalArchivos = request2.execute().getItems().size();
//		    HiloProgreso progressThread = new HiloProgreso(totalArchivos);
//		    progressThread.start();
		    
			for (File file : request2.execute().getItems()) {
			
//			if (archivosDD!=0) {  // para desdcargar archivo a partir de X
//				archivosDD--;
//				ind++;
//				continue;
//				
//			} else {
			
			String fileName = file.getTitle();
			String cabezerafile;
			String term2;
			
			if (cliente.getObjetivo().equals(Objetivo.definicion)) {
				int cantidad = 4;
			    cabezerafile = fileName.substring(0, fileName.length() - cantidad);
			    term2 = ".pdf";
			} else {
				int cantidad = 5;
			    cabezerafile = fileName.substring(0, fileName.length() - cantidad);
			    term2 = ".docx";
			}
			
			
			
		    OutputStream outputStream = new FileOutputStream(new java.io.File(destinationFolderA2, fileName+"-"+cliente.getNombreC()+term2));
//		    if (cliente.getObjetivo().equals(Objetivo.volumen)) {
//				outputStream=convertDocxToPdf(new FileInputStream(new java.io.File(destinationFolderA, "Dieta Mes "+ind+" "+cliente.getNombreC()+term)));
//			} else {
//
//			}
		    drive.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
		    outputStream.flush();
		    outputStream.close();
		   	ind++;
//		   	progressThread.archivoDescargado();
			}
//			progressThread.join();
//			}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "El archivo de dieta no existe.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		System.out.println("Las DIETAS VIEJAS han sido descargadas con exito");
		
		
		//AHORA DESCARGAMOS LOS ENTRENAMIENTOS--------------------------------------------------------------------------------------------------------------------------------
		String folderIdEntr = "";
		
		if (cliente.getNivel().equals(Nivel.avanzado)) {                                //SI EN AVANZADO LOS ENTRENAMIENTO NUEVOS
		if (cliente.getSexo().equals(Sexo.hombre)) { /////////// HOMBRE
			if (cliente.getLugar().equals(Lugar.gym)) {  //GYM
				if (cliente.getDiasentreno()==4) {
					folderIdEntr = "1NI19fd1EiwWYBXLPjst-9jpEg9tArBWD";
				} else if (cliente.getDiasentreno()==5) {
					folderIdEntr = "1OY524Nfjt_ZsC8O7Igfkc2mUlRJ16G4o";
				} else {
					folderIdEntr = "1BOY8YnsUUo8lf3UP9cOYxoCLX0xkpEb9";
				}
			} else if (cliente.getLugar().equals(Lugar.casa_material)) {  //CON MANCUERNAS Y BARRAS
				if (cliente.getDiasentreno()==4) {
					folderIdEntr = "13ko4o7TbZqYz521lm5aXvunOwvPIGglH";
				} else if (cliente.getDiasentreno()==5) {
					folderIdEntr = "1u4T8vCsFlDV0tkK74TzKcpf-kvi2CoNP";
				} else {
					folderIdEntr = "1Jv3PCagFDe7Vb3hMd2c4lqktWruQ8SKo";
				}
			} else {      // GOMAS
				if (cliente.getDiasentreno()==4) {
					folderIdEntr = "1DY4T88gcLJb_xlIn1oPydNpYQo2u86iJ";
				} else if (cliente.getDiasentreno()==5) {
					folderIdEntr = "1C1nuFH3U2_j1I6XRYaGIpOWx4yvE25YQ";
				} else {
					folderIdEntr = "1i7N_QMaG_yAah_faNK18vWMuoIgFPisv";
				}
			}
			
		} else {     //////////////////////////////////////////  MUJER
			if (cliente.getLugar().equals(Lugar.gym)) {  //GYM
				if (cliente.getDiasentreno()==4) {
					folderIdEntr = "1_T8bTOW8137kVFvSqxy29cP0cdA12JSB";
				} else if (cliente.getDiasentreno()==5) {
					folderIdEntr = "1NtpN71HXtTyzAGcgANVBBuw_TezqupEe";
				} else {
					folderIdEntr = "16G1RMKtO23AhT6_W4v4dBz-LhS6uP1Bp";
				}
			} else if (cliente.getLugar().equals(Lugar.casa_material)) {  //CON MANCUERNAS Y BARRAS
				if (cliente.getDiasentreno()==4) {
					folderIdEntr = "1C8w6xRtv8K65kpL8V-d1XnW4Y3S_icSS";
				} else if (cliente.getDiasentreno()==5) {
					folderIdEntr = "1A6xiR2cZeKuHBVJ5K1k5nZ6JFGs_Z13g";
				} else {
					folderIdEntr = "1T-C4SpSc-r8Ye4jPWKTGw4Lye4WIjg56";
				}
			} else {      // GOMAS
				if (cliente.getDiasentreno()==4) {
					folderIdEntr = "15kwrLIi9_yuh-83hJ0nx2cZTaSLXcwvf";
				} else if (cliente.getDiasentreno()==5) {
					folderIdEntr = "1webw5pAG2rx0k7oHbwbmlNY6MK_mNjwy";
				} else {
					folderIdEntr = "1MDcVwwuRuWOUl13osb6T2CSbRDNMzpSV";
				}
			}
		}
		} else {
		
		if (cliente.getSexo().equals(Sexo.hombre)) {
			if (cliente.getNivel().equals(Nivel.principiante)) {
				if (cliente.getDiasentreno()==2) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1QfUsS-0sj0m5Mhu2376jJVh6kyfkMAXe";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1LDno6fcXrzsAEX2blBQr5w6j8132WzF-";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1jWDsEWSug9bvepvUM-iKMVTiHYbTre8X";
					} else {
						folderIdEntr="1vSThko8SZS7_ihV6Wv1UEZW4aR0aGmKU";
					}
				} else if (cliente.getDiasentreno()==3) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1ZM-VCq-rnsinIMEv9oU9zgdfR7fWy7JN";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1eQik1_S7SksFwHcruKyW1T5Hyu18TGQD";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1aoV84vPPOi04Wu5dA5uXOZ776Q3CqwId";
					} else {
						folderIdEntr="1COwaKPnLKzOANRPZjylIJ6BWuI8Um5gi";
					}
				} else if (cliente.getDiasentreno()==4) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1acD5-KPPCB-CGVp_5AP5kDnFICVknPb_";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1TJB1A1oyvSxCvcmebtbJHAmT9CrUoPy_";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1n0M0gc-90SbT8rsiyFofQy8Rjcr5QUN-";
					} else {
						folderIdEntr="1NpGPxTbhM6jpxH_nj5De8RXsXbNbO8qC";
					}
				} else {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1nAvYhw2a5QTtU2-H5Mysd8rwuSXRvQaX";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1E08oHSD3UNyoj0_zVOEDb7rIMzeRzldi";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1m0loUg6mfe7tOX5gxaFPkEMDCaDvUpQu";
					} else {
						folderIdEntr="12qH7-5qACeGnbR3I7cuVx6xdIHVo-lz2";
					}
				}
			} else if (cliente.getNivel().equals(Nivel.intermedio)) {
				if (cliente.getDiasentreno()==2) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1ClggZ5JWABhv8FMcncdtzCpJGh6hgStE";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1ijkAwj7_FNjSMEMsgkQMVL8R08mIMooD";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="13jvZThnkUAD6U5aS33NersDPVN6tUjcM";
					} else {
						folderIdEntr="1PCUcHv-pOEYTkkE7nazpKsJOaykdTGXL";
					}
				} else if (cliente.getDiasentreno()==3) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1CRP-rOzCNro8Bg0wHLHx5fJ9gS6j3SQI";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1D2rr0CmVDtsS6DSrNWxJvHxZvN-jmjKQ";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1wlbjIFjFi3ldDZpELR5uwHRR1vJ1MqOa";
					} else {
						folderIdEntr="1_2NAJVf5_nGm_fjQkV2s5YzzH_rMrItw";
					}
				} else if (cliente.getDiasentreno()==4) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1FmEYqQ2onCj7UFhax__YAOE7fEW1l3Ov";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1JtGUjSxXm3TCy_eRk-l8SzkklYPDqygE";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1Nla71Y4Lo75gp2_vyaF6k39Z-hVJjqqQ";
					} else {
						folderIdEntr="1kz4fMWKPXuizObYao53wN-NsxfGlwUSk";
					}
				} else {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1EcjWufWyhltMOkGy5WY-VXWixDXImYiw";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1bVjcTZa4Gf1akPjUzxEwqlOK2Hc_Vexf";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1IrRGswKeVrzQU4geLOTo4GAGBohORaW4";
					} else {
						folderIdEntr="1jlgIReRcgI3Tex1oCyNP2Yai1rwRX4Ee";
					}
				}
			} else {
				if (cliente.getDiasentreno()==2) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1Fl3LMHO09KmpA2z-LMzlgOnhVUVuL7O1";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1xIX7Jdb4Jxh2sLmgn2oCJKf7KKDQcGb_";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1gZux_vXQVx4exp7DAmeRgn7r3TRVb5Kb";
					} else {
						folderIdEntr="1eIS6wSN4omg0zmniye1tKIis209gBk-i";
					}
				} else if (cliente.getDiasentreno()==3) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1Yun3yqQa2GkXkwN6-smjUgUPcG1b0WgA";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1atXlNSEatsPeeYC2t_HxuNnPJbHlv-iw";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1FW9NN3yR7ZHflOZ2DNrdpnRW9kh12QMg";
					} else {
						folderIdEntr="1NK9tkVjn4ZcPdwmSAQz5zJ_4u1GneSBC";
					}
				} else if (cliente.getDiasentreno()==4) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1ZoivhEWi44K-KZBXoSecSA1Hr8tLnR5Q";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1-tYqgpdySQJjh57BI6LXBLzn9spalTkP";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="18HVbBrdzVR0Kq5ohZKzXxcS9PjNgXRVZ";
					} else {
						folderIdEntr="1MChOpVmYnfmucok0MQGD4nFhP9s56T06";
					}
				} else {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1QpnWxsJf_X-zYCEnyJBF72-K63ruGrGI";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1UnAhlu0itaxUjs_QEq1Yq6jpaq20paEO";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1CgFFbA9nLghiOUqrCwaLJtfXQgNfdn6l";
					} else {
						folderIdEntr="14oFoq13q5tjiXlHFdoNHrjHwHwcGmIlL";
					}
				}
			}
		} else {
			if (cliente.getNivel().equals(Nivel.principiante)) {
				if (cliente.getDiasentreno()==2) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1gd7VG3JfWQZWzF5bNCFJqNXCUFqXfHgW";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="15Sy8jTKX0LjAiI0r_KpmzttyHi9oJEnn";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1vcsJPXLS_GaWJeCA3r8LIQAWp5RKNTrv";
					} else {
						folderIdEntr="1aVJO_YDkKTQ7yxZNl_ZTO6oL9np1JvEx";
					}
				} else if (cliente.getDiasentreno()==3) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1dRYHopSkP7yLKI67wBtj9-Oc3_60uYSP";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1J9MWLUSt5VPf5t9xFNvybiE45ecdgbcB";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1QWaFMV8I55uK8KcDbbnWQNLT55KxK3JN";
					} else {
						folderIdEntr="1O3Y1Z9ROhT0aDEteKzZO_VX4sAX2M9ju";
					}
				} else if (cliente.getDiasentreno()==4) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="18tp97ja7jMaePrbz7-CrWLreNxb2yPAD";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1wpT_MjGZE0qQMGiOkn22vtu9ZCxHJI0c";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1tX6Okt7bHlnTW3bAEmPsxjX3lKPsFQwV";
					} else {
						folderIdEntr="1Yu97CgAMV6RMRPBgrjLB4bgc0G453ws9";
					}
				} else {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1fJkWEcaOKg7IQiaTbR8rRCqf_PJwpmxF";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="14El42mfmIVQLUg6eA25sjAy4ogtsVXcB";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1dwKv6CrWgqkHmgEZCOwQ1xy3lrUub738";
					} else {
						folderIdEntr="18IMSHjsbMsh_Voe021wR5ZtgbVFqJNRd";
					}
				}
			} else if (cliente.getNivel().equals(Nivel.intermedio)) {
				if (cliente.getDiasentreno()==2) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1vqwKAgT5WbVP6e2tWqFUxqbNBbYQgQ4Q";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1N77IVdNfZaMi02R41mXhoFnnGGu7_yP-";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1Dfk9dRIBEKutlMxmwMoI9fBJxyiGwk_c";
					} else {
						folderIdEntr="1jfDlow4--oho4iWblXM1ctkuuN679f_g";
					}
				} else if (cliente.getDiasentreno()==3) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1EeirREjQ0-GzeE5uFkHeRhCaCBqb5EmV";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1dnUQpLboOWS68R9w6zu3isbkFEIzrFgw";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1FV_dJ4h8FKnO3BSssGXFfYyvl6CTCFLg";
					} else {
						folderIdEntr="19TJ3whIQhuAwLnMVB61ZJundYuwOpHVw";
					}
				} else if (cliente.getDiasentreno()==4) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1c_6yibLysqFpjeIjon2jnNgFOQqJQpX5";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1vY4rIJo0Uceq7yk2CdDIEo0K-GMBxVWE";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1eSa7XTmBM4NbacJZKmEIG8Ez7zk_Po0-";
					} else {
						folderIdEntr="13kYjNt2z50WTWx7k3CNgT4EVJ0cE5sXx";
					}
				} else {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1hnJYrshH_gYaTKV7lLNuP49HepFjmSVs";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1YN47RHxU74ZZwE-UTT9dTlL14aLX3bI6";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1GNrTGMun2QYTYoSdXK8-o--wdL4PXeVz";
					} else {
						folderIdEntr="1SDhjHwu7KS7GhHt-O2lvKnxn7hyZG5fR";
					}
				}
			} else {
				if (cliente.getDiasentreno()==2) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1377uJwBD1Q8GL75QxLb4PRkQJl1NT3Yo";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1IMgqofM6Ae624Lciqo9icxy0w3gh0azQ";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="18_cM-0pCrY6GFKDzfD8xU4ROHJGDdGtd";
					} else {
						folderIdEntr="1P06Ry1hz6Zc2xh4-yd9BWAAdSitGQSN8";
					}
				} else if (cliente.getDiasentreno()==3) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1_KKb6ocQHBhHf7277_IiTbmls0y2Ca_x";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1DF4lOZMF2Usqmzy_WGxtCZLXsGLakNxu";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="1J_1XxrwS_1V4ae9MhEb9ZV_HqQW8OQuy";
					} else {
						folderIdEntr="1COkZzI87Ln-iIBdolyoUp2FRTiHScww2";
					}
				} else if (cliente.getDiasentreno()==4) {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1awZBCiryUtaxuSXbVztsf1bXUqUrgUPw";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1J6H96cc8N5F3lhGaaKEaOn4Jd1fQvBI8";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="12uAORFIOjMaBzZKkF5JPv7LyZxImwmSO";
					} else {
						folderIdEntr="1K9wY2efNLpFhmuKThIHi6zzqgwDHvDuH";
					}
				} else {
					if (cliente.getLesion().equals(Lesion.ninguna)) {
						folderIdEntr="1PFfbZhHn8i6dSMxAlsrvJuOWwnaLOhwV";
					} else if (cliente.getLesion().equals(Lesion.lumbar)) {
						folderIdEntr="1V67bLcJs3VfCoSiU1HKWzB7DsiGkWnsv";
					} else if (cliente.getLesion().equals(Lesion.rodilla)) {
						folderIdEntr="11cTXi5aE34FTatmNXlceRNcV0QqJKlp1";
					} else {
						folderIdEntr="1hd6pXdc4PGGE4vqZUVKZSz_XI07jT9uB";
					}
				}
			}
		}
		}
		
		
		
		java.io.File destinationFolderD = new java.io.File(selectedFolder.getAbsolutePath() + "/"+clienteN+"Entr");
		if (!destinationFolderD.exists()) {
		    destinationFolderD.mkdirs();
		}
		
		if (destinationFolderD.exists()) {
		    // borrar todos los archivos de la carpeta
		    java.io.File[] archivos = destinationFolderD.listFiles();
		    for (java.io.File archivo : archivos) {
		        archivo.delete();
		    }
		}

		String query1 = "mimeType='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' and '" + folderIdEntr + "' in parents";
//		Files.List request = drive.files().list().setQ(query1);
		Files.List request3 = drive.files().list().setQ(query1).setOrderBy("title asc");
		
		int archivosD = 0;
		
//		if (cliente.getMesesentrenados()>11) {
//			archivosD=0;
//		} else {
//			archivosD=cliente.getMesesentrenados();
//		}
		
		try {
			
			int totalArchivos = request3.execute().getItems().size();
//		    HiloProgreso progressThread = new HiloProgreso(totalArchivos);
//		    progressThread.start();
		    
			for (File file : request3.execute().getItems()) {
			if (archivosD!=0) {
				archivosD--;
				continue;
					
			} else {
		    String fileName = file.getTitle();
		    int cantidad = 5;
		    String cabezerafile = fileName.substring(0, fileName.length() - cantidad);
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    drive.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
		    byte[] byteArray = outputStream.toByteArray();
		    FileOutputStream fos = new FileOutputStream(new java.io.File(destinationFolderD, cabezerafile+" "+cliente.getNombreC()+".xlsx"));
		    fos.write(byteArray);
		    fos.close();
//		    progressThread.archivoDescargado();
			}
		}
//			progressThread.join();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "El archivo de entrenamiento existe.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		System.out.println("Los ENTRENAMIENTOS han sido descargados con exito");
		} finally {
            // Cierra la conexión a Google Drive
            driveService = null;
            if (credential != null) {
                credential = null;
            }
        }
	}

	

	
	
}


