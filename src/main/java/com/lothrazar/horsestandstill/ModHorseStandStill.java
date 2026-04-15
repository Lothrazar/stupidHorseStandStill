package com.lothrazar.horsestandstill;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ModHorseStandStill.MODID)
public class ModHorseStandStill {

  public static final String MODID = "horsestandstill";

  public ModHorseStandStill(IEventBus modEventBus) {
    NeoForge.EVENT_BUS.register(new EventHorseStandStill());
  }
}
