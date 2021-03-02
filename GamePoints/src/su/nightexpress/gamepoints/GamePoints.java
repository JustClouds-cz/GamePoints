package su.nightexpress.gamepoints;

import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.NexDataPlugin;
import su.nexmedia.engine.commands.api.IGeneralCommand;
import su.nexmedia.engine.hooks.Hooks;
import su.nightexpress.gamepoints.commands.AddCmd;
import su.nightexpress.gamepoints.commands.BalanceCmd;
import su.nightexpress.gamepoints.commands.BaltopCmd;
import su.nightexpress.gamepoints.commands.PayCmd;
import su.nightexpress.gamepoints.commands.SetCmd;
import su.nightexpress.gamepoints.commands.StoreCmd;
import su.nightexpress.gamepoints.commands.TakeCmd;
import su.nightexpress.gamepoints.config.Config;
import su.nightexpress.gamepoints.config.Lang;
import su.nightexpress.gamepoints.data.GamePointsData;
import su.nightexpress.gamepoints.data.UserManager;
import su.nightexpress.gamepoints.data.objects.StoreUser;
import su.nightexpress.gamepoints.hooks.PlaceholderAPIHook;
import su.nightexpress.gamepoints.manager.StoreManager;

public class GamePoints extends NexDataPlugin<GamePoints, StoreUser> {

	private static GamePoints instance;
	
	private Config config;
	private Lang lang;
	
	private StoreManager store;
	private GamePointsData dataHandler;
	
	public static GamePoints getInstance() {
		return instance;
	}
	
	public GamePoints() {
		instance = this;
	}
	
	@Override
	public void enable() {
		this.store = new StoreManager(this);
		this.store.setup();
	}

	@Override
	public void disable() {
		if (this.store != null) {
			this.store.shutdown();
			this.store = null;
		}
	}

	@Override
	protected boolean setupDataHandlers() {
		try {
			this.dataHandler = GamePointsData.getInstance(this);
			this.dataHandler.setup();
		} 
		catch (SQLException e) {
			this.error("Could not initialize database!");
			e.printStackTrace();
			return false;
		}
		
		this.userManager = new UserManager(this);
		this.userManager.setup();
		
		return true;
	}

	@Override
	public void setConfig() {
		this.config = new Config(this);
		this.config.setup();
		
		this.lang = new Lang(this);
		this.lang.setup();
	}

	@Override
	@NotNull
	public Config cfg() {
		return this.config;
	}

	@Override
	@NotNull
	public Lang lang() {
		return this.lang;
	}

	@Override
	public void registerCmds(@NotNull IGeneralCommand<GamePoints> mainCommand) {
		mainCommand.addSubCommand(new AddCmd(this));
		mainCommand.addSubCommand(new BalanceCmd(this));
		mainCommand.addSubCommand(new BaltopCmd(this));
		mainCommand.addSubCommand(new PayCmd(this));
		mainCommand.addSubCommand(new SetCmd(this));
		mainCommand.addSubCommand(new StoreCmd(this));
		mainCommand.addSubCommand(new TakeCmd(this));
	}

	@Override
	public void registerEditor() {
		
	}

	@Override
	public void registerHooks() {
		if (Hooks.hasPlaceholderAPI()) {
			this.registerHook(Hooks.PLACEHOLDER_API, PlaceholderAPIHook.class);
		}
	}

	@NotNull
	public StoreManager getStoreManager() {
		return this.store;
	}
	
	@Override
	@NotNull
	public GamePointsData getData() {
		return this.dataHandler;
	}
}
