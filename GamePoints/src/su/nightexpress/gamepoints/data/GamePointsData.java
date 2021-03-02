package su.nightexpress.gamepoints.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.google.gson.reflect.TypeToken;

import su.nexmedia.engine.data.DataTypes;
import su.nexmedia.engine.data.IDataHandler;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.data.objects.StoreUser;

public class GamePointsData extends IDataHandler<GamePoints, StoreUser> {

	private static GamePointsData instance;
	
	private final Function<ResultSet, StoreUser> FUNC_USER;
	
	GamePointsData(@NotNull GamePoints plugin) throws SQLException {
		super(plugin);
		
		this.FUNC_USER = (rs) -> {
			try {
				UUID uuid = UUID.fromString(rs.getString(COL_USER_UUID));
				String name = rs.getString(COL_USER_NAME);
				long lastOnline = rs.getLong(COL_USER_LAST_ONLINE);
				
		    	int balance = rs.getInt("balance");
		    	Map<String, Set<String>> items = gson.fromJson(rs.getString("items"), new TypeToken<Map<String, Set<String>>>(){}.getType());
		    	
				return new StoreUser(plugin, uuid, name, lastOnline, balance, items);
			}
			catch (SQLException e) {
				return null;
			}
		};
	}

	@NotNull
	public static synchronized GamePointsData getInstance(@NotNull GamePoints plugin) throws SQLException {
		if (instance == null) {
			instance = new GamePointsData(plugin);
		}
		return instance;
	}
	
	@NotNull
    public Map<String, Integer> getUserBalance() {
    	Map<String, Integer> map = new HashMap<>();
    	String sql = "SELECT `name`, `balance` FROM " + this.TABLE_USERS;
    	
    	this.con = this.getConnection();
    	try (Statement ps = con.createStatement()) {
    		ResultSet rs = ps.executeQuery(sql);
    		while (rs.next()) {
            	String name = rs.getString("name");
            	int d = rs.getInt("balance");
            	
            	map.put(name, d);
            }
            return map;
    	}
    	catch (SQLException e) {
    		plugin.error("SQL Error!");
    		e.printStackTrace();
    		return map;
        }
    }
	
	@Override
	@NotNull
	protected LinkedHashMap<String, String> getColumnsToCreate() {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("items", DataTypes.STRING.build(this.dataType));
		map.put("balance", DataTypes.INTEGER.build(this.dataType));
		return map;
	}

	@Override
	@NotNull
	protected LinkedHashMap<String, String> getColumnsToSave(@NotNull StoreUser user) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("items", this.gson.toJson(user.getBoughtItems()));
		map.put("balance", String.valueOf(user.getBalance()));
		return map;
	}

	@Override
	@NotNull
	protected Function<ResultSet, StoreUser> getFunctionToUser() {
		return this.FUNC_USER;
	}
}
