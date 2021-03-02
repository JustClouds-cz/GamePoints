package su.nightexpress.gamepoints.manager.objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.manager.api.gui.ContentType;
import su.nexmedia.engine.manager.api.gui.GuiItem;
import su.nexmedia.engine.manager.api.gui.NGUI;
import su.nightexpress.gamepoints.GamePoints;

public class MainStore extends NGUI<GamePoints> {

	public MainStore(@NotNull GamePoints plugin, @NotNull JYML cfg, @NotNull String path) {
		super(plugin, cfg, path);
		
		for (String sId : cfg.getSection(path + "stores")) {
			Store store = plugin.getStoreManager().getStore(sId);
			if (store == null) {
				plugin.error("Invalid store '" + sId + "' in main store!");
				continue;
			}
			
			GuiItem guiItem = cfg.getGuiItem(path + "stores." + sId);
			if (guiItem == null) continue;
			
			guiItem.setClick((p, type, e) -> {
				store.open(p);
			});
			
			this.addButton(guiItem);
		}
		
		for (String id : cfg.getSection(path + "content")) {
			GuiItem guiItem = cfg.getGuiItem(path + "content." + id, ContentType.class);
			if (guiItem == null) continue;
			
			this.addButton(guiItem);
		}
	}

	@Override
	protected void onCreate(@NotNull Player player, @NotNull Inventory inv, int page) {
		
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
