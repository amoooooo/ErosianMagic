package aster.amo.erosianmagic.spellsnspellbooks.data;

import java.util.List;
import java.util.UUID;

public class TargetingData {
    private final UUID mainTarget;
    private final List<UUID> targets;

    public TargetingData(UUID mainTarget, List<UUID> targets) {
        this.mainTarget = mainTarget;
        this.targets = targets;
    }

    public UUID getMainTarget() {
        return mainTarget;
    }

    public List<UUID> getTargets() {
        return targets;
    }

    public boolean isTargeted(UUID uuid) {
        return targets.contains(uuid);
    }
}
