package com.fantasticsource.combattagged;

import net.minecraftforge.common.config.Config;

@Config(modid = CombatTagged.MODID)
public class CombatTagConfig {
    @Config.Comment({
            "",
            "",
            "How long a player is considered to be 'in combat' after taking damage (in seconds)",
            ""
    })
    public static int cooldown = 10;
}
