package serviciodietas.main;

import java.io.IOException;
import java.security.GeneralSecurityException;

import serviciodietas.gui.VentanaInicio;
import serviciodietas.gui.VentanaPrincipal;

public class Main {

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		
		VentanaInicio v = new VentanaInicio();
		v.setVisible(true);
	}

}
