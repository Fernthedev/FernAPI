package com.github.fernthedev.fernapi.server.forge;

import com.github.fernthedev.fernapi.server.forge.player.ForgeFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.logging.Logger;

public class ForgeInterface implements MethodInterface {
    private FernForgeAPI fernForgeAPI;
    ForgeInterface(FernForgeAPI fernForgeAPI) {
        this.fernForgeAPI = fernForgeAPI;
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger("fernapi");
    }

    @Override
    public ServerType getServerType() {
        return ServerType.FORGE;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernForgeAPI;
    }

    @Override
    public IFPlayer convertPlayerObjectToFPlayer(Object player) {
        return new ForgeFPlayer((EntityPlayer) player);
    }

    @Override
    public EntityPlayer convertFPlayerToPlayer(IFPlayer ifPlayer) {
        if(ifPlayer instanceof ForgeFPlayer) {
            return ((ForgeFPlayer) ifPlayer).getPlayer();
        }

        List<EntityPlayerMP> playerEntityList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for(EntityPlayer entityPlayer : playerEntityList) {
            if(entityPlayer.getUniqueID() == ifPlayer.getUuid() || entityPlayer.getName().equals(ifPlayer.getName())){
                return entityPlayer;
            }
        }

        return null;
    }
}
