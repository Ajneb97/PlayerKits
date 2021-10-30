package pk.ajneb97;

import org.bukkit.entity.Player;

public class KitEditando {

	private Player jugador;
	private String kit;
	private String paso;
	private String tipoDisplay;
	public KitEditando(Player jugador, String kit, String tipoDisplay) {
		this.jugador = jugador;
		this.kit = kit;
		this.tipoDisplay = tipoDisplay;
		this.paso = "";
	}
	public Player getJugador() {
		return jugador;
	}
	public void setJugador(Player jugador) {
		this.jugador = jugador;
	}
	public String getKit() {
		return kit;
	}
	public void setKit(String kit) {
		this.kit = kit;
	}
	public void setPaso(String paso) {
		this.paso = paso;
	}
	public String getPaso() {
		return this.paso;
	}
	public String getTipoDisplay() {
		return tipoDisplay;
	}
	public void setTipoDisplay(String tipoDisplay) {
		this.tipoDisplay = tipoDisplay;
	}
	
}
