package su.nightexpress.gamepoints.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.data.users.IUserManager;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.data.objects.StoreUser;

public class UserManager extends IUserManager<GamePoints, StoreUser> {

	public UserManager(@NotNull GamePoints plugin) {
		super(plugin);
	}

	@Override
	@NotNull
	protected StoreUser createData(@NotNull Player player) {
		return new StoreUser(plugin, player);
	}

}
