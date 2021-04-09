package com.lothrazar.horsestandstill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHorseStandStill {

  private static final String NBT_TRACKEDX = ModHorseStandStill.MODID + ".trackedx";
  private static final String NBT_TRACKEDY = ModHorseStandStill.MODID + ".trackedy";
  private static final String NBT_TRACKEDZ = ModHorseStandStill.MODID + ".trackedz";
  private static final String STATE_WAITING = ModHorseStandStill.MODID + ".waiting";
  private static final String STATE_RIDING = ModHorseStandStill.MODID + ".riding";
  private static final String NBT_RIDING = ModHorseStandStill.MODID + ".tracked";

  private boolean isValid(LivingEntity entity) {
    //some overly un necessary checks here, like mule and donkey already extend from abstract horse. pig does not but can be saddled and ridden in a custom way
    //    entity instanceof PigEntity ||
    if (entity instanceof AbstractHorseEntity ||
        entity instanceof AbstractChestedHorseEntity ||
        entity instanceof MuleEntity ||
        entity instanceof DonkeyEntity) {
      return true;
    }
    return false;
  }

  @SubscribeEvent
  public void onHit(LivingEvent.LivingUpdateEvent event) {
    LivingEntity living = event.getEntityLiving();
    if (isValid(living) == false) {
      return;
    }
    AbstractHorseEntity horse = (AbstractHorseEntity) living;
    //find my horse  
    boolean emptyState = !horse.getPersistentData().contains(NBT_RIDING);
    boolean ridingState = STATE_RIDING.equals(horse.getPersistentData().getString(NBT_RIDING));
    boolean isWaitingState = STATE_WAITING.equals(horse.getPersistentData().getString(NBT_RIDING));
    boolean isPlayerRiding = isRiddenByPlayer(horse);
    if (emptyState) {
      if (horse.world.isRemote) {
        return;//no client side data or tracking needed 
      }
      if (isPlayerRiding) {
        //from no state to RIDING.   
        setRidingState(horse);
      }
      else {
        horse.setNoAI(false);
      }
    }
    else if (ridingState) { ////////////// riding
      if (horse.world.isRemote) {
        return;
      }
      //player is riding
      //did it get off
      if (isPlayerRiding == false) {
        // still saddled, player has gotten off though 
        //but im in riding state
        //so move to waiting 
        if (horse.isHorseSaddled()) {
          horse.spawnExplosionParticle();
          horse.attackEntityFrom(DamageSource.MAGIC, 0F);
          if (horse.world.isRemote) {
            return;
          }
          setWaitingStateAndPos(horse);
        }
        else {
          if (living.world.isRemote) {
            return; //no client side data or tracking needed 
          }
          clearState(horse);
          horse.setNoAI(false);
        }
      }
      //still riding i guess 
    }
    else if (isWaitingState) { ////////////// waiting
      if (horse.world.isRemote) {
        return;
      }
      if (horse.isHorseSaddled() && horse.isAlive() && !horse.isInWater() && !horse.isSwimming()) {
        //wait did a player jump on my back just now 
        if (isPlayerRiding) {
          //waiting to riding  
          setRidingState(horse);
        }
        else {
          //stay waiting  
          horse.setNoAI(true); //the only place we use true 
        }
      }
      else {
        //dead or not saddled, player must have removed, just clear me
        //set to no state
        clearState(horse);
        horse.setNoAI(false);
      }
    }
  }

  private static boolean isRiddenByPlayer(LivingEntity horse) {
    return horse.getControllingPassenger() instanceof PlayerEntity;
  }

  private static void clearState(LivingEntity horse) {
    horse.getPersistentData().remove(NBT_RIDING);
    horse.getPersistentData().remove(NBT_TRACKEDX);
    horse.getPersistentData().remove(NBT_TRACKEDY);
    horse.getPersistentData().remove(NBT_TRACKEDZ);
  }

  private static void setWaitingStateAndPos(LivingEntity horse) {
    horse.getPersistentData().putString(NBT_RIDING, STATE_WAITING);
    Vector3d pos = horse.getPositionVec();
    horse.getPersistentData().putInt(NBT_TRACKEDX, (int) pos.getX());
    horse.getPersistentData().putInt(NBT_TRACKEDY, (int) pos.getY());
    horse.getPersistentData().putInt(NBT_TRACKEDZ, (int) pos.getZ());
  }

  private static void setRidingState(LivingEntity horse) {
    horse.getPersistentData().putString(NBT_RIDING, STATE_RIDING);
    horse.getPersistentData().remove(NBT_TRACKEDX);
    horse.getPersistentData().remove(NBT_TRACKEDY);
    horse.getPersistentData().remove(NBT_TRACKEDZ);
  }
}
