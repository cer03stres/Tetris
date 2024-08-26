

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

/**
 * El borderPane es el responsable de mostrar la cuadricula del juego y todo lo relacionado con los paneles del juego
 */
public class BoardPanel extends JPanel {

    
        private Sonido sonido = new Sonido();
            
	/**
	 * Valores mínimos de componentes de color para las cuadriculas
	 */
	public static final int COLOR_MIN = 35;
	
	/**
	 * Parte oscura de la cuadricual
	 */
	public static final int COLOR_MAX = 255 - COLOR_MIN;
	
	/**
	 * El ancho del borde alrededor del tablero de juego.
	 */
	private static final int BORDER_WIDTH = 5;
	
	/**
	 * El número de columnas en el tablero.
	 */
	public static final int COL_COUNT = 10;
		
	/**
	 * Numero de filas
	 */
	private static final int VISIBLE_ROW_COUNT = 20;
	
	/**
	 * Aqui se definen el numero de filas ocultas
	 */
	private static final int HIDDEN_ROW_COUNT = 2;
	
	/**
	 * número total de filas que contiene el tablero.
	 */
	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
	public static final int TILE_SIZE = 24;
	
	/**
	 * Ancho y sombreado
	 */
	public static final int SHADE_WIDTH = 4;
	
	/**
	 * Coordenada central del tablero
	 */
	private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
	
	/**
	 *Coordenada central y en el tablero de juego.
	 */
	private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
		
	/**
	 * Ancho total del panel
	 */
	public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * Altura total
	 */
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * Tipo de fuente mas grande
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

	/**
	 * Tipo de fuente mas pequeña
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);
	
	/**
	 * Instancias del tetris
	 */
	private Tetris tetris;
	
