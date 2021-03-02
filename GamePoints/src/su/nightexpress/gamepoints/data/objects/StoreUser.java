package su.nightexpress.gamepoints.data.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.data.users.IAbstractUser;
import su.nexmedia.engine.utils.ItemUT;
import su.nexmedia.engine.utils.PlayerUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.config.Config;
import su.nightexpress.gamepoints.manager.objects.Store;
import su.nightexpress.gamepoints.manager.objects.StoreProduct;

public class StoreUser extends IAbstractUser<GamePoints> {

	private int balance;
	private Map<String, Set<String>> items;
	
	public StoreUser(@NotNull GamePoints plugin, @NotNull Player player) {
		this(plugin, player.getUniqueId(), player.getName(), System.currentTimeMillis(), 
			Config.GENERAL_START_BALANCE, new HashMap<>());
	}
	
	public StoreUser(
			@NotNull GamePoints plugin,
			@NotNull UUID uuid,
			@NotNull String name,
			long login,
			int balance,
			@NotNull Map<String, Set<String>> items
			) {
		super(plugin, uuid, name, login);
		this.setBalance(balance);
		this.items = items;
	}
	
	public int getBalance() {
		return this.balance;
	}
	
	public void addPoints(int amount) {
		this.setBalance(this.getBalance() + amount);
	}
	
	public void takePoints(int amount) {
		this.addPoints(-amount);
	}
	
	public void setBalance(int balance) {
		this.balance = Math.max(0, balance);
		if (plugin.cfg().dataSaveInstant) {
			plugin.getUserManager().save(this, true);
		}
	}
	
	// --------------------------------------------- //
	
	@NotNull
	public Map<String, Set<String>> getBoughtItems() {
		return this.items;
	}
	
	@NotNull
	public Set<String> getBoughtItems(@NotNull Store store) {
		return this.getBoughtItems(store.getId());
	}
	
	@NotNull
	public Set<String> getBoughtItems(@NotNull String store) {
		return this.items.computeIfAbsent(store.toLowerCase(), set -> new HashSet<>());
	}
	
	// --------------------------------------------- //
	
	public boolean hasItem(@NotNull StoreProduct item) {
		return this.hasItem(item.getStore(), item.getId());
	}
	
	public boolean hasItem(@NotNull Store store, @NotNull String id) {
		return this.getBoughtItems(store).contains(id.toLowerCase());
	}
	
	public boolean hasItem(@NotNull String store, @NotNull String id) {
		return this.getBoughtItems(store).contains(id.toLowerCase());
	}
	
	// --------------------------------------------- //
	
	public boolean buyItem(@NotNull Player player, @NotNull StoreProduct product) {
		
		if (product.isOneTimeBuy() && this.hasItem(product)) {
			plugin.lang().Store_Buy_Error_Have.send(player);
			return false;
		}
		
		int price = this.getInheritancedPriceForItem(product);
		if (this.getBalance() < price) {
			plugin.lang().Store_Buy_Error_NoMoney.send(player);
			return false;
		}
		
		// Take user points.
		this.takePoints(price);
		
		plugin.lang().Store_Buy_Success
			.replace("%price%", String.valueOf(price))
			.replace("%item%", ItemUT.getItemName(product.getIcon()))
			.send(player);
			
		// Exec Item Commands
		Store store = product.getStore();
		List<String> commands = new ArrayList<>(product.getCommands());
			
		for (String id : product.getInheritanceProducts()) {
			StoreProduct inherited = store.getItemById(id);
			
			if (inherited == null) continue;
			commands.addAll(inherited.getCommands());
		}
		
		for (String cmd : commands) {
			PlayerUT.execCmd(player, cmd);
		}
		
		// Log transaction.
		this.plugin.getStoreManager().logTransaction(this, product);
			
		// Add item to user account if it's one-timed
		if (product.isOneTimeBuy()) {
			this.getBoughtItems(store).add(product.getId());
		}
		
		if (plugin.cfg().dataSaveInstant) {
			plugin.getUserManager().save(this, true);
		}
		return true;
	}
	
	public int getInheritancedPriceForItem(@NotNull StoreProduct product) {
		int price = product.getCost();
		if (!product.isOneTimeBuy()) return price;
		
		int userPrior = -Integer.MAX_VALUE;
		
		StoreProduct best = null;
		Store store = product.getStore();
		
		for (String id : product.getInheritancePrice()) {
			StoreProduct inherited = store.getItemById(id);
			
			if (inherited == null || !inherited.isOneTimeBuy()) continue;
			if (!this.hasItem(inherited)) continue;
			if (inherited.getPriority() > userPrior) {
				userPrior = inherited.getPriority();
				best = inherited;
			}
		}
		
		if (best == null) return price;
		
		return Math.max(0, price - best.getCost());
	}
}
