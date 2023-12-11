package aster.amo.erosianmagic.cleric.chapel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chapel {
    private ResourceKey<Level> dimension;
    private BlockPos lecternPos;
    private List<UUID> worshippers;
    private List<BlockPos> seats;
    private int chapelLevel = 1;
    private int chapelProgress = 0;
    private boolean sermon = false;

    public Chapel(ResourceKey<Level> dimension, BlockPos lecternPos, List<UUID> worshippers, int chapelLevel) {
        this.dimension = dimension;
        this.lecternPos = lecternPos;
        this.worshippers = worshippers;
        this.chapelLevel = chapelLevel;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public BlockPos getLecternPos() {
        return lecternPos;
    }

    public List<UUID> getWorshippers() {
        return worshippers;
    }

    public void setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    public void setLecternPos(BlockPos lecternPos) {
        this.lecternPos = lecternPos;
    }

    public void setWorshippers(List<UUID> worshippers) {
        this.worshippers = worshippers;
    }

    public void addWorshipper(UUID uuid) {
        worshippers.add(uuid);
    }

    public void removeWorshipper(UUID uuid) {
        worshippers.remove(uuid);
    }

    public boolean isWorshipper(UUID uuid) {
        return worshippers.contains(uuid);
    }

    public int getChapelLevel() {
        return chapelLevel;
    }

    public void setChapelLevel(int chapelLevel) {
        this.chapelLevel = chapelLevel;
    }

    public boolean isSermon() {
        return sermon;
    }




    public boolean validateChapel(Level level) {
        return level.dimension() == dimension && level.getBlockState(lecternPos).getBlock() instanceof LecternBlock && validateSeatBlocks(level, lecternPos, worshippers.size(), chapelLevel, this);
    }
    public void moveVillager(Level level) {
        if(chapelProgress >= worshippers.size() || chapelProgress < 0) return;
        Villager villager = (Villager) ((ServerLevel)level).getEntity(worshippers.get(chapelProgress));
        if(villager != null) {
            if(sermon) {
                final Vec3[] beforePos = {villager.position()};
                villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                    if(worshipper.getBeforePosition() == null) worshipper.setBeforePosition(beforePos[0]);
                });
                Vec3 seatPos = seats.get(chapelProgress).getCenter();
                villager.setPos(seatPos.x, seatPos.y, seatPos.z);
                villager.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 100, false, false));
                if(beforePos[0].distanceTo(villager.position()) > 5){
                    ((ServerLevel)level).sendParticles(ParticleTypes.SMOKE, villager.getX(), villager.getY(), villager.getZ(), 10, 0.25, 0.5, 0.25, 0.1);
                }
            }
        } else {
            worshippers.remove(chapelProgress);
        }
    }
    // TODO: Holding sermon should increase mana regen for 4 ingame days.
    // TODO: It should also apply a buff to the cleric that boosts spell power based on the number of worshippers.
    // TODO: Make it so villagers cant occupy the same seat.
    public void tick(Level level) {
        if (level.getGameTime() % 10 == 0 && sermon) {
            if (chapelProgress < worshippers.size()) {
                moveVillager(level);
                chapelProgress++;
                if(level.random.nextFloat() < 0.3 && chapelProgress < worshippers.size()) {
                    moveVillager(level);
                    chapelProgress++;
                }
            } else {
                chapelProgress = 0;
            }
        } if(chapelProgress == -1) {
            chapelProgress = 0;
            List<UUID> toRemove = new ArrayList<>();
            for(UUID uuid : worshippers) {
                Villager villager = (Villager) ((ServerLevel)level).getEntity(uuid);
                if(villager != null) {
                    villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                        if(worshipper.getBeforePosition() == null) return;
                        ((ServerLevel)level).sendParticles(ParticleTypes.SMOKE, villager.getX(), villager.getY(), villager.getZ(), 10, 0.25, 0.5, 0.25, 0.1);
                        villager.setPos(worshipper.getBeforePosition().x, worshipper.getBeforePosition().y, worshipper.getBeforePosition().z);
                        worshipper.setBeforePosition(null);
                    });
                } else {
                    toRemove.add(uuid);
                }
            }
            worshippers.removeAll(toRemove);
            sermon = false;
        }
    }

    private static boolean validateSeatBlocks(Level level, BlockPos lecternPos, int worshippers, int chapelLevel, Chapel chapel) {
        int seats = 0;
        for (int x = -chapelLevel; x <= chapelLevel; x++) {
            for (int z = -chapelLevel; z <= chapelLevel; z++) {
                if (level.getBlockState(lecternPos.offset(x, -1, z)).getBlock() instanceof StairBlock) {
                    chapel.seats.add(lecternPos.offset(x, -1, z));
                    seats++;
                }
            }
        }
        return seats >= worshippers;
    }

    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("dimension", dimension.location().toString());
        nbt.putInt("lecternX", lecternPos.getX());
        nbt.putInt("lecternY", lecternPos.getY());
        nbt.putInt("lecternZ", lecternPos.getZ());
        nbt.putInt("chapelLevel", chapelLevel);
        nbt.putInt("chapelProgress", chapelProgress);
        nbt.putBoolean("sermon", sermon);
        if(worshippers == null) worshippers = new ArrayList<>(); // TODO: 2021-01-20 20:52:00
        ListTag worshippersTag = new ListTag();
        worshippers.forEach(uuid -> {
            worshippersTag.add(NbtUtils.createUUID(uuid));
        });
        nbt.put("worshippers", worshippersTag);
        if(seats == null) seats = new ArrayList<>(); // TODO: 2021-01-20 20:52:00
        ListTag seatsTag = new ListTag();
        seats.forEach(pos -> {
            CompoundTag seatTag = new CompoundTag();
            seatTag.putInt("seatX", pos.getX());
            seatTag.putInt("seatY", pos.getY());
            seatTag.putInt("seatZ", pos.getZ());
            seatsTag.add(seatTag);
        });
        nbt.put("seats", seatsTag);
        return nbt;
    }

    public static Chapel fromNbt(CompoundTag tag) {
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("dimension")));
        BlockPos lecternPos = new BlockPos(tag.getInt("lecternX"), tag.getInt("lecternY"), tag.getInt("lecternZ"));
        int chapelLevel = tag.getInt("chapelLevel");
        int chapelProgress = tag.getInt("chapelProgress");
        boolean sermon = tag.getBoolean("sermon");
        List<UUID> worshippers = new ArrayList<>();
        ListTag worshippersTag = tag.getList("worshippers", 8);
        worshippersTag.forEach(uuidTag -> {
            worshippers.add(NbtUtils.loadUUID(uuidTag));
        });
        List<BlockPos> seats = new ArrayList<>();
        ListTag seatsTag = tag.getList("seats", 10);
        seatsTag.forEach(seatTag -> {
            CompoundTag seatTag1 = (CompoundTag) seatTag;
            seats.add(new BlockPos(seatTag1.getInt("seatX"), seatTag1.getInt("seatY"), seatTag1.getInt("seatZ")));
        });
        Chapel chapel = new Chapel(dimension, lecternPos, worshippers, chapelLevel);
        chapel.setChapelProgress(chapelProgress);
        chapel.setSermon(sermon);
        chapel.setSeats(seats);
        return chapel;
    }

    private void setSeats(List<BlockPos> seats) {
        this.seats = seats;
    }

    public void setSermon(boolean sermon) {
        this.sermon = sermon;
    }

    public void setChapelProgress(int chapelProgress) {
        this.chapelProgress = chapelProgress;
    }
}
