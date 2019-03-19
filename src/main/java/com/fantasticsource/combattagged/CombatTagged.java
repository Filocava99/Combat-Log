package com.fantasticsource.combattagged;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.fantasticsource.combattagged.CombatTagConfig.*;

@Mod(modid = CombatTagged.MODID, name = CombatTagged.NAME, version = CombatTagged.VERSION)
public class CombatTagged {
    public static final String MODID = "combattagged";
    public static final String NAME = "Combat Tagged!";
    public static final String VERSION = "1.12.2.001";

    private static Logger logger;

    public static final DamageSource smite = new DamageSource("smite").setDamageBypassesArmor().setDamageIsAbsolute();

    public CombatTagged() {
        MinecraftForge.EVENT_BUS.register(CombatTagged.class);
    }

    private static Map<Integer, Integer> timers = new HashMap<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @SubscribeEvent
    public static void entityDamage(LivingHurtEvent event)
    {
        Entity damagedEntity = event.getEntity();
        Entity damageSource = event.getSource().getImmediateSource();
        Entity damageController = event.getSource().getTrueSource();

        boolean sourceIsPlayer = isPlayer(damageSource);
        boolean controllerIsPlayer = isPlayer(damageController);

        if (isPlayer(damagedEntity) && (sourceIsPlayer || controllerIsPlayer))
        {
            timers.put(damagedEntity.getEntityId(), cooldown * 20);
            if (sourceIsPlayer) timers.put(damageSource.getEntityId(), cooldown * 20);
            if (controllerIsPlayer) timers.put(damageController.getEntityId(), cooldown * 20);
        }
    }

    @SubscribeEvent
    public static void countdown(TickEvent.WorldTickEvent event)
    {
        Iterator<Map.Entry<Integer, Integer>> iterator = timers.entrySet().iterator();
        Map.Entry<Integer, Integer> entry;
        int value;

        while(iterator.hasNext())
        {
            entry = iterator.next();
            value = entry.getValue();
            if (value > 0) entry.setValue(value - 1);
            else iterator.remove();
        }
    }

    @SubscribeEvent
    public static void playerLogged(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayer player = event.player;
        if (timers.containsKey(event.player.getEntityId()))
        {
            player.attackEntityFrom(smite, Float.MAX_VALUE);
            for(int i = 0; i < 3; i++) player.world.addWeatherEffect(new EntityLightningBolt(player.world, player.posX, player.posY, player.posZ, true));
        }
    }

    public static boolean isPlayer(Entity entity)
    {
        return entity instanceof EntityPlayer;
    }
}
