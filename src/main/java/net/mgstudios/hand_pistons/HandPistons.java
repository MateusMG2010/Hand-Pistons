package net.mgstudios.hand_pistons;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.mgstudios.hand_pistons.event.UseHandPistonCallback;
import net.mgstudios.hand_pistons.event.UseStickyHandPistonCallback;
import net.mgstudios.hand_pistons.item.HandPiston;

public class HandPistons implements ModInitializer {
    @Override
    public void onInitialize() {
        LogUtils.getLogger().info("Hand Pistons has been initialized!");
        HandPiston.registerItems();
        UseHandPistonCallback.register();
        UseStickyHandPistonCallback.register();
    }
}