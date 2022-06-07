package pk.ajneb97.managers;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pk.ajneb97.PlayerKits;
import pk.ajneb97.model.JugadorDatos;
import pk.ajneb97.model.KitJugador;
import pk.ajneb97.mysql.MySQL;

public class JugadorManager {

	private PlayerKits plugin;
	private ArrayList<JugadorDatos> jugadores;
	public JugadorManager(PlayerKits plugin) {
		this.plugin = plugin;
		cargarJugadores();
	}
	
	public void cargarJugadores() {
		jugadores = new ArrayList<JugadorDatos>();
		  if(MySQL.isEnabled(plugin.getConfig())) {
//			  this.jugadores = MySQL.getJugadores(this);
			  return;
		  } 
		  FileConfiguration config = plugin.getPlayers();
		  if(config.contains("Players")) {
			  for(String key : config.getConfigurationSection("Players").getKeys(false)) {
				  ArrayList<KitJugador> kits = new ArrayList<KitJugador>();
				  String player = config.getString("Players."+key+".name");
				  for(String kit : config.getConfigurationSection("Players."+key).getKeys(false)) {
					  if(!kit.equals("name")) {
						  boolean isBuyed = false;
						  boolean isOneTime = false;
						  long cooldown = 0;
						  if(config.contains("Players."+key+"."+kit+".buyed")) {
							  isBuyed = config.getBoolean("Players."+key+"."+kit+".buyed");
						  }
						  if(config.contains("Players."+key+"."+kit+".one_time")) {
							  isOneTime = config.getBoolean("Players."+key+"."+kit+".one_time");
						  }
						  if(config.contains("Players."+key+"."+kit+".cooldown")) {
							  cooldown = config.getLong("Players."+key+"."+kit+".cooldown");
						  }
						  kits.add(new KitJugador(kit,isOneTime,cooldown,isBuyed));
					  }
				  } 
				  JugadorDatos j = new JugadorDatos(player,key,kits);
				  this.jugadores.add(j);
			  }
		  }
	  }
	  
	  public void guardarJugadores() {
		  if(MySQL.isEnabled(plugin.getConfig())) {
			  return;
		  }
		  FileConfiguration players = plugin.getPlayers();
		  for(JugadorDatos j : jugadores) {
				String uuid = j.getUuid();
				String name = j.getPlayer();
				players.set("Players."+uuid+".name", name);
				for(KitJugador kit : j.getKits()) {
					String nombreKit = kit.getNombre();
					players.set("Players."+uuid+"."+nombreKit+".cooldown", kit.getCooldown());
					players.set("Players."+uuid+"."+nombreKit+".buyed", kit.isBuyed());
					players.set("Players."+uuid+"."+nombreKit+".one_time", kit.isOneTime());
				}
		  }
		  plugin.savePlayers();
	  }

	  public void agregarJugadorDatos(JugadorDatos jugador) {
		  jugadores.add(jugador);
	  }

	  public void removerJugadorDatos(String jugador) {
		  for(int i=0;i<jugadores.size();i++) {
			  if(jugadores.get(i).getPlayer().equals(jugador)) {
				  jugadores.remove(i);
			  }
		  }
	  }

	  public JugadorDatos getJugadorPorUUID(String uuid) {
		  for(JugadorDatos j : jugadores) {
			  if(j.getUuid().equals(uuid)) {
				  return j;
			  }
		  }
		  return null;
	  }
	  
	  public JugadorDatos getJugadorPorNombre(String nombre) {
		  for(JugadorDatos j : jugadores) {
			  if(j.getPlayer().equals(nombre)) {
				  return j;
			  }
		  }
		  return null;
	  }

	  public ArrayList<JugadorDatos> getJugadores(){
		  return this.jugadores;
	  }
	  
	  public boolean isBuyed(Player jugador,String kit) {
		  JugadorDatos j = getJugadorPorUUID(jugador.getUniqueId().toString());
		  if(j == null) {
			  return false;
		  }
		  for(KitJugador k : j.getKits()) {
			  if(k.getNombre().equals(kit) && k.isBuyed()) {
				  return true;
			  }
		  }
		  return false;
	  }
	  public boolean isOneTime(Player jugador,String kit) {
		  JugadorDatos j = getJugadorPorUUID(jugador.getUniqueId().toString());
		  if(j == null) {
			  return false;
		  }
		  for(KitJugador k : j.getKits()) {
			  if(k.getNombre().equals(kit) && k.isOneTime()) {
				  return true;
			  }
		  }
		  return false;
	  }
	  public long getCooldown(Player jugador,String kit) {
		  JugadorDatos j = getJugadorPorUUID(jugador.getUniqueId().toString());
		  if(j == null) {
			  return 0;
		  }
		  for(KitJugador k : j.getKits()) {
			  if(k.getNombre().equals(kit)) {
				  return k.getCooldown();
			  }
		  }
		  return 0;
	  }
	  
