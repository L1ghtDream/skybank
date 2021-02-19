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
    public static String overtax_percent;
    public static String overtax_price;
    public static String total_price;
    public static String not_enough_money;
    public static String pay_tax_to_leave_island;
    public static String use_tax_pay_confirm;
    public static String leave_tax_percent;
    public static String leave_tax_value;
    public static String already_have_leave_tax;
    public static String paid_taxes;

    public static void loadLang(){
        lang = Utils.loadFile("lang.yml", LoadFileType.DEFAULT);

        must_be_ingame          = lang.getString("must_be_ingame");
        no_permission_command   = lang.getString("no_permission_command");
        wrong_usage_command     = lang.getString("wrong_usage_command");
        no_island_found         = lang.getString("no_island_found");
        unpaid_taxes            = lang.getString("unpaid_taxes");
        island_size             = lang.getString("island_size");
        tax_value               = lang.getString("tax_value");
        tax_price               = lang.getString("tax_price");
        overtax_percent         = lang.getString("overtax_percent");
        overtax_price           = lang.getString("overtax_price");
        total_price             = lang.getString("total_price");
        not_enough_money        = lang.getString("not_enough_money");
        pay_tax_to_leave_island = lang.getString("pay_tax_to_leave_island");
        use_tax_pay_confirm     = lang.getString("use_tax_pay_confirm");
        leave_tax_percent       = lang.getString("leave_tax_percent");
        leave_tax_value         = lang.getString("leave_tax_value");
        already_have_leave_tax  = lang.getString("already_have_leave_tax");
        paid_taxes              = lang.getString("paid_taxes");
    }

}
