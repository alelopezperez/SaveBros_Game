package Heroe;

import cargadorImagenes.ImagesLoader;
import cargadorSonidos.ClipsLoader;

public class Bala extends Sprite{
	
	private int posX;
	private int posY;
	private boolean sniper;
	private int velocidad;
	private static String name="bullet";
	
	public Bala(int x, int y, int w, int h, ImagesLoader imsLd) {
		super(x, y, w, h, imsLd, name + "D");
		velocidad=10;
		sniper = false;
	}
	
	public boolean isSniper() {
		return sniper;
	}

	public void setSniper(boolean sniper) {
		this.sniper = sniper;
	}

	public void setImage(){
		super.setImage(name);
	}
	
	
	
	public void moveLeft() {	
		
		this.setStep(-velocidad, 0);
	    //Set all to false
		this.setNotMoving();
	    //Set left true
		this.setMovingLeft(true);
		this.setImage(name + "L");	
		
	}
	
	public void moveRight() {
		this.setStep(velocidad, 0);
		//Set all to false
		this.setNotMoving();
	    //Set Right true
		this.setMovingRight(true);
		this.setImage(name + "R");
		
	}
	
	public void moveUp() {
		
		this.setStep(0,-velocidad); 
		//Set all to false
		this.setNotMoving();
	    //Set up true
		this.setMovingUp(true);
		this.setImage(name + "U");
		
	}	
	
	public void moveDown() {
		
		this.setStep(0,velocidad); 
		//Set all to false
		this.setNotMoving();
	    //Set Down true
		this.setMovingDown(true);
		this.setImage(name  + "D");
		
	}
	
	
	
	
	
}