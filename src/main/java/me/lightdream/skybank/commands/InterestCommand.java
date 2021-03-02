package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;

import java.util.List;
import java.util.Map;


public class InterestCommand  extends BaseCommand{

    public InterestCommand() {
        forcePlayer = true;
        commandName = "interest";
        argLength = 0;
    }

    @Override
    public boolean run() {

        List<Map<?, ?>> list = SkyBank.config.getMapList("interests");
        Map<?, ?> map = list.get(0);

        double interestPercent = API.getInterestPercent(player.getUniqueId(), list);
        double interestValue = API.getInterest(player.getUniqueId(), interestPercent);

        API.sendColoredMessage(player, Language.interest_name.replace("%name%", String.valueOf(map.get("name"))));
        API.sendColoredMessage(player, Language.interest_percent.replace("%interest%", String.valueOf(interestPercent)));
        API.sendColoredMessage(player, Language.interest_range.replace("%max%", String.valueOf(map.get("max-money"))).replace("%min%", String.valueOf(map.get("min-money"))));
        API.sendColoredMessage(player, Language.estimated_interest.replace("%interest%", String.valueOf(interestValue)));






        return true;
    }
}