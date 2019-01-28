package com.github.fernthedev.fernapi.server.forge;

import com.github.fernthedev.fernapi.server.forge.interfaces.UUIDForge;
import com.github.fernthedev.fernapi.server.forge.network.ForgeMessageHandler;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = FernForgeAPI.MODID, name = FernForgeAPI.NAME, version = FernForgeAPI.VERSION, acceptedMinecraftVersions = "[1.8,)")
public class FernForgeAPI implements FernAPIPlugin {
    public static final String MODID = "fernapi";
    public static final String NAME = "FernAPI";
    public static final String VERSION = "1.0";

    public static File configfile;

    @SuppressWarnings("unused")
    public static double ver() {
        return Double.parseDouble(Minecraft.getMinecraft().getVersion());
    }
    //private static IPCClient client;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configfile = event.getSuggestedConfigurationFile();
        Universal.getInstance().setup(new ForgeInterface(this),null,new ForgeMessageHandler());
        UUIDFetcher.setFetchManager(new UUIDForge());
    }





    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }


    @Mod.EventHandler
    public void loaded(FMLPostInitializationEvent e) {

    }

    public static void print(Object someclass, Object text) {
        System.out.println("[DiscordMod] ["+someclass +"] " + text);
    }

    @Override
    @Deprecated
    public void cancelTask(int id) {

    }
}
