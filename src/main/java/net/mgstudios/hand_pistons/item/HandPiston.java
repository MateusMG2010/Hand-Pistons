package net.mgstudios.hand_pistons.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class HandPiston {
    private static Item HAND_PISTON;
    private static Item STICKY_HAND_PISTON;
    public static void registerItems(){
        HAND_PISTON = register("hand_piston");
        STICKY_HAND_PISTON = register("sticky_hand_piston");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((itemGroup) -> itemGroup.addAfter(Items.STICKY_PISTON, HAND_PISTON));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((itemGroup) -> itemGroup.addAfter(HAND_PISTON, STICKY_HAND_PISTON));
    }
    public static Item getHandPiston(){
        return HAND_PISTON;
    }
    public static Item getStickyHandPiston(){
        return STICKY_HAND_PISTON;
    }
    private static Item register(String item_id){
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("hand_pistons",item_id), new Item(new Item.Properties().stacksTo(1)));
    }
}