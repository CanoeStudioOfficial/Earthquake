package com.canoestudio.earthquake.config;

import com.canoestudio.earthquake.util.SeismicZoneUtil;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class SeismicZoneStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 获取当前存档的路径
     */
    private static Path getSavePath() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            Path worldPath = server.getWorld(0).getSaveHandler().getWorldDirectory().toPath();
            return Paths.get(worldPath.toString().replaceAll("\\.\\.", ""));
        } else {
            // 客户端模式获取世界路径
            Minecraft mc = Minecraft.getMinecraft();
            Path clientWorldPath = mc.getIntegratedServer().getWorld(0).getSaveHandler().getWorldDirectory().toPath();
            return Paths.get(clientWorldPath.toString().replaceAll("\\.\\.", ""));""));
        }
    }

    /**
     * 获取地震带文件路径（存档独立）
     */
    private static Path getSeismicFilePath() {
        Path worldDir = getSavePath();
        String fileName = "seismic_zones.json"; // 自定义文件名
        String safeFileName = FilenameUtils.getName(fileName); // 获取文件名部分，避免路径遍历漏洞
        return worldDir.resolve(safeFileName);
    }
    /**
     * 保存地震带列表到存档独立文件
     *
     * @param zones 地震带列表
     */
    public static void save(List<SeismicZoneUtil.SeismicZone> zones) {
        try (Writer writer = Files.newBufferedWriter(getSeismicFilePath())) {
            GSON.toJson(zones, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从存档独立文件加载地震带列表
     *
     * @return 地震带列表
     */
    public static List<SeismicZoneUtil.SeismicZone> load() {
        Path filePath = getSeismicFilePath();
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try (Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<SeismicZoneUtil.SeismicZone>>() {}.getType();
            return GSON.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 初始化文件夹和默认文件（如果需要）
     */
    public static void initialize() {
        try {
            Files.createDirectories(getSeismicFilePath().getParent());
            if (!Files.exists(getSeismicFilePath())) {
                Files.createFile(getSeismicFilePath());
                save(new ArrayList<>()); // 写入一个空的地震带列表
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}