package pk.ajneb97.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pk.ajneb97.PlayerKits;
import pk.ajneb97.model.JugadorDatos;
import pk.ajneb97.model.KitJugador;

public class MySQL {
	
	public static boolean isEnabled(FileConfiguration config){
		if(config.getString("Config.mysql_database.enabled").equals("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public static void createTable(PlayerKits plugin) {
        try(Connection connection = plugin.getConnection()) {
        	PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS playerkits_data (`UUID` varchar(200), `PLAYER_NAME` varchar(50), `KIT_NAME` varchar(50), `BUYED` INT(5), `ONE_TIME` INT(5), `COOLDOWN` LONG )");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
//	public static ArrayList<JugadorDatos> getJugadores(PlayerKits plugin){
//		ArrayList<JugadorDatos> jugadores = new ArrayList<JugadorDatos>();
//		try {
//			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM minechess_data");
//			ResultSet resultado = statement.executeQuery();
//			while(resultado.next()){	
//				String uuid = resultado.getString("UUID");
//				int wins = resultado.getInt("WINS");
//				int loses = resultado.getInt("LOSES");
//				int ties = resultado.getInt("TIES");
//				long timePlayed = resultado.getLong("PLAYED_TIME");
//				String name = resultado.getString("PLAYER_NAME");
//				jugadores.add(new JugadorDatos(name,uuid,wins,loses,ties,timePlayed));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return jugadores;
//	}

	public static void getJugadorByUUID(final String uuid,final PlayerKits plugin,final MySQLJugadorCallback callback){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	ArrayList<KitJugador> kitsJugador = new ArrayList<KitJugador>();
            	String nombre = null;
            	try(Connection connection = plugin.getConnection()) {
        			PreparedStatement statement = connection.prepareStatement("SELECT * FROM playerkits_data WHERE uuid=?");
        			statement.setString(1, uuid);
        			ResultSet resultado = statement.executeQuery();
        			while(resultado.next()){	
        				int buyed = resultado.getInt("BUYED");
        				boolean buyedBool = false;
        				if(buyed == 1) {
        					buyedBool = true;
        				}
        				int oneTime = resultado.getInt("ONE_TIME");
        				boolean oneTimeBool = false;
        				if(oneTime == 1) {
        					oneTimeBool = true;
        				}
        				long cooldown = resultado.getLong("COOLDOWN");
        				String kit = resultado.getString("KIT_NAME");
        				kitsJugador.add(new KitJugador(kit,oneTimeBool,cooldown,buyedBool));
        				nombre = resultado.getString("PLAYER_NAME");
        			}
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		if(kitsJugador.size() >= 1) {
        			final JugadorDatos j = new JugadorDatos(nombre,uuid,kitsJugador);
        			Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            callback.alTerminar(j);
                        }
                    });
        		}else {
        			Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            callback.alTerminar(null);
                        }
                    });
        		}
            }
		});
	}
	
	public static void getJugadorByName(final String nombre,final PlayerKits plugin,final MySQLJugadorCallback callback){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	ArrayList<KitJugador> kitsJugador = new ArrayList<KitJugador>();
            	String uuid = null;
            	try(Connection connection = plugin.getConnection()) {
        			PreparedStatement statement = connection.prepareStatement("SELECT * FROM playerkits_data WHERE player_name=?");
        			statement.setString(1, nombre);
        			ResultSet resultado = statement.executeQuery();
        			while(resultado.next()){	
        				int buyed = resultado.getInt("BUYED");
        				boolean buyedBool = false;
        				if(buyed == 1) {
        					buyedBool = true;
        				}
        				int oneTime = resultado.getInt("ONE_TIME");
        				boolean oneTimeBool = false;
        				if(oneTime == 1) {
        					oneTimeBool = true;
        				}
        				long cooldown = resultado.getLong("COOLDOWN");
        				String kit = resultado.getString("KIT_NAME");
        				kitsJugador.add(new KitJugador(kit,oneTimeBool,cooldown,buyedBool));
        				uuid = resultado.getString("UUID");
        			}
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		if(kitsJugador.size() >= 1) {
        			final JugadorDatos j = new JugadorDatos(nombre,uuid,kitsJugador);
        			Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            callback.alTerminar(j);
                        }
                    });
        		}else {
        			Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            callback.alTerminar(null);
                        }
                    });
        		}
            }
		});
	}
	
	public static void reiniciarKitJugador(final PlayerKits plugin, final String name, final String nombreKit){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	try(Connection connection = plugin.getConnection()) {
        			PreparedStatement insert = connection
        					.prepareStatement("DELETE FROM playerkits_data WHERE (player_name=? AND kit_name=?)");
        			insert.setString(1, name);
        			insert.setString(2,nombreKit);
        			insert.executeUpdate();
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
		});
	}
	public static void crearKitJugador(final PlayerKits plugin, final String name, final String uuid, final KitJugador kitJugador){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	if(kitJugador == null) {
            		//Esta creando por primera vez el jugador
            		try(Connection connection = plugin.getConnection()) {
            			PreparedStatement insert = connection
            					.prepareStatement("INSERT INTO playerkits_data (UUID,PLAYER_NAME,KIT_NAME,BUYED,ONE_TIME,COOLDOWN) VALUE (?,?,?,?,?,?)");
            			insert.setString(1, uuid);
            			insert.setString(2, name);
            			insert.setString(3, "name");
            			insert.setInt(4, 0);
            			insert.setInt(5, 0);
            			insert.setLong(6, 0);
            			insert.executeUpdate();
            		} catch (SQLException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            		return;
            	}
            	int resultado = 0;
            	try(Connection connection = plugin.getConnection()) {
            		PreparedStatement statement = connection.prepareStatement("UPDATE playerkits_data SET buyed=?, one_time=?, cooldown=?, player_name=? WHERE (uuid=? AND kit_name=?)");
            		statement.setInt(1,  kitJugador.isBuyed() ? 1: 0);
    				statement.setInt(2, kitJugador.isOneTime() ? 1: 0);
    				statement.setLong(3, kitJugador.getCooldown());
    				statement.setString(4, name);
    				statement.setString(5, uuid);
    				statement.setString(6, kitJugador.getNombre());
    				resultado = statement.executeUpdate();
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	if(resultado == 0) {
            		try(Connection connection = plugin.getConnection()) {
            			PreparedStatement insert = connection.prepareStatement("INSERT INTO playerkits_data (UUID,PLAYER_NAME,KIT_NAME,BUYED,ONE_TIME,COOLDOWN) VALUE (?,?,?,?,?,?)");
            			insert.setString(1, uuid);
            			insert.setString(2, name);
            			insert.setString(3, kitJugador.getNombre());
            			insert.setInt(4, kitJugador.isBuyed() ? 1: 0);
            			insert.setInt(5, kitJugador.isOneTime() ? 1: 0);
            			insert.setLong(6, kitJugador.getCooldown());
            			insert.executeUpdate();
            		} catch (SQLException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	
            }
		});
	}
	public static void actualizarNombre(final PlayerKits plugin, final String name, final String uuid){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	try(Connection connection = plugin.getConnection()) {
            		PreparedStatement statement = connection.prepareStatement("UPDATE playerkits_data SET player_name=? WHERE (uuid=?)");
    				statement.setString(1, name);
    				statement.setString(2, uuid);
    				statement.executeUpdate();
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
		});
	}
}
