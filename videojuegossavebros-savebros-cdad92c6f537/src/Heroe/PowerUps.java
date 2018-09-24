package Heroe;

import cargadorImagenes.*;

import cargadorSonidos.ClipsLoader;

import java.util.LinkedList;

import Enemigo.*;

public class PowerUps extends Sprite{
	private int posX;
	private int posY;
	private int tipo;
	private static String name="powerBox";
	
	public PowerUps(int x, int y, int w, int h, ImagesLoader imsLd, int tipo) {
		super(x, y, w, h, imsLd, name);
		this.tipo=tipo;
	}
	public void setImage(){
		super.setImage(name);
	}
	public int getTipo(){
		return tipo;
	}
	
}