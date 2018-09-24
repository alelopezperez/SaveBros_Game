package Heroe;

public class HeroeStatePrisionero implements HeroeState{
	
	private Heroe context;
	
	public HeroeStatePrisionero(Heroe p){
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
