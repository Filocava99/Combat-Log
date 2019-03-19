package com.fantasticsource.combattagged;

import net.minecraft.util.DamageSource;

public class SmiteDamageSource extends DamageSource {
    public SmiteDamageSource(String damageTypeIn) {
        super(damageTypeIn);
        setDamageBypassesArmor();
        setDamageIsAbsolute();
    }
}
