package serviciodietas.data;

public abstract class Usuario {
	
	protected String nombre_usu;
	protected String contraseña;
	
	public Usuario(String nombre_usu, String contraseña) {
        this.nombre_usu = nombre_usu;
        this.contraseña = contraseña;
    }
	
	
	
	public String getNombre_usu() {
		return nombre_usu;
	}



	public void setNombre_usu(String nombre_usu) {
		this.nombre_usu = nombre_usu;
	}



	public String getContraseña() {
		return contraseña;
	}



	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}



	public abstract void mostrarInformacion();
}
