package com.canoestudio.earthquake.event;

import com.canoestudio.earthquake.util.Rank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class EarthquakeEvent {

    private static final Random random = new Random();
    private static final double EARTHQUAKE_TRIGGER_CHANCE = 0.001;



    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.world.isRemote) {
            if (random.nextDouble() < EARTHQUAKE_TRIGGER_CHANCE) {
                int level = random.nextInt(10) + 1;
                Rank rank = new Rank(level);
                triggerEarthquake(event.world, rank);
            }
        }
    }

    private void triggerEarthquake(World world, Rank rank) {
        
        int level = rank.getLevel();
        for (EntityPlayer player : world.playerEntities) {
            BlockPos playerPos = player.getPosition();
            int range = rank.getRange();
            double destructionProbability = rank.getDestructionProbability();
            int effectDuration = rank.getEffectDuration();
            int slownessAmplifier = rank.getSlownessAmplifier();
            int nauseaAmplifier = rank.getNauseaAmplifier();

            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = playerPos.add(x, random.nextInt(level) - (level / 2), z);
                    IBlockState state = world.getBlockState(pos);

                    if (!state.getBlock().isAir(state, world, pos) && random.nextDouble() < destructionProbability) {
                        world.setBlockToAir(pos);
                    }
                }
            }

            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, effectDuration, slownessAmplifier));
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, effectDuration, nauseaAmplifier));

            player.sendMessage(new TextComponentTranslation("message.earthquake.level", level));
        }

        float volume = 0.5F + level * 0.1F; // 使用方法内部声明的level变量
        world.playSound(null, new BlockPos(0, 0, 0), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, volume, 1.0F);
        spawnEarthquakeParticles(world, level);
    }

    private void spawnEarthquakeParticles(World world, int level) {
        int particleCount = level * 20;
        for (int i = 0; i < particleCount; i++) {
            double x = random.nextDouble() * 20 - 10;
            double y = random.nextDouble() * 2 + 1;
            double z = random.nextDouble() * 20 - 10;

            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 0, 0, 0);
        }
    }
}