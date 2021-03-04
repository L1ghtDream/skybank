package me.lightdream.skybank.gui;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.API;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {

    public static ArrayList<String> protectedInventories = new ArrayList<>();

    private static final String positionPath = "%s.%s.position";

    private static final String tax = "tax-gui";
    private static final String main = "main-gui";



    public static Inventory getTaxGUI(Player player){

        String title = SkyBank.guiConfig.getString("tax-gui.title");

        Inventory inventory = Bukkit.createInventory(null, 54, title);
        if(!protectedInventories.contains(title))
            protectedInventories.add(title);

        inventory.setItem(Integer.parseInt(API.process1(positionPath, tax, "tax-item")),           ItemManager.getTaxItem(player));
        inventory.setItem(Integer.parseInt(API.process1(positionPath, tax, "size-item")),          ItemManager.getSizeItem(player));
        inventory.setItem(Integer.parseInt(API.process1(positionPath, tax, "tax-value-item")),     ItemManager.getTaxValueItem(player));
        inventory.setItem(Integer.parseInt(API.process1(positionPath, tax, "tax-price-item")),     ItemManager.getTaxPriceItem(player));
        inventory.setItem(Integer.parseInt(API.process1(positionPath, tax, "overtax-value-item")), ItemManager.getOverTaxValueItem(player));
        inventory.setItem(Integer.parseInt(API.process1(positionPath, tax, "overtax-price-item")), ItemManager.getOverTaxPriceItem(player));
        inventory.setItem(Integer.parseInt(API.process1(positionPath, tax, "total-item")),         ItemManager.getTotalTaxPriceItem(player));

        return inventory;
    }

    public static Inventory getMainGUI(Player player){
        String title = SkyBank.guiConfig.getString("main-gui.title");

        Inventory inventory = Bukkit.createInventory(null, 54, title);
        if(!protectedInventories.contains(title))
            protectedInventories.add(title);

        inventory.setItem(Integer.parseInt(API.process1(positionPath, main, "tax-gui")),  ItemManager.getTaxGUIItem(player));
        inventory.setItem(Integer.parseInt(API.process1(positionPath, main, "loan-gui")), ItemManager.getLoanGUIItem(player));
        inventory.setItem(Integer.parseInt(API.process1(positionPath, main, "bank-gui")), ItemManager.getBankGUIItem(player));


        return inventory;
    }

    public static Inventory getBankGUI(Player player){
        String title = SkyBank.guiConfig.getString("bank-gui.title");
        List<String> var1 = (List<String>) SkyBank.guiConfig.getList("bank-gui.deposit-positions");
        List<String> var2 = (List<String>) SkyBank.guiConfig.getList("bank-gui.withdraw-positions");

        Inventory inventory = Bukkit.createInventory(null, 54, title);
        if(!protectedInventories.contains(title))
            protectedInventories.add(title);

        for(int i=0;i<var1.size();i++)
            inventory.setItem(Integer.parseInt(var1.get(i)), ItemManager.getDepositItem(player, i));

        for(int i=0;i<var2.size();i++)
            inventory.setItem(Integer.parseInt(var2.get(i)), ItemManager.getWithdrawItem(player, i));

        return inventory;
    }

    public static Inventory getLoanGUI(Player player){

        String title = SkyBank.guiConfig.getString("loan-gui.title");
        List<String> var1 = (List<String>) SkyBank.guiConfig.getList("loan-gui.deposit-positions");

        Inventory inventory = Bukkit.createInventory(null, 54, title);
        if(!protectedInventories.contains(title))
            protectedInventories.add(title);

        for(int i=0;i<var1.size();i++)
            inventory.setItem(Integer.parseInt(var1.get(i)), ItemManager.getLoanItem(player, i));


        return inventory;
    }

}
