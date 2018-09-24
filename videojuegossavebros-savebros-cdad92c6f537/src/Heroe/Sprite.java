package Heroe;
import cargadorImagenes.*;

//Sprite.java
//Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A Sprite has a position, velocity (in terms of steps),
an image, and can be deactivated.

The sprite's image is managed with an ImagesLoader object,
and an ImagesPlayer object for looping.

The images stored until the image 'name' can be looped
through by calling loopImage(), which uses an
ImagesPlayer object.

*/

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;


public class Sprite 
{

// default dimensions when there is no image
protected static final int SIZE = 12;   

// image-related
protected ImagesLoader imsLoader;
protected String imageName;
protected BufferedImage image;
protected int width, height;     // image dimensions

protected ImagesPlayer player;  // for playing a loop of images
protected boolean isLooping;

protected int pWidth, pHeight;   // panel dimensions

protected boolean isActive = true;      
// a sprite is updated and drawn only when it is active

// protected vars
protected int locx, locy;        // location of sprite
protected int dx, dy;            // amount to move for each update

// movement bools
protected boolean isMovingLeft;
protected boolean isMovingRight;
protected boolean isMovingUp;
protected boolean isMovingDown;



public Sprite(int x, int y, int w, int h, ImagesLoader imsLd, String name) 
{ 
 locx = x; locy = y;
 pWidth = w; pHeight = h;
 dx = 0; dy = 0;
 isMovingLeft = isMovingRight = isMovingUp = isMovingDown = false;
 
 imsLoader = imsLd;
 setImage(name);    // the sprite's default image is 'name'
} // end of Sprite()



public boolean isMovingLeft() {
	return isMovingLeft;
}



public void setMovingLeft(boolean isMovingLeft) {
	this.isMovingLeft = isMovingLeft;
}



public boolean isMovingRight() {
	return isMovingRight;
}



public void setMovingRight(boolean isMovingRight) {
	this.isMovingRight = isMovingRight;
}



public boolean isMovingUp() {
	return isMovingUp;
}



public void setMovingUp(boolean isMovingUp) {
	this.isMovingUp = isMovingUp;
}



public boolean isMovingDown() {
	return isMovingDown;
}



public void setMovingDown(boolean isMovingDown) {
	this.isMovingDown = isMovingDown;
}

public void setNotMoving() {
	this.isMovingLeft = false;
	this.isMovingRight = false;
	this.isMovingUp = false;
	this.isMovingDown = false;
} 



public void setImage(String name)
// assign the name image to the sprite
{
 imageName = name;
 image = imsLoader.getImage(imageName);
 if (image == null) {    // no image of that name was found
   System.out.println("No sprite image for " + imageName);
   width = SIZE;
   height = SIZE;
 }
 else {
   width = image.getWidth();
   height = image.getHeight();
 }
 // no image loop playing 
 player = null;
 isLooping = false;
}  // end of setImage()


public void loopImage(int animPeriod, double seqDuration)
/* Switch on loop playing. The total time for the loop is
  seqDuration secs. The update interval (from the enclosing
  panel) is animPeriod ms. */
{
 if (imsLoader.numImages(imageName) > 1) {
   player = null;   // to encourage garbage collection of previous player
   player = new ImagesPlayer(imageName, animPeriod, seqDuration,
                                    true, imsLoader);
   isLooping = true;
 }
 else
   System.out.println(imageName + " is not a sequence of images");
}  // end of loopImage()


public void stopLooping()
{
 if (isLooping) {
   player.stop();
   isLooping = false;
 }
}  // end of stopLooping()


public int getWidth()    // of the sprite's image
{  return width;  }

public int getHeight()   // of the sprite's image
{  return height;  }

public int getPWidth()   // of the enclosing panel
{  return pWidth;  }

public int getPHeight()  // of the enclosing panel
{  return pHeight;  }


public boolean isActive() 
{  return isActive;  }

public void setActive(boolean a) 
{  isActive = a;  }

public void setPosition(int x, int y)
{  locx += x; locy += y;  }

public void translate(int xDist, int yDist)
{  locx += xDist;  locy += yDist;  }

public int getXPosn()
{  return locx;  }

public int getYPosn()
{  return locy;  }


public void setStep(int dx, int dy)
{  this.dx = dx; this.dy = dy; }

public int getXStep()
{  return dx;  }

public int getYStep()
{  return dy;  }


public Rectangle getMyRectangle()
{  return  new Rectangle(locx, locy, width, height);  }

//Actualizar dx y dx dependiendo de limites del panel
public boolean checkLimits(){
   boolean colision = false;
   if(isMovingLeft()){
	   if(locx < 0){
		  dx = 0;
		  //locx = 1;  
		  isLooping = false;
		  colision = true;
	   }
   }
   if(isMovingRight()){
	   if(locx + width > pWidth){
		   dx = 0;
		   //locx = pWidth - width - 1;
		   isLooping = false;
		   colision = true;
	   }
	   
   }
   if(isMovingUp()){
	   if(locy < 1){
		  dy = 0; 
		  //locy = 1;
		  isLooping = false;
		  colision = true;
	   }
	   
   }
   if(isMovingDown()){
	   if(locy + height + 1 > pHeight){
		  dy = 0;
		  //locy = pHeight - height - 1;
		  isLooping = false;
		  colision = true;
	   }
	   
   }
   return colision;
   
}

public void updateSprite()
{
 if (isActive()) {
   checkLimits();
   locx += dx;
   locy += dy;
   if (isLooping)
    player.updateTick();  // update the player
 }
} // end of updateSprite()



public void drawSprite(Graphics g) 
{
 if (isActive()) {
   if (image == null) {   // the sprite has no image
     g.setColor(Color.yellow);   // draw a yellow circle instead
     g.fillOval(locx, locy, SIZE, SIZE);
     g.setColor(Color.black);
   }
   else {
     if (isLooping)
       image = player.getCurrentImage();
     g.drawImage(image, locx, locy, null);
   }
 }
} // end of drawSprite()

}  // end of Sprite class
