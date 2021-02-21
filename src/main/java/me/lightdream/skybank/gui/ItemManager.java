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

public class ItemManager {

    private static final String taxPathName = "tax-gui.%s.display-name";
    private static final String taxPathLore = "tax-gui.%s.lore";

    public static ItemStack getTaxItem(Player player) {
        return getGenericItem(player, API.processPlaceholder1(player, taxPathName, "tax-item"), API.processPlaceholder2(player, taxPathLore, "tax-item"));
    }
    public static ItemStack getSizeItem(Player player) {
        return getGenericItem(player, API.processPlaceholder1(player, taxPathName, "size-item"), API.processPlaceholder2(player, taxPathLore, "size-item"));
    }
    public static ItemStack getTaxValueItem(Player player) {
        return getGenericItem(player, API.processPlaceholder1(player, taxPathName, "tax-value-item"), API.processPlaceholder2(player, taxPathLore, "tax-value-item"));
    }
    public static ItemStack getTaxPriceItem(Player player) {
        return getGenericItem(player, API.processPlaceholder1(player, taxPathName, "tax-price-item"), API.processPlaceholder2(player, taxPathLore, "tax-price-item"));
    }
    public static ItemStack getOverTaxValueItem(Player player) {
        return getGenericItem(player, API.processPlaceholder1(player, taxPathName, "overtax-value-item"), API.processPlaceholder2(player, taxPathLore, "overtax-value-item"));
    }
    public static ItemStack getOverTaxPriceItem(Player player) {
        return getGenericItem(player, API.processPlaceholder1(player, taxPathName, "overtax-price-item"), API.processPlaceholder2(player, taxPathLore, "tax-item"));
    }
    public static ItemStack getTotalTaxPrice(Player player) {
       return setNamespaceKey(getGenericItem(player, API.processPlaceholder1(player, taxPathName, "total-item"), API.processPlaceholder2(player, taxPathLore, "total-item")), SkyBank.payLoan);
    }


    public static ItemStack getGenericItem(Player player, String name, ArrayList<String> lore){
        return new ItemBuilder(Material.BOOK).setDisplayName(PlaceholderAPI.setPlaceholders(player, name)).setLore((ArrayList<String>) PlaceholderAPI.setPlaceholders(player, lore)).build();
    }

    public static ItemStack setNamespaceKey(ItemStack item, NamespacedKey key){
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, key.toString());
        item.setItemMeta(meta);
        return item;
    }





}
