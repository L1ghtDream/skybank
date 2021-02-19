package me.lightdream.skybank.utils;

import me.lightdream.skybank.enums.LoadFileType;
import org.bukkit.configuration.file.FileConfiguration;

public class Language {

    public static FileConfiguration lang;

    public static String must_be_ingame;
    public static String no_permission_command;
    public static String wrong_usage_command;
    public static String no_island_found;
    public static String unpaid_taxes;
    public static String island_size;
    public static String tax_value;
    public static String tax_price;

    public static void loadLang(){
        lang = Utils.loadConfig("lang.yml", LoadFileType.DEFAULT);

        must_be_ingame        = lang.getString("must_be_ingame");
        no_permission_command = lang.getString("no_permission_command");
        wrong_usage_command   = lang.getString("wrong_usage_command");
        no_island_found       = lang.getString("no_island_found");
        unpaid_taxes          = lang.getString("unpaid_taxes");
        island_size           = lang.getString("island_size");
        tax_value             = lang.getString("tax_value");
        tax_price             = lang.getString("tax_price");
    }

}
