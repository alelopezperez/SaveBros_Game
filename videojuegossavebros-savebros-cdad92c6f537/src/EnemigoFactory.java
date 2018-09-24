import Enemigo.*;
import cargadorImagenes.ImagesLoader;

public class EnemigoFactory {

    private Enemigo ene;
    private static EnemigoFactory instance;
    
    public static synchronized EnemigoFactory getInstance(){
	if(instance == null){
	    instance = new EnemigoFactory();
	}
	return instance;
    }
    
    public Enemigo getEnemigoObject(int x, int y, int pW, int pH, ImagesLoader imsLd, int period, int tipo, int vida){
		try {
			int velocidad;
			String nameImage;
		    switch(tipo){
				case 1: //Enemigo Normal
					velocidad = 2;
					vida = 3;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 2: //Enemigo Veloz
					velocidad = 3;
					vida = 2;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 3: //Enemigo Pesado
					velocidad = 1;
					vida = 5;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 4: //Enemigo Pesado
					velocidad = 1;
					vida = 5;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 5: 
					velocidad = 2;
					vida = 3;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 6: 
					velocidad = 3;
					vida = 1;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 7: 
					velocidad = 3;
					vida = 2;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 8: 
					velocidad = 3;
					vida = 1;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 9: 
					velocidad = 3;
					vida = 2;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				case 10: 
					velocidad = 4;
					vida = 1;
					nameImage = "Z" + tipo + "D";
					ene = new Enemigo(x, y, pW, pH, imsLd, nameImage, period, tipo, velocidad, vida);
					break;
				default: break;
				//s = (Shape) Class.forName("Rectangle").newInstance();
			}
		    //s = (Empleado) Class.forName("entity."+tipo).newInstance();
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} 
		return ene;
    }

}
