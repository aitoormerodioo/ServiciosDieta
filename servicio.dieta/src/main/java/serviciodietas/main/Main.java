package serviciodietas.main;

import java.io.IOException;
import java.security.GeneralSecurityException;

import serviciodietas.gui.VentanaPrincipal;

public class Main {

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		
		VentanaPrincipal v = new VentanaPrincipal();
		v.setVisible(true);
	}

}
