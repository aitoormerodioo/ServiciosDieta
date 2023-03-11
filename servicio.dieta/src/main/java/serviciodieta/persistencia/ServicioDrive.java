package serviciodieta.persistencia;

import java.io.ByteArrayOutputStream;
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
import serviciodietas.data.Nivel;
import serviciodietas.data.Objetivo;
import serviciodietas.data.Peso;
import serviciodietas.data.Sexo;

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
	
	String accessToken = credential.getAccessToken();
	String refreshToken = credential.getRefreshToken();
	
	GoogleCredential googleCredential = new GoogleCredential.Builder()
	        .setTransport(GoogleNetHttpTransport.newTrustedTransport())
	        .setJsonFactory(JacksonFactory.getDefaultInstance())
	        .setClientSecrets(clienteSecret)
	        .build()
	        .setAccessToken(accessToken)
	        .setRefreshToken(refreshToken)
	        .createScoped(scopes);
	
	if (googleCredential.getExpiresInSeconds() != null && googleCredential.getExpiresInSeconds() <= 60) {
        // If the access token has expired or will expire within 1 minute, then refresh it
        googleCredential.refreshToken();
    }
	return googleCredential;
	
	
	}
	
	public static Drive getDriveService() throws GeneralSecurityException, IOException {
		
		Credential credencial = authorize();
		
		return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credencial).setApplicationName(APPLICATION_NAME).build();
		
	}
	
	public static void descargarArchivos(Cliente cliente) throws GeneralSecurityException, IOException {
		
		Drive drive = serviciodieta.persistencia.ServicioDrive.getDriveService();
		
		String clienteN= cliente.getNombreC().replaceAll("\\s+", "");
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccione la carpeta de descarga");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = fileChooser.showOpenDialog(null);
		
		java.io.File selectedFolder = null;
		if (option == JFileChooser.APPROVE_OPTION) {
		    selectedFolder = fileChooser.getSelectedFile();
		}
		
		//PRIMERO DESCARGAMOS LAS DIETAS
		String folderIdDie = "";
		
		if (cliente.getObjetivo().equals(Objetivo.definicion)) {
			if (cliente.getNogustos().contains("NINGUNA")) {
				if (cliente.getPeso().equals(Peso.mas90)) {
					folderIdDie="1DFQ8HefsXmLFk2UnNLTWrm-Nj485HIpT";
				} else if (cliente.getPeso().equals(Peso.entre70y90)) {
					folderIdDie="1DFQ8HefsXmLFk2UnNLTWrm-Nj485HIpT";
				} else {
					folderIdDie="1Asp20Am8JuvK4taRx2sJh-qwYmsXuuCI";
				} //BASICA
			} else if (cliente.getNogustos().contains("Aguacate")) {
				if (cliente.getNogustos().contains("Claras")) {
					if (cliente.getNogustos().contains("Lactosa")) {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie="1JCOGqc8R2m2O6Xqh-006qXj65mFI5lNC";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie="1OM9q_6IydSHQ00Qu3z-6jk8zjM4pvAhZ";
						} else {
							folderIdDie="1IScUY3iizs6N7XCWbnUUszsFNfHx7XkI";
						} //NO AGUACATE NO CLARAS NO LACTOSA
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie="1L0hQk6PRqDBbkSQiHJfpmu3JrKSA7rka";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie="1DD-LaVj6Lcl6jLa-X-hMNlQZ7EmqM_oy";
						} else {
							folderIdDie="15BcdwY1sZlvAT_sO-huKrDf2imuuWjO_";
						} // NO AGUACATE NO CLARAS
					}
				} else {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie="1joedIbx6oxUwGzGLaoZkvWchgmlDcKan";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie="1sPxhjTpvtmaUjp_6m830cVfh6tqD92FR";
					} else {
						folderIdDie="1LHcUxegwA7gKfDXJGsxNDkVJfL8Q4igb";
					} //NO AGUACATE
				}
			} else if (cliente.getNogustos().contains("Soy Vegan@")) {
				if (cliente.getPeso().equals(Peso.mas90)) {
					folderIdDie="1rjpG5jm39RishNFII7Rt2q9JLUA7VVrq";
				} else if (cliente.getPeso().equals(Peso.entre70y90)) {
					folderIdDie="1iwukT6spXqVZ_kdyZj_vr3wmx4SdaRpN";
				} else {
					folderIdDie="1lix_YH5OJgh7oFdaTSwj0yAid6Mf62P1";
				} //VEGANA
			} else if (cliente.getNogustos().contains("Soy Vegetarian@")) {
				if (cliente.getPeso().equals(Peso.mas90)) {
					folderIdDie="1sFjXSTcgcxFeAyx8fA9Mb9ZkeicsNW9H";
				} else if (cliente.getPeso().equals(Peso.entre70y90)) {
					folderIdDie="1heGNYTi_-M82Uc6YDVEvW2U4AFGZ0dxg";
				} else {
					folderIdDie="1CnlCZmrKGP89dFmuNBSpY_jEmaxuinqx";
				} //VEGETARIANO
			} else if (cliente.getNogustos().contains("Lactosa")) {
				if (cliente.getNogustos().contains("Gluten")) {
					if (cliente.getNogustos().contains("Verdura") || cliente.getNogustos().contains("Pescado")) {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie="1KD6PS2N76zSJFa5hMiBKg7UiSCZvnHIQ";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie="1QYbChg5w6s8Pqqsv6__kpsWCR-ocFeva";
						} else {
							folderIdDie="1Y407j_PraGcoVbIxs6b_Ey990ulJq-Wc";
						} //NO LACTOSA NO GLUTEN NO VERDURA NO PESCADO
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie="1dO4vLkCtzalHM1c0YOIFgsCTwXYca8hu";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie="1MFlUOjs7xJRcwuqsAphDlisdZgf--FtD";
						} else {
							folderIdDie="1-NNZiilGQmH-0bOAJYjK5AmmTj9MWIZ5";
						} //NO LACTOSA NO GLUTEN
					}
				} else {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie="1FR0Ytg3dQPpuh_-7VK-9xCXv0IJAV7Y6";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie="1-WpkFXFeTIGqe3xJedDoRxoyBabzTYzk";
					} else {
						folderIdDie="1G3dBjdd_tEbjDVv1upt8q-rmhFSqCE4I";
					} //NO LACTOSA
				}
			} else if (cliente.getNogustos().contains("Frutos secos")) {
				if (cliente.getNogustos().contains("Claras")) {
					if (cliente.getNogustos().contains("Aguacate")) {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie="11OOZVxv-NNyMyYAPME2qKEbyna-KTKuk";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie="1v_PC5sE0sz4cIyItYLlYCbnh5M3eNGgP";
						} else {
							folderIdDie="1aBDszvP213UND7NFZmzCOGdqdqnMVSNi";
						} //NO FRUTOS SECOS NO CLARAS NO AGUACATE
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie="1HP5ceipxKz4viNbKJpdclp7fkI7Ogxnc";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie="1VmgsF_EraVfHt--VC1H4hLgJrOs7g-Hg";
						} else {
							folderIdDie="1zusjwpm1ecaaZdA-X9HsKrJWLFgfqJmj";
						} //NO FRUTOS SECOS NO CLARAS
					}
				} else if (cliente.getNogustos().contains("Lactosa")){
					if (cliente.getNogustos().contains("Claras")) {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie="132A2nhRNmL3jNdztT3GCppufQDhOfq_r";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie="1uV8Ik34M6ii7_naJd374hgOTUCRoJsDH";
						} else {
							folderIdDie="1Z9NHHWyaOq8uBUun1VNL0rCnt21yXW32";
						} //NO FRUTOS SECOS NO LACTOSA NO CLARAS
					} else {
						if (cliente.getPeso().equals(Peso.mas90)) {
							folderIdDie="1Ryul-j1aCdHFnM2A1OTxWJk_U8nS56Yp";
						} else if (cliente.getPeso().equals(Peso.entre70y90)) {
							folderIdDie="1_DFZ6AgIhbsj70Gmoft68-jUF9QP0zof";
						} else {
							folderIdDie="1EbRbbzWhVYB3p_qdGimU7MJZk8o5nOva";
						} //NO FRUTOS SECOS NO LACTOSA 
					}
				} else {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie="1IqzBOWb4CRtuAx1J3cFV0vM5FZMEX4Ro";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie="1xh9lhonPPWBtDG7ylLzSse9NzEhM8Ht9";
					} else {
						folderIdDie="1mJUtNrSUhzO4uJRKXVkQk9vIZ_HuBsmV";
					} //NO FRUTOS SECOS
				}
			} else if (cliente.getNogustos().contains("Gluten")) {
				if (cliente.getPeso().equals(Peso.mas90)) {
					folderIdDie="1pUKSmLtuHThlOpA31B9gMkWC_CxCf_2U";
				} else if (cliente.getPeso().equals(Peso.entre70y90)) {
					folderIdDie="10FOx7KRGphaPxvUiIwF5aq8e93iu2KSb";
				} else {
					folderIdDie="1QEJuOhlxK9iaDqtDRmLY2J5mw_nJTzcC";
				} //NO GLUTEN
			} else if (cliente.getNogustos().contains("Claras")){
				if (cliente.getNogustos().contains("Huevos")) {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie="1r76Xt-OIjL9wXr7nrAY76KPGwKWZ4WJi";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie="1sOYAyz0nkeseqGuJl4Ty3MRS-mB-WGsA";
					} else {
						folderIdDie="1aK6r6TnEADFUWxK5q5GRHL1PlAd34fQC";
					} //NO CLARAS NO HUEVOS
				} else {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie="1jnHn67a2uureQ7zik6xlMZCScyRduulB";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie="1MXoBYahkPhaSLTMirJ5iz178YyppkU2Z";
					} else {
						folderIdDie="1c3mB8VmEZqM8sRLRICNyR4H88HHRdA1c";
					} //NO CLARAS
				}
			} else if (cliente.getNogustos().contains("Pescado")) {
				if (cliente.getNogustos().contains("Verdura")) {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie="1bm79POOiusDC7o_HRI29wfB59sbDE3x6";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie="1_kTRk7djrQ7txbgH1aYhmVoTJVtwqeDH";
					} else {
						folderIdDie="1kJemvUf_TRAKGdFalUnA3YZtHriqmEAv";
					} //NO PESCADO NO VERDURA
				} else {
					if (cliente.getPeso().equals(Peso.mas90)) {
						folderIdDie="13qmXEsdP9rVowt3q_4gePWCKxpo8hpAy";
					} else if (cliente.getPeso().equals(Peso.entre70y90)) {
						folderIdDie="1uEnkdY7MnA1jQk1G4qXJVH9L32b02sFT";
					} else {
						folderIdDie="1IvWQyRtm3PJpE7j8N_Sca_V0hxeLeg8u";
					} //NO PESCADO
				}
			} else if (cliente.getNogustos().contains("Verdura")) {
				if (cliente.getPeso().equals(Peso.mas90)) {
					folderIdDie="1xJcr8F_XL0ZjsLDfKe0A5sCzjzg71C9f";
				} else if (cliente.getPeso().equals(Peso.entre70y90)) {
					folderIdDie="17rBbZ3TaOpi8mybr0ljBk82hFjBl9KiR";
				} else {
					folderIdDie="15dQhaBn2ae9vBGay3eECv7s-D_QbSAD9";
				} //NO VERDURA
			}
		} else { //SUPERAVIT-----------------------------------------------------------------------
			
			if (cliente.getNogustos().contains("NINGUNA")) {
				folderIdDie="1l2DveIYmvh_jAfkBlMPVxNnFIj_Yzb79"; //BASICA
			} else if (cliente.getNogustos().contains("Aguacate")) {
				if (cliente.getNogustos().contains("Claras")) {
					if (cliente.getNogustos().contains("Lactosa")) {
						folderIdDie="1l2cwFwA9hEQkVdfv2tqHPyZ-_Z46QbKw"; //NO AGUACATE NO CLARAS NO LACTOSA
					} else {
						folderIdDie="1pFf25DwKwm4HQCYCCnmJkBI6aUdkZQ--"; // NO AGUACATE NO CLARAS
					}
				} else {
					folderIdDie="1G8ByVmPw_3_Ch9st72j4uQ1CedTyj6Xb"; //NO AGUACATE
				}
			} else if (cliente.getNogustos().contains("Soy Vegan@")) {
				folderIdDie="1sMmIIM_gbq4hEpgT6q6sXOaWEKH29-tu"; //VEGANA
			} else if (cliente.getNogustos().contains("Soy Vegetarian@")) {
				folderIdDie="1gMXMlPpTcRtgR2XF3Fc7nBM3PEZx2KhS"; //VEGETARIANO
			} else if (cliente.getNogustos().contains("Lactosa")) {
				if (cliente.getNogustos().contains("Gluten")) {
					if (cliente.getNogustos().contains("Verdura") || cliente.getNogustos().contains("Pescado")) {
						folderIdDie="1WTM2BBRkmUM_bOJheNWO-8JhyRoMYN7I"; //NO LACTOSA NO GLUTEN NO VERDURA NO PESCADO
					} else {
						folderIdDie="1WTM2BBRkmUM_bOJheNWO-8JhyRoMYN7I"; //NO LACTOSA NO GLUTEN
					}
				} else {
					folderIdDie="1mT84QCLHHTMAFUlA7yoimGyqeU4xBfXy"; //NO LACTOSA
				}
			} else if (cliente.getNogustos().contains("Frutos secos")) {
				if (cliente.getNogustos().contains("Claras")) {
					if (cliente.getNogustos().contains("Aguacate")) {
						folderIdDie="1xGKSjZnZGA4dTO1cJLDBr39n1UzpMwcJ"; //NO FRUTOS SECOS NO CLARAS NO AGUACATE
					} else {
						folderIdDie="1jJ1h4mkDpeaNb65kqYL-JNWa8KVaqnQE"; //NO FRUTOS SECOS NO CLARAS
					}
				} else if (cliente.getNogustos().contains("Lactosa")){
					if (cliente.getNogustos().contains("Claras")) {
						folderIdDie="1R1dulbv0QzJAyY05uFof52GyEhlYdV-r"; //NO FRUTOS SECOS NO LACTOSA NO CLARAS
					} else {
						folderIdDie="1jveLSysA-e6T_Td4qjjAwQgUm4-OiffH"; //NO FRUTOS SECOS NO LACTOSA 
					}
				} else {
					folderIdDie="1feIcM_-m5Xq2558QOn2gouF-_8nmbQAb"; //NO FRUTOS SECOS
				}
			} else if (cliente.getNogustos().contains("Gluten")) {
				folderIdDie="1LPn5R1y-TKYcqP6ylQdaHUudN57oXZJf"; //NO GLUTEN
			} else if (cliente.getNogustos().contains("Claras")){
				if (cliente.getNogustos().contains("Huevos")) {
					folderIdDie="1xsKypiM7BLCMotHITEdofcLmuEYGpLOA"; //NO CLARAS NO HUEVOS
				} else {
					folderIdDie="1o3_VZ5_8qftzHnh1aWjRF4v6RhpuKx-K"; //NO CLARAS
				}
			} else if (cliente.getNogustos().contains("Pescado")) {
				if (cliente.getNogustos().contains("Verdura")) {
					folderIdDie="15A4oeHrOFeNms5NS6HZK64xItnaaylBd"; //NO PESCADO NO VERDURA
				} else {
					folderIdDie="1C6jhVnpe2l3n8KhJxAJx3b0syRZA4cG1"; //NO PESCADO
				}
			} else if (cliente.getNogustos().contains("Verdura")) {
				folderIdDie="1cZj1y0zrXvMmHxIh5CVDLWs95kQuyOqm"; //NO VERDURA
			}
		}
		
		java.io.File destinationFolderA = new java.io.File(selectedFolder.getAbsolutePath() + "/ArchivosClientes"+clienteN+"Dieta");
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
		
		String query;
		if (cliente.getObjetivo().equals(Objetivo.definicion)) {
			query = "mimeType='application/pdf' and '" + folderIdDie + "' in parents";
		} else {
			query = "mimeType='application/vnd.openxmlformats-officedocument.wordprocessingml.document' and '" + folderIdDie + "' in parents";
		}
		
		Files.List request1 = drive.files().list().setQ(query).setOrderBy("title asc");
		
		int archivosDD;
		if (cliente.getObjetivo().equals(Objetivo.definicion)) {
			if (cliente.getMesesentrenados()==12) {
				archivosDD=0;
			} else {
				archivosDD=cliente.getMesesentrenados();
			}
		} else {
			if (cliente.getMesesentrenados()>3) {
				archivosDD=0;
			} else {
				archivosDD=cliente.getMesesentrenados();
			}
		}
		
		int ind = 1;
		try {
			for (File file : request1.execute().getItems()) {
			
			if (archivosDD!=0) {
				archivosDD--;
				ind++;
				continue;
				
			} else {
			
			String fileName = file.getTitle();
			String cabezerafile;
			String term;
			if (cliente.getObjetivo().equals(Objetivo.definicion)) {
				int cantidad = 4;
			    cabezerafile = fileName.substring(0, fileName.length() - cantidad);
			    term = ".pdf";
			} else {
				int cantidad = 5;
			    cabezerafile = fileName.substring(0, fileName.length() - cantidad);
			    term = ".docx";
			}
			
			
		    OutputStream outputStream = new FileOutputStream(new java.io.File(destinationFolderA, "Dieta Mes "+ind+" "+cliente.getNombreC()+term));
		    drive.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
		    outputStream.flush();
		    outputStream.close();
		   	ind++;
			}
			}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "El archivo de dieta no existe.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		System.out.println("Las DIETAS han sido descargadas con exito");
		
		
		//AHORA DESCARGAMOS LOS ENTRENAMIENTOS
		String folderIdEntr = "";
		
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
		
		
		
		java.io.File destinationFolderD = new java.io.File(selectedFolder.getAbsolutePath() + "/ArchivosClientes"+clienteN+"Entr");
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
		Files.List request = drive.files().list().setQ(query1);
		
		int archivosD = cliente.getMesesentrenados();
		
		try {
			for (File file : request.execute().getItems()) {
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
			}
		}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "El archivo de dieta existe.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		System.out.println("Los ENTRENAMIENTOS han sido descargados con exito");
	}
	
}


