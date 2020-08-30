package it.filippocavallari.combatlog;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod(modid = CombatLog.MODID, name = CombatLog.NAME, version = CombatLog.VERSION, acceptableRemoteVersions = "*")
public class CombatLog {
    public static final String MODID = "combatlog";
    public static final String NAME = "Combat Log";
    public static final String VERSION = "1.0.1";
    public static final int timer = Configs.cooldown;
    private static final Map<EntityPlayer, Integer> timers = new HashMap<EntityPlayer, Integer>();

    private short elapsedTicks = 20;

    public CombatLog() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void entityDamage(LivingHurtEvent event) {
        Entity damagedEntity = event.entity;
        Entity damageSource = event.source.getSourceOfDamage();
        if (!(damagedEntity instanceof EntityPlayer) || !(damageSource instanceof EntityPlayer)) {
            return;
        }
        if (damagedEntity == damageSource) {
            return;
        }
        if (Configs.showMessages) {
            EntityPlayer damagedPlayer = (EntityPlayer) damagedEntity;
            EntityPlayer damagerPlayer = (EntityPlayer) damageSource;
            if (!timers.containsKey(damagedPlayer)) {
                damagedPlayer.addChatComponentMessage(new ChatComponentText("[ENTERING COMBAT MODE]"));
            }
            if (!timers.containsKey(damagerPlayer)) {
                damagerPlayer.addChatComponentMessage(new ChatComponentText("[ENTERING COMBAT MODE]"));
            }
            timers.put(damagedPlayer, timer);
            timers.put(damagerPlayer, timer);
        }
    }

    @SubscribeEvent
    public void countdown(TickEvent.ServerTickEvent event) {
        elapsedTicks -= 1;
        if(elapsedTicks == 0){
            elapsedTicks = 20;
            Iterator<Map.Entry<EntityPlayer, Integer>> iterator = timers.entrySet().iterator();
            Map.Entry<EntityPlayer, Integer> entry;
            int value;

            while (iterator.hasNext()) {
                entry = iterator.next();
                value = entry.getValue();
                if (value > 0) {
                    entry.setValue(value - 1);
                } else {
                    if (Configs.showMessages) {
                        entry.getKey().addChatComponentMessage(new ChatComponentText("[LEAVING COMBAT MODE]"));
                    }
                    iterator.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void playerLogged(PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        if (timers.containsKey(player)) {
            timers.remove(player);
            player.inventory.dropAllItems();
            player.setDead();
        }
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        timers.clear();
    }
}
