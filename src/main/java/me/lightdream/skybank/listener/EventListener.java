package me.lightdream.skybank.listener;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.commands.TaxCommand;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.enums.TaxType;
import me.lightdream.skybank.gui.GUIManager;
import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import world.bentobox.bentobox.api.events.island.IslandCreateEvent;
import world.bentobox.bentobox.api.events.team.TeamLeaveEvent;
import world.bentobox.bentobox.database.objects.Island;

import java.util.UUID;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        if(!SkyBank.playerLoggerNames.contains(player.getUniqueId().toString()))
            SkyBank.playerLogger.add(API.getIpLogTemplate(player.getUniqueId()));
        else
            SkyBank.playerLogger.set(SkyBank.playerLoggerNames.indexOf(player.getUniqueId().toString()), API.getIpLogTemplate(player.getUniqueId()));

        Island island = API.getIsland(player.getUniqueId());
        FileConfiguration data = API.loadPlayerDataFile(player.getUniqueId());

        if(island == null)
            return;

        if(island.getOwner() == player.getUniqueId()){

            if(API.getTaxData(player.getUniqueId()) != 0){
                API.setTax(player.getUniqueId(), TaxType.PAID);
            }
            return;
        }

        if(API.getTaxData(player.getUniqueId()) >= SkyBank.config.getInt("tax-limit")){

            SkyBank.overtaxedPlayersNames.add(player.getUniqueId().toString());

            Bukkit.dispatchCommand(player, SkyBank.config.getString("forced-tax-command"));

            data.set("over-tax", true);
            data.set("before-sanction-size", API.getIslandSize(player.getUniqueId()));

            API.savePlayerDataFile(player.getUniqueId(), data);

            API.setIslandSize(island, SkyBank.config.getInt("over-tax-size"));
        }

        if(API.getInterest(player.getUniqueId()) != 0){
            for(int i=0;i<API.getInterest(player.getUniqueId());i++){
                API.addBankBalance(player.getUniqueId() , API.getBankBalance(player.getUniqueId()) + API.getBankBalance(player.getUniqueId()) * API.getInterestPercent(player.getUniqueId()) / 100);
            }
        }

        if(API.getLoanData(player.getUniqueId()) != 0){
            for(int i=0;i<API.getLoanData(player.getUniqueId());i++){
                API.addLoanByInterest(player.getUniqueId());
            }
        }


    }

    @EventHandler
    public void onIslandLeave (TeamLeaveEvent event){

        UUID uuid = event.getPlayerUUID();

        if(!API.loadPlayerDataFile(uuid).getBoolean("leave-tax")){
            API.sendColoredMessage(Bukkit.getPlayer(event.getPlayerUUID()), Language.pay_tax_to_leave_island);

            event.setCancelled(true); //TODO: Fix the non canceling
        }
    }

    @EventHandler
    public void onIslandCreate (IslandCreateEvent event){
        API.setTax(event.getPlayerUUID(), TaxType.PAID);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(GUIManager.protectedInventories.contains(event.getView().getTitle())){
            if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(SkyBank.payTax, PersistentDataType.STRING))
                TaxCommand.payTax((Player) event.getWhoClicked(), API.getTaxTotalPrice(event.getWhoClicked().getUniqueId()));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        SkyBank.overtaxedPlayersNames.remove(event.getPlayer().getUniqueId().toString());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        System.out.println(event.getMessage());
        if(SkyBank.overtaxedPlayersNames.contains(event.getPlayer().getUniqueId().toString())){
            if(SkyBank.config.getList("blocked-tax-commands").contains(event.getMessage())){
                event.setCancelled(true);
            }
        }
        if(SkyBank.loanedPLayersNames.contains(event.getPlayer().getUniqueId().toString()))
            if(SkyBank.config.getList("loan-blocked-commands").contains(event.getMessage())){
                event.setCancelled(true);
            }
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event){

        System.out.println(1);
        Player player = event.getPlayer();
        System.out.println(2);

        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            System.out.println(3);

            if(event.getClickedBlock() != null){
                System.out.println(4);

                BlockState state = event.getClickedBlock().getState();
                if (!(state instanceof Sign)) {
                    System.out.println(5);

                    return;
                }
                Sign sign = (Sign) state;
                if(sign.getLine(0).equals(SkyBank.config.getString("sign-bank-name"))){
                    System.out.println(6);

                    if(sign.getLine(1).equals(SkyBank.config.getString("sign-balance"))){
                        System.out.println(7);
                        API.sendColoredMessage(player, Language.own_balance.replace("%money%", String.valueOf(API.getBankBalance(player.getUniqueId()))));

                    }
                    else if(sign.getLine(1).equals(SkyBank.config.getString("sign-deposit"))){
                        System.out.println(8);

                        int amount;

                        try {
                            System.out.println(9);

                            amount = Integer.parseInt(sign.getLine(2));
                        } catch (NumberFormatException e){
                            System.out.println(10);

                            API.sendColoredMessage(player, Language.invalid_number_format);
                            return;
                        }

                        if(amount <= API.getBalance(player.getUniqueId())){
                            System.out.println(11);

                            API.addBankBalance(player.getUniqueId(), amount);
                            API.removeBalance(player.getUniqueId(), amount);
                            API.sendColoredMessage(player, Language.balance_updated);
                        }
                        else {
                            System.out.println(12);

                            API.sendColoredMessage(player, Language.not_enough_money);
                        }
                    }
                    else if(sign.getLine(1).equals(SkyBank.config.getString("sign-withdraw"))){
                        System.out.println(13);

                        int amount;

                        try {
                            amount = Integer.parseInt(sign.getLine(2));
                            System.out.println(14);

                        } catch (NumberFormatException e){
                            System.out.println(15);

                            API.sendColoredMessage(player, Language.invalid_number_format);
                            return;
                        }
                        System.out.println(16);

                        FileConfiguration data = API.loadPlayerDataFile(player.getUniqueId());

                        if(data.getInt("bank-balance") >= amount){
                            System.out.println(17);

                            data.set("bank-balance", data.getInt("bank-balance") - amount);
                            API.savePlayerDataFile(player.getUniqueId(), data);
                            API.removeBalance(player.getUniqueId(), amount);
                            API.sendColoredMessage(player, Language.balance_updated);
                        }
                    }
                }

            }
        }
    }
}
