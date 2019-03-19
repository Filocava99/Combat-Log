package com.fantasticsource.combattagged;

import net.minecraftforge.common.config.Config;

@Config(modid = CombatTagged.MODID)
public class CombatTagConfig {
    @Config.Comment({
            "",
            "",
            "How long a player is considered to be 'in combat' after taking damage from, or dealing damage to, another player (in seconds)",
            "If they disconnect before this amount of time has passed, they die",
            ""
    })
    public static int cooldown = 10;
    @Config.Comment({
            "",
            "",
            "If set to true, sends players messages telling them when then enter and leave combat",
            "I very highly suggest leaving this set to true",
            ""
    })
    public static boolean showMessages = true;
    @Config.Comment({
            "",
            "",
            "If set to true, players who are killed by this mod look like they're hit by lightning (the lightning itself doesn't actually deal any damage)",
            "Why would you ever disable this?",
            ""
    })
    public static boolean zeusWasHere = true;
}
