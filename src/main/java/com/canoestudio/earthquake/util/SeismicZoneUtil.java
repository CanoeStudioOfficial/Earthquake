package com.canoestudio.earthquake.util;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class SeismicZoneUtil {

    // 存储所有地震带信息
    private static final List<SeismicZone> SEISMIC_ZONES = new ArrayList<>();

    static {
        // 添加地震带信息
        SEISMIC_ZONES.add(new SeismicZone(new BlockPos(0, 0, 0), 1000)); // 地震带1
        SEISMIC_ZONES.add(new SeismicZone(new BlockPos(2000, 0, -1500), 800)); // 地震带2
    }

    /**
     * 检查一个位置是否在任何地震带内
     *
     * @param pos 玩家位置
     * @return 是否在地震带内
     */
    public static boolean isInSeismicZone(BlockPos pos) {
        for (SeismicZone zone : SEISMIC_ZONES) {
            if (zone.isWithinZone(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 内部类表示单个地震带
     */
    private static class SeismicZone {
        private final BlockPos center;
        private final int radius;

        public SeismicZone(BlockPos center, int radius) {
            this.center = center;
            this.radius = radius;
        }

        public boolean isWithinZone(BlockPos pos) {
            double distance = pos.distanceSq(center);
            return distance <= radius * radius;
        }
    }
}