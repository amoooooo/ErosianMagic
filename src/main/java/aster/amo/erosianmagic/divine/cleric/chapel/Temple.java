package aster.amo.erosianmagic.divine.cleric.chapel;

import aster.amo.erosianmagic.registry.MobEffectRegistry;
import elucent.eidolon.api.altar.AltarInfo;
import elucent.eidolon.api.ritual.Ritual;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.ISoul;
import elucent.eidolon.client.particle.RuneParticleData;
import elucent.eidolon.common.deity.Deities;
import elucent.eidolon.common.tile.EffigyTileEntity;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SoulUpdatePacket;
import elucent.eidolon.registries.EidolonSounds;
import elucent.eidolon.registries.Runes;
import elucent.eidolon.registries.Signs;
import forge.net.mca.entity.VillagerEntityMCA;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Temple {
    private ResourceKey<Level> dimension;
    private BlockPos lecternPos;
    private List<UUID> worshippers;
    private List<BlockPos> seats;
    private int templeLevel = 1;
    private int templeProgress = 0;
    private boolean sermon = false;
    private int sermonTime = 0;
    private static int MAX_SERMON_LENGTH = 20 * 60 * 5;

    public Temple(ResourceKey<Level> dimension, BlockPos lecternPos, List<UUID> worshippers, int templeLevel) {
        this.dimension = dimension;
        this.lecternPos = lecternPos;
        this.worshippers = worshippers;
        this.templeLevel = templeLevel;
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
        templeLevel = (int) Math.ceil(Math.sqrt(worshippers.size()));
    }

    public void removeWorshipper(UUID uuid) {
        worshippers.remove(uuid);
        templeLevel = (int) Math.ceil(Math.sqrt(worshippers.size()));
    }

    public boolean isWorshipper(UUID uuid) {
        return worshippers.contains(uuid);
    }

    public int getTempleLevel() {
        templeLevel = (int) Math.ceil(Math.sqrt(worshippers.size()));
        return templeLevel;
    }

    public void setTempleLevel(int templeLevel) {
        this.templeLevel = templeLevel;
    }

    public boolean isSermon() {
        return sermon;
    }




    public boolean validateTemple(Level level, Player player) {
        boolean isSameDimension = level.dimension().equals(dimension);
        boolean isLectern = level.getBlockState(lecternPos).getBlock() instanceof LecternBlock;
        boolean enoughSeats = validateSeatBlocks(level, lecternPos, worshippers.size(), templeLevel, this, player);
        if(!isSameDimension) {
            player.sendSystemMessage(Component.literal("Not in the same dimension as your temple."));
        }
        return isSameDimension && isLectern && enoughSeats;
    }
    public void initWorship(Level level) {
        if(templeProgress >= worshippers.size() || templeProgress < 0) return;
        VillagerEntityMCA villager = (VillagerEntityMCA) ((ServerLevel)level).getEntity(worshippers.get(templeProgress));
        if(villager != null) {
            if(sermon) {
                final Vec3[] beforePos = {villager.position()};
                villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                    if(worshipper.getBeforePosition() == null) worshipper.setBeforePosition(beforePos[0]);
                });
                AtomicReference<Vec3> seatPos = new AtomicReference<>(villager.position());
                villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                    if(worshipper.getSeatPosition() != null) {
                        seatPos.set(worshipper.getSeatPosition());
                    }
                });
                Vec3 seat = seatPos.get();
                if(beforePos[0].distanceTo(villager.position()) > 5){
                    level.getChunk(villager.blockPosition());
                    villager.hurtMarked = true;
                    villager.setSilent(true);
                }
                villager.getNavigation().moveTo(seat.x, seat.y, seat.z, 0.5);
                if(villager.distanceToSqr(seat.x, seat.y, seat.z) < 3) {
                    villager.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 100, false, false));
                    villager.setJumping(false);
                    if(villager.getRandom().nextFloat() < 0.35f) {
                        double x = villager.getX() + 0.1 * villager.getRandom().nextGaussian(),
                                y = villager.getEyeY() + 0.1 * villager.getRandom().nextGaussian(),
                                z = villager.getZ() + 0.1 * villager.getRandom().nextGaussian();
                        Vector3f color = new Vector3f(Signs.SACRED_SIGN.getRed(), Signs.SACRED_SIGN.getGreen(), Signs.SACRED_SIGN.getBlue());
                        Vec3 look = villager.getLookAngle();
                        for (int i = 0; i < 2; i++) {
                            ((ServerLevel)level).sendParticles(new RuneParticleData(
                                    Runes.find(ResourceLocation.tryParse("eidolon:sin")),
                                    color.x, color.y, color.z,
                                    1, 1, 1
                            ), x, y, z, 1, look.x * 0.03, look.y * 0.03, look.z * 0.03, 0.03);
                        }
                        level.playSound(null, villager.blockPosition(), EidolonSounds.CHANT_WORD.get(), SoundSource.NEUTRAL, 0.25f, villager.getRandom().nextFloat() * 0.675f + 1.325f);
                    }
                }

            }
        } else {
            worshippers.remove(templeProgress);
        }
    }

    @Nullable
    private static EffigyTileEntity getEffigy(Level world, BlockPos pos) {
        List<EffigyTileEntity> effigies = Ritual.getTilesWithinAABB(EffigyTileEntity.class, world, new AABB(pos.offset(-4, -4, -4), pos.offset(5, 5, 5)));
        if (effigies.isEmpty()) return null;
        return effigies.stream().min(Comparator.comparingDouble((e) -> e.getBlockPos().distSqr(pos))).get();
    }

    public void tick(Level level, Player player) {
        if(sermonTime > 0) {
            sermonTime--;
        } else {
            sermon = false;
            templeProgress = -1;
        }
        if (level.getGameTime() % 5 == 0 && sermon) {
            // if player is not in area, end sermon
            if (player.distanceToSqr(lecternPos.getX(), lecternPos.getY(), lecternPos.getZ()) > this.templeLevel * this.templeLevel * 4) {
                templeProgress = -1;
                sermon = false;
                return;
            }
            if (templeProgress < worshippers.size()) {
                initWorship(level);
                templeProgress++;
                if(level.random.nextFloat() < 0.3 && templeProgress < worshippers.size()) {
                    initWorship(level);
                    templeProgress++;
                }
            } else {
                templeProgress = 0;
            }
            EffigyTileEntity effigy = getEffigy(level, lecternPos);
            if(effigy != null) {
                AltarInfo info = AltarInfo.getAltarInfo(level, effigy.getBlockPos());
                level.getCapability(IReputation.INSTANCE, null).ifPresent((rep) -> {
                    rep.addReputation(player, Deities.LIGHT_DEITY_ID, 0.01 * info.getPower());
                    player.getCapability(ISoul.INSTANCE).ifPresent((soul) -> {
                        double power = info.getPower();
                        soul.setMagic((float)Math.min((double)soul.getMaxMagic(), (double)soul.getMagic() + (power * worshippers.size() * 0.0001)));
                        if (!level.isClientSide) {
                            Networking.sendToTracking(level, player.blockPosition(), new SoulUpdatePacket(player));
                        }
                    });
                });
            }
        } if(templeProgress == -1) {
            templeProgress = 0;
            player.addEffect(new MobEffectInstance(MobEffectRegistry.MANA_REGEN.get(), 20 * 60 * 20 * 4, 0, false, false));
            List<UUID> toRemove = new ArrayList<>();
            for(UUID uuid : worshippers) {
                VillagerEntityMCA villager = (VillagerEntityMCA) ((ServerLevel)level).getEntity(uuid);
                if(villager != null) {
                    villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                        if(worshipper.getBeforePosition() == null) return;
                        ((ServerLevel)level).sendParticles(ParticleTypes.SMOKE, villager.getX(), villager.getY(), villager.getZ(), 60, 0.25, 0.5, 0.25, 0.1);
                        villager.getNavigation().moveTo(worshipper.getBeforePosition().x, worshipper.getBeforePosition().y, worshipper.getBeforePosition().z, 0.5);
                        worshipper.setBeforePosition(null);
                        villager.setSilent(false);
                    });
                } else {
                    toRemove.add(uuid);
                }
            }
            worshippers.removeAll(toRemove);
            sermon = false;
        }
        if(level.getGameTime() % 20 == 0) {
            templeLevel = (int) Math.ceil(Math.sqrt(worshippers.size()));
        }
    }

    private static void drawCubeParticles(AABB box, ServerLevel level) {
        Vec3 pos = new Vec3(box.minX, box.minY, box.minZ);
        Vec3 pos2 = new Vec3(box.maxX, box.maxY, box.maxZ);
        for (double x = pos.x; x < pos2.x; x += 0.25) {
            for (double y = pos.y; y < pos2.y; y += 0.25) {
                for (double z = pos.z; z < pos2.z; z += 0.25) {
                    level.sendParticles(ParticleTypes.SCRAPE, x, y, z, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    private static boolean validateSeatBlocks(Level level, BlockPos lecternPos, int worshippers, int templeLevel, Temple temple, Player player) {
        int seats = 0;
        temple.seats = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(lecternPos.offset(-templeLevel, -templeLevel, -templeLevel), lecternPos.offset(templeLevel, templeLevel, templeLevel))) {
            if (level.getBlockState(pos).getBlock() instanceof StairBlock) {
                seats++;
                if(seats > temple.worshippers.size()) {
                    break;
                }
                temple.seats.add(pos);
                drawCubeParticles(new AABB(pos), (ServerLevel)level);
                UUID worshipper = temple.worshippers.get(seats % Math.max(1, temple.worshippers.size()));
                if (worshipper != null) {
                    VillagerEntityMCA villager = (VillagerEntityMCA) ((ServerLevel)level).getEntity(worshipper);
                    if(villager != null) {
                        villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper1 -> {
                            worshipper1.setSeatPosition(pos.getCenter());
                        });
                    }
                }
            }
        }
        for (int i = 0; i < 360; i += 10) {
            double x = lecternPos.getX() + (float) templeLevel * Math.cos(Math.toRadians(i));
            double z = lecternPos.getZ() + (float) templeLevel * Math.sin(Math.toRadians(i));
            ((ServerLevel)level).sendParticles(ParticleTypes.SCRAPE, x, lecternPos.getY(), z, 1, 0, 0, 0, 0);
        }
        if(seats < worshippers) {
            player.sendSystemMessage(Component.literal("Not enough seats. Add " + (worshippers - seats) + " more seats."));
        }
        return seats >= worshippers;
    }

    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("dimension", dimension.location().toString());
        nbt.putInt("lecternX", lecternPos.getX());
        nbt.putInt("lecternY", lecternPos.getY());
        nbt.putInt("lecternZ", lecternPos.getZ());
        nbt.putInt("templeLevel", templeLevel);
        nbt.putInt("templeProgress", templeProgress);
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
        nbt.putInt("sermonTime", sermonTime);
        return nbt;
    }

    public static Temple fromNbt(CompoundTag tag) {
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("dimension")));
        BlockPos lecternPos = new BlockPos(tag.getInt("lecternX"), tag.getInt("lecternY"), tag.getInt("lecternZ"));
        int chapelLevel = tag.getInt("templeLevel");
        int chapelProgress = tag.getInt("templeProgress");
        boolean sermon = tag.getBoolean("sermon");
        List<UUID> worshippers = new ArrayList<>();
        ListTag worshippersTag = tag.getList("worshippers", Tag.TAG_INT_ARRAY);
        worshippersTag.forEach(uuidTag -> {
            worshippers.add(NbtUtils.loadUUID(uuidTag));
        });
        List<BlockPos> seats = new ArrayList<>();
        ListTag seatsTag = tag.getList("seats", Tag.TAG_COMPOUND);
        seatsTag.forEach(seatTag -> {
            CompoundTag seatTag1 = (CompoundTag) seatTag;
            seats.add(new BlockPos(seatTag1.getInt("seatX"), seatTag1.getInt("seatY"), seatTag1.getInt("seatZ")));
        });
        Temple temple = new Temple(dimension, lecternPos, worshippers, chapelLevel);
        temple.setTempleProgress(chapelProgress);
        temple.setSermon(sermon);
        temple.setSeats(seats);
        temple.setSermonTime(tag.getInt("sermonTime"));
        return temple;
    }

    private void setSermonTime(int sermonTime) {
        this.sermonTime = sermonTime;
    }

    private int getSermonTime() {
        return sermonTime;
    }

    private void setSeats(List<BlockPos> seats) {
        this.seats = seats;
    }

    public void setSermon(boolean sermon) {
        this.sermon = sermon;
        if(sermon) {
            sermonTime = MAX_SERMON_LENGTH;
        } else {
            sermonTime = 0;
            templeProgress = -1;
        }
    }

    public void setTempleProgress(int templeProgress) {
        this.templeProgress = templeProgress;
    }
}
