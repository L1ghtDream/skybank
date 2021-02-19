package me.lightdream.skybank;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class Runnable {

    private static double taxTime = 0;
    private static double lastTaxTime = 0;
    private static int taxes;

    public void init(){
        new BukkitRunnable(){
            @Override
            public void run() {
                double timeNow = new Date().getTime();
                if (timeNow - lastTaxTime  >= taxTime) {
                    taxes++;
                    lastTaxTime = timeNow;
                }
            }
        }.runTaskTimer(SkyBank.INSTANCE, 1200, 1200);

    }

    public static void setTaxTime(double time){
        taxTime = time;
    }

    public static void setLastTaxTime(double time){
        lastTaxTime = time;
    }

    public static void setTaxes(int tax){
        taxes = tax;
    }

    public static double getTaxTime(){
        return taxTime;
    }

    public static double getLastTaxTime(){
        return lastTaxTime;
    }

    public static int getTaxes() {
        return taxes;
    }
}
