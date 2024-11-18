package com.canoestudio.earthquake;

import com.canoestudio.earthquake.command.SeismicZoneCommand;
import com.canoestudio.earthquake.event.EarthquakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class Earthquake {

    @Mod.Instance
    public static Earthquake instance;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new SeismicZoneCommand());
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EarthquakeEvent());
    }

}