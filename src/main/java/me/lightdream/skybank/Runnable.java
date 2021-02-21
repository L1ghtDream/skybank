package me.lightdream.skybank;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class Runnable {

    //Taxes
    public static double taxTime = 0;
    public static double lastTaxTime = 0;

    //Interests
    public static double interestTime = 0;
    public static double lastInterestTime = 0;

    //Loans
    public static double loanTime = 0;
    public static double lastLoanTime = 0;


    public static void init(){

        //Taxes
        new BukkitRunnable(){
            @Override
            public void run() {
                double timeNow = new Date().getTime();
                if (timeNow - lastTaxTime  >= taxTime) {
                    SkyBank.data.set("taxes", SkyBank.data.getInt("taxes") + 1);
                    lastTaxTime = timeNow;
                }
            }
        }.runTaskTimer(SkyBank.INSTANCE, 0, SkyBank.config.getInt("update-time-tax-time"));

        //Interests
        new BukkitRunnable(){
            @Override
            public void run() {
                double timeNow = new Date().getTime();
                if (timeNow - lastInterestTime  >= interestTime) {
                    SkyBank.data.set("interest", SkyBank.data.getInt("interest") + 1);
                    lastInterestTime = timeNow;
                }
            }
        }.runTaskTimer(SkyBank.INSTANCE, 0, SkyBank.config.getInt("update-time-ineterest-time"));

        //Loans
        new BukkitRunnable(){
            @Override
            public void run() {
                double timeNow = new Date().getTime();
                if (timeNow - lastLoanTime  >= loanTime) {
                    SkyBank.data.set("loan", SkyBank.data.getInt("loan") + 1);
                    loanTime = timeNow;
                }
            }
        }.runTaskTimer(SkyBank.INSTANCE, 0, SkyBank.config.getInt("update-time-ineterest-time"));

    }
}
