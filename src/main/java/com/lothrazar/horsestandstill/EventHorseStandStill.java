package com.lothrazar.horsestandstill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHorseStandStill {

  private static final String STATE_WAITING = ModHorseStandStill.MODID + ".waiting";
  private static final String STATE_RIDING = ModHorseStandStill.MODID + ".riding";
  private static final String NBT_RIDING = ModHorseStandStill.MODID + ".tracked";
  private static final String NBT_TRACKEDX = ModHorseStandStill.MODID + ".trackedx";
  private static final String NBT_TRACKEDY = ModHorseStandStill.MODID + ".trackedy";
  private static final String NBT_TRACKEDZ = ModHorseStandStill.MODID + ".trackedz";

  @SubscribeEvent
  public void onHit(LivingEvent.LivingUpdateEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity instanceof HorseEntity == false) {
      return;
    }
    //find my horse 
    HorseEntity horse = (HorseEntity) entity;
    boolean emptyState = !horse.getPersistentData().contains(NBT_RIDING);
    boolean ridingState = STATE_RIDING.equals(horse.getPersistentData().getString(NBT_RIDING));
    boolean isWaitingState = STATE_WAITING.equals(horse.getPersistentData().getString(NBT_RIDING));
    boolean isPlayerRiding = this.isRiddenByPlayer(horse);
    boolean isSaddled = horse.isHorseSaddled();
    if (emptyState) {
      if (entity.world.isRemote) {
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
    else if (ridingState) {
      if (entity.world.isRemote) {
        return;//no client side data or tracking needed 
      }
      //player is riding
      //did it get off
      if (isPlayerRiding == false) {
        // still saddled, player has gotten off though 
        //but im in riding state
        //so move to waiting 
        if (isSaddled) {
          horse.spawnExplosionParticle();
          horse.attackEntityFrom(DamageSource.MAGIC, 0F);
          if (entity.world.isRemote) {
            return;//no client side data or tracking needed 
          }
          setWaitingStateAndPos(horse);
        }
        else {
          if (entity.world.isRemote) {
            return;//no client side data or tracking needed 
          }
          clearState(horse);
          horse.setNoAI(false);
        }
      }
      //still riding i guess 
    }
    else if (isWaitingState) {
      if (entity.world.isRemote) {
        return;//no client side data or tracking needed 
      }
      if (isSaddled && horse.isAlive()) {
        // still WAITING, ok do my thing
        //wait did a player jump on my back just now 
        if (isPlayerRiding) {
          //waiting to riding  
          setRidingState(horse);
        }
        else {
          //stay waiting 
          onWaitingStateTick(horse);
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

  private boolean isRiddenByPlayer(HorseEntity horse) {
    return horse.getControllingPassenger() instanceof PlayerEntity;
  }

  private void onWaitingStateTick(HorseEntity horse) {
    horse.setNoAI(true);//the only place we use true 
  }

  private void clearState(HorseEntity horse) {
    horse.getPersistentData().remove(NBT_RIDING);
    horse.getPersistentData().remove(NBT_TRACKEDX);
    horse.getPersistentData().remove(NBT_TRACKEDY);
    horse.getPersistentData().remove(NBT_TRACKEDZ);
  }

  private void setWaitingStateAndPos(HorseEntity horse) {
    horse.getPersistentData().putString(NBT_RIDING, STATE_WAITING);
    Vector3d pos = horse.getPositionVec();
    horse.getPersistentData().putInt(NBT_TRACKEDX, (int) pos.getX());
    horse.getPersistentData().putInt(NBT_TRACKEDY, (int) pos.getY());
    horse.getPersistentData().putInt(NBT_TRACKEDZ, (int) pos.getZ());
  }

  private void setRidingState(HorseEntity horse) {
    horse.getPersistentData().putString(NBT_RIDING, STATE_RIDING);
    horse.getPersistentData().remove(NBT_TRACKEDX);
    horse.getPersistentData().remove(NBT_TRACKEDY);
    horse.getPersistentData().remove(NBT_TRACKEDZ);
  }
}
