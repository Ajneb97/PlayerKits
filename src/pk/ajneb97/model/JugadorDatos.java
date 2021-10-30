package pk.ajneb97.model;

import java.util.ArrayList;


public class JugadorDatos {

	private String player;
	private String uuid;
	private ArrayList<KitJugador> kits;
	public JugadorDatos(String player, String uuid, ArrayList<KitJugador> kits) {
		super();
		this.player = player;
		this.uuid = uuid;
		this.kits = kits;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public ArrayList<KitJugador> getKits() {
		return kits;
	}
	public void setKits(ArrayList<KitJugador> kits) {
		this.kits = kits;
	}
}
