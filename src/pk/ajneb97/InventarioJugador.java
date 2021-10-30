package pk.ajneb97;

import org.bukkit.entity.Player;

import pk.ajneb97.managers.InventarioManager;

public class InventarioJugador {

	private Player jugador;
	private int pagina;
	private InventarioManager inventarioManager;
	private String tipoInventario;
	public InventarioJugador(Player jugador, int pagina, InventarioManager inventarioManager, String tipoInventario) {
		this.jugador = jugador;
		this.pagina = pagina;
		this.inventarioManager = inventarioManager;
		this.tipoInventario = tipoInventario;
	}
	
	public String getTipoInventario() {
		return tipoInventario;
	}

	public void setTipoInventario(String tipoInventario) {
		this.tipoInventario = tipoInventario;
	}

	public Player getJugador() {
		return jugador;
	}
	public void setJugador(Player jugador) {
		this.jugador = jugador;
	}
	public int getPagina() {
		return pagina;
	}
	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	public InventarioManager getInventarioManager() {
		return inventarioManager;
	}
	public void setInventarioManager(InventarioManager inventarioManager) {
		this.inventarioManager = inventarioManager;
	}
	
	
}
