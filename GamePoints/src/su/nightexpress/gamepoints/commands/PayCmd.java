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

public class PayCmd extends ISubCommand<GamePoints> {

	public PayCmd(@NotNull GamePoints plugin) {
		super(plugin, new String[] {"pay"}, Perms.CMD_PAY);
	}

	@Override
	@NotNull
	public String description() {
		return plugin.lang().Command_Pay_Desc.getMsg();
	}

	@Override
	public boolean playersOnly() {
		return true;
	}

	@Override
	@NotNull
	public String usage() {
		return plugin.lang().Command_Pay_Usage.getMsg();
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
		if (userName.equalsIgnoreCase(sender.getName())) {
			plugin.lang().Error_Self.send(sender);
			return;
		}
		
		StoreUser userTarget = plugin.getUserManager().getOrLoadUser(userName, false);
		if (userTarget == null) {
			this.errPlayer(sender);
			return;
		}
		
		Player from = (Player) sender;
		StoreUser userFrom = plugin.getUserManager().getOrLoadUser(from);
		if (userFrom == null) {
			plugin.lang().Error_NoData.replace("%player%", from.getName()).send(sender);
			return;
		}
		
		int amount = StringUT.getInteger(args[2], 0);
		if (amount <= 0) {
			plugin.lang().Error_Number.replace("%num%", args[2]).send(sender);
			return;
		}
		if (amount > userFrom.getBalance()) {
			plugin.lang().Command_Pay_Error_NoMoney.send(from);
			return;
		}
		
		userTarget.addPoints(amount);
		userFrom.takePoints(amount);
		
		plugin.lang().Command_Pay_Done_Sender
			.replace("%amount%", String.valueOf(amount))
			.replace("%player%", userTarget.getName())
			.send(sender);
		
		Player pTarget = plugin.getServer().getPlayer(userTarget.getName());
		if (pTarget != null) {
			plugin.lang().Command_Pay_Done_User
			.replace("%amount%", String.valueOf(amount))
			.replace("%player%", userFrom.getName())
			.send(pTarget);
		}
	}
}
