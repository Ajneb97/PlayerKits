package pk.ajneb97.managers;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pk.ajneb97.PlayerKits;
import pk.ajneb97.model.JugadorDatos;
import pk.ajneb97.model.KitJugador;
import pk.ajneb97.mysql.MySQL;
import pk.ajneb97.mysql.MySQLJugadorCallback;

public class PlayerListener implements Listener{

	private PlayerKits plugin;
	public PlayerListener(PlayerKits plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void alEntrar(PlayerJoinEvent event) {
		final Player jugador = event.getPlayer();
		final JugadorManager jManager = plugin.getJugadorManager();
		
		if(MySQL.isEnabled(plugin.getConfig())) {
			MySQL.actualizarNombre(plugin, jugador.getName(), jugador.getUniqueId().toString());
			MySQL.getJugadorByUUID(jugador.getUniqueId().toString(), plugin, new MySQLJugadorCallback() {
				@Override
				public void alTerminar(JugadorDatos j) {
					jManager.removerJugadorDatos(jugador.getName());
					if(j != null) {
						jManager.agregarJugadorDatos(j);
					}else {
						//Lo crea si no existe
						MySQL.crearKitJugador(plugin, jugador.getName(), jugador.getUniqueId().toString(), null);
						jManager.agregarJugadorDatos(new JugadorDatos(jugador.getName(),jugador.getUniqueId().toString(),new ArrayList<KitJugador>()));
						darKitInicial(jugador);
					}
				}
			});
		}else {
			JugadorDatos j = jManager.getJugadorPorUUID(jugador.getUniqueId().toString());
			if(j == null) {
				j = new JugadorDatos(jugador.getName(),jugador.getUniqueId().toString(),new ArrayList<KitJugador>());
				jManager.agregarJugadorDatos(j);
				darKitInicial(jugador);
			}else {
				j.setPlayer(jugador.getName());
			}
		}
	}
	
	public void darKitInicial(Player jugador) {
		FileConfiguration kitConfig = plugin.getKits();
		if(kitConfig.contains("Kits")) {
			for(String key : kitConfig.getConfigurationSection("Kits").getKeys(false)) {
				if(kitConfig.contains("Kits."+key+".first_join") && kitConfig.getString("Kits."+key+".first_join").equals("true")) {
					KitManager.claimKit(jugador, key, plugin, false, false, false);
				}
			}
		}
	}
}
