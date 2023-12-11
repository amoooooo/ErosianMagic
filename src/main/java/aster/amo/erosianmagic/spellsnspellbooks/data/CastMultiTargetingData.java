package aster.amo.erosianmagic.spellsnspellbooks.data;

import io.redspace.ironsspellbooks.api.spells.ICastData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.UUID;

public class CastMultiTargetingData implements ICastData {
    private List<UUID> targets;
    private UUID mainTarget;

    public CastMultiTargetingData() {
        targets = List.of();
    }

    public CastMultiTargetingData(List<LivingEntity> entities, LivingEntity mainTarget) {
        this.targets = entities.stream().map(LivingEntity::getUUID).toList();
        this.mainTarget = mainTarget.getUUID();
    }

    @Override
    public void reset() {

    }

    public List<UUID> getTargets() {
        return targets;
    }

    public List<LivingEntity> getTargets(ServerLevel level) {
        return targets.stream().map((uuid) -> ((LivingEntity)level.getEntity(uuid))).toList();
    }

    public UUID getMainTarget() {
        return mainTarget;
    }
}
