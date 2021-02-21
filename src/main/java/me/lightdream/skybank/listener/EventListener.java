package me.lightdream.skybank.listener;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.commands.TaxCommand;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.enums.TaxType;
import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.gui.GUIManager;
import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import world.bentobox.bentobox.api.events.island.IslandCreateEvent;
import world.bentobox.bentobox.api.events.team.TeamLeaveEvent;
import world.bentobox.bentobox.database.objects.Island;

import java.util.UUID;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        try {
            Player player = event.getPlayer();

            if(!SkyBank.playerLoggerNames.contains(player.getUniqueId().toString()))
                SkyBank.playerLogger.add(API.getIpLogTemplate(player));
            else
                SkyBank.playerLogger.set(SkyBank.playerLoggerNames.indexOf(player.getUniqueId().toString()), API.getIpLogTemplate(player));




            Island island = API.getIsland(player);
            FileConfiguration data = API.loadPlayerDataFile(player, LoadFileType.PLAYER_DATA);

            if(island == null)
                return;

            if(island.getOwner() == player.getUniqueId()){

                if(API.getTaxData(player) != 0){
                    API.setTax(player, TaxType.PAID);
                }
                return;
            }

            if(API.getTaxData(player) >= SkyBank.config.getInt("tax-limit")){

                data.set("over-tax", true);
                data.set("before-sanction-size", API.getIslandSize(player));

                API.savePlayerDataFile(player, data);

                API.setIslandSize(island, SkyBank.config.getInt("over-tax-size"));
            }

            if(API.getInterest(player) != 0){
                for(int i=0;i<API.getInterest(player);i++){
                    API.addBankBalance(player, API.getBankBalance(player) + API.getBankBalance(player) * API.getInterestPercent(player) / 100);
                }
            }

            if(API.getLoanData(player) != 0){
                for(int i=0;i<API.getLoanData(player);i++){
                    API.addLoanByInterest(player);
                }
            }

        } catch (FileNotFoundException e) {
            SkyBank.logger.severe("Exception where no exception was expected");
            e.printStackTrace();
            event.getPlayer().kickPlayer("");
        }
    }

    @EventHandler
    public void onIslandLeave (TeamLeaveEvent event){

        UUID uuid = event.getPlayerUUID();

        try {
            if(!API.loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY).getBoolean("leave-tax")){
                API.sendColoredMessage(Bukkit.getPlayer(event.getPlayerUUID()), Language.pay_tax_to_leave_island);

                event.setCancelled(true); //TODO: Fix the non canceling
            }
        } catch (FileNotFoundException e) {
            SkyBank.logger.severe("Exception where no exception was expected");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onIslandCreate (IslandCreateEvent event){
        try{
            API.setTax(event.getPlayerUUID(), TaxType.PAID);
        } catch (FileNotFoundException e) {
            SkyBank.logger.severe("Exception where no exception was expected");
            event.setCancelled(true);
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        try {
            if(GUIManager.protectedInventories.contains(event.getView().getTitle())){
                if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(SkyBank.payTax, PersistentDataType.STRING)){
                    TaxCommand.payTax((Player) event.getWhoClicked(), API.getTaxTotalPrice(event.getWhoClicked().getUniqueId()));
                }

                event.setCancelled(true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
