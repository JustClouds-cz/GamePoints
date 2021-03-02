package su.nightexpress.gamepoints.config;

import java.time.format.DateTimeFormatter;

import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.config.api.IConfigTemplate;
import su.nightexpress.gamepoints.GamePoints;

public class Config extends IConfigTemplate {
	
	public static int GENERAL_START_BALANCE;
	public static int GENERAL_TOP_UPDATE_MIN;
	
	public static String TRANSACTION_LOGS_FORMAT;
	public static DateTimeFormatter TRANSACTION_LOGS_DATE;
	public static boolean TRANSACTION_LOGS_CONSOLE;
	public static boolean TRANSACTION_LOGS_FILE;
	
	public Config(@NotNull GamePoints plugin) {
		super(plugin);
	}

	@Override
	public void load() {
		cfg.addMissing("store.transaction-logs.format", "[%date%] %player% bought %product% in %store% store for %price% points.");
		cfg.addMissing("store.transaction-logs.date", "dd/MM/yyyy HH:mm:ss");
		cfg.addMissing("store.transaction-logs.console.enabled", true);
		cfg.addMissing("store.transaction-logs.file.enabled", true);
		
    	GENERAL_START_BALANCE = cfg.getInt("general.start-balance", 0);
    	GENERAL_TOP_UPDATE_MIN = cfg.getInt("general.top-update-time", 20);
    	
    	String path = "store.transaction-logs.";
    	TRANSACTION_LOGS_CONSOLE = cfg.getBoolean(path + "console.enabled");
    	TRANSACTION_LOGS_FILE = cfg.getBoolean(path + "file.enabled");
    	if (TRANSACTION_LOGS_CONSOLE || TRANSACTION_LOGS_FILE) {
    		TRANSACTION_LOGS_FORMAT = cfg.getString(path + "format", "[%date%] %player% bought %product% in %store% store for %price% points.");
    		TRANSACTION_LOGS_DATE = DateTimeFormatter.ofPattern(cfg.getString(path + "date", "dd/MM/yyyy HH:mm:ss"));
    	}
	}
}
