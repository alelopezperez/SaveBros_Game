import Heroe.*;
import cargadorImagenes.ImagesLoader;

public class PowerUpFactory {

    private PowerUps p;
    private static PowerUpFactory instance;
    
    public static synchronized PowerUpFactory getInstance(){
	if(instance == null){
	    instance = new PowerUpFactory();
	}
	return instance;
    }
    
    public PowerUps getPowerUpsObject(int x, int y, int pW, int pH, ImagesLoader imsLd, int tipo){
		try {
		    //forName utiliza el Paquete y nombre del archivo
			//String nameImage;
		    switch(tipo){
				case 1: //nuke
					p = new PowerUps(x, y, pW, pH, imsLd, tipo);
					break;
				case 2: //flash
					p = new PowerUps(x, y, pW, pH, imsLd, tipo);
					break;
				case 3: //invencible
					p = new PowerUps(x, y, pW, pH, imsLd, tipo);
					break;
				default: break;
			}
		} catch (Exception e1) {
		    e1.printStackTrace();
		} 
		return p;
    }

}
