package Heroe;

import java.util.Random;

public class HeroeStateSiguiendo implements HeroeState{
	
	private Heroe context;
	
	public HeroeStateSiguiendo(Heroe p){
		context = p;
	}

	public void pagarLiberacion() {
		
	}

	public void pagarPuerta() {
		
	}

	public void cambioDePrincipal() {
		
	}

	public void personajeMuerto() {
		
	}

	public void atacado() {
		
	}

	public void disparar() {
		
	}

	public void caminar() {
		
	}

	public void correr() {
		
	}

	public void colision() {
		
	}

	@Override
	public void moveLeft() {
		context.setStep(-context.getVelocidad(), 0);
	    //Set all to false
		context.setNotMoving();
	    //Set left true
		context.setMovingLeft(true);
		context.setImage("H"+ context.getTipo() +"L");
		context.loopImage(context.period, Heroe.DURATION);   // cycle through the leftBugs2 images
	}

	@Override
	public void moveRight() {
		context.setStep(context.getVelocidad(), 0);
		//Set all to false
		context.setNotMoving();
	    //Set Right true
		context.setMovingRight(true);
		context.setImage("H"+ context.getTipo() +"R");
		context.loopImage(context.period, Heroe.DURATION);  // cycle through the images
	}

	@Override
	public void moveUp() {
		context.setStep(0, -context.getVelocidad()); 
		//Set all to false
		context.setNotMoving();
	    //Set up true
		context.setMovingUp(true);
		context.setImage("H"+ context.getTipo() +"U");
		context.loopImage(context.period, Heroe.DURATION);  // cycle through the images
	}

	@Override
	public void moveDown() {
		context.setStep(0, context.getVelocidad()); 
		//Set all to false
		context.setNotMoving();
	    //Set Down true
		context.setMovingDown(true);
		context.setImage("H"+ context.getTipo() +"D");
		context.loopImage(context.period, Heroe.DURATION);  // cycle through the images
	}

	@Override
	public void followLider(Heroe hActivo) {
		Random rand = new Random();
	    int hActX = hActivo.getXPosn();
		int hActY = hActivo.getYPosn();
		int hX = context.getXPosn();
		int hY = context.getYPosn();
		int difX = Math.abs(hActX - hX);
		int difY = Math.abs(hActY - hY);
		int maxX = rand.nextInt(85 - 45) + 45;
		int maxY = rand.nextInt(85 - 45) + 45;
		if(difX > maxX || difY > maxY){
		  if (difX > difY){
			  if(hActX > hX){
				  if(context.dx <= 0 || context.dx == 1){
					  context.moveRight(); 
					  //context.setStep(context.dx -= 1*(int)Math.signum(context.dx), context.dy -= 1*(int)Math.signum(context.dy));
				  }
			  }else{
				  if(context.dx >= 0 || context.dx == -1){
					  context.moveLeft();  
					  //context.setStep(context.dx -= 1*(int)Math.signum(context.dx), context.dy -= 1*(int)Math.signum(context.dy));
				  }
			  }
		  }else{
			  if(hActY > hY){
				  if(context.dy <= 0 || context.dy == 1){
					  context.moveDown(); 
					  //context.setStep(context.dx -= 1*(int)Math.signum(context.dx), context.dy -= 1*(int)Math.signum(context.dy));
				  }
			  }else{
				  if(context.dy >= 0 || context.dy == -1){
					  context.moveUp();
					  //context.setStep(context.dx -= 1*(int)Math.signum(context.dx), context.dy -= 1*(int)Math.signum(context.dy));
				  }
			  }
		  }
		  //context.setStep(context.dx -= 1*(int)Math.signum(context.dx), context.dy -= 1*(int)Math.signum(context.dy));
		}else{
			//context.stayStill();
			//context.setStep(0, 0);
			//int random = rand.nextInt(10);
			//if(random == 5){
			if(difX < 45 || difY < 45){
				context.setStep(1*(int)Math.signum(context.dx), 1*(int)Math.signum(context.dy));
			}
				//context.stayStill();
			//}
		}
		
	}

	@Override
	public void shootUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shootDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shootLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shootRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recarga() {	    
		if(!context.timerRecarga.isRunning()){
			context.timerRecarga.start(); // Go go go!
			context.timerRecarga.setRepeats(false); // Only execute once
		}		
	}

	@Override
	public void powerVeloz() {
		// TODO Auto-generated method stub
		
	}
}
