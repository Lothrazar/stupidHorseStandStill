package com.lothrazar.horsestandstill;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModHorseStandStill.MODID)
public class ModHorseStandStill {

  public static final String MODID = "horsestandstill";
  public static Logger logger;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new EventHorseStandStill());
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {}

  //
  //  @SubscribeEvent
  //  public void onEntity(PlayerInteractEvent.EntityInteract event) {
  //    EntityPlayer player = event.getEntityPlayer();
  //    ItemStack held = player.getHeldItem(event.getHand());
  //    if (held.getItem() == Items.APPLE && event.getEntity() instanceof AbstractHorse) {
  //      //
  //      
  //      //find my horse 
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
