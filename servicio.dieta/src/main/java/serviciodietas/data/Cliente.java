package serviciodietas.data;

public class Cliente {

		private String nombreC;
		private String numeroT;
		private String email;
		private String insta;
		private Sexo sexo;
		private Peso peso;
		private int kilos;
		private String nogustos;
		private int diasentreno;
		private int mesesentrenados;
		private Nivel nivel;
		private Lesion lesion;
		private Objetivo objetivo;
		private String entrenador;
		private Lugar lugar;
		private int numerocomidas;
		private float TMB;
		
		public Cliente(String nombreC, String numeroT, String email, String insta, Sexo sexo, Peso peso,int kilos, String nogustos,
				int diasentreno, int mesesentrenados, Nivel nivel, Lesion lesion, Objetivo objetivo, String entrenador,
				Lugar lugar, int numerocomidas) {
			super();
			this.nombreC = nombreC;
			this.numeroT = numeroT;
			this.email = email;
			this.insta = insta;
			this.sexo = sexo;
			this.peso = peso;
			this.kilos=kilos;
			this.nogustos = nogustos;
			this.diasentreno = diasentreno;
			this.mesesentrenados = mesesentrenados;
			this.nivel = nivel;
			this.lesion = lesion;
			this.objetivo = objetivo;
			this.entrenador = entrenador;
			this.lugar = lugar;
			this.numerocomidas = numerocomidas;
			
			//Calcular el TMB
			
			if (this.sexo.equals(sexo.hombre)) {
				TMB = 370+(18*this.kilos);
			} else {
				TMB = 370+(15*this.kilos);
			}

			float mult;
			
			if (this.diasentreno==5) {
				mult=1.6f;
			} else if (this.diasentreno==4){
				mult=1.5f;
				
			} else {
				mult=1.25f;
			}
			
			if (this.objetivo.equals(objetivo.definicion)) {
				TMB = TMB*mult-500;
			} else {
				TMB = TMB*mult+300;
			}
		}

		public String getNombreC() {
			return nombreC;
		}

		public void setNombreC(String nombreC) {
			this.nombreC = nombreC;
		}

		public String getNumeroT() {
			return numeroT;
		}

		public void setNumeroT(String numeroT) {
			this.numeroT = numeroT;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
		
		public String getInsta() {
			return insta;
		}

		public void setInsta(String insta) {
			this.insta = insta;
		}

		public Sexo getSexo() {
			return sexo;
		}

		public void setSexo(Sexo sexo) {
			this.sexo = sexo;
		}

		public Peso getPeso() {
			return peso;
		}

		public void setPeso(Peso peso) {
			this.peso = peso;
		}
		
		public int getKilos() {
			return kilos;
		}

		public void setKilos(int kilos) {
			this.kilos = kilos;
		}

		public String getNogustos() {
			return nogustos;
		}

		public void setNogustos(String nogustos) {
			this.nogustos = nogustos;
		}

		public int getDiasentreno() {
			return diasentreno;
		}

		public void setDiasentreno(int diasentreno) {
			this.diasentreno = diasentreno;
		}

		public int getMesesentrenados() {
			return mesesentrenados;
		}

		public void setMesesentrenados(int mesesentrenados) {
			this.mesesentrenados = mesesentrenados;
		}

		public Nivel getNivel() {
			return nivel;
		}

		public void setNivel(Nivel nivel) {
			this.nivel = nivel;
		}

		public Lesion getLesion() {
			return lesion;
		}

		public void setLesion(Lesion lesion) {
			this.lesion = lesion;
		}

		public Objetivo getObjetivo() {
			return objetivo;
		}

		public void setObjetivo(Objetivo objetivo) {
			this.objetivo = objetivo;
		}

		public String getEntrenador() {
			return entrenador;
		}

		public void setEntrenador(String entrenador) {
			this.entrenador = entrenador;
		}

		public Lugar getLugar() {
			return lugar;
		}

		public void setLugar(Lugar lugar) {
			this.lugar = lugar;
		}

		public int getNumerocomidas() {
			return numerocomidas;
		}

		public void setNumerocomidas(int numerocomidas) {
			this.numerocomidas = numerocomidas;
		}

		public float getTMB() {
			return TMB;
		}

		public void setTMB(float tMB) {
			TMB = tMB;
		}

		@Override
		public String toString() {
			return "Cliente [nombreC=" + nombreC + ", numeroT=" + numeroT + ", email=" + email + ", insta=" + insta
					+ ", sexo=" + sexo + ", peso=" + peso + ", kilos=" + kilos + ", nogustos=" + nogustos
					+ ", diasentreno=" + diasentreno + ", mesesentrenados=" + mesesentrenados + ", nivel=" + nivel
					+ ", lesion=" + lesion + ", objetivo=" + objetivo + ", entrenador=" + entrenador + ", lugar="
					+ lugar + ", numerocomidas=" + numerocomidas + ", TMB=" + TMB + "]";
		}

		
		
		
		
		
		
		

		
		
		
		
		
		
		
		
		
		
		
		
		
}
