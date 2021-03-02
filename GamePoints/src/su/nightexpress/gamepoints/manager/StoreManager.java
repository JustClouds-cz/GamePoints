package su.nightexpress.gamepoints.manager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.manager.api.task.ITask;
import su.nexmedia.engine.utils.CollectionsUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.config.Config;
import su.nightexpress.gamepoints.data.objects.StoreUser;
import su.nightexpress.gamepoints.manager.objects.MainStore;
import su.nightexpress.gamepoints.manager.objects.Store;
import su.nightexpress.gamepoints.manager.objects.StoreProduct;

public class StoreManager {

	private GamePoints plugin;
	private Map<String, Store> stores;
	private List<Map.Entry<String, Integer>> baltop;
	private MainStore mainStore;
	
	private TopTask topTask;
	
	public StoreManager(@NotNull GamePoints plugin) {
		this.plugin = plugin;
	}
	
	public void setup() {
		this.plugin.getConfigManager().extract("stores");
		this.stores = new HashMap<>();
		this.baltop = new ArrayList<>();
		
		for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + "/stores/", true)) {
			Store store = new Store(plugin, cfg);
			this.stores.put(store.getId(), store);
		}
		this.plugin.info("Stores Loaded: " + stores.size());
		
		this.mainStore = new MainStore(plugin, plugin.cfg().getJYML(), "store.");
		
		if (Config.GENERAL_TOP_UPDATE_MIN > 0) {
			this.topTask = new TopTask();
			this.topTask.start();
		}
	}
	
	public void shutdown() {
		if (this.topTask != null) {
			this.topTask.stop();
			this.topTask = null;
		}
		if (this.stores != null) {
			this.stores.values().forEach(store -> store.clear());
			this.stores.clear();
			this.stores = null;
		}
		if (this.mainStore != null) {
			this.mainStore.shutdown();
			this.mainStore = null;
		}
	}
	
	public void openMainStore(@NotNull Player p) {
		this.mainStore.open(p, 1);
	}
	
	public void logTransaction(@NotNull StoreUser user, @NotNull StoreProduct product) {
		String date = LocalDateTime.now().format(Config.TRANSACTION_LOGS_DATE);
		String format = Config.TRANSACTION_LOGS_FORMAT
			.replace("%date%", date)
			.replace("%player%", user.getName())
			.replace("%product%", product.getId())
			.replace("%store%", product.getStore().getId())
			.replace("%price%", String.valueOf(user.getInheritancedPriceForItem(product))
		);
		
		if (Config.TRANSACTION_LOGS_CONSOLE) {
			this.plugin.info(format);
		}
		if (Config.TRANSACTION_LOGS_FILE) {
			BufferedWriter output;
			try {
				output = new BufferedWriter(new FileWriter(plugin.getDataFolder() + "/transactions.log", true));
				output.append(format);
				output.newLine();
				output.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@NotNull
	public List<String> getStoreIds() {
		return new ArrayList<>(stores.keySet());
	}
	
	@Nullable
	public Store getStore(@NotNull String id) {
		return this.stores.get(id.toLowerCase());
	}
	
	@NotNull
	public Collection<Store> getStores() {
		return this.stores.values();
	}
	
	@NotNull
	public List<Map.Entry<String, Integer>> getBalanceTop() {
		return this.baltop;
	}
	
	class TopTask extends ITask<GamePoints> {

		TopTask() {
			super(StoreManager.this.plugin, Config.GENERAL_TOP_UPDATE_MIN * 60, true);
		}

		@Override
		public void action() {
			plugin.info("Updating balance top...");
			long took = System.currentTimeMillis();
			
			Map<String, Integer> map = plugin.getData().getUserBalance();
			map = CollectionsUT.sortByValueUpDown(map);
			
			baltop.clear();
			map.forEach((name, bal) -> {
				baltop.add(new AbstractMap.SimpleEntry<>(name, bal));
			});
			
			took = System.currentTimeMillis() - took;
			plugin.info("Balance top updated in " + took + " ms!");
		}
	}
}
