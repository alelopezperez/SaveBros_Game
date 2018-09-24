package Heroe;

public class HeroeStateActivo implements HeroeState{
	
	private Heroe context;
	
	public HeroeStateActivo(Heroe p){
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
	    //Set all to false
		context.setNotMoving();
		if(context.getYStep() != 0){
			int vy = (context.getVelocidad() * (int)Math.signum(context.dy)) - 1*(int)Math.signum(context.dy);
			context.setStep(-context.getVelocidad() + 1, vy);
			if(context.getYStep() > 0)
				context.setMovingDown(true);
			if(context.getYStep() < 0)
				context.setMovingUp(true);
		}else{
			context.setStep(-context.getVelocidad(), 0);	
		}
	    //Set left true
		context.setMovingLeft(true);
		context.setImage("H"+ context.getTipo() +"L");
		context.loopImage(context.period, Heroe.DURATION);   // cycle through the leftBugs2 images
	}

	@Override
	public void moveRight() {
		//Set all to false
		context.setNotMoving();
		if(context.getYStep() != 0){
			int vy = (context.getVelocidad() * (int)Math.signum(context.dy)) - 1*(int)Math.signum(context.dy);
			context.setStep(context.getVelocidad() - 1, vy);
			if(context.getYStep() > 0)
				context.setMovingDown(true);
			if(context.getYStep() < 0)
				context.setMovingUp(true);
		}else{
			context.setStep(context.getVelocidad(), 0);	
		}
	    //Set Right true
		context.setMovingRight(true);
		context.setImage("H"+ context.getTipo() +"R");
		context.loopImage(context.period, Heroe.DURATION);  // cycle through the images
	}

	@Override
	public void moveUp() {
		//Set all to false
		context.setNotMoving();
		if(context.getXStep() != 0){
			int vx = (context.getVelocidad() * (int)Math.signum(context.dx)) - 1*(int)Math.signum(context.dx);
			context.setStep(vx, -context.getVelocidad() + 1); 
			if(context.getXStep() > 0)
				context.setMovingRight(true);
			if(context.getXStep() < 0)
				context.setMovingLeft(true);
		}else{
			context.setStep(0, -context.getVelocidad()); 	
		}
	    //Set up true
		context.setMovingUp(true);
		context.setImage("H"+ context.getTipo() +"U");
		context.loopImage(context.period, Heroe.DURATION);  // cycle through the images
	}

	@Override
	public void moveDown() {
		//Set all to false
		context.setNotMoving();
		if(context.getXStep() != 0){
			int vx = (context.getVelocidad() * (int)Math.signum(context.dx)) - 1*(int)Math.signum(context.dx);
			context.setStep(vx, context.getVelocidad() - 1); 
			if(context.getXStep() > 0)
				context.setMovingRight(true);
			if(context.getXStep() < 0)
				context.setMovingLeft(true);
		}else{
			context.setStep(0, context.getVelocidad()); 	
		} 
	    //Set Down true
		context.setMovingDown(true);
		context.setImage("H"+ context.getTipo() +"D");
		context.loopImage(context.period, Heroe.DURATION);  // cycle through the images
	}
	
	@Override
	public void shootUp() {
		// TODO Auto-generated method stub
		context.setImage("H"+ context.getTipo() +"U");
		if(context.getXStep() != 0 || context.getYStep() != 0)
			context.loopImage(context.period, Heroe.DURATION);
	}
	
	@Override
	public void shootDown() {
		// TODO Auto-generated method stub
		context.setImage("H"+ context.getTipo() +"D");
		if(context.getXStep() != 0 || context.getYStep() != 0)
			context.loopImage(context.period, Heroe.DURATION);
	}

	@Override
	public void shootLeft() {
		// TODO Auto-generated method stub
		context.setImage("H"+ context.getTipo() +"L");
		if(context.getXStep() != 0 || context.getYStep() != 0)
			context.loopImage(context.period, Heroe.DURATION);
	}

	@Override
	public void shootRight() {
		// TODO Auto-generated method stub
		context.setImage("H"+ context.getTipo() +"R");
		if(context.getXStep() != 0 || context.getYStep() != 0)
			context.loopImage(context.period, Heroe.DURATION);
	}

	@Override
	public void followLider(Heroe hActivo) {
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
		if(!context.isVeloz()){
			context.timerVeloz.start(); // Go go go!
			context.timerVeloz.setRepeats(false); // Only execute once
			context.setVeloz(true);
			context.setVelocidad((context.getVelocidad() + 3));
			System.out.println("rapido");
		}
	}
}
