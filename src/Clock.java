



/**
 *Contabiliza el numero de ciclos, o cuanto tiempo ha transcurrido
 */
public class Clock {
	
	/**
	 * El número de milisegundos que forman un ciclo.
	 */
	private float millisPerCycle;
	
	/**
	 * Muestra la ultima vez que actualizo el reloj.
	 */
	private long lastUpdate;
	
	/**
	 * El número de ciclos que han transcurrido y aún no se han registrado
	 */
	private int elapsedCycles;
	private float excessCycles;
	
	/**
	 * Verifica si el reloj esta en pausa 
	 */
	private boolean isPaused;
	
	/**
	 * Crea un nuevo reloj y establece sus ciclos por segundo.
	 */
	public Clock(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}
	
	/**
	 * Establece el número de ciclos que transcurren por segundo.
	 */
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
	}
	
	/**
	 * Restablece las estadísticas del reloj, el indicador de pausa se establecerá en falso.
	 */
	public void reset() {
		this.elapsedCycles = 0;
		this.excessCycles = 0.0f;
		this.lastUpdate = getCurrentTime();
		this.isPaused = false;
	}
	
	/**
	 * Actualiza las estadisticas de reloj y el exceso de ciclo se calculará solo si el reloj no está en pausa. 
	 */
	public void update() {
		//Se obtiene la hora actual  y se calcula la hora 
		long currUpdate = getCurrentTime();
		float delta = (float)(currUpdate - lastUpdate) + excessCycles;
		
		//Actualiza los numeros si no estan en pausa
		if(!isPaused) {
			this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
			this.excessCycles = delta % millisPerCycle;
		}
		this.lastUpdate = currUpdate;
	}
	
	/**
	 * Pausa y quita la pausa , y mientras este el juego pausado, el reloj no se actualizará
	 */
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	/**
	 * Comprueba si el reloj esta en pausa
     * @return 
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Comprueba si ya paso un ciclo de reloj , si ya paso, los ciclos se reducen de uno en uno.
     * @return
	 */
	public boolean hasElapsedCycle() {
		if(elapsedCycles > 0) {
			this.elapsedCycles--;
			return true;
		}
		return false;
	}
	
	/**
	 * Comprueba si ya paso un ciclo diferente a ElapsedCycle, el numero de ciclos no se reduce.
	 * @return 
	 */
	public boolean peekElapsedCycle() {
		return (elapsedCycles > 0);
	}
	
	/**
	 * Calcula el tiempo actual en milisegundos
	 */
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}

}
