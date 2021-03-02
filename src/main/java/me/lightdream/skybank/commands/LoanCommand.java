package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static me.lightdream.skybank.SkyBank.loanedPLayers;
import static me.lightdream.skybank.SkyBank.loanedPLayersNames;

public class LoanCommand extends BaseCommand{

    public LoanCommand() {
        forcePlayer = true;
        commandName = "loan";
        argLength = 0;
        usage = "<loan_name/pay> [amount]";
    }

    @Override
    public boolean run() {

        if(args.length == 1) {

            ArrayList<String> loans = API.getLoans();

            API.sendColoredMessage(player, Language.unpaid_loans.replace("%money%", String.valueOf(API.getAmountToPayForLoans(player.getUniqueId()))));

            if(loans.size() == 0){
                API.sendColoredMessage(player, Language.no_available_loans);
                return true;
            }

            API.sendColoredMessage(player, Language.available_loans);

            for(String name : loans){
                API.sendColoredMessage(player, Language.loan_listing.replace("%loan%", name).replace("%status%", API.getBeautifiedLoanStatus(player.getUniqueId(), name)));
            }
        }
        if (args.length >= 2) {
            if(args[1].equalsIgnoreCase("pay")){
                double totalBalance = API.getBalance(player.getUniqueId()) + API.getBankBalance(player.getUniqueId());
                if(args.length == 2){
                    sendHelpLine();
                    return true;
                }
                try{
                    int amount = Integer.parseInt(args[2]);
                    if(totalBalance >= amount){
                        API.removeLoan(player.getUniqueId(), amount);
                        double  var1 = Math.min(amount, API.getBankBalance(player.getUniqueId()));
                        amount -= var1;

                        API.removeBankBalance(player.getUniqueId(), var1);
                        API.removeBalance(player.getUniqueId(), amount);

                        API.sendColoredMessage(player, Language.loan_paid);
                    }
                    else{
                        API.sendColoredMessage(player, Language.not_enough_money);
                    }

                }catch (NumberFormatException e){
                    API.sendColoredMessage(player, Language.invalid_number_format);
                }
                return true;
            }

            Map<?, ?> loan = API.getBankLoan(args[1]);

            if(loan != null){
                if(player.hasPermission((String) loan.get("permission"))){

                    boolean canGetLoan = false;

                    int index = loanedPLayersNames.indexOf(player.getUniqueId().toString());
                    //System.out.println(index);

                    List<Map<?, ?>> multipleLoansPermission = SkyBank.config.getMapList("multiple-loan");

                    for(int i = multipleLoansPermission.size() - 1; i >= 0; i--){
                        if(player.hasPermission((String) multipleLoansPermission.get(i).get("permission"))){
                            if(index == -1){
                                canGetLoan = (int) multipleLoansPermission.get(i).get("loans") != 0;
                                break;
                            }
                            canGetLoan = (int) multipleLoansPermission.get(i).get("loans") > ((List<String>) loanedPLayers.get(index).get("loan")).size();
                            break;
                        }
                    }

                    if(API.getLoanStatus(player.getUniqueId(), args[1])){
                        if(canGetLoan){
                            if(API.getHoursPlayed(player.getUniqueId()) >= Double.parseDouble(String.valueOf(loan.get("hours-needed")))){
                                if(!SkyBank.loanedPLayersNames.contains(String.valueOf(player.getUniqueId()))){
                                    SkyBank.loanedPLayersNames.add(String.valueOf(player.getUniqueId()));
                                    loanedPLayers.add(API.getBankLoanTemplate(player.getUniqueId(), Double.parseDouble(String.valueOf(loan.get("money"))), Collections.singletonList((String) loan.get("name"))));
                                }
                                else {
                                    loanedPLayers.set(index, API.getBankLoanTemplate(player.getUniqueId(), Double.parseDouble(String.valueOf(loan.get("money"))), (String) loan.get("name"), loanedPLayers.get(index)));
                                }
                                API.addBankBalance(player.getUniqueId(), Double.parseDouble(String.valueOf(loan.get("money"))));
                                API.sendColoredMessage(player, Language.balance_updated);
                            }
                            else
                                API.sendColoredMessage(player, Language.do_not_have_enough_hours_played_loan);
                        }
                        else
                            API.sendColoredMessage(player, Language.can_not_take_loan);
                    }
                    else
                        API.sendColoredMessage(player, Language.loan_already_taken);


                }
                else
                    API.sendColoredMessage(player, Language.do_not_have_permission_loan);
            }
            else {
                API.sendColoredMessage(player, Language.loan_does_not_exist);
            }


        }


        return true;
    }

}
