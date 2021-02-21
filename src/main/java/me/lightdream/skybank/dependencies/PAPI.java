package me.lightdream.skybank.dependencies;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.utils.API;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PAPI extends PlaceholderExpansion {

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    @NotNull
    public String getAuthor(){
        return "_LightDream";
    }

    @Override
    @NotNull
    public String getIdentifier(){
        return "skybank";
    }

    @Override
    @NotNull
    public String getVersion(){
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        try {
            if(identifier.equals("tax_count")){
                return String.valueOf(API.getTaxData(player.getUniqueId()));
            }
            if(identifier.equals("island_size")){
                return String.valueOf(API.getIslandSize(player.getUniqueId()));
            }
            if(identifier.equals("tax_value")){
                return String.valueOf(API.getTaxValue(player.getUniqueId()));
            }
            if(identifier.equals("tax_price")){
                return String.valueOf(API.getTaxPrice(player.getUniqueId()));
            }
            if(identifier.equals("overtax_value")){
                return String.valueOf(API.getPlayerOvertaxValue(player.getUniqueId()));
            }
            if(identifier.equals("overtax_price")){
                return String.valueOf(API.getPlayerOvertaxPrice(player.getUniqueId()));
            }
            if(identifier.equals("tax_total_price")){
                return String.valueOf(API.getTaxTotalPrice(player.getUniqueId()));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        return null;
    }

}
