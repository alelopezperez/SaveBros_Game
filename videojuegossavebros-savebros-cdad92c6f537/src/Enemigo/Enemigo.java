package Enemigo;

import Heroe.*;
import cargadorImagenes.ImagesLoader;


public class Enemigo extends Sprite{
	
	private int posX;
	private int posY;
	private int vida;
	private int fuerza;
	private int velocidad;
	
	
	private int tipo; //tipo: 1 = Normal, 2 = Veloz, 3 = Joggernaut.
	
	private String imagen;
	
	private EnemigoState muerto;
	private EnemigoState recibiendoDano;
	private EnemigoState persiguiendo;
	private EnemigoState currentState;
	
	protected int period;
	public static double DURATION = 0.5;
	
	
	public Enemigo(int x, int y, int pW, int pH, ImagesLoader imsLd, String name, int p, int tipo, int velocidad, int vida){
		super(x, y, pW, pH, imsLd, name );
		this.period = p;
		this.tipo = tipo;
		this.vida = vida;
		this.velocidad = velocidad;
		
		persiguiendo = new EnemigoStatePersiguiendo(this);
		
		currentState = new EnemigoStatePersiguiendo(this);
		
	}
	
	
	//BORRAR DESPUES
	public void moveLeft(){
		currentState.moveLeft();
	}

	public void moveRight(){
		currentState.moveRight();
	}
	
	public void moveUp(){
		currentState.moveUp();
	}

	public void moveDown(){
		currentState.moveDown();
	}
	
	public void stayStill()
	  // stop the ant moving
	  { setStep(0, 0); 
	    stopLooping();
	  }
	
	public void setState(EnemigoState newState){
		currentState = newState;
	}
	
	public void recibirDano(){
		currentState.recibirDano();
	}
	
	public void atacar(){
		currentState.atacar();
	}
	
	public boolean seguir(Heroe heroe, Enemigo enemigo){
		return currentState.seguir(heroe, enemigo);
	}

	
	
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getFuerza() {
		return fuerza;
	}

	public void setFuerza(int fuerza) {
		this.fuerza = fuerza;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public EnemigoState getMuerto() {
		return muerto;
	}

	public void setMuerto(EnemigoState muerto) {
		this.muerto = muerto;
	}

	public EnemigoState getRecibiendoDano() {
		return recibiendoDano;
	}

	public void setRecibiendoDano(EnemigoState recibiendoDano) {
		this.recibiendoDano = recibiendoDano;
	}

	public EnemigoState getPersiguiendo() {
		return persiguiendo;
	}

	public void setPersiguiendo(EnemigoState persiguiendo) {
		this.persiguiendo = persiguiendo;
	}

	public EnemigoState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(EnemigoState currentState) {
		this.currentState = currentState;
	}
	
	public int getTipo() {
		return tipo;
	}
	
}
