package me.lightdream.skybank.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.API;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private static final String namePath = "%s.%s.display-name";
    private static final String lorePath = "%s.%s.lore";
    private static final String materialPath = "%s.%s.material";

    private static final String tax = "tax-gui";
    private static final String main = "main-gui";

    public static ItemStack getTaxItem(Player player) {
        return getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , tax, "tax-item"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , tax, "tax-item"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, tax, "tax-item"));
    }
    public static ItemStack getSizeItem(Player player) {
        return getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , tax, "size-item"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , tax, "size-item"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, tax, "size-item"));
    }
    public static ItemStack getTaxValueItem(Player player) {
        return getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , tax, "tax-value-item"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , tax, "tax-value-item"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, tax, "tax-value-item"));
    }
    public static ItemStack getTaxPriceItem(Player player) {
        return getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , tax, "tax-price-item"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , tax, "tax-price-item"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, tax, "tax-price-item"));
    }
    public static ItemStack getOverTaxValueItem(Player player) {
        return getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , tax, "overtax-value-item"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , tax, "overtax-value-item"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, tax, "overtax-value-item"));
    }
    public static ItemStack getOverTaxPriceItem(Player player) {
        return getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , tax, "overtax-price-item"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , tax, "overtax-price-item"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, tax, "overtax-price-item"));
    }
    public static ItemStack getTotalTaxPriceItem(Player player) {
        return setNamespaceKey(getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , tax, "total-item"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , tax, "total-item"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, tax, "total-item")),
                SkyBank.payTax);
    }

    public static ItemStack getTaxGUIItem(Player player) {
        return setNamespaceKey(getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , main, "tax-gui"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , main, "tax-gui"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, main, "tax-gui")),
                SkyBank.taxGUI);
    }
    public static ItemStack getLoanGUIItem(Player player) {
        return setNamespaceKey(getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , main, "loan-gui"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , main, "loan-gui"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, main, "loan-gui")),
                SkyBank.taxGUI);
    }
    public static ItemStack getBankGUIItem(Player player) {
        return setNamespaceKey(getGenericItem(player,
                API.processPlaceholder1(player.getUniqueId(), namePath    , main, "bank-gui"),
                API.processPlaceholder2(player.getUniqueId(), lorePath    , main, "bank-gui"),
                API.processPlaceholder3(player.getUniqueId(), materialPath, main, "bank-gui")),
                SkyBank.taxGUI);
    }

    public static ItemStack getDepositItem(Player player, int index){
        return setNamespaceKey(getGenericItem(player,
                API.color(PlaceholderAPI.setPlaceholders(player, SkyBank.guiConfig.getString("bank-gui.deposit-name"))),
                processPlaceholderDepositItem(API.color(PlaceholderAPI.setPlaceholders(player, (List<String>) SkyBank.guiConfig.getList("bank-gui.deposit-lore"))), index, "deposit"),
                SkyBank.guiConfig.getString("bank-gui.deposit-material")),
                SkyBank.deposit);
    }
    public static ItemStack getWithdrawItem(Player player, int index){
        return setNamespaceKey(getGenericItem(player,
                API.color(PlaceholderAPI.setPlaceholders(player, SkyBank.guiConfig.getString("bank-gui.withdraw-name"))),
                processPlaceholderDepositItem(API.color(PlaceholderAPI.setPlaceholders(player, (List<String>) SkyBank.guiConfig.getList("bank-gui.deposit-lore"))), index, "withdraw"),
                SkyBank.guiConfig.getString("bank-gui.withdraw-material")),
                SkyBank.withdraw);
    }

    public static ItemStack getLoanItem(Player player, int index){
        return setNamespaceKey(getGenericItem(player,
                API.color(PlaceholderAPI.setPlaceholders(player, SkyBank.guiConfig.getString("loan-gui.name"))),
                processPlaceholderLoantItem(API.color(PlaceholderAPI.setPlaceholders(player, (List<String>) SkyBank.guiConfig.getList("bank-gui.deposit-lore"))), index),
                SkyBank.guiConfig.getString("loan-gui.material")),
                SkyBank.loan);
    }

    public static ArrayList<String> processPlaceholderLoantItem(ArrayList<String> lore, int index){
        ArrayList<String> output = new ArrayList<>();
        for(String str : lore)
            output.add(str.replace("{loan}", (String) SkyBank.guiConfig.getList("loan-gui.loans").get(index)));
        return output;
    }

    public static ArrayList<String> processPlaceholderDepositItem(ArrayList<String> lore, int index, String type){
        ArrayList<String> output = new ArrayList<>();
        for(String str : lore)
            output.add(str.replace("{amount}", (String) SkyBank.guiConfig.getList("bank-gui." + type + "-amounts").get(index)));
        return output;
    }

    public static ItemStack getGenericItem(Player player, String name, ArrayList<String> lore, String material){
        return new ItemBuilder(Material.getMaterial(material)).setDisplayName(PlaceholderAPI.setPlaceholders(player, name)).setLore((ArrayList<String>) PlaceholderAPI.setPlaceholders(player, lore)).build();
    }
    public static ItemStack setNamespaceKey(ItemStack item, NamespacedKey key){
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, key.toString());
        item.setItemMeta(meta);
        return item;
    }





}
