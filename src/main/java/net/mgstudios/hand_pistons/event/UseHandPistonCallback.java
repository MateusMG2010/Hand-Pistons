package net.mgstudios.hand_pistons.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mgstudios.hand_pistons.item.HandPiston;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

public final class UseHandPistonCallback {
    public static void register(){
        UseBlockCallback.EVENT.register((player, level, interactionHand, blockHitResult) -> {
            final Direction blockDirection = blockHitResult.getDirection().getOpposite();
            if (player.getItemInHand(interactionHand).is(HandPiston.getHandPiston())) {
                if(player.isShiftKeyDown() && blockDirection == Direction.DOWN){
                    if(player.onGround() && player.blockPosition().equals(blockHitResult.getBlockPos().above())){
                        level.playSound(player, blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                        player.setDeltaMovement(0, 1, 0);
                    }
                    return InteractionResult.PASS;
                }
                if(interactionHand == InteractionHand.MAIN_HAND){
                    final BlockPos oldLocation = blockHitResult.getBlockPos();
                    final boolean canPlayEffect = switch (blockDirection){
                        case NORTH -> moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY(), oldLocation.getZ() - 1), level, oldLocation);
                        case WEST -> moveBlock(BlockPos.containing(oldLocation.getX() - 1, oldLocation.getY(), oldLocation.getZ()), level, oldLocation);
                        case SOUTH -> moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY(), oldLocation.getZ() + 1), level, oldLocation);
                        case EAST -> moveBlock(BlockPos.containing(oldLocation.getX() + 1, oldLocation.getY(), oldLocation.getZ()), level, oldLocation);
                        case UP -> moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY() + 1, oldLocation.getZ()), level, oldLocation);
                        case DOWN -> moveBlock(BlockPos.containing(oldLocation.getX(), oldLocation.getY() - 1, oldLocation.getZ()), level, oldLocation);
                    };
                    if(canPlayEffect) level.playSound(player, oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS);
                }
            }
            return InteractionResult.PASS;
        });
    }
    private static boolean moveBlock(BlockPos newLocation, Level level, BlockPos oldLocation){
        final BlockState state1 = level.getBlockState(oldLocation); //Block to place.
        final BlockState state2 = level.getBlockState(newLocation); //New location old block.
        if((state2.getBlock() instanceof AirBlock || state2.getBlock() instanceof LiquidBlock) && !(state1.getBlock() instanceof BedBlock) && !state1.is(Blocks.CHEST)){
            if(!state2.is(Blocks.LAVA)) level.setBlockAndUpdate(oldLocation, state2);
            else level.setBlockAndUpdate(oldLocation, Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(newLocation, state1);
            if(!state1.canSurvive(level, newLocation)) {
                level.setBlockAndUpdate(newLocation, state2);
                Block.dropResources(state1, level, newLocation);
            }
            return true;
        }
        return false;
    }
}