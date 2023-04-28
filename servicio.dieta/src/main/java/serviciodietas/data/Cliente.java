package serviciodietas.data;

public class Cliente {

		private String nombreC;
		private String numeroT;
		private String email;
		private Sexo sexo;
		private Peso peso;
		private String nogustos;
		private int diasentreno;
		private int mesesentrenados;
		private Nivel nivel;
		private Lesion lesion;
		private Objetivo objetivo;
		private String entrenador;
		
		public Cliente(String nombreC, String numeroT, String email,Sexo sexo, Peso peso, String nogustos, int diasentreno,
				int mesesentrenados, Nivel nivel, Lesion lesion, Objetivo objetivo, String entrenador) {
			super();
			this.nombreC = nombreC;
			this.numeroT = numeroT;
			this.email = email;
			this.sexo = sexo;
			this.peso = peso;
			this.nogustos = nogustos;
			this.diasentreno = diasentreno;
			this.mesesentrenados = mesesentrenados;
			this.nivel = nivel;
			this.lesion = lesion;
			this.objetivo = objetivo;
			this.entrenador = entrenador;
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

		@Override
		public String toString() {
			return "Cliente [nombreC=" + nombreC + ", numeroT=" + numeroT + ", email=" + email + ", sexo=" + sexo
					+ ", peso=" + peso + ", nogustos=" + nogustos + ", diasentreno=" + diasentreno
					+ ", mesesentrenados=" + mesesentrenados + ", nivel=" + nivel + ", lesion=" + lesion + ", objetivo="
					+ objetivo + ", entrenador=" + entrenador + "]";
		}

		
		
		
		
		
		
		
		
		
		
		
		
		
}
