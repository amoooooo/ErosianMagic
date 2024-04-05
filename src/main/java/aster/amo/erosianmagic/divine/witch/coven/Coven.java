package aster.amo.erosianmagic.divine.witch.coven;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Coven {
    List<UUID> members = new ArrayList<>();
    int covenLevel = 1;
    boolean isRecruiting = false;
    public void addMember(UUID uuid) {
        members.add(uuid);
        recalculateCovenLevel();
    }
    public void removeMember(UUID uuid) {
        members.remove(uuid);
        recalculateCovenLevel();
    }
    public List<UUID> getMembers() {
        return members;
    }
    public boolean hasMember(UUID uuid) {
        return members.contains(uuid);
    }
    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    private void recalculateCovenLevel() {
        // min of 1
        covenLevel = Math.max(1, (int) Math.sqrt(members.size()));
    }

    public void handleManaSplit(ServerLevel level, AbstractSpell spell, ServerPlayer caster, int spellLevel){
        // get all members within covenLevel^2 radius of the caster, and reduce their mana by 5% multiplicatively per member, and decrease the other members' mana by the reduced amount split evenly
        List<ServerPlayer> players = level.getPlayers(player -> player.distanceToSqr(caster) <= Math.max(4, covenLevel * covenLevel));
        int mana = spell.getManaCost(spellLevel, caster);
        int reductionMod = (int) Math.pow(0.95, members.size());
        int reducedMana = mana - (mana * reductionMod);
        int splitMana;
        List<ServerPlayer> usersWithoutMana = players.stream().filter(player -> MagicData.getPlayerMagicData(player).getMana() == 0).toList();
        usersWithoutMana.forEach(players::remove);
        splitMana = reducedMana / (players.size());
        players.forEach(player -> {
            MagicData magicData = MagicData.getPlayerMagicData(player);
            if(magicData.getMana() > 0) {
                magicData.setMana(magicData.getMana() - splitMana);
            }
        });
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        members.forEach(uuid -> list.add(StringTag.valueOf(uuid.toString())));
        nbt.put("members", list);
        nbt.putInt("covenLevel", covenLevel);
        return nbt;
    }

    public static Coven deserializeNBT(CompoundTag nbt) {
        Coven coven = new Coven();
        ListTag list = nbt.getList("members", 8);
        list.forEach(tag -> coven.addMember(UUID.fromString(tag.getAsString())));
        coven.covenLevel = nbt.getInt("covenLevel");
        return coven;
    }

    public int getCovenLevel() {
        return covenLevel;
    }

    public void setCovenLevel(int covenLevel) {
        this.covenLevel = covenLevel;
    }

    public boolean isRecruiting() {
        return isRecruiting;
    }

    public void setRecruiting(boolean recruiting) {
        isRecruiting = recruiting;
    }
}
