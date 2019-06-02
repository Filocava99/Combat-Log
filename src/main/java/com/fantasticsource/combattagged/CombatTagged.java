package com.fantasticsource.combattagged;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.fantasticsource.combattagged.CombatTagConfig.*;

@Mod(modid = CombatTagged.MODID, name = CombatTagged.NAME, version = CombatTagged.VERSION, acceptableRemoteVersions = "*")
public class CombatTagged
{
    public static final String MODID = "combattagged";
    public static final String NAME = "Combat Tagged!";
    public static final String VERSION = "1.12.2.009a";
    public static final DamageSource smite = new SmiteDamage();
    private static Map<EntityPlayer, Integer> timers = new HashMap<>();

    public CombatTagged()
    {
        MinecraftForge.EVENT_BUS.register(CombatTagged.class);
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID))
        {
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
        }
    }

    @EventHandler
    public static void serverStarting(FMLServerStartingEvent event)
    {
        if (smiteCommand) event.registerServerCommand(new SmiteCommand());
    }

    @SubscribeEvent
    public static void entityDamage(LivingHurtEvent event)
    {
        Entity damagedEntity = event.getEntity();
        Entity damageSource = event.getSource().getImmediateSource();
        Entity damageController = event.getSource().getTrueSource();

        boolean sourceIsPlayer = damageSource instanceof EntityPlayer;
        boolean controllerIsPlayer = damageController instanceof EntityPlayer;

        if (damagedEntity instanceof EntityPlayer && (sourceIsPlayer || controllerIsPlayer))
        {
            if (showMessages && !timers.containsKey(damagedEntity)) damagedEntity.sendMessage(new TextComponentString(TextFormatting.RED + "[ENTERING COMBAT MODE]"));
            timers.put((EntityPlayer) damagedEntity, cooldown * 20);

            if (sourceIsPlayer)
            {
                if (showMessages && !timers.containsKey(damageSource)) damageSource.sendMessage(new TextComponentString(TextFormatting.RED + "[ENTERING COMBAT MODE]"));
                timers.put((EntityPlayer) damageSource, cooldown * 20);
            }
            if (controllerIsPlayer && damageSource != damageController)
            {
                if (showMessages && !timers.containsKey(damageController)) damageController.sendMessage(new TextComponentString(TextFormatting.RED + "[ENTERING COMBAT MODE]"));
                timers.put((EntityPlayer) damageController, cooldown * 20);
            }
        }
    }

    @SubscribeEvent
    public static void countdown(TickEvent.ServerTickEvent event)
    {
        Iterator<Map.Entry<EntityPlayer, Integer>> iterator = timers.entrySet().iterator();
        Map.Entry<EntityPlayer, Integer> entry;
        int value;

        while (iterator.hasNext())
        {
            entry = iterator.next();
            value = entry.getValue();
            if (value > 0) entry.setValue(value - 1);
            else
            {
                if (showMessages) entry.getKey().sendMessage(new TextComponentString(TextFormatting.GREEN + "[LEAVING COMBAT MODE]"));
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public static void playerLogged(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayer player = event.player;
        if (timers.containsKey(player)) punish(player);
    }

    private static void punish(EntityPlayer player)
    {
        smite(player);
    }

    static boolean smite(EntityPlayer player)
    {
        if (bypassAllItems) player.inventory.dropAllItems();
        player.attackEntityFrom(smite, Float.MAX_VALUE);
        if (player.getHealth() <= 0)
        {
            if (zeusWasHere) for (int i = 0; i < 3; i++) player.world.addWeatherEffect(new EntityLightningBolt(player.world, player.posX, player.posY, player.posZ, true));
            return true;
        }
        return false;
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
        timers.clear();
        timers.clear();
    }
}
