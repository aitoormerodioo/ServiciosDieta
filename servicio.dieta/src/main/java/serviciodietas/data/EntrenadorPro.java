package serviciodietas.data;

public class EntrenadorPro extends Usuario{
	
	private String codigoPro;
	
	public EntrenadorPro(String nombre_usu, String contraseña,String codigoPro ) {
		super(nombre_usu, contraseña);
		this.codigoPro=codigoPro;
	}

	@Override
	public void mostrarInformacion() {
		
		System.out.println("Tipo de usuario: EntrenadorPro");
        System.out.println("Nombre: " + getNombre_usu());
        System.out.println("Contrasenya: " + getContraseña());
        System.out.println("CodigoPro: " + this.codigoPro);
		
	}

	
}
