
// BugPanel.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The game's drawing surface. Uses active rendering to a JPanel
   with the help of Java 3D's timer.
   See WormP for a version with statistics generation.
*/
import Heroe.*;
import cargadorImagenes.*;
import cargadorSonidos.*;

import Enemigo.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.image.*;
import java.io.IOException;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import com.sun.j3d.utils.timer.J3DTimer;




public class Panel extends JPanel implements Runnable
{
  private int state=0;
  public ImagesLoader imsLoader; 
  public Random rand = new Random();
  public boolean crearHeroe = false;
  public boolean crearEnemigo = false;
  public boolean crearPowerUp = false;
  public Timer timerMetralletaUp;
  public Timer timerMetralletaDown;
  public Timer timerMetralletaLeft;
  public Timer timerMetralletaRight;
  private int keyPressedFlag = 0;
  private int numh=0;
  private static final int PWIDTH = 928 ;   // size of panel
  private static final int PHEIGHT = 640; 
  private int wait2=0;
  private int wait3=0;

  private int waitPU=0;
  private String tipoPU;

  private int waitp=0;
  private int timePaused=0;

  
  private boolean rest=false;
  
  private boolean indestructible=false;

  private static final int NO_DELAYS_PER_YIELD = 16;
  private int numRonda=1;
  private int dificultad=1;
  private int mapa=0;
  private int numZoombies=5;
  private int eneMuertos = 0;
  private int puntaje = 0;
  private int totalHeroesLiberados = 1;
  private int limZoombiesCrear=numZoombies;
  /* Number of frames with a delay of 0 ms before the animation thread yields
     to other running threads. */
  private static int MAX_FRAME_SKIPS = 5;
    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered

  // image and clip loader information files
  private static final String IMS_INFO = "imsInfo.txt";
  private static final String SNDS_FILE = "clipsInfo.txt";


  private Thread animator;           // the thread that performs the animation
  private volatile boolean running = false;   // used to stop the animation thread
  private volatile boolean isPaused = false;

  private long period;                // period between drawing in _nanosecs_

  private SaveBros saveBros;
  private ClipsLoader clipsLoader;

  //private BallSprite ball;        // the sprites
  //private BatSprite bat;
  
  private LinkedList <Heroe> listHeroePrisionero;
  private LinkedList <Heroe> listHeroe;
  //private Enemigo EnemigoNormal;
  private LinkedList <Enemigo> listEnemigo;
  private LinkedList <Bala> listBala;
  private LinkedList <PowerUps> listPowerUp;
  private Bala b;

  private long gameStartTime;   // when the game started
  private int timeSpentInGame;

  // used at game termination
  private volatile boolean gameOver = false;
  private int score = 0;

  // for displaying messages
  private Font msgsFont;
  private FontMetrics metrics;

  // off-screen rendering
  private Graphics dbg; 
  private Image dbImage = null;

  // holds the background image
  private BufferedImage bgImage = null;
  
  //Game components
  private int posXHeroe = 50;
  private int posYHeroe = 50;
  private Heroe heroePistolero;
  
