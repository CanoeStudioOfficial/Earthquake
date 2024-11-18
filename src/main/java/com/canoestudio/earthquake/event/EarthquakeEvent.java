package com.canoestudio.earthquake.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class EarthquakeEvent {
    private static final Random random = new Random();
    private static final int EARTHQUAKE_RANGE = 10;
    private static final int PARTICLE_COUNT = 100;

    // 地震触发逻辑
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.world.isRemote) {
            // 随机概率触发地震
            if (random.nextDouble() < 0.001) { // 调整触发几率
                triggerEarthquake(event.world);
            }
        }
    }

    // 地震逻辑核心
    private void triggerEarthquake(World world) {
        for (EntityPlayer player : world.playerEntities) {
            BlockPos playerPos = player.getPosition();

            // 模拟地表破坏
            for (int x = -EARTHQUAKE_RANGE; x <= EARTHQUAKE_RANGE; x++) {
                for (int z = -EARTHQUAKE_RANGE; z <= EARTHQUAKE_RANGE; z++) {
                    BlockPos pos = playerPos.add(x, random.nextInt(5) - 2, z); // 随机高度扰动
                    IBlockState state = world.getBlockState(pos);

                    if (!state.getBlock().isAir(state, world, pos)) {
                        world.setBlockToAir(pos); // 删除方块
                    }
                }
            }

            // 给玩家添加负面效果
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 1));
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
        }

        // 播放地震音效
        world.playSound(null, new BlockPos(0, 0, 0), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 1.0F, 1.0F);

        // 粒子效果
        spawnEarthquakeParticles(world);
    }

    // 地震粒子效果
    private void spawnEarthquakeParticles(World world) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double x = random.nextDouble() * 20 - 10;
            double y = random.nextDouble() * 2 + 1;
            double z = random.nextDouble() * 20 - 10;

            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 0, 0, 0);
        }
    }
}