package su.nightexpress.gamepoints.config;

import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.config.api.ILangMsg;
import su.nexmedia.engine.core.config.CoreLang;
import su.nightexpress.gamepoints.GamePoints;

public class Lang extends CoreLang {

	public Lang(@NotNull GamePoints plugin) {
		super(plugin);
	}

	@Override
	protected void setupEnums() {
		
	}
	
	public ILangMsg Command_Add_Usage = new ILangMsg(this, "<player> <amount>");
	public ILangMsg Command_Add_Desc = new ILangMsg(this, "Add points to player account.");
	public ILangMsg Command_Add_Done_Sender = new ILangMsg(this, "&7New &b%player%&7's balance: &b%balance% Game Points");
	public ILangMsg Command_Add_Done_User = new ILangMsg(this, "&7You received &b%amount% Game Points&7!");
	
	public ILangMsg Command_Take_Usage = new ILangMsg(this, "<player> <amount>");
	public ILangMsg Command_Take_Desc = new ILangMsg(this, "Take points from player account.");
	public ILangMsg Command_Take_Done_Sender = new ILangMsg(this, "&7New &b%player%&7's balance: &b%balance% Game Points");
	public ILangMsg Command_Take_Done_User = new ILangMsg(this, "&7You lost &b%amount% Game Points&7!");
	
	public ILangMsg Command_Set_Usage = new ILangMsg(this, "<player> <amount>");
	public ILangMsg Command_Set_Desc = new ILangMsg(this, "Set balance of player account.");
	public ILangMsg Command_Set_Done_Sender = new ILangMsg(this, "&7New &b%player%&7's balance: &b%balance% Game Points");
	public ILangMsg Command_Set_Done_User = new ILangMsg(this, "&7Your balance has been set to &b%amount% Game Points&7!");
	
	public ILangMsg Command_Pay_Usage = new ILangMsg(this, "<player> <amount>");
	public ILangMsg Command_Pay_Desc = new ILangMsg(this, "Transfer points to player.");
	public ILangMsg Command_Pay_Error_NoMoney = new ILangMsg(this, "You don't have enought points!");
	public ILangMsg Command_Pay_Done_Sender = new ILangMsg(this, "&7You sent &b%amount% Game Points &7to &b%player%&7!");
	public ILangMsg Command_Pay_Done_User = new ILangMsg(this, "&7You received &b%amount% Game Points&7 from &b%player%&7!");
	
	public ILangMsg Command_Balance_Usage = new ILangMsg(this, "[player]");
	public ILangMsg Command_Balance_Desc = new ILangMsg(this, "Displays player balance.");
	public ILangMsg Command_Balance_Done = new ILangMsg(this, "&b%player%&7's balance: &b%balance% Game Points");
	
	public ILangMsg Command_BalanceTop_Usage = new ILangMsg(this, "[page]");
	public ILangMsg Command_BalanceTop_Desc = new ILangMsg(this, "Displays top balances.");
	public ILangMsg Command_BalanceTop_List = new ILangMsg(
			this, "&6&m             &6&l[ &f&lGame Points - Top 10 &6&l]&6&m             &7"
			+ "\n"
			+ "&6%pos%. &e%player%: &a%points% Game Points"
			+ "\n"
			+ "&6&m             &6&l[ &f&lEnd Game Points Top &6&l]&6&m              &7");

	public ILangMsg Command_Store_Usage = new ILangMsg(this, "[store] [player]");
	public ILangMsg Command_Store_Desc = new ILangMsg(this, "Opens specified store.");
	
	public ILangMsg Store_Open_Error_Invalid = new ILangMsg(this, "&cInvalid store!");
	public ILangMsg Store_Open_Error_Perm = new ILangMsg(this, "&cWhoops! &7You don't access to %store% &7store!");
	
	public ILangMsg Store_Buy_Error_NoMoney = new ILangMsg(this, "&cWhoops! &7You don't have enough points!");
	public ILangMsg Store_Buy_Error_Have = new ILangMsg(this, "&cWhoops! &7You already have this item!");
	
	public ILangMsg Store_Buy_Success = new ILangMsg(this, "&7You successfully bought &a%item% &7for &a%price% Game Points&7!");
	
	public ILangMsg Store_Format_Buy_Yes = new ILangMsg(this, "&aClick to buy for %cost%");
	public ILangMsg Store_Format_Buy_No = new ILangMsg(this, "&cYou don't have enough points");
	public ILangMsg Store_Format_Buy_Have = new ILangMsg(this, "&6You already have this item.");
}
