package su.nightexpress.gamepoints.manager.objects;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.config.api.ILangMsg;
import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.manager.api.gui.ContentType;
import su.nexmedia.engine.manager.api.gui.GuiClick;
import su.nexmedia.engine.manager.api.gui.GuiItem;
import su.nexmedia.engine.manager.api.gui.JIcon;
import su.nexmedia.engine.manager.api.gui.NGUI;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.data.objects.StoreUser;

public class StoreView extends NGUI<GamePoints> {

	private Store store;
	
	StoreView(@NotNull Store store) {
		super((GamePoints) store.plugin, store.getConfig(), "");
		this.store = store;
		
		GuiClick click = (p, type, e) -> {
			if (type == null) return;
			
			Class<?> clazz = type.getClass();
			if (!clazz.equals(ContentType.class)) return;
			
			ContentType type2 = (ContentType) type;
			if (type2 == ContentType.RETURN) {
				plugin.getStoreManager().openMainStore(p);
			}
			else if (type2 == ContentType.EXIT) {
				p.closeInventory();
			}
		};
		
		JYML cfg = store.getConfig();
		for (String id : cfg.getSection("content")) {
			GuiItem guiItem = cfg.getGuiItem("content." + id, ContentType.class);
			if (guiItem == null) continue;
			
			if (guiItem.getType() != null) {
				guiItem.setClick(click);
			}
			
			this.addButton(guiItem);
		}
	}

	@Override
	protected void onCreate(@NotNull Player player, @NotNull Inventory inv, int page) {
		StoreUser user = plugin.getUserManager().getOrLoadUser(player);
		if (user == null) return;
		
		for (StoreProduct storeItem : this.store.getItems()) {
			ItemStack stack = storeItem.getIcon();
			this.replaceStoreItem(player, stack, storeItem, user);
			
			JIcon icon = new JIcon(stack);
			icon.setClick((p, type, e) -> {
				user.buyItem(p, storeItem);
				this.open(p, this.getUserPage(p, 0));
			});
			
			this.addButton(player, icon, storeItem.getStoreSlot());
		}
	}

	private void replaceStoreItem(@NotNull Player player, @NotNull ItemStack item, 
			@NotNull StoreProduct storeItem, @NotNull StoreUser user) {
		
		ItemMeta meta = item.getItemMeta();
		if (meta == null) return;
		
		int price = user.getInheritancedPriceForItem(storeItem);
		int balance = user.getBalance();
		
		String costDef = String.valueOf(storeItem.getCost());
		String costUser = String.valueOf(price);
		ILangMsg formatBuy = balance >= price ? plugin.lang().Store_Format_Buy_Yes : plugin.lang().Store_Format_Buy_No;
		if (user.hasItem(storeItem)) {
			formatBuy = plugin.lang().Store_Format_Buy_Have;
		}
		String formatBuyStr = formatBuy
			.replace("%cost%", costDef)
			.replace("%cost-inherited%", costUser)
			.replace("%balance%", balance)
			.getMsg();
		
		meta.setDisplayName(meta.getDisplayName()
			.replace("%cost%", costDef)
			.replace("%cost-inherited%", costUser)
			.replace("%can-buy%", formatBuyStr)
			.replace("%balance%", String.valueOf(balance))
		);
		
		List<String> lore = meta.getLore();
		if (lore != null) {
			lore.replaceAll(line -> line
				.replace("%cost%", costDef)
				.replace("%cost-inherited%", costUser)
				.replace("%can-buy%", formatBuyStr)
				.replace("%balance%", String.valueOf(balance))
			);
			meta.setLore(lore);
		}
		
		item.setItemMeta(meta);
	}

	@Override
	protected boolean cancelClick(int slot) {
		return true;
	}

	@Override
	protected boolean cancelPlayerClick() {
		return true;
	}

	@Override
	protected boolean ignoreNullClick() {
		return true;
	}
}
