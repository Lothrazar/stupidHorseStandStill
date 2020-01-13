package com.lothrazar.horsestandstill;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(ModHorseStandStill.MODID)
public class ModHorseStandStill {

  public static final String MODID = "horsestandstill";
  public static Logger logger;

  public ModHorseStandStill() {
    //    logger = event.getModLog();
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new EventHorseStandStill());
  }
}
