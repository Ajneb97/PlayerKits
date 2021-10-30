package pk.ajneb97.managers;

import org.bukkit.scheduler.BukkitRunnable;

import pk.ajneb97.PlayerKits;

public class PlayerDataSaveTask {

	private PlayerKits plugin;
	private boolean stop;
	public PlayerDataSaveTask(PlayerKits plugin) {
		this.plugin = plugin;
		this.stop = false;
	}
	
	public void end() {
		this.stop = true;
	}
	
	public void start() {
		int timeSeconds = plugin.getConfig().getInt("Config.player_data_save_time");
		new BukkitRunnable() {
			@Override
			public void run() {
				if(stop) {
					this.cancel();
				}else {
					execute();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 20L*timeSeconds);
	}
	
	public void execute() {
		plugin.getJugadorManager().guardarJugadores();
	}
}
