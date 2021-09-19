package com.worldanchor.cripplingdebt;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class BreakBlockGoal extends Goal {

    protected final Mob mob;
    private LivingEntity target;
    private BlockPos markedLoc;
    private BlockPos mobBlockPos;
    private int ticksSpentBreaking;
    private int lastBreakProgress;

    public BreakBlockGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        // Can dig if tracking a target but can't reach them
        // and a block is in their way
        if (mob.getTarget() != null && !mob.getNavigation().hasDelayedRecomputation() && !canReachTarget())  {
            BlockPos blockPos = getBlock(mob);
            if (blockPos == null)
                return false;
            markedLoc = blockPos;
            mobBlockPos = mob.blockPosition();
            target = mob.getTarget();
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && mob.isAlive() && markedLoc != null
                && mob.distanceToSqr(mobBlockPos.getX(), mobBlockPos.getY(), mobBlockPos.getZ()) < 9
                && mob.distanceTo(target) > 1D;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        mob.getNavigation().recomputePath();
        ticksSpentBreaking = 0;
        if (markedLoc != null)
            mob.level.destroyBlockProgress(mob.getId(), markedLoc, -1);
        markedLoc = null;
        lastBreakProgress = -1;
    }

    @Override
    public void tick() {
        if (markedLoc == null || mob.level.getBlockState(markedLoc).getMaterial() == Material.AIR) {
            ticksSpentBreaking = 0;
            return;
        }
        BlockState state = mob.level.getBlockState(markedLoc);

        int ticksToBreakBlock;
        boolean dropBlock = true;
        if (state.requiresCorrectToolForDrops() && !mob.getMainHandItem().isCorrectToolForDrops(state)) {
            ticksToBreakBlock = (int) (5 * 20f * state.getDestroySpeed(mob.level, markedLoc));
            dropBlock = false;
        }
        else ticksToBreakBlock = (int) (20f * state.getDestroySpeed(mob.level, markedLoc));


        int i = (int)((float) ticksSpentBreaking / (float)ticksToBreakBlock * 10.0F);
        if (i != lastBreakProgress) {
            mob.level.destroyBlockProgress(mob.getId(), markedLoc, i);
            lastBreakProgress = i;
        }

        if (ticksSpentBreaking < ticksToBreakBlock) {
            ticksSpentBreaking++;
            if (ticksSpentBreaking % 20 == 0) {
                SoundType sound = state.getBlock().getSoundType(state);
                mob.level.playSound(null, markedLoc, sound.getBreakSound(), SoundSource.BLOCKS, 0.3F,0.5F);
                mob.swing(InteractionHand.MAIN_HAND);
                mob.getLookControl().setLookAt(markedLoc.getX(), markedLoc.getY(), markedLoc.getZ(), 0.0F, 0.0F);
            }
        }
        else {
            ticksSpentBreaking = 0;
            mob.level.destroyBlock(markedLoc, dropBlock);
            markedLoc = null;
            mob.setSpeed(0);
            mob.getNavigation().stop();
            mob.getNavigation().moveTo(mob.getNavigation().createPath(target, 0), 1D);
            lastBreakProgress = -1;
        }
    }

    public BlockPos getBlock(Mob mob) {

        // They must also be broken in the direction which the target is first
        // Create new bounding box area based on the mobs original one
        Vec3 mobPosAtFeet = new Vec3(mob.getX(), mob.getBoundingBox().minY, mob.getX());
        Vec3 targetPosAtFeet = new Vec3(mob.getTarget().getX(), mob.getTarget().getBoundingBox().minY, mob.getTarget().getX());

        Vec3 difference = targetPosAtFeet.subtract(mobPosAtFeet);
        double horizontalDistance = mobPosAtFeet.distanceTo(new Vec3(targetPosAtFeet.x, mobPosAtFeet.y, targetPosAtFeet.z));

        int yOffset = 0;
        if (mob.blockPosition().getY() < mob.getTarget().blockPosition().getY()) yOffset = 1;
        else if (mob.blockPosition().getY() > mob.getTarget().blockPosition().getY()) yOffset = -1;

        // Blocks must be broken from top to bottom so the mob can go up
        for (int y = (int) mob.getBoundingBox().getYsize(); y >= 0; y -= 1) {
            for (int x = (int) mob.getBoundingBox().getXsize(); x >= 0; x -= 1) {
                for (int z = (int) mob.getBoundingBox().getZsize(); z >= 0; z -= 1) {
                    int xPos = x + mob.blockPosition().getX() + (int) Math.round(difference.x/horizontalDistance);
                    int zPos = z + mob.blockPosition().getZ() + (int) Math.round(difference.z/horizontalDistance);
                    BlockPos blockPos = new BlockPos(xPos,y + yOffset + mob.blockPosition().getY(),zPos);
                    if (!mob.level.getBlockState(blockPos).getCollisionShape(mob.level, blockPos).isEmpty()
                            && mob.level.getBlockState(blockPos).entityCanStandOn(mob.level, blockPos, mob)) {
                        return blockPos;
                    }
                }
            }
        }
        return null;
    }

    private boolean canReachTarget() {
        Path path = mob.getNavigation().getPath();
        if (path == null || path.getEndNode() == null) return false;
        else {
            int i = path.getEndNode().x - mob.getTarget().getBlockX();
            int j = path.getEndNode().z - mob.getTarget().getBlockZ();
            return (double)(i * i + j * j) <= 2.25D;
        }
    }

}

