package com.fantasticsource.combattagged;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import static com.fantasticsource.combattagged.CombatTagConfig.*;

public class SmiteDamage extends DamageSource {
    public SmiteDamage(String damageTypeIn) {
        super("smite");
        setDamageBypassesArmor();
        setDamageIsAbsolute();
    }

    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        return new TextComponentString(deathMessage.replaceAll("%s", entityLivingBaseIn.getName()));
    }
}
