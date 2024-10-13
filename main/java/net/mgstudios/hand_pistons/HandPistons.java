package net.mgstudios.hand_pistons;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HandPistons implements ModInitializer {

    private static final Item HAND_PISTON = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("hand_pistons","hand_piston"), new Item(new Item.Properties().stacksTo(1)));

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((itemGroup) -> itemGroup.addAfter(Items.STICKY_PISTON, HAND_PISTON));
        UseBlockCallback.EVENT.register((player, level, interactionHand, blockHitResult) -> {
            if(interactionHand == InteractionHand.MAIN_HAND && player.getMainHandItem().is(HAND_PISTON)){
                final BlockPos oldLocation = blockHitResult.getBlockPos();
                switch (blockHitResult.getDirection().getOpposite()){
                    case NORTH -> {
                        if(moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY(), oldLocation.getZ() - 1), level, oldLocation)) level.playSound(player, oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                    }
                    case WEST -> {
                        if(moveBlock(BlockPos.containing(oldLocation.getX() - 1, oldLocation.getY(), oldLocation.getZ()), level, oldLocation)) level.playSound(player, oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                    }
                    case SOUTH -> {
                        if(moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY(), oldLocation.getZ() + 1), level, oldLocation)) level.playSound(player, oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                    }
                    case EAST -> {
                        if(moveBlock(BlockPos.containing(oldLocation.getX() + 1, oldLocation.getY(), oldLocation.getZ()), level, oldLocation)) level.playSound(player, oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                    }
                    case UP -> {
                        if(moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY() + 1, oldLocation.getZ()), level, oldLocation)) level.playSound(player, oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                    }
                    case DOWN -> {
                        if(moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY() - 1, oldLocation.getZ()), level, oldLocation)) level.playSound(player, oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                    }
                }
            }
            return InteractionResult.PASS;
        });
    }
    boolean moveBlock(BlockPos newLocation, Level level, BlockPos oldLocation){
        final BlockState blockState = level.getBlockState(oldLocation);
        if(level.getBlockState(newLocation).is(Blocks.AIR) && !level.getBlockState(oldLocation).is(Blocks.CHEST)){
            level.setBlock(oldLocation, Blocks.AIR.defaultBlockState(), 1);
            level.setBlock(newLocation, blockState, 1);
            return true;
        }
        return false;
    }
}