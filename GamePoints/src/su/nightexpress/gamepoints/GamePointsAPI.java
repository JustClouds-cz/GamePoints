package su.nightexpress.gamepoints;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import su.nexmedia.engine.data.users.IUserManager;
import su.nightexpress.gamepoints.data.objects.StoreUser;
import su.nightexpress.gamepoints.manager.StoreManager;
import su.nightexpress.gamepoints.manager.objects.Store;

public class GamePointsAPI {

	private static GamePoints plugin;
	
	static {
		plugin = GamePoints.getInstance();
	}
	
	@Nullable
	public static StoreUser getUserData(@NotNull Player player) {
		return plugin.getUserManager().getOrLoadUser(player);
	}
	
	@Nullable
	public static StoreUser getUserData(@NotNull String name, boolean uuid) {
		return plugin.getUserManager().getOrLoadUser(name, uuid);
	}
	
	@Nullable
	public static Store getStore(@NotNull String id) {
		return plugin.getStoreManager().getStore(id);
	}
	
	@NotNull
	public static Collection<Store> getStores() {
		return plugin.getStoreManager().getStores();
	}
	
	@NotNull
	public static IUserManager<GamePoints, StoreUser> getUserManager() {
		return plugin.getUserManager();
	}
	
	@NotNull
	public static StoreManager getStoreManager() {
		return plugin.getStoreManager();
	}
}
