package Enemigo;

import Heroe.*;

public interface EnemigoState {
	
	public void recibirDano();
	public void atacar();
	public boolean seguir(Heroe heroe, Enemigo enemigo);
	public void moveLeft();
	public void moveRight();
	public void moveUp();
	public void moveDown();
	
}
