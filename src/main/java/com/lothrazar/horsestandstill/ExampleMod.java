package com.lothrazar.horsestandstill;

import org.apache.logging.log4j.Logger;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.ItemLead;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ExampleMod.MODID)
public class ExampleMod {

  private static final int DISTANCE = 3;
  public static final String MODID = "examplemod";
  private static final String NBT_TRACKED = MODID + ":tracked";
  private static final String NBT_TRACKEDX = MODID + ":trackedx";
  private static final String NBT_TRACKEDY = MODID + ":trackedy";
  private static final String NBT_TRACKEDZ = MODID + ":trackedz";
  private static Logger logger;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    MinecraftForge.EVENT_BUS.register(this);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {}

  @SubscribeEvent
  public void onHit(LivingEvent.LivingUpdateEvent event) {
    EntityLivingBase horse = event.getEntityLiving();
    //    ItemStack held = player.getHeldItem(event.getHand());
    if (horse instanceof AbstractHorse) {
      //find my horse 
      ItemLead lead;
      //am i ridden by player
      if (horse.canPassengerSteer()) {
        logger.info("player is here null me out");
        
        if( !horse.getEntityData().getBoolean(NBT_TRACKED)) {

          logger.info("untracked horse started to ride");
          horse.getEntityData().setBoolean(NBT_TRACKED, true);
          horse.getEntityData().removeTag(NBT_TRACKEDX);
          horse.getEntityData().removeTag(NBT_TRACKEDY);
          horse.getEntityData().removeTag(NBT_TRACKEDZ);
        }
       }
      else {

        if( horse.getEntityData().getBoolean(NBT_TRACKED)) {
          logger.info("a tracked horse has no passenger now. so reset to not tracked");
          horse.getEntityData().setBoolean(NBT_TRACKED, false);
             
        }
        
      }
//      int x = horse.getEntityData().getInteger(NBT_TRACKEDX);
//      int y = horse.getEntityData().getInteger(NBT_TRACKEDY);
//      int z = horse.getEntityData().getInteger(NBT_TRACKEDZ);
//      BlockPos pos = new BlockPos(x, y, z);
//      if (UtilWorld.distanceBetweenHorizontal(pos, horse.getPosition()) > DISTANCE) {
//        //so here we go
//        horse.setPosition(x, y, z);
//        horse.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
//        logger.info("warped horse ");
//      }
    }
  }
//
//  @SubscribeEvent
//  public void onEntity(PlayerInteractEvent.EntityInteract event) {
//    EntityPlayer player = event.getEntityPlayer();
//    ItemStack held = player.getHeldItem(event.getHand());
//    if (held.getItem() == Items.APPLE && event.getEntity() instanceof AbstractHorse) {
//      //
//      
//      //find my horse 
//      AbstractHorse horse = (AbstractHorse) event.getEntity();
//      BlockPos pos = horse.getPosition();
//      horse.getEntityData().setBoolean(NBT_TRACKED, true);
//      horse.getEntityData().setInteger(NBT_TRACKEDX, pos.getX());
//      horse.getEntityData().setInteger(NBT_TRACKEDY, pos.getY());
//      horse.getEntityData().setInteger(NBT_TRACKEDZ, pos.getZ());
//      held.shrink(1);
//      //and then 
//    }
//    else
//      if (held.isEmpty() && event.getEntity() instanceof AbstractHorse) {
//        //find my horse 
//        AbstractHorse horse = (AbstractHorse) event.getEntity();
//        if (horse.getEntityData().getBoolean(NBT_TRACKED)) {}
//      }
//    //      //did we turn it off? is the visible timer still going?
//    //      if (ActionType.getTimeout(held) > 0) {
//    //        return;
//    //      }
//    //      ActionType.setTimeout(held);
//    //      event.setCanceled(true);
//    //      UtilSound.playSound(player, player.getPosition(), SoundRegistry.tool_mode, SoundCategory.PLAYERS, 0.1F);
//    //      if (!player.getEntityWorld().isRemote) { // server side
//    //        ActionType.toggle(held);
//    //        UtilChat.sendStatusMessage(player, UtilChat.lang(ActionType.getName(held)));
//    //      }
//    //    }
//  }
}
