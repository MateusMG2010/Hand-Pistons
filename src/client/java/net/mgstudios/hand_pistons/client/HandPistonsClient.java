package net.mgstudios.hand_pistons.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;
import java.util.Map;

public class HandPistonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LogUtils.getLogger().info("HandPistons has been initialized!");
    }
}
