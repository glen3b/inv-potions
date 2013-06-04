package com.glen3b.plugin.invpotions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InventoryPotionEffects extends JavaPlugin implements Listener {

	private int taskid;
	int tickDelayEventProcess = 2;
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerInventoryEvent(InventoryClickEvent event)
	{
		PlayerInventory pi = null;
		if(event.getInventory().getType() == InventoryType.PLAYER){
			//I have a funny feeling this is a player's inventory
		try{
			pi = (PlayerInventory)event.getInventory();
		}catch(ClassCastException c){
			//Nope! Not a player inventory!
			return;
		}
		//At this point, it is probably a player inventory we are dealing with
		final HumanEntity player = pi.getHolder();
		//Ugly! To do post-event processing.
		getServer().getScheduler()
        .scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
            	performInventoryLogic(player);
            }
        }, tickDelayEventProcess);
		
		}
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerInventoryEvent(PlayerItemHeldEvent event)
	{
		final HumanEntity player = event.getPlayer();
		//Ugly! To do post-event processing.
		getServer().getScheduler()
        .scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
            	performInventoryLogic(player);
            }
        }, tickDelayEventProcess);
		
		}
		
	protected boolean nonstandardArmor(PlayerInventory pi, int index) {
		switch (index) {
		case 0:
			ItemStack h = pi.getHelmet();
			return h != null && h.getType() != Material.AIR
					&& h.getType() != Material.LEATHER_HELMET
					&& h.getType() != Material.GOLD_HELMET
					&& h.getType() != Material.CHAINMAIL_HELMET
					&& h.getType() != Material.IRON_HELMET
					&& h.getType() != Material.DIAMOND_HELMET;
		case 1:
			ItemStack c = pi.getChestplate();
			return c != null && c.getType() != Material.AIR
					&& c.getType() != Material.LEATHER_CHESTPLATE
					&& c.getType() != Material.GOLD_CHESTPLATE
					&& c.getType() != Material.CHAINMAIL_CHESTPLATE
					&& c.getType() != Material.IRON_CHESTPLATE
					&& c.getType() != Material.DIAMOND_CHESTPLATE;
		case 2:
			ItemStack l = pi.getLeggings();
			return l != null && l.getType() != Material.AIR
					&& l.getType() != Material.LEATHER_LEGGINGS
					&& l.getType() != Material.GOLD_LEGGINGS
					&& l.getType() != Material.CHAINMAIL_LEGGINGS
					&& l.getType() != Material.IRON_LEGGINGS
					&& l.getType() != Material.DIAMOND_LEGGINGS;
		case 3:
			ItemStack b = pi.getBoots();
			return b != null && b.getType() != Material.AIR
					&& b.getType() != Material.LEATHER_BOOTS
					&& b.getType() != Material.GOLD_BOOTS
					&& b.getType() != Material.CHAINMAIL_BOOTS
					&& b.getType() != Material.IRON_BOOTS
					&& b.getType() != Material.DIAMOND_BOOTS;
		default:
			return false;
		}
	}

	protected void performInventoryLogic(HumanEntity humanEntity){
		Iterator<Entry<String, Object>> nondeepconfig = getConfig()
				.getRoot().getValues(false).entrySet()
				.iterator();
		if (humanEntity != null) {
			PlayerInventory pi = humanEntity.getInventory();
			ItemStack[] parmor = pi.getArmorContents();
			while (nondeepconfig.hasNext()) {
				Entry<String, Object> entry = nondeepconfig
						.next();
				String basekey = entry.getKey() + ".";
				if (!getConfig().getBoolean(
						basekey + "ignore", false)) {
					String[] configvalues = new String[] {
							getConfig()
									.getString(
											basekey
													+ "armor.helmet"),
							getConfig()
									.getString(
											basekey
													+ "armor.chestplate"),
							getConfig()
									.getString(
											basekey
													+ "armor.leggings"),
							getConfig()
									.getString(
											basekey
													+ "armor.boots") };
					List<ArmorConfigValueType> armorcfg = ArmorConfigValueType
							.populateList(configvalues);
					int[] numbers = new int[] { -257, -257,
							-257, -257 };
					Material[] armor = new Material[] {
							Material.getMaterial(configvalues[0]),
							Material.getMaterial(configvalues[1]),
							Material.getMaterial(configvalues[2]),
							Material.getMaterial(configvalues[3]) };
					for (int i = 0; i < 4; i++) {
						boolean usenum = true;
						try {
							numbers[i] = Integer
									.parseInt(configvalues[i]);
						} catch (NumberFormatException e) {
							usenum = false;
						}
						if (usenum) {
							armor[i] = Material
									.getMaterial(numbers[i]);
						}
					}
					boolean armorvalid = true;
					boolean inventoryvalid = true;
					for (int i = 0; i < 4; i++) {
						switch (armorcfg.get(i)) {
						case Acknowledge:
							ItemStack checking;
							boolean useItemStack = false;
							if (configvalues[i].split(":").length == 1) {
								checking = new ItemStack(
										Material.getMaterial(configvalues[i]));
							} else {
								useItemStack = true;
								checking = new ItemStack(
										Material.getMaterial(configvalues[i]
												.split(":")[0]),
										1,
										Short.parseShort(configvalues[i]
												.split(":")[1]));
							}
							if (!(useItemStack ? parmor[3 - i]
									.getType() == checking
									.getType()
									&& parmor[3 - i]
											.getDurability() == checking
											.getDurability()
									: parmor[3 - i]
											.getType() == Material
											.getMaterial(configvalues[i]))) {
								armorvalid = false;
							}
							break;
						case NonStandard:
							if (!nonstandardArmor(pi, 3 - i)) {
								armorvalid = false;
							}
							break;
						case Ignore:
							break;
						}
					}
					List<String> criteria = getConfig()
							.getStringList(
									basekey + "criteria");
					if (criteria != null) {
						for (int i = 0; i < criteria.size(); i++) {
							ItemStack checking;
							boolean useItemStack = false;
							if (criteria.get(i).split(":").length == 1) {
								checking = new ItemStack(
										Material.getMaterial(criteria
												.get(i)));
							} else {
								useItemStack = true;
								checking = new ItemStack(
										Material.getMaterial(criteria
												.get(i)
												.split(":")[0]),
										1,
										Short.parseShort(criteria
												.get(i)
												.split(":")[1]));
							}
							if (checking != null
									&& Material
											.getMaterial(criteria
													.get(i)) != null
									&& !(useItemStack ? pi
											.containsAtLeast(
													checking,
													1)
											: pi.contains(Material
													.getMaterial(criteria
															.get(i))))) {
								inventoryvalid = false;
							}
						}
					}
					
					//Item in hand
					String handitem = getConfig()
							.getString(basekey + "handitem");
					if (handitem != null) {
						ItemStack checking;
						boolean useItemStack = false;
						if (handitem.split(":").length == 1) {
							checking = new ItemStack(
									Material.getMaterial(handitem));
						} else {
							useItemStack = true;
							checking = new ItemStack(
									Material.getMaterial(handitem
											.split(":")[0]),
									1,
									Short.parseShort(handitem
											.split(":")[1]));
						}
						if (!(useItemStack ? pi
								.getItemInHand().getType() == checking
								.getType()
								&& pi.getItemInHand()
										.getDurability() == checking
										.getDurability()
								: pi.getItemInHand()
										.getType() == Material
										.getMaterial(handitem))) {
							inventoryvalid = false;
						}
					}

					// And now, check for blacklisted items
					List<String> blacklist = getConfig()
							.getStringList(
									basekey + "blacklist");
					if (blacklist != null) {
						for (int i = 0; i < blacklist
								.size(); i++) {
							String itemroot = blacklist
									.get(i);
							Material checking = Material
									.getMaterial(itemroot
											.split(":")[0]);
							boolean useDamage = false;
							useDamage = itemroot.split(":").length > 1;
							if (useDamage ? pi
									.containsAtLeast(
											new ItemStack(
													checking,
													1,
													Short.parseShort(itemroot
															.split(":")[1])),
											1)
									: pi.contains(checking)) {
								inventoryvalid = false;
							}
						}
					}

					if(armorvalid && inventoryvalid){
						//This is a valid potion effect to apply
					if (!humanEntity.hasPermission("invpotions.bypass") && (humanEntity.hasPermission("invpotions.potion."
									+ entry.getKey()) || humanEntity
										.hasPermission("invpotions.potion.*"))) {
						List<String> potioneffects = getConfig()
								.getStringList(
										basekey + "effects");
						
						for (int i = 0; i < potioneffects
								.size(); i++) {
							String[] components = potioneffects
									.get(i).split("::");
							int level;
							try {
								level = Integer
										.parseInt(components[1]) - 1;
							} catch (NumberFormatException n) {
								getLogger().log(
										Level.WARNING,
										"Your potion effect configuration has an invalid number as a level in "
												+ basekey);
								break;
							} catch (ArrayIndexOutOfBoundsException n) {
								getLogger()
										.log(Level.WARNING,
												"Your potion effect configuration does not have a valid splitting character ('::') in "
														+ basekey
														+ " Attempting to assume level 1.");
								level = 0;
							}
							PotionEffectType potionefc = PotionEffectType
									.getByName(components[0]
											.toUpperCase());
							try {
								humanEntity.removePotionEffect(potionefc);
								humanEntity.addPotionEffect(new PotionEffect(
										potionefc, getConfig().getInt(basekey+"tickDuration", 250),
										level));
							} catch (NullPointerException n) {
								getLogger()
										.log(Level.WARNING,
												"There appears to be an invalid potion effect in your config file (check in "
														+ entry.getKey()
														+ ").");
							}
						}
					}
				}else{
					//This is not a potion effect to apply
					if(getConfig().getBoolean(basekey+"forcefulPotionEffect", false)){
						//Remove the potion effect. it is forceful.
						List<String> potioneffects = getConfig()
								.getStringList(
										basekey + "effects");
						for (int i = 0; i < potioneffects
								.size(); i++) {
							String[] components = potioneffects
									.get(i).split("::");
							PotionEffectType potionefc = PotionEffectType
									.getByName(components[0]
											.toUpperCase());
							try {
								humanEntity.removePotionEffect(potionefc);
							} catch (NullPointerException n) {
								getLogger()
										.log(Level.WARNING,
												"There appears to be an invalid potion effect in your config file (check in "
														+ entry.getKey()
														+ ").");
							}
						}
					}
				}
				}
			}
		}
	}
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(this, this);
		taskid = this.getServer().getScheduler()
				.scheduleSyncRepeatingTask(this, new Runnable() {

					@Override
					public void run() {
						for (Player p : getServer().getOnlinePlayers()) {
							performInventoryLogic(p);
							}
					}
				}, 150L, 90L);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("invpotions")) {
			if (args.length < 1) {
				sender.sendMessage("§cToo few arguments.");
				return false;
			}
			if(args[0].equalsIgnoreCase("help")){
				sender.sendMessage(ChatColor.GOLD+"Inventory-based potions version "+getDescription().getVersion()+" commands:");
				sender.sendMessage(ChatColor.GREEN+"/invpotions reload - Reload from the configuration file");
				sender.sendMessage(ChatColor.GREEN+"/invpotions remove <potion name in config> - Remove the specified potion effect");
				sender.sendMessage(ChatColor.GREEN+"/invpotions add <potion name> <potion  effect 1> [potion effect 2] ... - Add a new potion matching your inventory with the specified effects");
				sender.sendMessage(ChatColor.GREEN+"/invpotions list - List the names of all potion effects");
			}
			else if (args[0].equalsIgnoreCase("reload")) {
				this.reloadConfig();
				sender.sendMessage("§aReloaded configuration.");
				return true;
			}
			else if (args[0].equalsIgnoreCase("list")) {
				Iterator<Entry<String, Object>> nondeepconfig = getConfig()
						.getRoot().getValues(false).entrySet()
						.iterator();
					while (nondeepconfig.hasNext()) {
						Entry<String, Object> entry = nondeepconfig
								.next();
						sender.sendMessage(ChatColor.AQUA+entry.getKey());
					}
				return true;
			}
			else if (args[0].equalsIgnoreCase("remove")) { 
				if (args.length < 2) {
					sender.sendMessage("§cToo few arguments. Please specify a configuration name for the potion to remove.");
					return false;
				}
				getConfig().set(args[1], null);
				saveConfig();
				reloadConfig();
				sender.sendMessage("§aRemoved potion effect " + args[1]
						+ " from configuration.");
				return true;
			
			} else if (args[0].equalsIgnoreCase("add")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("§cI'm sorry, at the moment only players can use this command.");
					return true;
				}
				if (args.length < 2) {
					sender.sendMessage("§cToo few arguments. Please specify a configuration name for this potion.");
					return false;
				}
				if (args.length < 3) {
					sender.sendMessage("§cToo few arguments. Please specify potion effects to use.");
					return false;
				}
				Player invbase = (Player) sender;
				List<String> itemsInInv = new ArrayList<String>();
				for (ItemStack item : invbase.getInventory().getContents()) {
					if(item != null){
					itemsInInv.add(item.getType().toString());
					}
				}
				getConfig().set(args[1] + ".criteria", itemsInInv);
				getConfig()
						.set(args[1] + ".armor.helmet",
								invbase.getInventory().getHelmet().getType()
										.toString());
				getConfig().set(
						args[1] + ".armor.chestplate",
						invbase.getInventory().getChestplate().getType()
								.toString());
				getConfig().set(
						args[1] + ".armor.leggings",
						invbase.getInventory().getLeggings().getType()
								.toString());
				getConfig().set(args[1] + ".armor.boots",
						invbase.getInventory().getBoots().getType().toString());
				List<String> potionFx = new ArrayList<String>();
				for (int i = 2; i < args.length; i++) {
					potionFx.add(args[i]);
				}
				getConfig().set(args[1] + ".effects", potionFx);
				saveConfig();
				reloadConfig();
				sender.sendMessage("§aAdded potion effect " + args[1]
						+ " matching your inventory.");
				return true;
			} else {
				sender.sendMessage("§cUnrecognized subcommand");
				return false;
			}
		} else if (cmd.getName().equalsIgnoreCase("matchinv")) {
			boolean getplayer = true;
			if (!(sender instanceof Player)) {
				if (args.length < 2) {
					sender.sendMessage("§cToo few arguments (console users must pass player name).");
					return false;
				}
				getplayer = false;
			}
			if (args.length == 2) {
				getplayer = false;
			}
			if (args.length < 1) {
				sender.sendMessage("§cToo few arguments.");
				return false;
			}
			if (args.length > 2) {
				sender.sendMessage("§cToo many arguments from your friend /matchinv.");
				return false;
			}
			Player target = (Player) sender;
			if (!getplayer) {
				target = getServer().getPlayer(args[1]);
			}
			List<String> items = getConfig().getStringList(
					args[0] + ".criteria");
			String[] cfgvalues = new String[] {
					getConfig().getString(args[0] + ".armor.helmet"),
					getConfig().getString(args[0] + ".armor.chestplate"),
					getConfig().getString(args[0] + ".armor.leggings"),
					getConfig().getString(args[0] + ".armor.boots") };

			Material[] armor = new Material[] {
					Material.getMaterial(cfgvalues[0]),
					Material.getMaterial(cfgvalues[1]),
					Material.getMaterial(cfgvalues[2]),
					Material.getMaterial(cfgvalues[3]) };
			int[] numbers = new int[] { -257, -257, -257, -257 };
			for (int i = 0; i < 4; i++) {
				boolean usenum = true;
				try {
					numbers[i] = Integer.parseInt(cfgvalues[i]);
				} catch (NumberFormatException e) {
					usenum = false;
				}
				if (usenum) {
					armor[i] = Material.getMaterial(numbers[i]);
				}
			}
			String handitem = getConfig().getString(args[0] + ".handitem");
			if (handitem != null) {
				ItemStack checking;
				if (handitem.split(":").length == 1) {
					checking = new ItemStack(Material.getMaterial(handitem));
				} else {
					checking = new ItemStack(Material.getMaterial(handitem
							.split(":")[0]), 1, Short.parseShort(handitem
							.split(":")[1]));
				}
				target.setItemInHand(checking);
			}
			try {
				for (String str : items) {
					if (str.split(":").length == 1) {
						target.getInventory().addItem(
								new ItemStack(Material.getMaterial(str)));
					} else {
						target.getInventory().addItem(
								new ItemStack(Material.getMaterial(str
										.split(":")[0]), 1, Short
										.parseShort(str.split(":")[1])));
					}
				}
			} catch (NullPointerException n) {
				sender.sendMessage("§cAn error occured.");
				if (target == null) {
					sender.sendMessage("§cThe player you targeted couldn't be found.");
				}
				return true;
			} catch (NumberFormatException nf) {
				sender.sendMessage("§cError parsing damage value in configuration.");
				return true;
			}
			try {
				ItemStack add;
				if (ArmorConfigValueType.getType(cfgvalues[0]) != ArmorConfigValueType.Ignore) {
					if (cfgvalues[0].split(":").length == 1) {
						add = new ItemStack(armor[0]);
					} else {
						add = new ItemStack(armor[0], 1,
								Short.parseShort(cfgvalues[0].split(":")[1]));
					}
					target.getInventory().setHelmet(add);
				}
				if (ArmorConfigValueType.getType(cfgvalues[1]) != ArmorConfigValueType.Ignore) {
					if (cfgvalues[1].split(":").length == 1) {
						add = new ItemStack(armor[1]);
					} else {
						add = new ItemStack(armor[1], 1,
								Short.parseShort(cfgvalues[1].split(":")[1]));
					}
					target.getInventory().setChestplate(add);
				}
				if (ArmorConfigValueType.getType(cfgvalues[2]) != ArmorConfigValueType.Ignore) {
					if (cfgvalues[2].split(":").length == 1) {
						add = new ItemStack(armor[2]);
					} else {
						add = new ItemStack(armor[2], 1,
								Short.parseShort(cfgvalues[2].split(":")[1]));
					}
					target.getInventory().setLeggings(add);
				}
				if (ArmorConfigValueType.getType(cfgvalues[3]) != ArmorConfigValueType.Ignore) {
					if (cfgvalues[3].split(":").length == 1) {
						add = new ItemStack(armor[3]);
					} else {
						add = new ItemStack(armor[3], 1,
								Short.parseShort(cfgvalues[3].split(":")[1]));
					}
					target.getInventory().setBoots(add);
				}
			} catch (NullPointerException n) {
				sender.sendMessage("§cEither a bad material name or the potion doesn't exist.");
				return true;
			} catch (NumberFormatException nf) {
				sender.sendMessage("§cError parsing damage value in configuration.");
				return true;
			}
			sender.sendMessage("§aMatched inventory of "
					+ target.getDisplayName() + "§a to requirements of "
					+ args[0] + ".");
			return true;
		}
		return false;
	}

	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTask(taskid);
	}

}
