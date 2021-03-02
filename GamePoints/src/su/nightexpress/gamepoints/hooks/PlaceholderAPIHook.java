package su.nightexpress.gamepoints.hooks;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import su.nexmedia.engine.hooks.HookState;
import su.nexmedia.engine.hooks.NHook;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.data.objects.StoreUser;
import su.nightexpress.gamepoints.manager.objects.Store;
import su.nightexpress.gamepoints.manager.objects.StoreProduct;

public class PlaceholderAPIHook extends NHook<GamePoints> {

	public PlaceholderAPIHook(@NotNull GamePoints plugin) {
		super(plugin);
	}

	@Override
	@NotNull
	protected HookState setup() {
		new GPExpansion().register();
		return HookState.SUCCESS;
	}

	@Override
	protected void shutdown() {
		
	}

	class GPExpansion extends PlaceholderExpansion {

		@Override
		@NotNull
		public String getAuthor() {
			return plugin.getAuthor();
		}

		@Override
		@NotNull
		public String getIdentifier() {
			return "gamepoints";
		}

		@Override
		@NotNull
		public String getVersion() {
			return plugin.getDescription().getVersion();
		}
		
		@Override
		public String onPlaceholderRequest(Player p, String holder) {
			// %gp_item_rawprice_ranks_rank-vip%
			if (holder.startsWith("item_rawprice")) {
				String[] ss = this.getItemStore("item_rawprice_", holder);
				String storeId = ss[0];
				String itemId = ss[1];
				
				Store store = plugin.getStoreManager().getStore(storeId);
				if (store == null) return null;
				
				StoreProduct item = store.getItemById(itemId);
				if (item == null) return null;
				
				return String.valueOf(item.getCost());
			}
			
			StoreUser user = plugin.getUserManager().getOrLoadUser(p);
			if (user == null) return "NaN";
			
			if (holder.startsWith("item_price")) {
				String[] ss = this.getItemStore("item_price_", holder);
				String storeId = ss[0];
				String itemId = ss[1];
				
				Store store = plugin.getStoreManager().getStore(storeId);
				if (store == null) return null;
				
				StoreProduct item = store.getItemById(itemId);
				if (item == null) return null;
				
				return String.valueOf(user.getInheritancedPriceForItem(item));
			}
			
			if (holder.equalsIgnoreCase("balance")) {
				return String.valueOf(user.getBalance());
			}
			
			return null;
		}
		
		@NotNull
		private String[] getItemStore(@NotNull String prefix, @NotNull String holder) {
			// ranks_rank-vip
			String left = holder.replace(prefix, "");
			int index = left.indexOf('_');
			String storeId = left.substring(0, index);
			String itemId = left.substring(index + 1, left.length());
			
			//System.out.println("store: " + storeId);
			//System.out.println("item: " + itemId);
			
			return new String[] {storeId, itemId};
		}
	}
}
