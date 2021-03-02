package su.nightexpress.gamepoints.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.PlayerUT;
import su.nexmedia.engine.utils.StringUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.Perms;
import su.nightexpress.gamepoints.data.objects.StoreUser;

public class SetCmd extends ISubCommand<GamePoints> {

	public SetCmd(@NotNull GamePoints plugin) {
		super(plugin, new String[] {"set"}, Perms.ADMIN);
	}

	@Override
	@NotNull
	public String description() {
		return plugin.lang().Command_Set_Desc.getMsg();
	}

	@Override
	public boolean playersOnly() {
		return false;
	}

	@Override
	@NotNull
	public String usage() {
		return plugin.lang().Command_Set_Usage.getMsg();
	}

	@Override
	@NotNull
	public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
		if (i == 1) {
			return PlayerUT.getPlayerNames();
		}
		if (i == 2) {
			return Arrays.asList("<amount>", "1", "10", "50", "100");
		}
		return super.getTab(player, i, args);
	}
	
	@Override
	public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (args.length != 3) {
			this.printUsage(sender);
			return;
		}
		
		String userName = args[1];
		StoreUser user = plugin.getUserManager().getOrLoadUser(userName, false);
		if (user == null) {
			this.errPlayer(sender);
			return;
		}
		
		int amount = StringUT.getInteger(args[2], user.getBalance());
		
		user.setBalance(amount);
		
		plugin.lang().Command_Set_Done_Sender
			.replace("%balance%", user.getBalance())
			.replace("%player%", user.getName())
			.send(sender);
		
		Player player = user.getPlayer();
		if (player != null) {
			plugin.lang().Command_Set_Done_User
				.replace("%amount%", amount)
				.replace("%player%", user.getName())
				.send(player);
		}
	}
}
