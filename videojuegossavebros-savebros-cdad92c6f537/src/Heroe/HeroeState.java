package Heroe;

public interface HeroeState {
	public void pagarLiberacion();
	public void pagarPuerta();
	public void cambioDePrincipal();
	public void personajeMuerto();
	public void atacado();
	public void disparar();
	public void caminar();
	public void correr();
	public void colision();
	public void moveLeft();
	public void moveRight();
	public void moveUp();
	public void moveDown();
	public void shootUp();
	public void shootDown();
	public void shootLeft();
	public void shootRight();
	public void recarga();
	public void followLider(Heroe hActivo);
	public void powerVeloz();
}
