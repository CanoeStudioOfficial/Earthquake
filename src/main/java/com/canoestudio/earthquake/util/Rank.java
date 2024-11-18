package com.canoestudio.earthquake.util;

public class Rank {
    private final int level; // 地震等级
    private final int range; // 影响范围
    private final double destructionProbability; // 方块破坏概率
    private final int effectDuration; // 负面效果持续时间
    private final int slownessAmplifier; // 缓慢效果强度
    private final int nauseaAmplifier; // 眩晕效果强度

    public Rank(int level) {
        this.level = level;
        this.range = 5 + level * 2; // 范围公式
        this.destructionProbability = level * 0.1; // 破坏概率公式
        this.effectDuration = 100 + level * 20; // 效果持续时间公式
        this.slownessAmplifier = level / 3; // 缓慢效果强度
        this.nauseaAmplifier = level / 5; // 眩晕效果强度
    }

    public int getLevel() {
        return level;
    }

    public int getRange() {
        return range;
    }

    public double getDestructionProbability() {
        return destructionProbability;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    public int getSlownessAmplifier() {
        return slownessAmplifier;
    }

    public int getNauseaAmplifier() {
        return nauseaAmplifier;
    }
}