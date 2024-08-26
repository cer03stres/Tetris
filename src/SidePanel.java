

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The {@code SidePanel} class is responsible for displaying various information
 * on the game such as the next piece, the score and current level, and controls.
 * @author Brendan Jones
 *
 */
public class SidePanel extends JPanel {

	private static final int TILE_SIZE = BoardPanel.TILE_SIZE >> 1;
	
	/**
	 * Ancho de celdas
	 */
	private static final int SHADE_WIDTH = BoardPanel.SHADE_WIDTH >> 1;
	
	/**
	 * El número de filas y columnas en la ventana de vista previa.
	 */
	private static final int TILE_COUNT = 5;
	
	/**
	 * Centro del cuadro
	 */
	private static final int SQUARE_CENTER_X = 140;
	
	/**
	 * Vista previa de la siguiente pieza
	 */
	private static final int SQUARE_CENTER_Y = 65;
	
	/**
	 * El tamaño del cuadro de vista previa de la siguiente pieza.
	 */
	private static final int SQUARE_SIZE = (TILE_SIZE * TILE_COUNT >> 1);
	private static final int SMALL_INSET = 20;
	
	/**
	 * Numero de pixeles .
	 */
	private static final int LARGE_INSET = 40;
	
	/**
	 * Coordernas y
	 */
	private static final int STATS_INSET = 175;
	
	/**
	 * Controles de coordenas y
	 */
	private static final int CONTROLS_INSET = 300;
	
	/**
	 * Numero de piexeles en cada cadena de caracteres 
	 */
	private static final int TEXT_STRIDE = 25;
	
	/**
	 * Tipo de fuente pequeña.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 11);
	
	/**
	 * Tipo de fuente grande.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 13);
	
	/**
	 * El color para dibujar el texto y el cuadro de vista previa.
	 */
	private static final Color DRAW_COLOR = new Color(128, 192, 128);
	
	/**
	 * Instancias del tetris
	 */
	private Tetris tetris;
	
	/**
	 * Se crea un panel para mostrar las propiedades 
     * @param tetris
	 */
	public SidePanel(Tetris tetris) {
		this.tetris = tetris;
		
		setPreferredSize(new Dimension(200, BoardPanel.PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Color para dibular
		g.setColor(DRAW_COLOR);
		
		/*
		 * Esta variable almacena la coordenada y actual de la cadena.
		 */
		int offset;
		
		/*
		 * Dibuja las caracteristicas en pantalla.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Estadisticas", SMALL_INSET, offset = STATS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("Nivel: " + tetris.getLevel(), LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Puntuación: " + tetris.getScore(), LARGE_INSET, offset += TEXT_STRIDE);
                g.drawString("Tiempo:"+ tetris.getTime(), LARGE_INSET, offset +=TEXT_STRIDE);
		
		/*
		 * Dibuja en pantalla los controles.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Controles", SMALL_INSET, offset = CONTROLS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("<- - Mover a la izquierda", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("-> - Mover a la derecha", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Arriba- Rotar hacia la izq", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Abajo- Rotar hacia la der", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("S - Bajar Mas Rapido", LARGE_INSET, offset += TEXT_STRIDE);
                g.drawString("F - Bajar  a Fondo", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("P - Pausar y Despausar ", LARGE_INSET, offset += TEXT_STRIDE);
                
		
		/*
		 * Dibula la proxima ficha en pantalla.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Proxima ficha:", SMALL_INSET, 70);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
		
		/*
		 Dibuja una vista previa de la siguiente pieza que se generará
		 */
		TipoPieza type = tetris.getNextPieceType();
		if(!tetris.isGameOver() && type != null) {
			/*
			 * GCaracteristicas de la ficha.
			 */
			int cols = type.getCols();
			int rows = type.getRows();
			int dimension = type.getDimension();
		
			/*
			 * Calcula la esquina superior izquierda  de la ficha
			 */
			int startX = (SQUARE_CENTER_X - (cols * TILE_SIZE / 2));
			int startY = (SQUARE_CENTER_Y - (rows * TILE_SIZE / 2));
			int top = type.getTopInset(0);
			int left = type.getLeftInset(0);
		
			/*
			 * Recorre la pieza y dibuje sus fichas en la vista previa.
			 */
			for(int row = 0; row < dimension; row++) {
				for(int col = 0; col < dimension; col++) {
					if(type.isTile(col, row, 0)) {
						drawTile(type, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
	}
	
	/**
	 * Dibuja en la ventana de vista previa 
	 * @param type The type of tile to draw.
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @param g The graphics object.
	 */
	private void drawTile(TipoPieza type, int x, int y, Graphics g) {
		/*
		 * Rellena el resto con el color base.
		 */
		g.setColor(type.getBaseColor());
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		g.setColor(type.getDarkColor());
		g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
		g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
		g.setColor(type.getLightColor());
		for(int i = 0; i < SHADE_WIDTH; i++) {
			g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
		}
	}
	
}
