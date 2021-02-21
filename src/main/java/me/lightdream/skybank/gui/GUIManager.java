package me.lightdream.skybank.gui;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.API;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class GUIManager {

    public static ArrayList<String> protectedInventories = new ArrayList<>();

    private static final String taxPathPosition = "tax-gui.%s.position";

    public static Inventory getTaxInventory(Player player){
        String title = SkyBank.guiConfig.getString("tax-gui.title");

        Inventory inventory = Bukkit.createInventory(null, 54, title);
        if(!protectedInventories.contains(title))
            protectedInventories.add(title);

        inventory.setItem(Integer.parseInt(API.process1(taxPathPosition, "tax-item")),           ItemManager.getTaxItem(player));
        inventory.setItem(Integer.parseInt(API.process1(taxPathPosition, "size-item")),          ItemManager.getSizeItem(player));
        inventory.setItem(Integer.parseInt(API.process1(taxPathPosition, "tax-value-item")),     ItemManager.getTaxValueItem(player));
        inventory.setItem(Integer.parseInt(API.process1(taxPathPosition, "tax-price-item")),     ItemManager.getTaxPriceItem(player));
        inventory.setItem(Integer.parseInt(API.process1(taxPathPosition, "overtax-value-item")), ItemManager.getOverTaxValueItem(player));
        inventory.setItem(Integer.parseInt(API.process1(taxPathPosition, "overtax-price-item")), ItemManager.getOverTaxPriceItem(player));
        inventory.setItem(Integer.parseInt(API.process1(taxPathPosition, "total-item")),         ItemManager.getTotalTaxPrice(player));

        return inventory;
    }

}
