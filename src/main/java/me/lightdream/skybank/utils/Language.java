package me.lightdream.skybank.utils;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import org.bukkit.Bukkit;
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
    public static String invalid_number_format;
    public static String own_balance;
    public static String others_balance;
    public static String player_does_not_exist;
    public static String exception_where_no_exception_was_expected_kick_reason;
    public static String balance_updated;
    public static String interest_name;
    public static String interest_percent;
    public static String interest_range;
    public static String estimated_interest;
    public static String do_not_have_permission_loan;
    public static String do_not_have_enough_hours_played_loan;
    public static String available_loans;
    public static String no_available_loans;
    public static String loan_listing;
    public static String loan_available;
    public static String loan_unavailable;
    public static String not_on_an_island;
    public static String size_0_island;
    public static String something_went_wrong;
    public static String loan_does_not_exist;
    public static String can_not_take_loan;
    public static String loan_already_taken;
    public static String unpaid_loans;
    public static String loan_paid;
    public static String gui_status_updated;


    public static void loadLang(){

        lang = API.loadFile("lang.yml", LoadFileType.DEFAULT);

        must_be_ingame                                        = lang.getString("must_be_ingame");
        no_permission_command                                 = lang.getString("no_permission_command");
        wrong_usage_command                                   = lang.getString("wrong_usage_command");
        no_island_found                                       = lang.getString("no_island_found");
        unpaid_taxes                                          = lang.getString("unpaid_taxes");
        island_size                                           = lang.getString("island_size");
        tax_value                                             = lang.getString("tax_value");
        tax_price                                             = lang.getString("tax_price");
        overtax_percent                                       = lang.getString("overtax_percent");
        overtax_price                                         = lang.getString("overtax_price");
        total_price                                           = lang.getString("total_price");
        not_enough_money                                      = lang.getString("not_enough_money");
        pay_tax_to_leave_island                               = lang.getString("pay_tax_to_leave_island");
        use_tax_pay_confirm                                   = lang.getString("use_tax_pay_confirm");
        leave_tax_percent                                     = lang.getString("leave_tax_percent");
        leave_tax_value                                       = lang.getString("leave_tax_value");
        already_have_leave_tax                                = lang.getString("already_have_leave_tax");
        paid_taxes                                            = lang.getString("paid_taxes");
        invalid_number_format                                 = lang.getString("invalid_number_format");
        own_balance                                           = lang.getString("own_balance");
        others_balance                                        = lang.getString("others_balance");
        player_does_not_exist                                 = lang.getString("player_does_not_exist");
        exception_where_no_exception_was_expected_kick_reason = lang.getString("exception_where_no_exception_was_expected_kick_reason");
        balance_updated                                       = lang.getString("balance_updated");
        interest_name                                         = lang.getString("interest_name");
        interest_percent                                      = lang.getString("interest_percent");
        interest_range                                        = lang.getString("interest_range");
        estimated_interest                                    = lang.getString("estimated_interest");
        do_not_have_permission_loan                           = lang.getString("do_not_have_permission_loan");
        do_not_have_enough_hours_played_loan                  = lang.getString("do_not_have_enough_hours_played_loan");
        available_loans                                       = lang.getString("available_loans");
        no_available_loans                                    = lang.getString("no_available_loans");
        loan_listing                                          = lang.getString("loan_listing");
        loan_available                                        = lang.getString("loan_available");
        loan_unavailable                                      = lang.getString("loan_unavailable");
        not_on_an_island                                      = lang.getString("not_on_an_island");
        size_0_island                                         = lang.getString("size_0_island");
        something_went_wrong                                  = lang.getString("something_went_wrong");
        loan_does_not_exist                                   = lang.getString("loan_does_not_exist");
        can_not_take_loan                                     = lang.getString("can_not_take_loan");
        loan_already_taken                                    = lang.getString("loan_already_taken");
        unpaid_loans                                          = lang.getString("unpaid_loans");
        loan_paid                                             = lang.getString("loan_paid");
        gui_status_updated                                    = lang.getString("gui_status_updated");



    }

}
