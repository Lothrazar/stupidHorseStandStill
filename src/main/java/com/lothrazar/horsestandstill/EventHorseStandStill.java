package com.lothrazar.horsestandstill;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHorseStandStill {

  private static final String NBT_TRACKEDX = ModHorseStandStill.MODID + ".trackedx";
  private static final String NBT_TRACKEDY = ModHorseStandStill.MODID + ".trackedy";
  private static final String NBT_TRACKEDZ = ModHorseStandStill.MODID + ".trackedz";
  private static final String STATE_WAITING = ModHorseStandStill.MODID + ".waiting";
  private static final String STATE_RIDING = ModHorseStandStill.MODID + ".riding";
  private static final String NBT_RIDING = ModHorseStandStill.MODID + ".tracked";

  @SubscribeEvent
  public void onHit(LivingTickEvent event) {
    LivingEntity living = event.getEntity();
    if (event.getEntity() instanceof AbstractHorse horse) {
      //find my horse  
      boolean emptyState = !horse.getPersistentData().contains(NBT_RIDING);
      boolean ridingState = STATE_RIDING.equals(horse.getPersistentData().getString(NBT_RIDING));
      boolean isWaitingState = STATE_WAITING.equals(horse.getPersistentData().getString(NBT_RIDING));
      boolean isPlayerRiding = (horse.getControllingPassenger() instanceof Player);
      Level level = horse.level();
      if (emptyState) {
        if (level.isClientSide) {
          return;//no client side data or tracking needed 
        }
        if (isPlayerRiding) {
          //from no state to RIDING.   
          setRidingState(horse);
        }
        else {
          horse.setNoAi(false);
        }
      }
      else if (ridingState) { ////////////// riding
        if (level.isClientSide) {
          return;
        }
        //player is riding
        //did it get off
        if (isPlayerRiding == false) {
          // still saddled, player has gotten off though 
          //but im in riding state
          //so move to waiting 
          if (horse.isSaddled()) {
            horse.spawnAnim();
            horse.hurt(level.damageSources().magic(), 0F);
            if (level.isClientSide) {
              return;
            }
            setWaitingStateAndPos(horse);
          }
          else {
            if (living.level().isClientSide) {
              return; //no client side data or tracking needed 
            }
            clearState(horse);
            horse.setNoAi(false);
          }
        }
        //still riding i guess 
      }
      else if (isWaitingState) { ////////////// waiting
        if (level.isClientSide) {
          return;
        }
        if (horse.isSaddled() && horse.isAlive() && !horse.isInWater() && !horse.isSwimming()) {
          //wait did a player jump on my back just now 
          if (isPlayerRiding) {
            //waiting to riding  
            setRidingState(horse);
          }
          else {
            //stay waiting  
            horse.setNoAi(true); //the only place we use true 
          }
        }
        else {
          //dead or not saddled, player must have removed, just clear me
          //set to no state
          clearState(horse);
          horse.setNoAi(false);
        }
      }
    }
  }

  private static void clearState(LivingEntity horse) {
    horse.getPersistentData().remove(NBT_RIDING);
    horse.getPersistentData().remove(NBT_TRACKEDX);
    horse.getPersistentData().remove(NBT_TRACKEDY);
    horse.getPersistentData().remove(NBT_TRACKEDZ);
  }

  private static void setWaitingStateAndPos(LivingEntity horse) {
    horse.getPersistentData().putString(NBT_RIDING, STATE_WAITING);
    Vec3 pos = horse.position();
    horse.getPersistentData().putInt(NBT_TRACKEDX, (int) pos.x());
    horse.getPersistentData().putInt(NBT_TRACKEDY, (int) pos.y());
    horse.getPersistentData().putInt(NBT_TRACKEDZ, (int) pos.z());
  }

  private static void setRidingState(LivingEntity horse) {
    horse.getPersistentData().putString(NBT_RIDING, STATE_RIDING);
    horse.getPersistentData().remove(NBT_TRACKEDX);
    horse.getPersistentData().remove(NBT_TRACKEDY);
    horse.getPersistentData().remove(NBT_TRACKEDZ);
  }
}
