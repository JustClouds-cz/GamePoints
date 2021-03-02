package su.nightexpress.gamepoints.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.CollectionsUT;
import su.nexmedia.engine.utils.StringUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.Perms;

public class BaltopCmd extends ISubCommand<GamePoints> {

	public BaltopCmd(@NotNull GamePoints plugin) {
		super(plugin, new String[] {"top", "balancetop", "baltop"}, Perms.USER);
	}

	@Override
	@NotNull
	public String description() {
		return plugin.lang().Command_BalanceTop_Desc.getMsg();
	}

	@Override
	public boolean playersOnly() {
		return false;
	}

	@Override
	@NotNull
	public String usage() {
		return plugin.lang().Command_BalanceTop_Usage.getMsg();
	}

	@Override
	@NotNull
	public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
		if (i == 1) {
	       	return Arrays.asList("1", "2", "<page>");
	    }
		return super.getTab(player, i, args);
	}
	
	@Override
	public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		int page = args.length == 1 ? StringUT.getInteger(args[0], 1) - 1 : 0;
		
		List<List<Map.Entry<String, Integer>>> total = CollectionsUT.split(plugin.getStoreManager().getBalanceTop(), 10);
		int pages = total.size();
		
		if (page >= pages) page = pages - 1;
		if (page < 0) page = 0;
		
		List<Map.Entry<String, Integer>> list = pages > 0 ? total.get(page) : new ArrayList<>();
		int pos = 1 + 10 * page;
		
		for (String line : plugin.lang().Command_BalanceTop_List.asList()) {
			if (line.contains("%player%")) {
				for (Map.Entry<String, Integer> entry : list) {
					sender.sendMessage(line
							.replace("%pos%", String.valueOf(pos++))
							.replace("%points%", String.valueOf(entry.getValue()))
							.replace("%player%", entry.getKey()));
				}
			}
			else {
				sender.sendMessage(line);
			}
		}
	}

}
