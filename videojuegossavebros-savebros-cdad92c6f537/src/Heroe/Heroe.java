package Heroe;
import static Heroe.Heroe.DURATION;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import cargadorImagenes.ImagesLoader;

public class Heroe extends Sprite {
	private Bala Balas[];
	
	private HeroeState prisionero;
	private HeroeState siguiendo;
	private HeroeState activoState;
	private HeroeState recibiendoDano;
	private HeroeState muerto;
	private HeroeState currentState;
	
	private int vida;
	private int velocidad;
	private boolean activo;
	
	
	private int tipo; //tipo: 1 = pistolero, 2 = metralletero, 3 = escopetero, 4 = snipero. 
	
	private int numBalas;
	private int balasTotales;
	private boolean vivo;
	private boolean veloz;
	private String estado;
	public Timer timerRecarga;
	public Timer timerVeloz;

	private static final int XSTEP = 10;
	protected int period;
	   /* in ms. The game's animation period used by the image
	      cycling of the bat's left and right facing images. */
	public static double DURATION = 0.5;  // secs
    // total time to cycle through all the images

	public Heroe(int x, int y, int pW, int pH, ImagesLoader imsLd, String nameImage, int period, 
				 int tipo, int numBalas, int velocidad, boolean activo){
		super(x, y, pW, pH, imsLd, nameImage);
		this.period = period;
		this.tipo = tipo;
		this.numBalas = numBalas;
		this.balasTotales = numBalas; 
		this.vida = 5;
		this.velocidad = velocidad;
		this.activo = activo;
		this.vivo = true;		
		
		prisionero = new HeroeStatePrisionero(this);
		siguiendo = new HeroeStateSiguiendo(this);
		activoState = new HeroeStateActivo(this);
		if(activo){
			currentState = new HeroeStateActivo(this);
		}else{
			currentState = new HeroeStatePrisionero(this);
			this.estado = "prisionero";
		}
		
		timerRecarga = new Timer(4500, new ActionListener() {
		  	  @Override
		  	  public void actionPerformed(ActionEvent e) {
		  		setNumBalas(balasTotales);
	     	  }
		});
		
		timerVeloz = new Timer(5000, new ActionListener() {
		  	  @Override
		  	  public void actionPerformed(ActionEvent e) {
		  		if(isVeloz()){
					setVeloz(false);
					setVelocidad((getVelocidad() - 3));
					System.out.println("lento");
				}
	     	  }
		});
	}
	
	public boolean isVeloz() {
		return veloz;
	}



	public void setVeloz(boolean veloz) {
		this.veloz = veloz;
	}
	
	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getNumBalas() {
		return numBalas;
	}

	public void setNumBalas(int numBalas) {
		this.numBalas = numBalas;
	}
	
	/*
	public abstract void draw(Graphics g);
	public abstract void move(KeyEvent e);
	*/
	
	public int getBalasTotales() {
		return balasTotales;
	}

	public void setBalasTotales(int balasTotales) {
		this.balasTotales = balasTotales;
	}

	public Bala[] getBalas() {
		return Balas;
	}

	public void setBalas(Bala[] balas) {
		Balas = balas;
	}
	
	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public boolean isVivo() {
		return vivo;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HeroeState getPrisionero() {
		return prisionero;
	}

	public void setPrisionero(HeroeState prisionero) {
		this.prisionero = prisionero;
	}

	public HeroeState getSiguiendo() {
		return siguiendo;
	}

	public void setSiguiendo(HeroeState siguiendo) {
		this.siguiendo = siguiendo;
	}

	public HeroeState getActivoState() {
		return activoState;
	}

	public void setActivoState(HeroeState activoState) {
		this.activoState = activoState;
	}

	public HeroeState getRecibiendoDano() {
		return recibiendoDano;
	}

	public void setRecibiendoDano(HeroeState recibiendoDano) {
		this.recibiendoDano = recibiendoDano;
	}

	public HeroeState getMuerto() {
		return muerto;
	}

	public void setMuerto(HeroeState muerto) {
		this.muerto = muerto;
	}

	public HeroeState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(HeroeState currentState) {
		this.currentState = currentState;
	}

	public void setState(HeroeState newState){
		currentState = newState;
	}
	
	public void pagarLiberacion(){
		currentState.pagarLiberacion();
	}
	
	public void pagarPuerta(){
		currentState.pagarLiberacion();
	}
	
	public void cambioDePrincipal(){
		currentState.cambioDePrincipal();
	}
	
	public void personajeMuerto(){
		currentState.personajeMuerto();
	}
	
	public void atacado(){
		currentState.atacado();
	}
		
	public void disparar(){
		currentState.disparar();
	}
	
	public void caminar(){
		currentState.caminar();
	}
	
	public void correr(){
		currentState.correr();
	}
	
	public void colision(){
		currentState.pagarLiberacion();
	}
	
	public void moveLeft()
	  // start the heroe moving left
	  { 
	    currentState.moveLeft();
	  } // end of moveLeft()

	public void moveRight()
	  // start the heroe moving right
	  { 
		currentState.moveRight();
	  } // end of moveRight()
	  
	public void moveUp()
	  // start the heroe moving up
	  { 
		currentState.moveUp();
	  } // end of moveRight()
	
	public void moveDown()
	  // start the heroe moving down
	  { 
		currentState.moveDown();
	  } // end of moveRight()
	
	public void shootUp()
	  // start the heroe moving down
	  { 
		currentState.shootUp();
	  } // end of moveRight()
	
	public void shootDown()
	  // start the heroe moving down
	  { 
		currentState.shootDown();
	  } // end of moveRight()
	
	public void shootLeft()
	  // start the heroe moving down
	  { 
		currentState.shootLeft();
	  } // end of moveRight()
	
	public void shootRight()
	  // start the heroe moving down
	  { 
		currentState.shootRight();
	  } // end of moveRight()
	
	public void followLider(Heroe hActivo){
		currentState.followLider(hActivo);
	}
	
	public void recarga(){
		currentState.recarga();
	}
	
	public void powerVeloz(){
		currentState.powerVeloz();
	}
	
	public void stayStill()
	// stop the ant moving
	{ setStep(0, 0); 
	  stopLooping();
	}

}
