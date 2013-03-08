package com.glen3b.plugin.invpotions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;

public class InventoryPotionEffects extends JavaPlugin {
	
	private int taskid;
	
	protected boolean woolHelmet(PlayerInventory pi){
		ItemStack h = pi.getHelmet();
		return h != null && h.getType() != Material.LEATHER_HELMET && h.getType() != Material.GOLD_HELMET && h.getType() != Material.CHAINMAIL_HELMET && h.getType() != Material.IRON_HELMET && h.getType() != Material.DIAMOND_HELMET;
	}
	
	@Override
	public void onEnable(){
	    taskid = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	    	
	        @Override  
	        public void run() {
	            for(Player p : getServer().getOnlinePlayers()) {
	            	if(p != null){
	            		
	            		
	            		
	            		/*
	            	PlayerInventory pi = p.getInventory();
	                if(woolHelmet(pi) && pi.getBoots() != null && pi.getBoots().getType() == Material.DIAMOND_BOOTS && pi.getLeggings() != null &&pi.getLeggings().getType() == Material.DIAMOND_LEGGINGS && pi.getChestplate() != null && pi.getChestplate().getType() == Material.DIAMOND_CHESTPLATE && pi.contains(Material.DIAMOND_SWORD)){
	                 	p.removePotionEffect(PotionEffectType.NIGHT_VISION);
	                 	p.removePotionEffect(PotionEffectType.WATER_BREATHING);
	                 	p.removePotionEffect(PotionEffectType.SPEED);
	                 	p.removePotionEffect(PotionEffectType.WEAKNESS);
	                 	p.removePotionEffect(PotionEffectType.JUMP);
	                 	p.removePotionEffect(PotionEffectType.INVISIBILITY);
	                	p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1500, 3, false));
	                 	p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 900, 0, false));
	                    if(r.nextInt(12) == 9){p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 250, 0, false));p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 50, 0, false));}
	                }else if(woolHelmet(pi) && pi.contains(Material.STONE_SWORD) && pi.getBoots() == null && pi.getLeggings() == null && pi.getChestplate() != null && pi.getChestplate().getType() == Material.LEATHER_CHESTPLATE){
	                	p.removePotionEffect(PotionEffectType.NIGHT_VISION);
	                 	p.removePotionEffect(PotionEffectType.SLOW);
	                 	p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	                 	p.removePotionEffect(PotionEffectType.WEAKNESS);
	                 	p.removePotionEffect(PotionEffectType.JUMP);
	                 	// p.removePotionEffect(PotionEffectType.INVISIBILITY);
	                	p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 1500, 0, false));
	                 	p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 900, 1, false));
	                }else if(woolHelmet(pi) && pi.contains(Material.BOW) && pi.contains(Material.IRON_SWORD) && pi.getBoots() == null && pi.getLeggings() == null && pi.getChestplate() != null && pi.getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE){
	                	p.removePotionEffect(PotionEffectType.NIGHT_VISION);
	                 	p.removePotionEffect(PotionEffectType.SLOW);
	                 	p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	                 	p.removePotionEffect(PotionEffectType.SPEED);
	                 	p.removePotionEffect(PotionEffectType.WATER_BREATHING);
	                 	p.removePotionEffect(PotionEffectType.INVISIBILITY);
	                	p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 75, 0, false));
	                 	p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1500, 0, false));
	                }else if(woolHelmet(pi) && pi.contains(Material.BOW) && !pi.contains(Material.IRON_SWORD) && pi.getBoots() == null && pi.getLeggings() == null && pi.getChestplate() != null && pi.getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE){
	                	p.removePotionEffect(PotionEffectType.WEAKNESS);
	                	p.removePotionEffect(PotionEffectType.JUMP);
	                 	p.removePotionEffect(PotionEffectType.SLOW);
	                 	p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	                 	p.removePotionEffect(PotionEffectType.SPEED);
	                 	p.removePotionEffect(PotionEffectType.WATER_BREATHING);
	                 	p.removePotionEffect(PotionEffectType.INVISIBILITY);
	                	p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1500, 0, false));
	                }else if(woolHelmet(pi) && pi.contains(Material.GOLD_SWORD) && pi.getBoots() != null && pi.getBoots().getType() == Material.CHAINMAIL_BOOTS && pi.getLeggings() == null && pi.getChestplate() == null){
	                	p.removePotionEffect(PotionEffectType.WEAKNESS);
	                	p.removePotionEffect(PotionEffectType.JUMP);
	                 	p.removePotionEffect(PotionEffectType.SLOW);
	                 	p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	                 	p.removePotionEffect(PotionEffectType.SPEED);
	                 	p.removePotionEffect(PotionEffectType.WATER_BREATHING);
	                 	p.removePotionEffect(PotionEffectType.NIGHT_VISION);
	                	p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1500, 0, false));
	                }else if(pi.contains(Material.DIAMOND_SWORD) && !pi.contains(Material.BOW) && woolHelmet(pi) && pi.getBoots() == null && pi.getLeggings() == null && pi.getChestplate() != null && pi.getChestplate().getType() == Material.IRON_CHESTPLATE){
	                	p.removePotionEffect(PotionEffectType.JUMP);
	                 	p.removePotionEffect(PotionEffectType.SLOW);
	                 	p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	                 	p.removePotionEffect(PotionEffectType.SPEED);
	                 	p.removePotionEffect(PotionEffectType.WATER_BREATHING);
	                 	p.removePotionEffect(PotionEffectType.NIGHT_VISION);
	                 	p.removePotionEffect(PotionEffectType.INVISIBILITY);
	                	p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 87, 0, false));
	                }
	                else if((p.getLocation().getBlockY() == 25 || p.getLocation().getBlockY() == 24 || p.getLocation().getBlockY() == 26) && pi.contains(Material.DIAMOND_SWORD) && pi.contains(Material.SHEARS) && pi.contains(Material.DIAMOND_SPADE) && pi.getBoots() == null && pi.getLeggings() == null && pi.getChestplate() == null && pi.getHelmet() == null){
	                	p.removePotionEffect(PotionEffectType.NIGHT_VISION);
	                	p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 1500, 0, false)));
	                }*/
	            	}
	                
	            }
	        
	        }
	    }, 250L, 100L);
	}
	
	@Override
	public void onDisable(){
		this.getServer().getScheduler().cancelTask(taskid);
	}

}
