package com.worldanchor.monsterteams;

import com.worldanchor.monsterteams.mixin.GoalSelectorAccessor;
import com.worldanchor.monsterteams.mixin.MobAccessor;
import com.worldanchor.monsterteams.mixin.NearestAttackableTargetGoalAccessor;
import com.worldanchor.monsterteams.mixin.TargetingConditionsAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;


// All these are server side only
public class TeamUtil {

    public static void addLivingEntityTeamMechanics(LivingEntity livingEntity) {
        // Display name based on gamerule
        displayNameBasedOnGamerule(livingEntity);
        // Add team goals
        if (livingEntity instanceof Mob) addTeamGoals((Mob) livingEntity);
    }

    public static void removeTeamGoals(Mob mobEntity) {
        // Get goals to remove
        List<WrappedGoal> toRemove = new ArrayList<>();
        for (WrappedGoal prioritizedGoal :
                ((GoalSelectorAccessor) ((MobAccessor) mobEntity).getTargetSelector()).getGoals()) {
            if (prioritizedGoal.getGoal() instanceof NearestAttackableTargetGoal
                    && (((TargetingConditionsAccessor)((NearestAttackableTargetGoalAccessor)prioritizedGoal.getGoal()).getTargetConditions()).getSelector()
                                instanceof notOnTeamAndHostileExceptCreeperEntityPredicate
                    || ((TargetingConditionsAccessor)((NearestAttackableTargetGoalAccessor)prioritizedGoal.getGoal()).getTargetConditions()).getSelector()
                                instanceof OnTeamEntityPredicate)) {
                toRemove.add(prioritizedGoal);
            }
        }
        // Remove goals
        for (WrappedGoal prioritizedGoal : toRemove) {
            ((MobAccessor) mobEntity).getTargetSelector().removeGoal(prioritizedGoal.getGoal());
        }
    }


    public static void addTeamGoals(Mob mobEntity) {
        // Add attack goals
        ((MobAccessor) mobEntity).getTargetSelector().addGoal(2,
                new NearestAttackableTargetGoal<>(mobEntity, LivingEntity.class,
                        5,false, false,
                        new OnTeamEntityPredicate()));
        ((MobAccessor) mobEntity).getTargetSelector().addGoal(3,
                new NearestAttackableTargetGoal<>(mobEntity, LivingEntity.class,
                        5,false, false,
                        new notOnTeamAndHostileExceptCreeperEntityPredicate()));
    }

    public static void displayNameBasedOnGamerule(LivingEntity livingEntity) {
        // Turn on name tag if rule is set to
        livingEntity.setCustomNameVisible(
                livingEntity.level.getGameRules().getBoolean(Server.DISPLAY_TEAM_NAME_TAGS));

    }

    public static ArrayList<Mob> getMobEntitiesOnTeam(Team team, MinecraftServer server) {
        ArrayList<Mob> mobEntities = new ArrayList<>();
        for (String string : team.getPlayers()) {
            // Add entity to collection
            if (string.length() > 16) {
                // Is non player entity
                Entity entity = null;
                for (ServerLevel level : server.getAllLevels()) {
                    // For each world
                    entity = level.getEntity(UUID.fromString(string));
                    if (entity instanceof Mob) {
                        mobEntities.add((Mob)entity);
                        break;
                    }
                }
            }
        }
        return  mobEntities;
    }

    public static void removeFromTeamHelper(LivingEntity livingEntity) {
        if (livingEntity.getTeam() != null) {
            // Get serverScoreboard
            ServerScoreboard scoreboard = Objects.requireNonNull(livingEntity.getServer()).getScoreboard();
            // Remove livingEntity from team
            if (livingEntity instanceof Player) {
                Player playerEntity = ((Player) livingEntity);
                scoreboard.removePlayerFromTeam(playerEntity.getName().getString());
            } else {
                scoreboard.removePlayerFromTeam(livingEntity.getStringUUID());
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
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (livingEntity instanceof Player) {
            Player playerEntity = ((Player) livingEntity);
            scoreboard.addPlayerToTeam(playerEntity.getName().getString(), team);
        } else {
            // Add livingEntity to team
            scoreboard.addPlayerToTeam(livingEntity.getStringUUID(), team);
        }
    }

    public static boolean teamExists(String name, Scoreboard scoreboard) {
        for (String teamName : scoreboard.getTeamNames()) {
            if (name.equals(teamName)) return true;
        }
        return false;
    }

    public static void makeTeam(String name, ServerScoreboard scoreboard) {
        PlayerTeam team = scoreboard.addPlayerTeam(name);
        if (ChatFormatting.getByName(name) != null) team.setColor(ChatFormatting.getByName(name));
        else team.setColor(ChatFormatting.WHITE);
    }





    public static void alertOthersOnTeam(LivingEntity attacked, LivingEntity attacker) {
        double alertHorizontalDistance = 16;
        double alertVerticalDistance = 16;
        AABB box = AABB.unitCubeFromLowerCorner(attacked.getEyePosition()).inflate(alertHorizontalDistance, alertVerticalDistance,
                alertHorizontalDistance);
        // Maybe add team predicate
        List<Mob> list = attacked.level.getEntitiesOfClass(Mob.class, box, EntitySelector.LIVING_ENTITY_STILL_ALIVE);
        Iterator iterator = list.iterator();

        Mob mobentity;
        while (true) {
            if (!iterator.hasNext()) {
                return;
            }
            mobentity = (Mob) iterator.next();
            if (mobentity.isAlliedTo(attacked) && attacked != mobentity && mobentity.getTarget() == null
                    && attacker != null && !mobentity.isAlliedTo(attacker) && attacker.isAlive()) {
                mobentity.setTarget(attacker);
                mobentity.setLastHurtByMob(attacker);
            }
        }
    }


    // Predicate filter to target only LivingEntities on a team
    public static class OnTeamEntityPredicate implements Predicate<LivingEntity> {
        @Override
        public boolean test(LivingEntity target) {
            // Target must be on a team to target
            return target.getTeam() != null;
        }
    }

    public static class notOnTeamAndHostileExceptCreeperEntityPredicate implements Predicate<LivingEntity> {
        @Override
        public boolean test(LivingEntity target) {
            return (!(target instanceof Creeper) && target.getTeam() == null &&
                    (target instanceof Monster || target instanceof Slime));
        }
    }


    public static class OnTeamAndHostileExceptCreeperEntityPredicate implements Predicate<LivingEntity> {
        @Override
        public boolean test(LivingEntity target) {
            return (!(target instanceof Creeper) && target.getTeam() != null &&
                    (target instanceof Monster || target instanceof Slime));
        }
    }
}
