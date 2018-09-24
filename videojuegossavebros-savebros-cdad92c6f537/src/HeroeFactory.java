import Heroe.*;
import cargadorImagenes.ImagesLoader;

public class HeroeFactory {

    private Heroe h;
    private static HeroeFactory instance;
    
    public static synchronized HeroeFactory getInstance(){
	if(instance == null){
	    instance = new HeroeFactory();
	}
	return instance;
    }
    
    public Heroe getHeroeObject(int x, int y, int pW, int pH, ImagesLoader imsLd, int period, int tipo, boolean activo){
		try {
		    //forName utiliza el Paquete y nombre del archivo
			int numBalas;
			int velocidad;
			String nameImage;
		    switch(tipo){
				case 1: //pistolero
					numBalas = 9;
					velocidad = 6;
					if(activo){
						nameImage = "H"+ tipo +"D";
					}else{
						nameImage = "H"+ tipo +"DPrisionero";
					}
					h = new Heroe(x, y, pW, pH, imsLd, nameImage, period, tipo, numBalas, velocidad, activo);
					break;
				case 2: //metralletero
					numBalas = 30;
					velocidad = 5;
					if(activo){
						nameImage = "H"+ tipo +"DStill";
					}else{
						nameImage = "H"+ tipo +"DPrisionero";
					}
					h = new Heroe(x, y, pW, pH, imsLd, nameImage, period, tipo, numBalas, velocidad, activo);
					break;
				case 3: //escopetero
					numBalas = 5;
					velocidad = 4;
					if(activo){
						nameImage = "H"+ tipo +"DStill";
					}else{
						nameImage = "H"+ tipo +"DPrisionero";
					}
					h = new Heroe(x, y, pW, pH, imsLd, nameImage, period, tipo, numBalas, velocidad, activo);
					break;
				case 4: //snipero
					numBalas = 6;
					velocidad = 4;
					if(activo){
						nameImage = "H"+ tipo +"DStill";
					}else{
						nameImage = "H"+ tipo +"DPrisionero";
					}
					h = new Heroe(x, y, pW, pH, imsLd, nameImage, period, tipo, numBalas, velocidad, activo);
					break;
				default: break;
				//s = (Shape) Class.forName("Rectangle").newInstance();
			}
		    //s = (Empleado) Class.forName("entity."+tipo).newInstance();
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} 
		return h;
    }

}
