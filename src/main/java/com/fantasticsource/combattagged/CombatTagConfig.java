package com.fantasticsource.combattagged;

import net.minecraftforge.common.config.Config;

@Config(modid = CombatTagged.MODID)
public class CombatTagConfig
{
    @Config.LangKey(CombatTagged.MODID + ".config.bypassItems")
    @Config.Comment({"If set to true, smite damage bypasses all item effects (eg. infinity armor's special protection, soulbound enchant, curse of vanishing, etc)"})
    public static boolean bypassAllItems = true;

    @Config.LangKey(CombatTagged.MODID + ".config.cooldown")
    @Config.Comment(
            {
                    "How long a player is considered to be 'in combat' after taking damage from, or dealing damage to, another player (in seconds)",
                    "If they disconnect before this amount of time has passed, they die"
            })
    public static int cooldown = 10;

    @Config.LangKey(CombatTagged.MODID + ".config.deathMessage")
    @Config.Comment({"Sets the death message for when someone dies via this mod. %s is a tag that is replaced by the name of the killed player"})
    public static String deathMessage = "%s was smote for their cowardice!";

    @Config.LangKey(CombatTagged.MODID + ".config.showMessages")
    @Config.Comment(
            {
                    "If set to true, sends players messages telling them when then enter and leave combat",
                    "I very highly suggest leaving this set to true"
            })
    public static boolean showMessages = true;

    @Config.LangKey(CombatTagged.MODID + ".config.smiteCommand")
    @Config.Comment(
            {
                    "If set to true, OP players can access the /smite <playername> command",
                    "As a side note, OP players are immune to smite, so if all players are OP, this command (and this entire mod) does nothing"
            })
    @Config.RequiresMcRestart
    public static boolean smiteCommand = true;

    @Config.LangKey(CombatTagged.MODID + ".config.lightning")
    @Config.Comment(
            {
                    "If set to true, players who are killed by this mod seem like they're hit by lightning (the lightning itself doesn't actually deal any damage)",
                    "Why would you ever disable this?"
            })
    public static boolean zeusWasHere = true;
}
