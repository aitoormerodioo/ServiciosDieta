package serviciodietas.data;

public class EntrenadorPrincipiante extends Usuario{
	
	private static int idPri=1;
	
	public EntrenadorPrincipiante(String nombre_usu, String contraseña) {
		super(nombre_usu, contraseña);
		this.idPri= idPri;
		idPri++;
		
	}

	@Override
	public void mostrarInformacion() {
		
		System.out.println("Tipo de usuario: EntrenadorPrincipiante");
        System.out.println("Nombre: " + getNombre_usu());
        System.out.println("Contrasenya: " + getContraseña());
		
	}

}