	/**
	 * Fichas que componen el tablero
	 */
	private TipoPieza[][] tiles;
	public BoardPanel(Tetris tetris) {
		this.tetris = tetris;
		this.tiles = new TipoPieza[ROW_COUNT][COL_COUNT];
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.BLACK);
                
	}

    


    
	
	/**
	 * Se reestablece el tablero y lo deja limpio.
	 */
	public void clear() {
		/*
		 * Se recorre el indice del tablero y le establece un valor
		 */
		for(int i = 0; i < ROW_COUNT; i++) {
			for(int j = 0; j < COL_COUNT; j++) {
				tiles[i][j] = null;
                                
			}
                       
		}
	}
        
	
	/**
	Determina si una pieza se puede colocar o no en las coordenadas.
	 */
	public boolean isValidAndEmpty(TipoPieza type, int x, int y, int rotation) {
				
		//Se verifica si la ficha esta en una columna valida
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
			return false;
		}
		
		//Y verifica si esta en una fila valida
		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
			return false;
		}
		
		/*
		 * Recorre cada celda de la ficha y ve si entra en conflicto con alguna celda existente
		 */
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Se grega una ficha al tablero de juego, pero aun no comprueba las fichas existentes.
	 */
	public void addPiece(TipoPieza type, int x, int y, int rotation) {
		/*
		 * Recorre cada celda dentro de la pieza y agrega al tablero solo si es booleano .
		 */
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation)) {
					setTile(col + x, row + y, type);
				}
			}
		}
	}
	
	/**
	 * Comprueba el tablero para ver si se han borrado algunas líneas y
         * los elimina del juego.
	 */
	public int checkLines() {
		int completedLines = 0;
		
		/*
		 * Se recorre cada linea y se verifica si...
                 * Si se ha elminado una linea o no, si es asi, se incrementa el numero de lineas completadas 
                 * y de nuevo se verifica la linea, la función checkLine se encarga de borrar la línea y
                 * Desplazar el resto del tablero hacia abajo.
		 */
		for(int row = 0; row < ROW_COUNT; row++) {
			if(checkLine(row)) {
				completedLines++;
			}
		}
		return completedLines;
	}
			
	/**
	 * Se comprueba si la fila esta llena
	 */
	private boolean checkLine(int line) {
		/*
		 * Si una columna o una fila esta vacia, entonces la fila no se llena
		 */
		for(int col = 0; col < COL_COUNT; col++) {
			if(!isOccupied(col, line)) {
				return false;
			}
		}
		
		/*
		 * Cuando la linea esta llena, necesitamos eliminarlo del juego 
                 * Para hacer esto, simplemente desplazamos cada fila por encima hacia abajo en una.
		 */
		for(int row = line - 1; row >= 0; row--) {
			for(int col = 0; col < COL_COUNT; col++) {
				setTile(col, row + 1, getTile(col, row));
			}
                                         sonido.Fx(2);
                                         break;


		}
		return true;
                
	}
	
	
	/**
	 * Comprueba si las celdas estan vacios
	 * @param x The x coordinate to check.
	 * @param y The y coordinate to check.
	 */
	private boolean isOccupied(int x, int y) {
		return tiles[y][x] != null;
	}
	
	/**
	 * Establece una celda ubicado en la columna y fila deseadas.
	 * @param x The column.
	 * @param y The row.
	 */
	private void setTile(int  x, int y, TipoPieza type) {
		tiles[y][x] = type;
	}
		

	private TipoPieza getTile(int x, int y) {
		return tiles[y][x];
	}
        
        private Image imagen;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Ayuda a que el posicionamiento sean mas simples
		g.translate(BORDER_WIDTH, BORDER_WIDTH);
		
		/*
		 * Dibuja el tablero de manera diferente dependiendo del estado actual del juego.
		 */
		if(tetris.isPaused()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			String msg = new String ("Juego Pausado");
                       
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
		} else if(tetris.isNewGame() || tetris.isGameOver()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			
			/*
			 * Debido a que tanto el juego terminado como las nuevas pantallas son casi idénticas,
                         * podemos manejarlos juntas y solo usar un operador ternario para cambiar
                         * los mensajes que se muestran. (Niveles)
			 */
                        
			String msg = tetris.isNewGame() ? "" : "Juego Terminado";
                        imagen = new ImageIcon(getClass().getResource("/Tetris2.jpg")).getImage();
                        g.drawImage(imagen, 0, 0, getWidth(), getHeight(),this);
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
			msg = "Presiona Enter para jugar" + (tetris.isNewGame() ? "" : " De nuevo");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 240);
		} else {
			
			/*
			 * Dibuja de nuevo las celdas 
			 */
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					TipoPieza tile = getTile(x, y);
					if(tile != null) {
						drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			
			/*
			 * Dibuja la pieza actual. Esto no se puede dibujar como el resto de
                         * piezas porque todavía no es parte del tablero de juego. Si fuera
                         * parte del tablero, debería eliminarse cada cuadro que
                         * sería mas lento.
			 */
			TipoPieza type = tetris.getTipoPieza();
			int pieceCol = tetris.getPieceCol();
			int pieceRow = tetris.getPieceRow();
			int rotation = tetris.getPieceRotation();
			
			//Dibuja la ficha en el tablero
			for(int col = 0; col < type.getDimension(); col++) {
				for(int row = 0; row < type.getDimension(); row++) {
					if(pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
						drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			
			/*
			 * Muestra el tipo de ayuda donde caera la ficha , se toma la poscion actual y nos movemos hacia abajo
                         *hasta que llegue al final 
			 */
			Color base = type.getBaseColor();
			base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
			for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
				//Si no se detecta una colisión, intente la siguiente fila.
				if(isValidAndEmpty(type, pieceCol, lowest, rotation)) {					
					continue;
				}
				
				//Dibuja el fantasma una fila más arriba de la que tuvo lugar la colisión.
				lowest--;
				
				//Dibuja la pieza fantasma.

				for(int col = 0; col < type.getDimension(); col++) {
					for(int row = 0; row < type.getDimension(); row++) {
						if(lowest + row >= 2 && type.isTile(col, row, rotation)) {
							drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}
				
				break;
			}
			
			/*
			 * Dibuja la cuadrícula de fondo encima de las piezas
			 */
			g.setColor(Color.BLACK);
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
					g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}
		}
		
		/*
		 * Dibujar el contorno 
		 */
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
	}
	
	/**
	 * Dibula las celdas en pantalla
	 * @param type The type of tile to draw.
	 * @param x The column.
	 * @param y The row.
	 * @param g The graphics object.
	 */
	private void drawTile(TipoPieza type, int x, int y, Graphics g) {
		drawTile(type.getBaseColor(), type.getLightColor(), type.getBaseColor(), x, y, g);
	}
	
	/**
	 * Dibuja las celdas en las coordenadas
	 * @param base The base color of tile.
	 * @param light The light color of the tile.
	 * @param dark The dark color of the tile.
	 * @param x The column.
	 * @param y The row.
	 * @param g The graphics object.
	 */
	private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {
		
		/*
		 * Rellena el contorno del color base 
		 */
		g.setColor(base);
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		
		/*
		 * Rellenas los bordes con el sombreado oscuro
		 */
		g.setColor(dark);
		g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
		g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
		
		/*
		 * Y el resto lo rellena con sombreado claro 
		 */
		g.setColor(light);
		for(int i = 0; i < SHADE_WIDTH; i++) {
			g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
		}
	}

   

}
