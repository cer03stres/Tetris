

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javafx.application.Platform;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JFrame {

	/*
         * Numero de milisegundos por frame
	 */
	private static final long TIEMPO_FRAME = 1000L / 50L;
	
	/**
	 * Numero de piezas que existen
	 */
	private static final int TIPO_PIEZA = TipoPieza.values().length;
		
	/**
	 * Instancia de BorderPanel
	 */
	private BoardPanel board;
	private SidePanel side;
	
	/**
	 * Saber si el juego esta pausado o no
	 */
	private boolean isPaused;
	
	/**
	Si hemos jugado o no un juego, Esto queda en verdadero
        inicialmente y luego se establece en falso cuando comienza el juego.
	 */
	private boolean isNewGame;
	
	/**
	 * Si ya terminó el juego o no
	 */
	private boolean isGameOver;
	
	/**
	 * Muestra el nivel actual en el que estamos jugando
	 */
	private int level;
	
	/**
	 * La puntuacion actual
	 */
	private int score;
	
        private int time;
	/**
	 *Metodo para que las fichas salgan aleatoriamente
	 */
	private Random random;
	
	/**
	 * Reloj de tiempo
	 */
	private Clock logicTimer;
				
	/**
	 * Tipo de ficha 
	 */
	private TipoPieza currentType;
	
	/**
	 * Ficha siguiente
	 */
	private TipoPieza nextType;
		
	/**
	 * Tipo de columna
	 */
	private int currentCol;
	
	/**
	 * Fila
	 */
	private int currentRow;
	
	/**
	 * Rotacion
	 */
	private int currentRotation;
		
	/**
	 * Asegura que se espere un momento en la lapso de cada ficha
	 */
	private int dropCooldown;
	
	/**
	 *Velocidad del juego
	 */
	private float gameSpeed;
        
        private Sonido sonido = new Sonido();
        
       
                
                
		
	/**
	 * Crea una nueva instancia de Tetris. Configura las propiedades de la ventana
	 */
	Tetris() {
		
		super("Proyecto Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);
		
		add(board, BorderLayout.CENTER);
		add(side, BorderLayout.EAST);
                
                
                
                
                
                   
		
		/*
		 * Añadimos un KeyListener
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
								
				switch(e.getKeyCode()) {
				
				/*
				 Cuando presionamos el boton, verificamos que el juego no esta pausado, y tambien un temporizador a velocidad
                                 * De 10 ciclos por segundo
				 */
				case KeyEvent.VK_S:
					if(!isPaused && dropCooldown == 0) {
						logicTimer.setCyclesPerSecond(10.0f);
					}
					break;
                                        
                                        // Aumentar un poco mas la velocidad
                                  
                                case KeyEvent.VK_F:
                                    if(!isPaused && dropCooldown == 0){
                                        logicTimer.setCyclesPerSecond(25.0f);   
                        }
                        break;
                                    
				/*
				 * Mover a la izquierda: cuando se presiona, verificamos que el juego no este pausado,ni que el limite este hacia el maximo de la izquierda ,y si la posicion es valida 
                                 * se disminuye la columna en 1
				 */
				case KeyEvent.VK_LEFT:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
						currentCol--;
                                                 
                                                  
					}
					break;
					
				/*
				 Mover a la derecha: cuando se presiona, verificamos que el juego no
                                 este pausado y que la posición a la derecha del actual no supere el maximo
                                 si la , incrementamos la columna actual en 1.
				 */
				case KeyEvent.VK_RIGHT:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
						currentCol++;
                                                 
					}
					break;
					
				/*
				 * Girar la ficha hacia el lado izquierdo, pero hace la verfificaion si el juego no esta en pausa
                                 *Y lo gira de acuerdo al tamaño
				 */
				case KeyEvent.VK_DOWN:
					if(!isPaused) {
						rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
                                                
                                                sonido.Fx(3);
					}
					break;
				
				/*
			     * Igualmente como el metodo de arriba, pero este metodo hace girar las fichas al lado derecho, siempre 
                             * Siempre verficando que el juego no este pausado.
				 */
				case KeyEvent.VK_UP:
					if(!isPaused) {
						rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
                                                 
					}
					sonido.Fx(3);break;
					
				/*
				 *Para pausar el juego, se verifica que estemos actualmente en un juego , si es asi para tambien el 
                                 *Temporizador se detiene 
				 */
				case KeyEvent.VK_P:
					if(!isGameOver && !isNewGame) {
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);
                                               
                                                
                                               
					}
					break;
				
				/*
				 * Iniciar el juego de nuevo, cuando se presiona Enter, y verfica si estamos aun en el juego
				 */
				case KeyEvent.VK_ENTER:
					if(isGameOver || isNewGame) {
						resetGame();
                                                                                 sonido.Fx(0);

					}
					break;
                                }        
                                
			}
			
                        
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {
				
				/*
				 * Cuando se le quita la pausa, el juego toma de nuevo su velocidad normalmente.
				 */
				case KeyEvent.VK_S:
					logicTimer.setCyclesPerSecond(gameSpeed);
					logicTimer.reset();
					break;
				}
				
			}
			
		});
		
		/*
		 * Aquí cambiamos el tamaño del marco para contener las instancias del Panel 
                 *del tablero y del Panel lateral y centramos la ventana en la pantalla 
                 *y se la mostramos al usuario.
		 */
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Comienza el juego corriendo e inicializa todo y entra en el ciclo del juego.
	 */
	void startGame() {
		/*
		 * Nuevas variables y numero aleatorios 
		 */
		this.random = new Random();
		this.isNewGame = true;
		this.gameSpeed = 1.0f;

		
		/*
		 * Verificamos que el temporizador evite que el juego se abra antes de que el usuario presione Enter
		 */
		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);
		
		while(true) {
			//Hora de inicio del juego
			long start = System.nanoTime();
			
			//Se actualiza el tiempo.
			logicTimer.update();
			
			/*
			 * Si ha transcurrido un ciclo en el temporizador, podemos actualizar el juego y
                         * mueve nuestra pieza actual hacia abajo.
			 */
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
		
			//Disminuir la velocidad si es necesario
			if(dropCooldown > 0) {
				dropCooldown--;
			}
                        
			
			//Mostramos la ventana al usuario
			renderGame();
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < TIEMPO_FRAME) {
				try {
					Thread.sleep(TIEMPO_FRAME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
                                
			}
                        
		}
                
                
	}
        
	
	/**
	 * Actualizacion del juego
	 */
	private void updateGame() {
		/*
		 * Verifique si la posición de la pieza puede moverse hacia la siguiente fila.
		 */
		if(board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
			//Aqui se incrementa la fila actual
			currentRow++;
		} else {
			/*
			 * Llegamos al fondo del tablero o aterrizamos en otra pieza,
                         * Necesitamos agregar la pieza al tablero.
			 */
			board.addPiece(currentType, currentCol, currentRow, currentRotation);
			
			/*
			 * Verificamos si agregar la nueva pieza resultó en líneas despejadas, si esto ocurre...
                         * Se aumenta la puntuacion del usuario, de la siguiente manera (Por filas)
                         * [1 = 100 puntos, 2 = 200 puntos, 3 = 400 puntos, 4 = 800 puntos]
			 */
			int cleared = board.checkLines();
			if(cleared > 0) {
				score += 50 << cleared;
			}
                        
                        
			
			/*
			 * Aumentamos la velocidad de la siguiente ficha y se actualiza el temporizador.
			 */
			gameSpeed += 0.035f;
			logicTimer.setCyclesPerSecond(gameSpeed);
			logicTimer.reset();
			
			/*
			 * Establece el tiempo de reutilización para que la siguiente pieza no salga volando automáticamente
			 */
			dropCooldown = 25;
			
			/*
			 * Actualiza el nivel de dificultad. Esto no tiene efecto en el juego, y solo es
* u                       utilizado en la cadena "Nivel" en el Panel lateral.
			 */
			level = (int)(gameSpeed * 1.70f);

                        
			/*
			 * Se genera una nueva ficha si es necesario
			 */
			spawnPiece();
		}		
	}
	
	/**
	 * Actualiza el panel lateral con los nuevos datos
	 */
	void renderGame() {
		board.repaint();
		side.repaint();
	}
	
	/**
	 * Restablece las variables del juego a sus valores predeterminados al comienzo, en caso de que sea un nuevo juego
	 */
	private void resetGame() {
		this.level = 1;
		this.score = 0;
		this.gameSpeed = 1.0f;
		this.nextType = TipoPieza.values()[random.nextInt(TIPO_PIEZA)];
		this.isNewGame = false;
		this.isGameOver = false;		
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}
		
	/**
	 * Genera una nueva ficha y restablece las variables de nuestra pieza a su valor predeterminado
	 */
	private void spawnPiece() {
		/*
		 * Encuesta la última ficha y restablece nuestra posición y rotación a
                 * sus variables predeterminadas, y muestra en pantalla la siguiente ficha a caer.
		 */
		this.currentType = nextType;
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;
		this.nextType = TipoPieza.values()[random.nextInt(TIPO_PIEZA)];
		
		/*
		 * Si el punto de generación no es válido cuando caen las fichas, debemos pausar el juego y marcar que hemos perdido.
                 * porque significa que las piezas en el tablero se han elevado demasiado y se salen del rango de la pantalla.
		 */
		if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			this.isGameOver = true;
			logicTimer.setPaused(true);
		}		
	}

	/**
	 Nueva rotacion
	 */
	private void rotatePiece(int newRotation) {
		/*
		 * A veces, las piezas deberán moverse cuando se giran para evitar que traspasen la pantalla.
		 */
		int newColumn = currentCol;
		int newRow = currentRow;
		
		/*
		 * Determina cuantas columnas hay en cada lado.
		 */
		int left = currentType.getLeftInset(newRotation);
		int right = currentType.getRightInset(newRotation);
		int top = currentType.getTopInset(newRotation);
		int bottom = currentType.getBottomInset(newRotation);
		
		/*
		 * Si la pieza actual está demasiado recosada hacia la izquierda o la derecha, aleja la ficha de los bordes
		 */
		if(currentCol < -left) {
			newColumn -= currentCol - left;
		} else if(currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
			newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
		}
		
		/*
		 * Si la pieza actual está demasiado lejos de la parte superior o inferior, aleje la pieza de los bordes
		 */
		if(currentRow < -top) {
			newRow -= currentRow - top;
		} else if(currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
			newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
		}
		
		/*
		 * Verifique si la nueva posición es aceptable. Si es así, actualice la rotación de nuevo.
		 */
		if(board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
			currentRotation = newRotation;
			currentRow = newRow;
			currentCol = newColumn;
		}
	}
	
	/**
	 * De nuevo se verifica si el juego esta pausado o no.

	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Si el juego ha terminado
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	/**
	 * Comprueba si estamos en un nuevo juego
	 */
	public boolean isNewGame() {
		return isNewGame;
	}
	
	/**
	 * Devuleve la puntuacion
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Devuelve el nivel actual
	 */
	public int getLevel() {
		return level;
	}
        public int getTime(){
            return time;
        }
	
	/**
	 *Muestra el tipo actual de la pieza que estamos usando
	 */
	public TipoPieza getTipoPieza() {
		return currentType;
	}
	
	/**
	 *Muestra la siguiente pieza a mostrar 
	 */
	public TipoPieza getNextPieceType() {
		return nextType;
	}
	
	/**
	 * Colomna de la pieza actual
	 */
	public int getPieceCol() {
		return currentCol;
	}
	
	/**
	 *Obtiene la fila de la pieza actual.
	 */
	public int getPieceRow() {
		return currentRow;
	}
	
	/**
	 * Rotacion de la pieza actual
	 * @return The rotation.
	 */
	public int getPieceRotation() {
		return currentRotation;
	}
        
      
   
       

	/**
	 * Punto de entrada del juego, este el responsable de crear y comenzar un nuevo juego
	 * @param args Unused.
	 */
	public static void main(String[] args) {
		Tetris tetris = new Tetris();
		tetris.startGame();
                
               
	}

   
    }


