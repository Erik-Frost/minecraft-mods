package com.worldanchor.monsterteams;

import com.worldanchor.monsterteams.mixin.GoalSelectorAccessor;
import com.worldanchor.monsterteams.mixin.MobEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;


// All these are server side only
public class TeamUtil {

    public static void addLivingEntityTeamMechanics(LivingEntity livingEntity) {
        // Display name based on gamerule
        displayNameBasedOnGamerule(livingEntity);
        // Add team goals
        if (livingEntity instanceof MobEntity) addTeamGoals((MobEntity) livingEntity);
    }

    public static void removeTeamGoals(MobEntity mobEntity) {
        // Get goals to remove
        List<PrioritizedGoal> toRemove = new ArrayList<>();
        for (PrioritizedGoal prioritizedGoal :
                ((GoalSelectorAccessor) ((MobEntityAccessor) mobEntity).getTargetSelector()).getGoals()) {
            if (prioritizedGoal.getGoal() instanceof NearestAttackableTargetGoal
                    && (((NearestAttackableTargetGoal<?>) prioritizedGoal.getGoal()).targetEntitySelector
                                instanceof notOnTeamAndHostileExceptCreeperEntityPredicate
                    || ((NearestAttackableTargetGoal<?>) prioritizedGoal.getGoal()).targetEntitySelector
                                instanceof OnTeamEntityPredicate)) {
                toRemove.add(prioritizedGoal);
            }
            else if (prioritizedGoal.getGoal() instanceof FollowLivingEntityGoal) toRemove.add(prioritizedGoal);
        }
        // Remove goals
        for (PrioritizedGoal prioritizedGoal : toRemove) {
            ((MobEntityAccessor) mobEntity).getTargetSelector().remove(prioritizedGoal.getGoal());
        }
    }


    public static void addTeamGoals(MobEntity mobEntity) {
        // Add attack goals
        ((MobEntityAccessor) mobEntity).getTargetSelector().add(2,
                new NearestAttackableTargetGoal<>(mobEntity, LivingEntity.class,
                        5,false, false,
                        new OnTeamEntityPredicate()));
        ((MobEntityAccessor) mobEntity).getTargetSelector().add(3,
                new NearestAttackableTargetGoal<>(mobEntity, LivingEntity.class,
                        5,false, false,
                        new notOnTeamAndHostileExceptCreeperEntityPredicate()));
        if (mobEntity instanceof HostileEntity) {
            ((MobEntityAccessor) mobEntity).getGoalSelector().add(0,
                    new FollowLivingEntityGoal(mobEntity, (livingEntity) -> {
                        return livingEntity.getScoreboardTeam() == mobEntity.getScoreboardTeam()
                                && livingEntity.isInvisible() == mobEntity.isInvisible()
                                && (livingEntity instanceof PlayerEntity && !((PlayerEntity)livingEntity).isCreative()
                                    && !livingEntity.isSpectator() && livingEntity.isSneaking())
                                && livingEntity.getOffHandStack().getItem() instanceof BannerItem;
                    },1d, mobEntity.getRandom().nextInt(4) + 3,16, true));
            ((MobEntityAccessor) mobEntity).getGoalSelector().add(1,
                    new FollowLivingEntityGoal(mobEntity, (livingEntity) -> {
                        return livingEntity.getScoreboardTeam() == mobEntity.getScoreboardTeam()
                                && livingEntity.isInvisible() == mobEntity.isInvisible()
                                && (livingEntity instanceof PlayerEntity && !((PlayerEntity)livingEntity).isCreative()
                                    && !livingEntity.isSpectator())
                                && livingEntity.getOffHandStack().getItem() instanceof BannerItem;
                    },1d, mobEntity.getRandom().nextInt(4) + 5,32, false));
            ((MobEntityAccessor) mobEntity).getGoalSelector().add(8,
                    new FollowLivingEntityGoal(mobEntity, (livingEntity) -> {
                        return livingEntity.getScoreboardTeam() == mobEntity.getScoreboardTeam()
                                && livingEntity.isInvisible() == mobEntity.isInvisible()
                                && (livingEntity instanceof PlayerEntity && !((PlayerEntity)livingEntity).isCreative()
                                && !livingEntity.isSpectator());
                    },1d, mobEntity.getRandom().nextInt(4) + 7,48, false));
        }
    }

    public static void displayNameBasedOnGamerule(LivingEntity livingEntity) {
        // Turn on name tag if rule is set to
        livingEntity.setCustomNameVisible(
                livingEntity.getEntityWorld().getGameRules().getBoolean(Mod.DISPLAY_TEAM_NAME_TAGS));

    }

