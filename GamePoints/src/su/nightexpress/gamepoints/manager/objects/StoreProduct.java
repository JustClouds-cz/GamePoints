package su.nightexpress.gamepoints.manager.objects;

import java.util.List;
import java.util.Set;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StoreProduct {
	
	private final Store store;
	private final String id;
	
	private int cost;
	private boolean oneTime;
	private int priority;
	private Set<String> inheritProducts;
	private Set<String> inheritPrice;
	private ItemStack icon;
	private List<String> commands;
	private int storeSlot;
	
	public StoreProduct(
			@NotNull Store store,
			@NotNull String id,
			int cost,
			boolean oneTime,
			int priority,
			@NotNull Set<String> inheritProducts,
			@NotNull Set<String> inheritPrice,
			@NotNull ItemStack icon,
			@NotNull List<String> commands,
			int storeSlot
			) {
		this.store = store;
		this.id = id.toLowerCase();
		this.setCost(cost);
		this.setOneTimeBuy(oneTime);
		this.setPriority(priority);
		this.inheritProducts = inheritProducts;
		this.inheritPrice = inheritPrice;
		this.setIcon(icon);
		this.commands = commands;
		this.storeSlot = storeSlot;
	}
	
	@NotNull
	public Store getStore() {
		return this.store;
	}
	
	@NotNull
	public String getId() {
		return this.id;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public void setCost(int cost) {
		this.cost = Math.max(0, cost);
	}
	
	public boolean isOneTimeBuy() {
		return this.oneTime;
	}
	
	public void setOneTimeBuy(boolean oneTime) {
		this.oneTime = oneTime;
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@NotNull
	public Set<String> getInheritanceProducts() {
		return this.inheritProducts;
	}
	
	@NotNull
	public Set<String> getInheritancePrice() {
		return this.inheritPrice;
	}
	
	@NotNull
	public ItemStack getIcon() {
		return new ItemStack(this.icon);
	}
	
	public void setIcon(@NotNull ItemStack icon) {
		this.icon = new ItemStack(icon);
	}
	
	@NotNull
	public List<String> getCommands() {
		return this.commands;
	}
	
	public int getStoreSlot() {
		return this.storeSlot;
	}
}
