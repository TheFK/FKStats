package main.Listener;

import main.FKStats;
import main.GUI.ChoosePlayerGUI;
import main.GUI.PlayerInventoryGUI;
import main.GUI.PlayerProfileGUI;
import main.GUI.StatsGUI;
import main.Spectating.SpectateHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by FKPro on 08.10.2018.
 */
public class GUIListener implements org.bukkit.event.Listener{
    private FKStats plugin;
    private StatsGUI gm;

    public GUIListener(FKStats plugin){
        this.plugin = plugin;
        gm = new StatsGUI(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        String title = event.getView().getTitle();
        if(title.startsWith("[FKStats]")){
            ItemStack clickedItem = event.getInventory().getItem(event.getSlot());
            Player p = (Player) event.getWhoClicked();
            if(title.startsWith("[FKStats][ChoosePlayerGUI]")) {
                int site = Character.getNumericValue(event.getInventory().getItem(13).getItemMeta().getDisplayName().charAt(0));
                if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                    p.openInventory(new PlayerProfileGUI(plugin).create(clickedItem.getItemMeta().getDisplayName()));
                }
                else if(clickedItem != null && clickedItem.getType() == Material.GREEN_BANNER){
                    if(clickedItem.getItemMeta().getDisplayName().equals("Seite zurück")) {
                        p.openInventory(new ChoosePlayerGUI(plugin).create(site-1));
                    }
                    else if (clickedItem.getItemMeta().getDisplayName().equals("Seite weiter")){
                        p.openInventory(new ChoosePlayerGUI(plugin).create(site+1));
                    }
                }
            }
            else if(title.startsWith("[FKStats][PlayerProfileGUI]")){
                SkullMeta head_meta = (SkullMeta) event.getInventory().getItem(8).getItemMeta();
                String profileplayername = head_meta.getOwningPlayer().getName();
                if(clickedItem != null && clickedItem.getType() == Material.IRON_PICKAXE){
                    p.openInventory(new StatsGUI(plugin).create(profileplayername));
                }
                else if(clickedItem != null && clickedItem.getType() == Material.CHEST){
                    p.openInventory(new PlayerInventoryGUI(plugin).create(profileplayername));
                }
                else if(clickedItem != null && clickedItem.getType() == Material.GLASS_PANE){
                    Collection<Player> players = (Collection<Player>) plugin.getServer().getOnlinePlayers();
                    Player target = null;
                    for(Player player : players){
                        if(player.getDisplayName().equals(profileplayername)){
                            target = player;
                            SpectateHandler sh = new SpectateHandler(plugin);
                            sh.startSpectating(p,target);
                            p.closeInventory();
                            break;
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
