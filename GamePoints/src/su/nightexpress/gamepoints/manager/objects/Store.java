package su.nightexpress.gamepoints.manager.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.manager.LoadableItem;
import su.nexmedia.engine.manager.api.Cleanable;
import su.nexmedia.engine.utils.ItemUT;
import su.nexmedia.engine.utils.StringUT;
import su.nexmedia.engine.utils.constants.JStrings;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.Perms;

public class Store extends LoadableItem implements Cleanable {

	private String name;
	private boolean isPermission;
	private Map<String, StoreProduct> products;
	
	private StoreView view;
	
	// Unfinished
	public Store(@NotNull GamePoints plugin, @NotNull String path) {
		super(plugin, path);
		
		this.name = "&aNew Store";
		this.isPermission = false;
		this.products = new HashMap<>();
	}

	public Store(@NotNull GamePoints plugin, @NotNull JYML cfg) {
		super(plugin, cfg);
		
		this.name = StringUT.color(cfg.getString("name", ""));
		this.isPermission = cfg.getBoolean("need-permission");
		
		this.products = new HashMap<>();
		for (String pId : cfg.getSection("items")) {
			String path = "items." + pId + ".";
			
			int pCost = cfg.getInt(path + "cost");
			boolean pOneTime = cfg.getBoolean(path + "one-time-buy");
			int pPriority = cfg.getInt(path + "priority");
			Set<String> pInherProduct = new HashSet<>(cfg.getStringList(path + "inheritance-item"));
			Set<String> pInherPrice = new HashSet<>(cfg.getStringList(path + "inheritance-price"));
			ItemStack pIcon = cfg.getItem(path + "icon");
			if (ItemUT.isAir(pIcon)) {
				plugin.error("Invalid product icon for '" + pId + "' in '" + getId() + "' store!");
				continue;
			}
			List<String> pCmds = cfg.getStringList(path + "commands");
			int pSlot = cfg.getInt(path + "store-slot");
			
			StoreProduct product = new StoreProduct(
					this, pId, 
					pCost, pOneTime, pPriority,
					pInherProduct, pInherPrice,
					pIcon, pCmds, pSlot);
			
			this.products.put(product.getId(), product);
		}
		
		this.view = new StoreView(this);
	}

	// Unfinished
	@Override
	protected void save(@NotNull JYML cfg) {
		
	}
	
	@Override
	public void clear() {
		if (this.view != null) {
			this.view.shutdown();
			this.view = null;
		}
		if (this.products != null) {
			this.products.clear();
			this.products = null;
		}
	}
	
	public void open(@NotNull Player player) {
		if (!this.hasPermission(player)) {
			GamePoints plugin = (GamePoints) this.plugin;
			plugin.lang().Store_Open_Error_Perm.replace("%store%", this.getName()).send(player);
			return;
		}
		this.view.open(player, 1);
	}

	public boolean hasPermission(@NotNull Player player) {
		if (!this.isPermissionRequired()) return true;
		
		return player.hasPermission(Perms.STORE_USE + this.getId()) 
				|| player.hasPermission(Perms.STORE_USE + JStrings.MASK_ANY);
	}

	public boolean isPermissionRequired() {
		return this.isPermission;
	}
	
	@NotNull
	public String getName() {
		return this.name;
	}
	
	@Nullable
	public StoreProduct getItemById(@NotNull String id) {
		return this.products.get(id.toLowerCase());
	}
	
	@NotNull
	public Collection<StoreProduct> getItems() {
		return this.products.values();
	}
}
