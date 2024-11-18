package com.canoestudio.earthquake.util;

import com.canoestudio.earthquake.config.SeismicZoneStorage;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.ArrayList;

public class SeismicZoneUtil {

    private static final List<SeismicZone> SEISMIC_ZONES = new ArrayList<>();

    static {
        // 初始化时加载地震带
        SeismicZoneStorage.initialize();
        SEISMIC_ZONES.addAll(SeismicZoneStorage.load());
    }

    public static boolean isInSeismicZone(BlockPos pos) {
        for (SeismicZone zone : SEISMIC_ZONES) {
            if (zone.isWithinZone(pos)) {
                return true;
            }
        }
        return false;
    }

    public static void addSeismicZone(BlockPos center, int radius) {
        SEISMIC_ZONES.add(new SeismicZone(center, radius));
        SeismicZoneStorage.save(SEISMIC_ZONES); // 保存更新
    }

    public static boolean removeSeismicZone(BlockPos center) {
        boolean removed = SEISMIC_ZONES.removeIf(zone -> zone.getCenter().equals(center));
        if (removed) {
            SeismicZoneStorage.save(SEISMIC_ZONES); // 保存更新
        }
        return removed;
    }

    public static List<SeismicZone> getSeismicZones() {
        return new ArrayList<>(SEISMIC_ZONES);
    }

    public static class SeismicZone {
        private final BlockPos center;
        private final int radius;

        public SeismicZone(BlockPos center, int radius) {
            this.center = center;
            this.radius = radius;
        }

        public BlockPos getCenter() {
            return center;
        }

        public int getRadius() {
            return radius;
        }

        public boolean isWithinZone(BlockPos pos) {
            double distance = pos.distanceSq(center);
            return distance <= radius * radius;
        }

        @Override
        public String toString() {
            return "SeismicZone{" +
                    "center=" + center +
                    ", radius=" + radius +
                    '}';
        }
    }
}