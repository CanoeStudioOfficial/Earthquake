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

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.world.isRemote) {
            if (random.nextDouble() < 0.001) { // 随机触发地震
                int level = random.nextInt(10) + 1; // 随机生成地震等级（1-10）
                Rank rank = new Rank(level); // 使用 Rank 类
                triggerEarthquake(event.world, rank);
            }
        }
    }

    private void triggerEarthquake(World world, Rank rank) {
        for (EntityPlayer player : world.playerEntities) {
            BlockPos playerPos = player.getPosition();
            int range = rank.getRange(); // 获取范围

            // 模拟地表破坏
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = playerPos.add(x, random.nextInt(rank.getLevel()) - (rank.getLevel() / 2), z); // 高度扰动
                    IBlockState state = world.getBlockState(pos);

                    if (!state.getBlock().isAir(state, world, pos)) {
                        if (random.nextDouble() < rank.getDestructionProbability()) { // 根据等级破坏概率
                            world.setBlockToAir(pos); // 删除方块
                        }
                    }
                }
            }

            // 给玩家添加负面效果
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, rank.getEffectDuration(), rank.getSlownessAmplifier()));
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, rank.getEffectDuration(), rank.getNauseaAmplifier()));

            // 提示玩家地震等级
            player.sendMessage(new TextComponentTranslation("message.earthquake.level", rank.getLevel()));
        }

        // 播放地震音效
        float volume = 0.5F + rank.getLevel() * 0.1F; // 音量随等级变化
        world.playSound(null, new BlockPos(0, 0, 0), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, volume, 1.0F);

        // 生成粒子效果
        spawnEarthquakeParticles(world, rank.getLevel());
    }

    private void spawnEarthquakeParticles(World world, int level) {
        for (int i = 0; i < level * 20; i++) { // 粒子数量随等级增加
            double x = random.nextDouble() * 20 - 10;
            double y = random.nextDouble() * 2 + 1;
            double z = random.nextDouble() * 20 - 10;

            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 0, 0, 0);
        }
    }
}