  public Panel(SaveBros br, long period)
  {
    saveBros = br;
    this.period = period;

    setDoubleBuffered(false);
    setBackground(Color.black);
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

    setFocusable(true);
    requestFocus();    // the JPanel now has focus, so receives key events
    
    // create game components
 	//heroePistolero = HeroeFactory.getInstance().getHeroeObject(1,posXHeroe,posYHeroe,PWIDTH, PHEIGHT, );

	addKeyListener( new KeyAdapter() {
       public void keyPressed(KeyEvent e)
       { processKey(e); }
       public void keyReleased(KeyEvent e) 
       { processKeyReleased(e); }
     });
	

    // load the background image
    imsLoader = new ImagesLoader(IMS_INFO); 
    bgImage = imsLoader.getImage("saveBros"+mapa);

    // initialise the clips loader
    clipsLoader = new ClipsLoader(SNDS_FILE); 

    // create game sprites
    int tipoHeroe = 1; 
    boolean heroeActivo = true;
    listHeroe = new LinkedList<Heroe>();
    listHeroePrisionero = new LinkedList<Heroe>();
    listHeroe.add(HeroeFactory.getInstance().getHeroeObject(PWIDTH/2, PHEIGHT/2, PWIDTH, PHEIGHT, imsLoader,
			(int)(period/1000000L), tipoHeroe, heroeActivo));
    
    listEnemigo = new LinkedList<Enemigo>();
    listBala = new LinkedList<Bala>();
    listPowerUp= new LinkedList<PowerUps>();

    int tipoEnemigo = 1;

    addMouseListener( new MouseAdapter() {
      public void mousePressed(MouseEvent e)
      { testPress(e); }  // handle mouse presses
    });

    // set up message font
    msgsFont = new Font("SansSerif", Font.BOLD, 24);
    metrics = this.getFontMetrics(msgsFont);
    
    timerMetralletaUp = new Timer(100, new ActionListener() {
	  	  @Override
	  	  public void actionPerformed(ActionEvent e) {
	  		  Heroe heroe = getHeroeActivo();
	  		  if(heroe.getNumBalas() > 0){
		  		  Bala bu= new Bala(heroe.getXPosn() + (5*heroe.getWidth()/6),heroe.getYPosn(),PWIDTH, PHEIGHT, imsLoader);
	        	  bu.moveUp();
	        	  listBala.add(bu);
	        	  clipsLoader.play("machine_gun", true);
	        	  heroe.shootUp();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	  		  }else{
	  			timerMetralletaUp.stop();
	  			clipsLoader.stop("machine_gun");
		        heroe.recarga();
		      }
	  	  }
	 });
    
    timerMetralletaDown= new Timer(100, new ActionListener() {
	  	  @Override
	  	  public void actionPerformed(ActionEvent e) {
	  		  Heroe heroe = getHeroeActivo();
	  		  if(heroe.getNumBalas() > 0){
	  			  Bala bu= new Bala(heroe.getXPosn() + (1*heroe.getWidth()/6),heroe.getYPosn() + (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.moveDown();
	        	  listBala.add(bu);
	        	  clipsLoader.play("machine_gun", true);
	        	  heroe.shootDown();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	  		  }else{
	  			timerMetralletaUp.stop();
	  			clipsLoader.stop("machine_gun");
		        heroe.recarga();
		      }
	  	  }
	 });
     
    timerMetralletaLeft= new Timer(100, new ActionListener() {
	  	  @Override
	  	  public void actionPerformed(ActionEvent e) {
	  		  Heroe heroe = getHeroeActivo();
	  		  if(heroe.getNumBalas() > 0){
	  			  Bala bu= new Bala(heroe.getXPosn(),heroe.getYPosn() + (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.moveLeft();
	        	  listBala.add(bu);
	        	  clipsLoader.play("machine_gun", true);
	        	  heroe.shootLeft();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	  		  }else{
	  			timerMetralletaUp.stop();
	  			clipsLoader.stop("machine_gun");
		        heroe.recarga();
		      }
	  	  }
	 });
    
    timerMetralletaRight= new Timer(100, new ActionListener() {
	  	  @Override
	  	  public void actionPerformed(ActionEvent e) {
	  		  Heroe heroe = getHeroeActivo();
	  		  if(heroe.getNumBalas() > 0){
	  			  Bala bu= new Bala(heroe.getXPosn() + heroe.getWidth()/2,heroe.getYPosn()+ (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.moveRight();
	        	  listBala.add(bu);
	        	  clipsLoader.play("machine_gun", true);
	        	  heroe.shootRight();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	  		  }else{
	  			timerMetralletaUp.stop();
	  			clipsLoader.stop("machine_gun");
		        heroe.recarga();
		      }
	  	  }
	 });

  }  // end of Panel()

  

  public void processKey(KeyEvent e)
  // handles termination and game-play keys
  {
    int keyCode = e.getKeyCode();
    Heroe heroe = getHeroeActivo();
    if(keyPressedFlag == keyCode){
    	keyCode = 0;
    }else{
    	keyPressedFlag = keyCode;
    }

    // termination keys
	// listen for esc, q, end, ctrl-c on the canvas to
	// allow a convenient exit from the full screen configuration
    if (keyCode == KeyEvent.VK_P){
  	  isPaused=!isPaused;
  	  if(isPaused){
  		  waitp=timeSpentInGame;
  		  System.out.println(timeSpentInGame);  
  	  }
  	  else{
  		  timePaused+=-timePaused+
  		          (int) ((J3DTimer.getValue() - gameStartTime)/1000000000L)-waitp;
  	  }
  	}
    
    if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) ||
        (keyCode == KeyEvent.VK_END) ||
        ((keyCode == KeyEvent.VK_C) && e.isControlDown()) )
    
      running = false;
    
    // game-play keys
    if (!isPaused && !gameOver && state!=1) {
	    if(keyCode==KeyEvent.VK_ENTER){
	  	  state=1;
	    }
    }
    if (!isPaused && !gameOver && state==1) {
      if (keyCode == KeyEvent.VK_A)
    	  heroe.moveLeft();
      if (keyCode == KeyEvent.VK_D)
    	  heroe.moveRight();
      if (keyCode == KeyEvent.VK_S)
    	  heroe.moveDown();
      if (keyCode == KeyEvent.VK_W)
    	  heroe.moveUp();
      
      if(keyCode==KeyEvent.VK_ENTER){
    	  state=1;
      }
      
      if(keyCode == KeyEvent.VK_SPACE){
    	  cambiarHeroe(heroe);
    	  keyPressedFlag = 0;
    	  if(timerMetralletaUp.isRunning())
			  timerMetralletaUp.stop(); // stop timer!
    	  if(timerMetralletaDown.isRunning())
    		  timerMetralletaDown.stop(); // stop timer!
    	  if(timerMetralletaLeft.isRunning())
			  timerMetralletaLeft.stop(); // stop timer!
    	  if(timerMetralletaRight.isRunning())
			  timerMetralletaRight.stop(); // stop timer!
      }
            
      if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT){
      	  if(heroe.getNumBalas() > 0){
      		  heroeShoot(keyCode, heroe);
	    	  if(heroe.getNumBalas() == 0)
	    		  heroe.recarga();
	      }else{
	    	    clipsLoader.play("emptyMag", false);
	      }
      }
      
      if (keyCode == KeyEvent.VK_R){
    	  heroe.setNumBalas(0);
	      heroe.recarga();
      }
    }
  }  // end of processKey()
  
  public void processKeyReleased(KeyEvent e)
  // handles termination and game-play keys
  {
    int keyCode = e.getKeyCode();
    Heroe heroe = getHeroeActivo();
    // game-play keys
    if(keyCode == KeyEvent.VK_P){
    	  keyPressedFlag = 0;
    	  System.out.println("Pausa");
      }
    if (!isPaused && !gameOver && state==1) {
    	
      if (keyCode == KeyEvent.VK_A){
    	  heroe.setStep(0, heroe.getYStep());
    	  heroe.setMovingLeft(false);
    	  keyPressedFlag = 0;
      }else if (keyCode == KeyEvent.VK_D){
    	  heroe.setStep(0, heroe.getYStep());
    	  heroe.setMovingRight(false);
    	  keyPressedFlag = 0;
    	  //clipsLoader.stop("pew");
      }else if (keyCode == KeyEvent.VK_S){
    	  heroe.setStep(heroe.getXStep(), 0);
    	  heroe.setMovingDown(false);
    	  keyPressedFlag = 0;
    	  //clipsLoader.stop("pew");
      }else if (keyCode == KeyEvent.VK_W){
    	  heroe.setStep(heroe.getXStep(), 0);
    	  heroe.setMovingUp(false);
    	  keyPressedFlag = 0;
    	  //clipsLoader.stop("pew");
      }
      
      if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D || 
          keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_W){
          if(heroe.getXStep() == 0 && heroe.getYStep() == 0)      
           	  heroe.stopLooping();
          else if(heroe.isMovingUp()){
           	  heroe.shootUp();
          }else if(heroe.isMovingDown()){
           	  heroe.shootDown();
          }else if(heroe.isMovingLeft()){
           	  heroe.shootLeft();
          }else if(heroe.isMovingRight()){
           	  heroe.shootRight();
          }
      }
      
      if (keyCode == KeyEvent.VK_UP){
    	  keyPressedFlag = 0;
    	  clipsLoader.stop("machine_gun");
    	  if(timerMetralletaUp.isRunning()){
			  timerMetralletaUp.stop(); // stop timer!
			  if(heroe.getNumBalas() <= 0)
	    		  heroe.recarga();
		  }
    	  //clipsLoader.stop("pew");
      }else if (keyCode == KeyEvent.VK_DOWN){
    	  keyPressedFlag = 0;
    	  clipsLoader.stop("machine_gun");
    	  if(timerMetralletaDown.isRunning()){
    		  timerMetralletaDown.stop(); // stop timer!
    		  if(heroe.getNumBalas() <= 0)
	    		  heroe.recarga();
		  }
      }else if (keyCode == KeyEvent.VK_LEFT){
    	  keyPressedFlag = 0;
    	  clipsLoader.stop("machine_gun");
    	  if(timerMetralletaLeft.isRunning()){
    		  timerMetralletaLeft.stop(); // stop timer!
    		  if(heroe.getNumBalas() <= 0)
	    		  heroe.recarga();
		  }
      }else if (keyCode == KeyEvent.VK_RIGHT){
    	  keyPressedFlag = 0;
    	  clipsLoader.stop("machine_gun");
    	  if(timerMetralletaRight.isRunning()){
    		  timerMetralletaRight.stop(); // stop timer!
    		  if(heroe.getNumBalas() <= 0)
	    		  heroe.recarga();
		  }
      }else if (keyCode == KeyEvent.VK_R){
    	  keyPressedFlag = 0;
      }else if(keyCode == KeyEvent.VK_SHIFT){
    	  keyPressedFlag = 0;
      }
     
      
     
    }
  }  // end of processKey()
  
  
  public void heroeShoot(int keyCode, Heroe heroe) {	    
	      clipsLoader.stop("pew");
		  clipsLoader.stop("shotgun");
		  clipsLoader.stop("sniper");
		  if(heroe.getTipo() == 1){ //Pistola
			  if (keyCode == KeyEvent.VK_UP){
	        	  //clipsLoader.stop("pew");
	        	  Bala bu= new Bala(heroe.getXPosn() + (5*heroe.getWidth()/6),heroe.getYPosn(),PWIDTH, PHEIGHT, imsLoader);
	        	  bu.moveUp();
	        	  listBala.add(bu);
	        	  clipsLoader.play("pew", false);
	        	  heroe.shootUp();
	        	  
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_DOWN){
	        	  //clipsLoader.stop("pew");
	              Bala bu= new Bala(heroe.getXPosn() + (1*heroe.getWidth()/6),heroe.getYPosn() + (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.moveDown();
	              listBala.add(bu);
	        	  clipsLoader.play("pew", false);
	        	  heroe.shootDown();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_LEFT){
	        	  //clipsLoader.stop("pew");
	              Bala bu= new Bala(heroe.getXPosn(),heroe.getYPosn() + (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.moveLeft();
	              listBala.add(bu);
	        	  clipsLoader.play("pew", false);
	        	  heroe.shootLeft();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_RIGHT){
	        	  //clipsLoader.stop("pew");
	              Bala bu= new Bala(heroe.getXPosn() + heroe.getWidth()/2,heroe.getYPosn()+ (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.moveRight();
	              listBala.add(bu);
	        	  clipsLoader.play("pew", false);
	        	  heroe.shootRight();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }
		  }else if(heroe.getTipo() == 2){ //Metralleta
			  if (keyCode == KeyEvent.VK_UP){
	  			  if(!timerMetralletaUp.isRunning()){
	  				  timerMetralletaUp.start(); // Go go go!
	  				  timerMetralletaUp.setRepeats(true); // repeats
	  			  }
	          }else if (keyCode == KeyEvent.VK_DOWN){
	        	  if(!timerMetralletaDown.isRunning()){
	        		  timerMetralletaDown.start(); // Go go go!
	        		  timerMetralletaDown.setRepeats(true); // repeats
	        	  }
	          }else if (keyCode == KeyEvent.VK_LEFT){
	        	  if(!timerMetralletaLeft.isRunning()){
	        		  timerMetralletaLeft.start(); // Go go go!
	        		  timerMetralletaLeft.setRepeats(true); // repeats
	        	  }
	          }else if (keyCode == KeyEvent.VK_RIGHT){
	        	  if(!timerMetralletaRight.isRunning()){
	        		  timerMetralletaRight.start(); // Go go go!
	        		  timerMetralletaRight.setRepeats(true); // repeats
	        	  }
	          }
		  }else if(heroe.getTipo() == 3){ //Escopeta
			  if (keyCode == KeyEvent.VK_UP){
	        	  //clipsLoader.stop("pew");
	        	  Bala bu= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12),heroe.getYPosn() + 4,PWIDTH, PHEIGHT, imsLoader);
	        	  bu.moveUp();
	        	  Bala bu2= new Bala(heroe.getXPosn() + (8*heroe.getWidth()/12),heroe.getYPosn(),PWIDTH, PHEIGHT, imsLoader);
	        	  bu2.moveUp();
	        	  Bala bu3= new Bala(heroe.getXPosn() + (12*heroe.getWidth()/12),heroe.getYPosn(),PWIDTH, PHEIGHT, imsLoader);
	        	  bu3.moveUp();
	        	  Bala bu4= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12),heroe.getYPosn() - 4,PWIDTH, PHEIGHT, imsLoader);
	        	  bu4.moveUp();
	        	  Bala bu5= new Bala(heroe.getXPosn() + (12*heroe.getWidth()/12),heroe.getYPosn() + 8,PWIDTH, PHEIGHT, imsLoader);
	        	  bu5.moveUp();
	        	  Bala bu6= new Bala(heroe.getXPosn() + (8*heroe.getWidth()/12),heroe.getYPosn() + 8,PWIDTH, PHEIGHT, imsLoader);
	        	  bu6.moveUp();
	        	  Bala bu7= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12),heroe.getYPosn() + 12,PWIDTH, PHEIGHT, imsLoader);
	        	  bu7.moveUp();
	        	  listBala.add(bu);
	  	          listBala.add(bu2);
	  	          listBala.add(bu3);
	  	          listBala.add(bu4);
	  	          listBala.add(bu5);
	  	          listBala.add(bu6);
	  	          listBala.add(bu7);
	        	  clipsLoader.play("shotgun", false);
	        	  heroe.shootUp();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_DOWN){
	        	  //clipsLoader.stop("pew");
	        	  Bala bu= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12) - 30,heroe.getYPosn() + 4 + 20,PWIDTH, PHEIGHT, imsLoader);
	        	  bu.moveDown();
	        	  Bala bu2= new Bala(heroe.getXPosn() + (8*heroe.getWidth()/12) - 30,heroe.getYPosn() + 20,PWIDTH, PHEIGHT, imsLoader);
	        	  bu2.moveDown();
	        	  Bala bu3= new Bala(heroe.getXPosn() + (12*heroe.getWidth()/12) - 30,heroe.getYPosn() + 20,PWIDTH, PHEIGHT, imsLoader);
	        	  bu3.moveDown();
	        	  Bala bu4= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12) - 30,heroe.getYPosn() - 4 + 20,PWIDTH, PHEIGHT, imsLoader);
	        	  bu4.moveDown();
	        	  Bala bu5= new Bala(heroe.getXPosn() + (12*heroe.getWidth()/12) - 30,heroe.getYPosn() + 8 + 20,PWIDTH, PHEIGHT, imsLoader);
	        	  bu5.moveDown();
	        	  Bala bu6= new Bala(heroe.getXPosn() + (8*heroe.getWidth()/12) - 30,heroe.getYPosn() + 8 + 20,PWIDTH, PHEIGHT, imsLoader);
	        	  bu6.moveDown();
	        	  Bala bu7= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12) - 30,heroe.getYPosn() + 12 + 20,PWIDTH, PHEIGHT, imsLoader);
	        	  bu7.moveDown();
	        	  listBala.add(bu);
	  	          listBala.add(bu2);
	  	          listBala.add(bu3);
	  	          listBala.add(bu4);
	  	          listBala.add(bu5);
	  	          listBala.add(bu6);
	  	          listBala.add(bu7);
	        	  clipsLoader.play("shotgun", false);
	        	  heroe.shootDown();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_LEFT){
	        	  //clipsLoader.stop("pew");
	        	  Bala bu= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12) - 30,heroe.getYPosn() + 4 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu.moveLeft();
	        	  Bala bu2= new Bala(heroe.getXPosn() + (8*heroe.getWidth()/12) - 30,heroe.getYPosn() + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu2.moveLeft();
	        	  Bala bu3= new Bala(heroe.getXPosn() + (12*heroe.getWidth()/12) - 30,heroe.getYPosn() + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu3.moveLeft();
	        	  Bala bu4= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12) - 30,heroe.getYPosn() - 4 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu4.moveLeft();
	        	  Bala bu5= new Bala(heroe.getXPosn() + (12*heroe.getWidth()/12) - 30,heroe.getYPosn() + 8 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu5.moveLeft();
	        	  Bala bu6= new Bala(heroe.getXPosn() + (8*heroe.getWidth()/12) - 30,heroe.getYPosn() + 8 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu6.moveLeft();
	        	  Bala bu7= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12) - 30,heroe.getYPosn() + 12 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu7.moveLeft();
	        	  listBala.add(bu);
	  	          listBala.add(bu2);
	  	          listBala.add(bu3);
	  	          listBala.add(bu4);
	  	          listBala.add(bu5);
	  	          listBala.add(bu6);
	  	          listBala.add(bu7);
	        	  clipsLoader.play("shotgun", false);
	        	  heroe.shootLeft();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_RIGHT){
	        	  //clipsLoader.stop("pew");
	        	  Bala bu= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12),heroe.getYPosn() + 4 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu.moveRight();
	        	  Bala bu2= new Bala(heroe.getXPosn() + (8*heroe.getWidth()/12),heroe.getYPosn() + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu2.moveRight();
	        	  Bala bu3= new Bala(heroe.getXPosn() + (12*heroe.getWidth()/12),heroe.getYPosn() + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu3.moveRight();
	        	  Bala bu4= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12),heroe.getYPosn() - 4 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu4.moveRight();
	        	  Bala bu5= new Bala(heroe.getXPosn() + (12*heroe.getWidth()/12),heroe.getYPosn() + 8 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu5.moveRight();
	        	  Bala bu6= new Bala(heroe.getXPosn() + (8*heroe.getWidth()/12),heroe.getYPosn() + 8 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu6.moveRight();
	        	  Bala bu7= new Bala(heroe.getXPosn() + (10*heroe.getWidth()/12),heroe.getYPosn() + 12 + 10,PWIDTH, PHEIGHT, imsLoader);
	        	  bu7.moveRight();
	        	  listBala.add(bu);
	  	          listBala.add(bu2);
	  	          listBala.add(bu3);
	  	          listBala.add(bu4);
	  	          listBala.add(bu5);
	  	          listBala.add(bu6);
	  	          listBala.add(bu7);
	        	  clipsLoader.play("shotgun", false);
	        	  heroe.shootRight();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }
		  }else if(heroe.getTipo() == 4){ //Sniper
			  if (keyCode == KeyEvent.VK_UP){
	        	  //clipsLoader.stop("pew");
	        	  Bala bu= new Bala(heroe.getXPosn() + (5*heroe.getWidth()/6),heroe.getYPosn(),PWIDTH, PHEIGHT, imsLoader);
	        	  bu.setSniper(true);
	        	  bu.moveUp();
	        	  listBala.add(bu);
	        	  clipsLoader.play("sniper", false);
	        	  heroe.shootUp();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_DOWN){
	        	  //clipsLoader.stop("pew");
	              Bala bu= new Bala(heroe.getXPosn() + (1*heroe.getWidth()/6),heroe.getYPosn() + (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.setSniper(true);
	              bu.moveDown();
	              listBala.add(bu);
	        	  clipsLoader.play("sniper", false);
	        	  heroe.shootDown();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_LEFT){
	        	  //clipsLoader.stop("pew");
	              Bala bu= new Bala(heroe.getXPosn(),heroe.getYPosn() + (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.setSniper(true);
	              bu.moveLeft();
	              listBala.add(bu);
	        	  clipsLoader.play("sniper", false);
	        	  heroe.shootLeft();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }else if (keyCode == KeyEvent.VK_RIGHT){
	        	  //clipsLoader.stop("pew");
	              Bala bu= new Bala(heroe.getXPosn() + heroe.getWidth()/2,heroe.getYPosn()+ (heroe.getHeight()/2),PWIDTH, PHEIGHT, imsLoader);
	              bu.setSniper(true);
	              bu.moveRight();
	              listBala.add(bu);
	        	  clipsLoader.play("sniper", false);
	        	  heroe.shootRight();
	        	  heroe.setNumBalas(heroe.getNumBalas() -1);
	          }
		  }
		  if(heroe.getNumBalas() == 0)
    		  heroe.recarga();
  }

  public void gameOver()
  // called by BallSprite to signal that the game is over
  { 
    int finalTime = 
          (int) ((J3DTimer.getValue() - gameStartTime)/1000000000L);  // ns --> secs
    score = finalTime;   // could be more fancy!
    //clipsLoader.play("gameOver", false);   // play a game over clip once
    gameOver = true;  
    
  } // end of gameOver()



  public void addNotify()
  // wait for the JPanel to be added to the JFrame before starting
  { super.addNotify();   // creates the peer
    startGame();         // start the thread
  }


  private void startGame()
  // initialise and start the thread 
  { 
	 
	  if (animator == null || !running) {
	      animator = new Thread(this);
		  animator.start();
	    }
  } // end of startGame()
    

  // ------------- game life cycle methods ------------
  // called by the JFrame's window listener methods


  public void resumeGame()
  // called when the JFrame is activated / deiconified
  {  isPaused = false;  } 


  public void pauseGame()
  // called when the JFrame is deactivated / iconified
  { isPaused = true;   } 


  public void stopGame() 
  // called when the JFrame is closing
  {  running = false;   }

  // ----------------------------------------------


  private void testPress(MouseEvent e){ 
	  if (!isPaused && !gameOver){
		  if(state == 0){
			  if(e.getX()>=690 && e.getX()<=872&&
					e.getY()>=53 && e.getY()<=140){
					clipsLoader.stop("menu");
					state=1;
				}  
				else if(e.getX()>=690 && e.getX()<=872 &&e.getY()>=202 && e.getY()<=282){
					state=2;
				}
				System.out.println(e.getX()+" "+e.getY());
		  }else if(state == 2){
			  if(e.getX()>=690 && e.getX()<=872&&
  				e.getY()>=53 && e.getY()<=140){
  				state=1;
  				clipsLoader.stop("menu");
  			  }
		  }
	  }
	  else if(gameOver){
		  if(state == 3){
			  if(e.getX()>=369 && e.getX()<=557&&
					  e.getY()>=427 && e.getY()<=470){
				  System.out.println("cambio");
				  state=0;
				  
				  gameOver=false;
				  restart();
				  
			  }
			 
		  }
		  
	  }
  
  
  }  // end of testPress()


  public void run()
  /* The frames of the animation are drawn inside the while loop. */
  {
    long beforeTime, afterTime, timeDiff, sleepTime;
    long overSleepTime = 0L;
    int noDelays = 0;
    long excess = 0L;

    gameStartTime = J3DTimer.getValue();
    beforeTime = gameStartTime;

	running = true;

	while(running) {
	  gameUpdate();
      gameRender();
      paintScreen();      

      afterTime = J3DTimer.getValue();
      timeDiff = afterTime - beforeTime;
      sleepTime = (period - timeDiff) - overSleepTime;  

      if (sleepTime > 0) {   // some time left in this cycle
        try {
          Thread.sleep(sleepTime/1000000L);  // nano -> ms
        }
        catch(InterruptedException ex){}
        overSleepTime = (J3DTimer.getValue() - afterTime) - sleepTime;
      }
      else {    // sleepTime <= 0; the frame took longer than the period
        excess -= sleepTime;  // store excess time value
        overSleepTime = 0L;

        if (++noDelays >= NO_DELAYS_PER_YIELD) {
          Thread.yield();   // give another thread a chance to run
          noDelays = 0;
        }
      }

      beforeTime = J3DTimer.getValue();

      /* If frame animation is taking too long, update the game state
         without rendering it, to get the updates/sec nearer to
         the required FPS. */
      int skips = 0;
      while((excess > period) && (skips < MAX_FRAME_SKIPS)) {
        excess -= period;
	    gameUpdate();    // update state but don't render
        skips++;
      }
	}
    System.exit(0);   // so window disappears
  } // end of run()


  private void gameUpdate() 
  { 
	Heroe hActivo = getHeroeActivo();
	if (!isPaused && !gameOver) {
	  updateRonda();
	  updateEnemigos(hActivo);
	  updateHeroes(hActivo);
	  createHeroe();
	  createEnemigo();
	  createPowerUp();
	  liberaHeroe();
	  updateBalas();
	  updatePowerUp();
	  
	  if(rest){
		  if((timeSpentInGame-wait3)>5){
			  rest=false;
			  limZoombiesCrear=numZoombies;
		  }
	  }
    }
  }  // end of gameUpdate()
  
  public void updateRonda(){
	  if(numZoombies==0){
	    	 numRonda++;
	    	 clipsLoader.play("round",false);
	    	 System.out.println("Estamos En la Ronda: "+numRonda);
	    	 if(numRonda % 3 == 0){
	    		 dificultad++;
	    	 }
	    	 if(numRonda % 3 == 0){
	    		 if(mapa == 0){
	    			 mapa++; 
	    		 }else{
	    			 mapa--;
	    		 }
	    		 
	    	 }
	    	 wait3=timeSpentInGame;
	    	 rest=true;
	    	 numZoombies=numRonda*5;
	      }
  }
  
  public void updateHeroes(Heroe hActivo){
	  for (Heroe h : listHeroe) {
		  if(hActivo.equals(h) == false){
		  	  h.followLider(hActivo);
		  	  int dispara = rand.nextInt(1500);
		  	  if(dispara == 10 && h.getTipo() != 2){
		  		 int direccion = rand.nextInt(4) + 1;
		  		 int keyCode;
		  		 switch(direccion){
		  		 	case 1:
		  		 		keyCode = KeyEvent.VK_UP;
		  		 		break;
		  		 	case 2:
		  		 		keyCode = KeyEvent.VK_DOWN;
		  		 		break;
		  		 	case 3:
		  		 		keyCode = KeyEvent.VK_LEFT;
		  		 		break;
		  		 	case 4:
		  		 		keyCode = KeyEvent.VK_RIGHT;
		  		 		break;
		  		 	default:
		  		 		keyCode = KeyEvent.VK_RIGHT;
		  		 		break;
		  		 }
		  		 if(h.getNumBalas() > 0){
		      		  heroeShoot(keyCode, h);
			    	  if(h.getNumBalas() == 0)
			    		  h.recarga();
			      }
		  	  }
	  	  }
		  h.updateSprite();
	  }
  }
  
  public void updateEnemigos(Heroe hActivo){
	  for(Enemigo ene : listEnemigo){
		  ene.updateSprite();
	 	  ene.seguir(hActivo, ene);
	 	  if(ene.seguir(hActivo, ene)){
	 		 eliminarHeroeEnemigo(hActivo, ene);
	 		 break;
	 	  }
	  }
  }
  
  public void updateBalas(){
	  boolean x = false;
	  try{
		  for(Bala bullet : listBala){
			  bullet.updateSprite();
			  if(bullet.checkLimits()){
				  if(bullet != null)
					  listBala.remove(bullet);
				  	  break;
			  }
			  for(Enemigo ene : listEnemigo){
					int difXB=Math.abs(bullet.getXPosn() - ene.getXPosn() );
					int difYB=Math.abs(bullet.getYPosn() - ene.getYPosn());
					  Rectangle rect = ene.getMyRectangle();
					  if (bullet != null && rect.intersects(bullet.getMyRectangle())) { 
						  if(bullet.isSniper()){
							  clipsLoader.stop("hi");
							  clipsLoader.stop("hi2");
							  clipsLoader.stop("hi3");
							  if(ene != null){
								  ene.setVida(ene.getVida() - 3);
								  if(ene.getVida() > 1){
									  clipsLoader.play("hitZom", false);
									  if(bullet != null)
										  listBala.remove(bullet);
									  x = true;
								  	  break;
								  }else{
									  listEnemigo.remove(ene);
									  puntaje += dificultad*10;
									  eneMuertos ++;
									  System.out.println("mata");
									  numZoombies--;
									  clipsLoader.play("deadZom", false);
									  break;
								  }
							  }
						  }else{
							  clipsLoader.stop("hi");
							  clipsLoader.stop("hi2");
							  clipsLoader.stop("hi3");
							  if(ene != null){
								  if (ene.getVida() > 1){
									  ene.setVida(ene.getVida() - 1);
									  clipsLoader.play("hitZom", false);
								  } else {
									  listEnemigo.remove(ene);
									  puntaje += dificultad*10;
									  eneMuertos ++;
									  numZoombies--;
									  //clipsLoader.stop("hi");
									  clipsLoader.play("deadZom", false);
								  }
								  if(bullet != null)
									  listBala.remove(bullet);
								  	  x = true;
							  	  break;
							  }
						  }
						  
					  }	 
			  }
			  if(x){
				  break;
			  }
		  }
	  }catch(ConcurrentModificationException e){
		  System.out.println("Error en iteracion de lista de balas");
	  }
  }
  
  public void updatePowerUp(){
	  boolean x = false;
	  if(indestructible){
		  if((timeSpentInGame-wait2)>5){
			  indestructible=false;
			  clipsLoader.stop("shield");
			  System.out.println("ya no es indestructible");
		  }
	  }
	  for(PowerUps power : listPowerUp){
		  power.updateSprite();
		  try{
			  Rectangle rect= getHeroeActivo().getMyRectangle(); 
			  Rectangle powerRect = power.getMyRectangle();
			  powerRect.setBounds(power.getXPosn() + 25, power.getYPosn() + 27, power.getWidth() - 45, power.getHeight() - 27);
			  if (power != null && rect.intersects(powerRect)) { 
				  if(getHeroeActivo() != null){
					  clipsLoader.stop("box");
					  if(power.getTipo()==1){
						  tipoPU = "Nuke";
						  waitPU = timeSpentInGame;
						  powerUpEliminarEnemigos();
						  clipsLoader.play("nuke", false);
					  }
					  else if(power.getTipo()==2){
						  getHeroeActivo().powerVeloz();
						  tipoPU = "Flash";
						  waitPU = timeSpentInGame;
						  clipsLoader.play("flash", false);
					  }
					  else if(power.getTipo()==3){
						  tipoPU = "Invencible";
						  waitPU = timeSpentInGame;
						  indestructible=true;
						  //System.out.println("Invecieble");
						  wait2=timeSpentInGame;
						  clipsLoader.play("shield", true);
					  }
					  if(power !=null)
						  listPowerUp.remove(power);
					  	  x=true;
					  break;
				  }
			  }
		  }catch(NullPointerException e){
			  System.out.println("Heroe nulo");
		  }
		  if(x){
			  System.out.println("bye");
			  break;
		  }
	  }
  }
  
  public void liberaHeroe(){
	  Heroe hAct = getHeroeActivo();
	  if(hAct != null){
		  try{
			  if(!listHeroePrisionero.isEmpty()){
				  for (Heroe h : listHeroePrisionero) {
					  //if(hAct.equals(h) == false){
					  if(h.getEstado() == "prisionero" ){
						  Rectangle rect = h.getMyRectangle();
						  if (rect.intersects( hAct.getMyRectangle() )) {     
							  //System.out.println(h.getCurrentState());
							  h.setState(h.getSiguiendo()); //Estado del heroe
							  h.setEstado("sigiendo"); //String de estado
							  h.setImage("H"+ h.getTipo() +"D");
							  System.out.println("Heroe Liberado!");
							  listHeroe.add(h);
							  listHeroePrisionero.remove(h);
							  clipsLoader.play("open", false);
							  totalHeroesLiberados++;
							  System.out.println("Heroes liberados: " + totalHeroesLiberados);
							  break;
						  } 
					  }
				  }
			  }
		  }catch(NullPointerException e) {
		       System.out.println("Lista de pricioneros vacia");
	     }
  	  }
  }
  
  public void cambiarHeroe(Heroe heroe){
	  numh++;
	  int total=0;
	  for (Heroe h : listHeroe) {
		  if(h.getEstado()!="prisionero"){
			  total++;
		  }
	  }
	  if(numh<total){
		  heroe.setState(heroe.getSiguiendo());
		  heroe.setActivo(false);
		  heroe.setEstado("siguiendo");
		  Heroe newActivo=listHeroe.get(numh);
		  newActivo.setState(newActivo.getActivoState());
		  newActivo.setActivo(true);
		  newActivo.setEstado("activo");
		  newActivo.setStep(0, 0);
	  }
	  else{
		  numh=0;
		  
		  heroe.setState(heroe.getSiguiendo());
		  heroe.setActivo(false);
		  heroe.setEstado("siguiendo");
		  
		  Heroe newActivo=listHeroe.getFirst();
		  newActivo.setState(newActivo.getActivoState());
		  newActivo.setActivo(true);
		  newActivo.setEstado("activo");
		  newActivo.setStep(0, 0);
	  }
	  
	  //System.out.println(total+" y "+numh+" INDEX DEL ACTIVO: "+listHeroe.indexOf(getHeroeActivo()));
  }
  
  public void createHeroe()
  // crear heroes prisioneros
  {
	  if(eneMuertos % (5*dificultad) == 0 && crearHeroe){
		  //ImagesLoader imsLoader = new ImagesLoader(IMS_INFO); 
		  //int Result = r.nextInt(High-Low) + Low;
		  int posX = rand.nextInt(PWIDTH - 50);
		  int posY = rand.nextInt(PHEIGHT - 50);
		  int tipo = rand.nextInt(4) + 1;
		  System.out.println("Heroe nuevo tipo: " + tipo);
		  listHeroePrisionero.add(HeroeFactory.getInstance().getHeroeObject(posX, posY, PWIDTH, PHEIGHT, imsLoader,
					(int)(period/1000000L), tipo, false));
		  crearHeroe = false;
	  }
	  if((eneMuertos + 1) % (5*dificultad) == 0){
		  crearHeroe = true;
	  }
  }  // end of createHeroe()
  
  public void createPowerUp(){
	  if(eneMuertos % (6*dificultad) == 0 && crearPowerUp ){
		  int posX = rand.nextInt(PWIDTH - 150);
		  int posY = rand.nextInt(PHEIGHT - 100);
		  int tipo = rand.nextInt(3) + 1;
		  System.out.println("poder nuevo tipo: " + tipo);
		  PowerUps pu = (PowerUpFactory.getInstance().getPowerUpsObject(posX, posY, 
				  PWIDTH, PHEIGHT, imsLoader,tipo));
		  pu.loopImage((int)(period/1000000L), 4);
		  listPowerUp.add(pu);
		  clipsLoader.play("box", false);
		  crearPowerUp=false;
	  }
	  if((eneMuertos + 1) % (6*dificultad) == 0){
		  crearPowerUp = true;
	  }
  }
  
  public void createEnemigo()
  // crear enemigos
  {
	  if(timeSpentInGame % 3 == 0 ){
		  //ImagesLoader imsLoader = new ImagesLoader(IMS_INFO); 
		  //int Result = r.nextInt(High-Low) + Low;
		  for(int i = 0; i<dificultad; i++){
			  if(crearEnemigo && !rest && limZoombiesCrear!=0){
				  int posX;
				  int posY;
				  int entra = rand.nextInt(4);
					switch(entra){
						case 0: //arriba
							posX = (int) (Math.random() * (PWIDTH - 35));
							posY = (0 - 35);
						break;
						case 1: //izquierda
							posX = (0 - 35);
							posY = (int) (Math.random() * (PHEIGHT - 35));
						break;
						case 2: //derecha
							posX = PWIDTH + 35;
							posY = (int) (Math.random() * (PHEIGHT - 35));
						break;
						case 3: //abajo
							posX = (int) (Math.random() * (PWIDTH - 35));
							posY = PHEIGHT + 35;
						break;
						default:
							posX = (int) (Math.random() * (PWIDTH - 35));
							posY = (0 - 35);
						break;
					}
				  int tipo = rand.nextInt(10) + 1;
				  int vida = 3;
				  listEnemigo.add(EnemigoFactory.getInstance().getEnemigoObject(posX, posY, PWIDTH, PHEIGHT, imsLoader,
					  		(int)(period/1000000L), tipo, vida));
				  limZoombiesCrear--;
				  Enemigo enemigo = listEnemigo.getLast();
				  enemigo.stayStill();
				  ///--Sonidos
				  //int snd = rand.nextInt(3) + 1;
				  int snd = 1;
				  //System.out.println(snd);
				  if(tipo == 9 || tipo == 10){
					  clipsLoader.play("rat", false);
				  }else{
					  if(tipo % 3 == 0){
						  clipsLoader.play("hi", false);
					  }
					  else if(tipo % 3 == 1){
						  clipsLoader.play("hi2", false);
					  }
					  else if(tipo % 3 == 2){
						  clipsLoader.play("hi3", false);
					  }  
				  }
			  }
		  }
		  crearEnemigo = false; 
	  }
	  if((timeSpentInGame + 1) % 3 == 0){
		  crearEnemigo = true;
	  }
  }  // end of createEnemigo()
  
  
  public Heroe getHeroeActivo()
  // regresa heroe activo
  {
	  Heroe hActivo = null;
	  for (Heroe h : listHeroe) {
		  if(h.isActivo())
			  hActivo = h;
	  }
	  if(hActivo == null){
		  if(!gameOver)
		  gameOver();
	  }
	  return hActivo;
  }  // end of getHeroeActivo()
  
  
private void eliminarHeroeEnemigo(Heroe h, Enemigo e){
	  Heroe heroe = h;
	  Enemigo enemigo = e;
		  //enemigo ataca
		  enemigo.stayStill();
		  if(enemigo != null){
			  if(enemigo.getVida() > 1){
				  enemigo.setVida(enemigo.getVida() - 1) ;
				  //clipsLoader.stop("hi");
				  clipsLoader.play("zomAtack", false);
			  } else {
				  listEnemigo.remove(enemigo);
				  eneMuertos ++;
				  numZoombies--;
				  clipsLoader.stop("hi");
				  clipsLoader.play("deadZom", false);
				  puntaje += dificultad*10;
			  }
		  }
		  if(!indestructible){
			  if(heroe != null){
				  listHeroe.remove(heroe);
				  clipsLoader.play("hitHeroe", false);  
			  }
			  if(heroesLibres() != 0){
				  Heroe newHeroe = listHeroe.getFirst();
				  newHeroe.setState(newHeroe.getActivoState());
				  newHeroe.setActivo(true);
				  newHeroe.setEstado("activo");
				  newHeroe.setStep(0, 0);
				  indestructible=true;
				  //System.out.println("Invecieble");
				  wait2=timeSpentInGame-5;
			  }
			  
		  }
		  
  }
  
public void powerUpEliminarEnemigos(){
	  int cuantos=0;
	  while (!listEnemigo.isEmpty()) {
		  	createPowerUp();
		  	createHeroe();
	        listEnemigo.removeFirst();
	        puntaje += dificultad*10;
	        eneMuertos ++;
	        cuantos++;
	  }
	  numZoombies-=cuantos;
}
  
private int heroesLibres(){
	  int libres = 0;
	  for (Heroe h : listHeroe) {
		  if(h.getEstado() != "prisionero")
			  libres++;
	  }
	  return libres;
  } 
  

  private void gameRender()
  {
    if(state==0){ //menu
    	clipsLoader.play("menu", true);
    	timePaused=(int) ((J3DTimer.getValue() - gameStartTime)/1000000000L);
    	Graphics menu= this.getGraphics();
    	ImageIcon start= new ImageIcon(getClass().getResource("cargadorImagenes/Images/SaveBrosMenu.png"));
    	menu.drawImage(start.getImage(),0,0,null);    	
	}
    
    else if (state==1){ //juego
    	clipsLoader.stop("menu");
        if (dbImage == null){
            dbImage = createImage(PWIDTH, PHEIGHT);
            if (dbImage == null) {
              System.out.println("dbImage is null");
              return;
            }
            else
              dbg = dbImage.getGraphics();
          }
          // draw the background: use the image or a black colour
          bgImage = imsLoader.getImage("saveBros"+mapa);
          if (bgImage == null) {
            dbg.setColor(Color.black);
            dbg.fillRect (0, 0, PWIDTH, PHEIGHT);
          }
          else
          	dbg.drawImage(bgImage, 0, 0, this);
      		for (Heroe h : listHeroe) {
      			h.drawSprite(dbg);
      		}
      		try{
      			for (Heroe h : listHeroePrisionero) {
      				h.drawSprite(dbg);
      			}
      		}catch(NullPointerException e) {
      		    System.out.println("Lista de pricioneros vacia");
      		}
      		Heroe h = getHeroeActivo();
      		if(h != null){
      			h.drawSprite(dbg);	
      		}
      		for (Enemigo ene : listEnemigo) {
      			ene.drawSprite(dbg);
      		}
      		try{
      			for (Bala bullet : listBala) {
      				bullet.drawSprite(dbg);
      			}
      		}catch(ConcurrentModificationException e) {
      		    System.out.println("Excepcion: iterador de bala en draww");
      		}
      		try{
      			for (PowerUps power : listPowerUp) {
      				power.drawSprite(dbg);
      			}
      		}catch(ConcurrentModificationException e) {
      		    System.out.println("Excepcion: iterador de power en draww");
      		}
      		if(b!=null){
          		b.drawSprite(dbg);
          	}
          reportStats(dbg);
          if (gameOver){
        	  clipsLoader.stop("hi");
        	  clipsLoader.stop("hi2");
        	  clipsLoader.stop("hi3");
        	  clipsLoader.stop("rat");
        	  clipsLoader.stop("hitHeroe");
        	  clipsLoader.stop("zomAtack");
        	  clipsLoader.stop("box");
        	  clipsLoader.stop("deadZom");
        	  clipsLoader.stop("open");
        	  clipsLoader.stop("machine_gun");
        	  clipsLoader.stop("sniper");
        	  clipsLoader.stop("shotgun");
        	  clipsLoader.stop("round");
        	  state = 3;
        	  gameOverMessage(dbg);
        	  clipsLoader.play("lose", false);
        	  //clipsLoader.play("laugh", false);
          }
    }
    
    else if(state ==2){ //help
    	timePaused=(int) ((J3DTimer.getValue() - gameStartTime)/1000000000L);
    	Graphics help= this.getGraphics();
    	ImageIcon back= new ImageIcon(getClass().getResource("cargadorImagenes/Images/SaveBrosHelp.png"));
    	help.drawImage(back.getImage(),0,0,null);
    }
    
    else if (state==3){
    	
    	
    }
  }  // end of gameRender()


  private void reportStats(Graphics g)
  // Report the number of returned balls, and time spent playing
  {
    if (!gameOver && !isPaused)    // stop incrementing the timer once the game is over
      
      timeSpentInGame =-timePaused+
          (int) ((J3DTimer.getValue() - gameStartTime)/1000000000L);  // ns --> secs
    
    

    if(mapa == 0){
    	g.setColor(Color.yellow);
    }else{
    	g.setColor(Color.black);
    }
    g.setFont(msgsFont);

    //Columna 1
	g.drawString("Bros: " + listHeroe.size(), 15, 25);
	Heroe heroe = getHeroeActivo();
	if(heroe != null && heroe.getNumBalas() > 0){
		g.drawString("Municiones: " + heroe.getNumBalas(), 15, 50);
	}else{
		//g.drawString("Recargando: " + timerRecarga.getDelay(), 15, 75);
		g.drawString("Recargando... ", 15, 50);
	}	
	if((timeSpentInGame - waitPU)  <= 5){
		if(tipoPU == "Nuke"){
			g.drawString("PowerUp: "+ tipoPU, 15, 75);
		} else if (tipoPU == "Flash"){
			g.drawString("PowerUp: "+ tipoPU, 15, 75);
		} else if (tipoPU == "Invencible"){
			g.drawString("PowerUp: "+ tipoPU, 15, 75);
		}	
	}else{
		g.drawString(" ", 15, 75);
	}
	
	//Columna 2
	g.drawString("Tiempo: " + timeSpentInGame, 335, 25);
	if(rest){
    g.drawString("Sigiente ronda en: "+(5-(timeSpentInGame-wait3)), 335, 50);
		
	}
	
	//Columna 3
	g.drawString("Ronda: "+ numRonda, 625, 25);
	g.drawString("Zombies Eliminados: "+eneMuertos, 625, 50);
	//puntaje = dificultad*eneMuertos*10;
	g.drawString("Puntaje: "+puntaje, 625, 75);
	
	g.setColor(Color.black);
  }  // end of reportStats()


  private void gameOverMessage(Graphics g)
  // center the game-over message in the panel
  {
    String msg = " Fin Del Juego";
    String msg2 = "  Ronda: " + numRonda;
    String msg3 = "  Zombies Eliminados: " + eneMuertos;
    String msg4 = "  Tiempo: " + timeSpentInGame + " segundos";
    String msg5 = "  Puntaje: " + puntaje;

	g.setColor(Color.black);
	
	ImageIcon over= new ImageIcon(getClass().getResource("cargadorImagenes/Images/gameOverMenu.png"));
	ImageIcon retry= new ImageIcon(getClass().getResource("cargadorImagenes/Images/reiniciar.png"));
	g.drawImage(over.getImage(),(PWIDTH/2-over.getIconWidth()/2),(PHEIGHT/2-over.getIconHeight()/2),null);
	
	g.setFont(new Font("msgsFont", Font.PLAIN, 30));
	g.drawString(msg,(PWIDTH/2-over.getIconWidth()/2)+15, 200);
	
	g.setFont(new Font("msgsFont", Font.PLAIN, 15));
	g.drawString(msg2,(PWIDTH/2-over.getIconWidth()/2)+15, 250);
	g.drawString(msg3,(PWIDTH/2-over.getIconWidth()/2)+15, 300);
	g.drawString(msg4,(PWIDTH/2-over.getIconWidth()/2)+15, 350);
	g.drawString(msg5,(PWIDTH/2-over.getIconWidth()/2)+15, 400);
	
	g.drawImage(retry.getImage(),368,425,190,50,null);
	
	
	
	
  }  // end of gameOverMessage()


  private void paintScreen()
  // use active rendering to put the buffered image on-screen
  { 
    Graphics g;
    try {
      g = this.getGraphics();
      if ((g != null) && (dbImage != null))
        g.drawImage(dbImage, 0, 0, null);
      // Sync the display on some systems.
      // (on Linux, this fixes event queue problems)
      Toolkit.getDefaultToolkit().sync();

      g.dispose();
    }
    catch (Exception e)
    { System.out.println("Graphics context error: " + e);  }
  } // end of paintScreen()
  
  private void restart(){
	  state=0;
	  crearHeroe = false;
	  crearEnemigo = false;
	  crearPowerUp = false;
	  keyPressedFlag = 0;
	  numh=0;
	  wait2=0;
	  wait3=0;
	  waitPU=0;
	  tipoPU="";
	  waitp=0;
	  timePaused=0;
	  rest=false;
	  indestructible=false;
	  numRonda=1;
	  dificultad=1;
	  mapa=0;
	  numZoombies=5;
	  eneMuertos = 0;
	  puntaje = 0;
	  totalHeroesLiberados = 1;
	  limZoombiesCrear=numZoombies;
	  gameOver = false;
	  dbImage = null;
	  dbImage = null;
	  posXHeroe = 50;
	  posYHeroe = 50;
	  
	  listHeroe = new LinkedList<Heroe>();
	  listHeroePrisionero = new LinkedList<Heroe>();
	  listHeroe.add(HeroeFactory.getInstance().getHeroeObject(PWIDTH/2, PHEIGHT/2, PWIDTH, PHEIGHT, imsLoader,
				(int)(period/1000000L), 1, true));
	    
	  listEnemigo = new LinkedList<Enemigo>();
	  listBala = new LinkedList<Bala>();
	  listPowerUp= new LinkedList<PowerUps>();


  }


}  // end of BugPanel class
