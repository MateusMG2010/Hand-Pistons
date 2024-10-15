package net.mgstudios.hand_pistons;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

public class HandPistons implements ModInitializer {

    private static final Item HAND_PISTON = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("hand_pistons","hand_piston"), new Item(new Item.Properties().stacksTo(1)));

    @Override
    public void onInitialize() {
        LogUtils.getLogger().info("Hand Pistons ModInitializer has been initialized!");
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((itemGroup) -> itemGroup.addAfter(Items.STICKY_PISTON, HAND_PISTON));
        UseBlockCallback.EVENT.register((player, level, interactionHand, blockHitResult) -> {
            if(interactionHand == InteractionHand.MAIN_HAND && player.getMainHandItem().is(HAND_PISTON)){
                if(player.isShiftKeyDown() && blockHitResult.getDirection().getOpposite() == Direction.DOWN && player.onGround() && player.blockPosition().equals(blockHitResult.getBlockPos().above())){
                    level.playSound(player, blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                    player.setDeltaMovement(0, 1, 0);
                    return InteractionResult.PASS;
                }
                final BlockPos oldLocation = blockHitResult.getBlockPos();
                final boolean canPlayEffect = switch (blockHitResult.getDirection().getOpposite()){
                    case NORTH -> moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY(), oldLocation.getZ() - 1), level, oldLocation);
                    case WEST -> moveBlock(BlockPos.containing(oldLocation.getX() - 1, oldLocation.getY(), oldLocation.getZ()), level, oldLocation);
                    case SOUTH -> moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY(), oldLocation.getZ() + 1), level, oldLocation);
                    case EAST -> moveBlock(BlockPos.containing(oldLocation.getX() + 1, oldLocation.getY(), oldLocation.getZ()), level, oldLocation);
                    case UP -> moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY() + 1, oldLocation.getZ()), level, oldLocation);
                    case DOWN -> moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY() - 1, oldLocation.getZ()), level, oldLocation);
                };
                if(canPlayEffect) level.playSound(player, oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
            }
            return InteractionResult.PASS;
        });
    }
    private boolean moveBlock(BlockPos newLocation, Level level, BlockPos oldLocation){
        final BlockState newBlock = level.getBlockState(oldLocation);
        final BlockState newLocationBlock = level.getBlockState(newLocation);
        if((newLocationBlock.getBlock() instanceof AirBlock || newLocationBlock.getBlock() instanceof LiquidBlock) && !newBlock.is(Blocks.CHEST) && !(newBlock.getBlock() instanceof BedBlock)){
            if(!newLocationBlock.is(Blocks.LAVA)) level.setBlockAndUpdate(oldLocation, newLocationBlock);
            else level.setBlockAndUpdate(oldLocation, Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(newLocation, newBlock);
            if(!newBlock.canSurvive(level, newLocation)) {
                level.setBlockAndUpdate(newLocation, newLocationBlock);
                Block.dropResources(newBlock, level, newLocation);
            }
            return true;
        }
        return false;
    }
}