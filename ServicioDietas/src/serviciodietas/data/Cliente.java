package serviciodietas.data;

public class Cliente {

		private String nombreC;
		private String vegano;
		
		
		public Cliente(String nombreC, String vegano) {
			super();
			this.nombreC = nombreC;
			this.vegano = vegano;
		}


		public String getNombreC() {
			return nombreC;
		}


		public void setNombreC(String nombreC) {
			this.nombreC = nombreC;
		}


		public String isVegano() {
			return vegano;
		}


		public void setVegano(String vegano) {
			this.vegano = vegano;
		}
		
		
		
		
}