	  public void setBuyed(Player jugador,String kit) {
		  JugadorDatos j = getJugadorPorUUID(jugador.getUniqueId().toString());
		  if(j == null) {
			  j = new JugadorDatos(jugador.getName(),jugador.getUniqueId().toString(),new ArrayList<KitJugador>());
			  agregarJugadorDatos(j);
		  }
		  
		  boolean listo = false;
		  for(KitJugador k : j.getKits()) {
			  if(k.getNombre().equals(kit)) {
				  k.setBuyed(true);
				  listo = true;
				  if(MySQL.isEnabled(plugin.getConfig())) {
					  MySQL.crearKitJugador(plugin, jugador.getName(), j.getUuid(), k);
				  }
				  break;
			  }
		  }
		  
		  if(!listo) {
			  KitJugador kitNuevo = new KitJugador(kit,false,0,true);
			  j.getKits().add(kitNuevo);
			  if(MySQL.isEnabled(plugin.getConfig())) {
				  MySQL.crearKitJugador(plugin, jugador.getName(), j.getUuid(), kitNuevo);
			  }
		  }
	  }  
	  
	  public void setOneTime(Player jugador,String kit) {
		  JugadorDatos j = getJugadorPorUUID(jugador.getUniqueId().toString());
		  if(j == null) {
			  j = new JugadorDatos(jugador.getName(),jugador.getUniqueId().toString(),new ArrayList<KitJugador>());
			  agregarJugadorDatos(j);
		  }
		  
		  boolean listo = false;
		  for(KitJugador k : j.getKits()) {
			  if(k.getNombre().equals(kit)) {
				  k.setOneTime(true);
				  listo = true;
				  if(MySQL.isEnabled(plugin.getConfig())) {
					  MySQL.crearKitJugador(plugin, jugador.getName(), j.getUuid(), k);
				  }
				  break;
			  }
		  }
		  
		  if(!listo) {
			  KitJugador kitNuevo = new KitJugador(kit,true,0,false);
			  j.getKits().add(kitNuevo);
			  if(MySQL.isEnabled(plugin.getConfig())) {
				  MySQL.crearKitJugador(plugin, jugador.getName(), j.getUuid(), kitNuevo);
			  }
		  }
	  } 
	  
	  public void setCooldown(Player jugador,String kit,long millis) {
		  JugadorDatos j = getJugadorPorUUID(jugador.getUniqueId().toString());
		  if(j == null) {
			  j = new JugadorDatos(jugador.getName(),jugador.getUniqueId().toString(),new ArrayList<KitJugador>());
			  agregarJugadorDatos(j);
		  }
		  
		  boolean listo = false;
		  for(KitJugador k : j.getKits()) {
			  if(k.getNombre().equals(kit)) {
				  k.setCooldown(millis);
				  listo = true;
				  if(MySQL.isEnabled(plugin.getConfig())) {
					  MySQL.crearKitJugador(plugin, jugador.getName(), j.getUuid(), k);
				  }
				  break;
			  }
		  }
		  
		  if(!listo) {
			  KitJugador kitNuevo = new KitJugador(kit,false,millis,false);
			  j.getKits().add(kitNuevo);
			  if(MySQL.isEnabled(plugin.getConfig())) {
				  MySQL.crearKitJugador(plugin, jugador.getName(), j.getUuid(), kitNuevo);
			  }
		  }
	  } 
	  
	  public boolean reiniciarKit(String jugador,String kit) {
		  JugadorDatos j = getJugadorPorNombre(jugador);
		  if(j == null) {
			  return false;
		  }
		  
		  ArrayList<KitJugador> kits = j.getKits();
		  for(int i=0;i<kits.size();i++) {
			  if(kits.get(i).getNombre().equals(kit)) {
				  kits.remove(i);
				  if(MySQL.isEnabled(plugin.getConfig())) {
					 MySQL.reiniciarKitJugador(plugin, jugador, kit);
				  }
				  return true;
			  }
		  }
		  
		  return false;
	  }
	  
//	  public void setNombre(Player jugador) {
//		  JugadorDatos j = getJugadorPorUUID(jugador.getUniqueId().toString());
//		  if(j == null) {
//			  j = new JugadorDatos(jugador.getName(),jugador.getUniqueId().toString(),new ArrayList<KitJugador>());
//			  agregarJugadorDatos(j);
//		  }
//		  
//		  j.setPlayer(jugador.getName());
//	  }
}
