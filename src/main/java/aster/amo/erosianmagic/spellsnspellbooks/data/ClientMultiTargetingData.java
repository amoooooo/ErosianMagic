package aster.amo.erosianmagic.spellsnspellbooks.data;

import io.redspace.ironsspellbooks.capabilities.magic.ClientSpellTargetingData;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.UUID;

public class ClientMultiTargetingData extends ClientSpellTargetingData {
    public List<UUID> targets;

    public ClientMultiTargetingData() {
        super();
        targets = List.of();
    }

    public ClientMultiTargetingData(List<UUID> targets, String spellId) {
        super();
        this.targets = targets;
        this.spellId = spellId;
    }

    @Override
    public boolean isTargeted(LivingEntity livingEntity) {
        return targets.contains(livingEntity.getUUID());
    }

    @Override
    public boolean isTargeted(UUID uuid) {
        return targets.contains(uuid);
    }
}
