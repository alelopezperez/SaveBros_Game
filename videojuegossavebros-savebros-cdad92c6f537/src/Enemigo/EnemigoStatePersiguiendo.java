package Enemigo;

import Heroe.*;

public class EnemigoStatePersiguiendo implements EnemigoState {
	
	private Enemigo context;
	
	public EnemigoStatePersiguiendo (Enemigo en){
		context = en;
	}
	
	public void recibirDano() {
		
	}

	
	public void atacar() {
		
	}

	
	public boolean seguir(Heroe heroe, Enemigo enemigo) {
		
		int heroeX = heroe.getXPosn();
		int heroeY = heroe.getYPosn();
		int enemigoX = enemigo.getXPosn();
		int enemigoY = enemigo.getYPosn();
		int difX = Math.abs(heroeX - enemigoX);
		int difY = Math.abs(heroeY - enemigoY);
		if(difX > heroe.getWidth() - 12 || difY > heroe.getHeight() - 12){
			if (difX > difY){
				if(heroeX > enemigoX){
					if(enemigo.getXStep() <= 0)
						moveRight();  
				}else {
					if(enemigo.getXStep() >= 0)
						moveLeft();
				}
			}else{
				if(heroeY > enemigoY){
					if(enemigo.getYStep() <= 0)
						moveDown();
				}else{
					if(enemigo.getYStep() >= 0)
						moveUp();
				}
			}
		}else{
			return true;
		}
		return false;
	}
	
	@Override
	public void moveLeft() {
		context.setStep(-context.getVelocidad(), 0);
	    
		context.setImage("Z"+ context.getTipo() +"L");
		context.loopImage(context.period, Enemigo.DURATION);   // cycle through the leftBugs2 images
	}

	@Override
	public void moveRight() {
		context.setStep(context.getVelocidad(), 0);
		
		context.setImage("Z"+ context.getTipo() +"R");
		context.loopImage(context.period, Enemigo.DURATION);  // cycle through the images
	}

	@Override
	public void moveUp() {
		context.setStep(0, -context.getVelocidad()); 
		
		context.setImage("Z"+ context.getTipo() +"U");
		context.loopImage(context.period, Enemigo.DURATION);  // cycle through the images
	}

	@Override
	public void moveDown() {
		context.setStep(0, context.getVelocidad()); 
		
		context.setImage("Z"+ context.getTipo() +"D");
		context.loopImage(context.period, Enemigo.DURATION);  // cycle through the images
	}

}
