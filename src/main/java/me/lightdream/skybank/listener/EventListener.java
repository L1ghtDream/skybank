package me.lightdream.skybank.listener;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.TaxType;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import world.bentobox.bentobox.api.events.island.IslandExitEvent;
import world.bentobox.bentobox.database.objects.Island;

import java.util.UUID;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        //TODO: [DONE] Check if the player is on an island
        //TODO: [DONE] Check if the player is the owner of the island

        Player player = event.getPlayer();

        Island island = Utils.getIsland(player);

        if(island == null)
            return;

        if(island.getOwner() == player.getUniqueId()){
            //TODO: [DONE] Check if the player has unpaid taxes
            if(Utils.getTax(player) != 0){
                Utils.setTax(player, TaxType.PAID);
            }
            return;
        }

        FileConfiguration data = Utils.getPlayerDataFile(player);

        //TODO: [DONE] Check if the difference between player paid taxes and server taxes

        if(Utils.getTax(player) >= SkyBank.config.getInt("tax-limit")){

            //TODO: [DONE] Apply sanctions

            data.set("over-tax", true);
            data.set("before-sanction-size", Utils.getIslandSize(player));

            Utils.savePlayerDataFile(player, data);

            Utils.setIslandSize(island, SkyBank.config.getInt("over-tax-size"));
        }
    }

    @EventHandler
    public void onIslandLeave (IslandExitEvent event){

        UUID uuid = event.getPlayerUUID();

        if(!Utils.getPlayerDataFile(uuid).getBoolean("leave-tax")){
            Utils.sendColoredMessage(Bukkit.getPlayer(event.getPlayerUUID()), Language.pay_tax_to_leave_island);
            event.setCancelled(true);
        }



        //TODO: [DONE] Display a message informing the player that he can not leave the island without paying a tax because the island is locked
        //TODO: [DONE] Do not execute the leave command without the check
    }

}
