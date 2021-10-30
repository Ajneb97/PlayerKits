package pk.ajneb97.model;

public class KitJugador {

	private boolean oneTime;
	private long cooldown;
	private boolean buyed;
	private String nombre;
	public KitJugador(String nombre,boolean oneTime, long cooldown, boolean buyed) {
		super();
		this.nombre = nombre;
		this.oneTime = oneTime;
		this.cooldown = cooldown;
		this.buyed = buyed;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isOneTime() {
		return oneTime;
	}
	public void setOneTime(boolean oneTime) {
		this.oneTime = oneTime;
	}
	public long getCooldown() {
		return cooldown;
	}
	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	public boolean isBuyed() {
		return buyed;
	}
	public void setBuyed(boolean buyed) {
		this.buyed = buyed;
	}
	
}
