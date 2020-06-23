package com.lothrazar.horsestandstill;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(ModHorseStandStill.MODID)
public class ModHorseStandStill {

  public static final String MODID = "horsestandstill";

  public ModHorseStandStill() {
    MinecraftForge.EVENT_BUS.register(new EventHorseStandStill());
  }
}
