package su.nightexpress.gamepoints.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.PlayerUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.Perms;
import su.nightexpress.gamepoints.data.objects.StoreUser;

public class BalanceCmd extends ISubCommand<GamePoints> {

	public BalanceCmd(@NotNull GamePoints plugin) {
		super(plugin, new String[] {"balance", "bal"}, Perms.USER);
	}

	@Override
	@NotNull
	public String description() {
		return plugin.lang().Command_Balance_Desc.getMsg();
	}

	@Override
	public boolean playersOnly() {
		return false;
	}

	@Override
	@NotNull
	public String usage() {
		return plugin.lang().Command_Balance_Usage.getMsg();
	}

	@Override
	@NotNull
	public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
		if (i == 1) {
			return PlayerUT.getPlayerNames();
		}
		return super.getTab(player, i, args);
	}
	
	@Override
	public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if ((args.length < 2 && !(sender instanceof Player)) || args.length > 2) {
			this.printUsage(sender);
			return;
		}
		
		String userName = args.length == 2 ? args[1] : sender.getName();
		StoreUser user = plugin.getUserManager().getOrLoadUser(userName, false);
		if (user == null) {
			this.errPlayer(sender);
			return;
		}
		
		plugin.lang().Command_Balance_Done
			.replace("%balance%", user.getBalance())
			.replace("%player%", user.getName())
			.send(sender);
	}
}
