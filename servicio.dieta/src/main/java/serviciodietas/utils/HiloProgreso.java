package serviciodietas.utils;

public class HiloProgreso extends Thread {
	

	    private int totalArchivos;
	    private int archivosDescargados;

	    public HiloProgreso(int totalArchivos) {
	        this.totalArchivos = totalArchivos;
	        this.archivosDescargados = 0;
	    }

	    @Override
	    public void run() {
	        while (archivosDescargados < totalArchivos) {
	            int porcentaje = (int) (((double) archivosDescargados / totalArchivos) * 100);
	            System.out.print("\rDescargando archivos: " + porcentaje + "%");

	            try {
	                Thread.sleep(500); // Ajusta el tiempo de actualización de la barra de progreso
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }

	        System.out.println("\rDescarga completa");
	    }

	    public void archivoDescargado() {
	        archivosDescargados++;
	    }
	
}