    public static ArrayList<MobEntity> getMobEntitiesOnTeam(Team team, MinecraftServer server) {
        ArrayList<MobEntity> mobEntities = new ArrayList<>();
        for (String string : team.getPlayerList()) {
            // Add entity to collection
            if (string.length() > 16) {
                // Is non player entity
                Entity entity = null;
                for (ServerWorld world : server.getWorlds()) {
                    // For each world
                    entity = world.getEntity(UUID.fromString(string));
                    if (entity instanceof MobEntity) {
                        mobEntities.add((MobEntity)entity);
                        break;
                    }
                }
            }
        }
        return  mobEntities;
    }

    public static void removeFromTeamHelper(LivingEntity livingEntity) {
        if (livingEntity.getScoreboardTeam() != null) {
            // Get serverScoreboard
            ServerScoreboard scoreboard = Objects.requireNonNull(livingEntity.getServer()).getScoreboard();
            // Remove livingEntity from team
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity playerEntity = ((PlayerEntity) livingEntity);
                scoreboard.clearPlayerTeam(playerEntity.getName().getString());
            } else {
                scoreboard.clearPlayerTeam(livingEntity.getUuidAsString());
            }
        }
    }

    public static void addToTeamHelper(LivingEntity livingEntity, String teamName) {
        // Get serverScoreboard
        ServerScoreboard scoreboard = Objects.requireNonNull(livingEntity.getServer()).getScoreboard();
        // Make team if team doesn't exist
        if (!teamExists(teamName, scoreboard)) {
            makeTeam(teamName, scoreboard);
        }
        // Add livingEntity to team
        Team team = scoreboard.getTeam(teamName);
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity playerEntity = ((PlayerEntity) livingEntity);
            scoreboard.addPlayerToTeam(playerEntity.getName().getString(), team);
        } else {
            // Add livingEntity to team
            scoreboard.addPlayerToTeam(livingEntity.getUuidAsString(), team);
        }
    }

    public static boolean teamExists(String name, Scoreboard scoreboard) {
        for (String teamName : scoreboard.getTeamNames()) {
            if (name.equals(teamName)) return true;
        }
        return false;
    }

    public static void makeTeam(String name, ServerScoreboard scoreboard) {
        Team team = scoreboard.addTeam(name);
        if (Formatting.byName(name) != null) team.setColor(Formatting.byName(name));
        else team.setColor(Formatting.WHITE);
    }





    public static void alertOthersOnTeam(LivingEntity attacked, LivingEntity attacker) {
        double alertHorizontalDistance = 16;
        double alertVerticalDistance = 16;
        Box box = Box.method_29968(attacked.getPos()).expand(alertHorizontalDistance, alertVerticalDistance,
                alertHorizontalDistance);
        // Maybe add team predicate
        List<MobEntity> list = attacked.world.getEntitiesIncludingUngeneratedChunks(MobEntity.class, box);
        Iterator iterator = list.iterator();

        MobEntity mobentity;
        while (true) {
            if (!iterator.hasNext()) {
                return;
            }
            mobentity = (MobEntity) iterator.next();
            if (mobentity.isTeammate(attacked) && attacked != mobentity && mobentity.getTarget() == null
                    && attacker != null && !mobentity.isTeammate(attacker) && attacker.isAlive()) {
                mobentity.setTarget(attacker);
                mobentity.setAttacker(attacker);
                mobentity.world.sendEntityStatus(mobentity, (byte)123);
            }
        }
    }


    // Predicate filter to target only LivingEntities on a team
    public static class OnTeamEntityPredicate extends TargetPredicate {
        @Override
        public boolean test(@Nullable LivingEntity attacker, LivingEntity target) {
            // Target must be on a team to target
            return target.getScoreboardTeam() != null && super.test(attacker, target);
        }
    }

    // Predicate filter to target only LivingEntities on a team
    public static class notOnTeamAndHostileExceptCreeperEntityPredicate extends TargetPredicate {
        @Override
        public boolean test(@Nullable LivingEntity attacker, LivingEntity target) {
            return (target.getScoreboardTeam() == null &&
                    ((target instanceof HostileEntity && !(target instanceof CreeperEntity))
                            || target instanceof SlimeEntity))
                    && super.test(attacker, target);
        }
    }


    public static class OnTeamAndHostileExceptCreeperEntityPredicate extends TargetPredicate {
        @Override
        public boolean test(@Nullable LivingEntity attacker, LivingEntity target) {
            return (target.getScoreboardTeam() != null &&
                    ((target instanceof HostileEntity && !(target instanceof CreeperEntity))
                            || target instanceof SlimeEntity))
                    && super.test(attacker, target);
        }
    }






    // Probably can remove
    // Pretty much an exact copy of Nearest attackable target, but it is using the team only attack target
    public static class NearestAttackableTargetGoal<T extends LivingEntity> extends TrackTargetGoal {
        protected final Class<T> targetClass;
        protected final int targetChance;
        protected LivingEntity nearestTarget;
        /** This filter is applied to the Entity search. Only matching entities will be targeted. */
        public TargetPredicate targetEntitySelector;

        public NearestAttackableTargetGoal(MobEntity goalOwnerIn, Class<T> targetClassIn,
                                           int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn,
                                           TargetPredicate targetPredicate) {
            super(goalOwnerIn, checkSight, nearbyOnlyIn);
            targetClass = targetClassIn;
            targetChance = targetChanceIn;
            setControls(EnumSet.of(Goal.Control.TARGET));
            targetEntitySelector = (targetPredicate).setBaseMaxDistance(getFollowRange());
        }


        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canStart() {
            if (targetChance > 0 && mob.getRandom().nextInt(100) < targetChance) {
                return false;
            } else {
                findNearestTarget();
                return nearestTarget != null;
            }
        }

        protected Box getTargetableArea(double targetDistance) {
            return mob.getBoundingBox().expand(targetDistance, targetDistance, targetDistance);
        }

        protected void findNearestTarget() {
            if (targetClass != PlayerEntity.class && targetClass != ServerPlayerEntity.class) {
                nearestTarget = mob.world.getClosestEntity(targetClass, targetEntitySelector, mob, mob.getX(),
                        mob.getEyeY(), mob.getZ(), getTargetableArea(getFollowRange()));
            } else {
                nearestTarget = mob.world.getClosestPlayer(targetEntitySelector, mob, mob.getX(), mob.getEyeY(),
                        mob.getZ());
            }

        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            if (nearestTarget.isAlive()) {
                mob.setTarget(nearestTarget);
            }
            super.start();
        }
    }

    public static class FollowLivingEntityGoal extends Goal {
        private final MobEntity mob;
        private final Predicate<LivingEntity> followTargetPredicate;
        private LivingEntity target;
        private final double speed;
        private final EntityNavigation navigation;
        private int updateCountdownTicks;
        private final float minDistance;
        private float oldWaterPathFindingPenalty;
        private final float maxDistance;
        private final boolean sneak;

        public FollowLivingEntityGoal(MobEntity mob, Predicate<LivingEntity> followTargetPredicate, double speed,
                                      float minDistance, float maxDistance, boolean sneak) {
            this.mob = mob;
            this.followTargetPredicate = followTargetPredicate;
            this.speed = speed;
            this.navigation = mob.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.sneak = sneak;
            this.setControls(EnumSet.of(Goal.Control.MOVE));

        }

        public boolean canStart() {
            List<LivingEntity> list = this.mob.world.getEntitiesByClass(LivingEntity.class,
                    this.mob.getBoundingBox().expand(this.maxDistance), this.followTargetPredicate);
            LivingEntity closestEntity = this.mob.world.getClosestEntity(list, new TargetPredicate(), null,
                    mob.getX(), mob.getX(), mob.getX());
            if (closestEntity != null) {
                this.target = closestEntity;
                return true;
            }

            return false;
        }

        public boolean shouldContinue() {
            if (sneak && !target.isSneaking()) return false;
            return this.target != null && !this.navigation.isIdle() && this.mob.squaredDistanceTo(this.target) >
                                                                       (double)(this.minDistance * this.minDistance);
        }

        public void start() {
            if (sneak) {
                mob.setSneaking(true);
                mob.setSilent(true);
            }
            this.updateCountdownTicks = 0;
            this.oldWaterPathFindingPenalty = this.mob.getPathfindingPenalty(PathNodeType.WATER);
            this.mob.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            if (sneak) {
                mob.setSneaking(false);
                mob.setSilent(false);
            }
            this.target = null;
            this.navigation.stop();
            this.mob.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathFindingPenalty);
        }

        public void tick() {
            if (this.target != null && !this.mob.isLeashed()) {
                if (--this.updateCountdownTicks <= 0) {
                    this.updateCountdownTicks = 10;
                    double d = this.mob.getX() - this.target.getX();
                    double e = this.mob.getY() - this.target.getY();
                    double f = this.mob.getZ() - this.target.getZ();
                    double g = d * d + e * e + f * f;
                    if (!(g <= (double)(this.minDistance * this.minDistance))) {
                        this.navigation.startMovingTo(this.target, this.speed);
                    } else {
                        this.navigation.stop();
                    }
                }
            }
        }
    }
}
