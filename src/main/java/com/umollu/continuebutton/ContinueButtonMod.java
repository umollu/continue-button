package com.umollu.continuebutton;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ContinueButtonMod implements ClientModInitializer {

    public static boolean lastLocal = true;
    public static String serverName = "";
    public static String serverAddress = "";

    @Override
    public void onInitializeClient() {
    }

    public static void saveConfig() {
        File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "continuebutton");
        File configFile = new File(configDir, "config.properties");
        Properties properties = new Properties();
        try (FileInputStream stream = new FileInputStream(configFile)) {
            properties.load(stream);
        } catch (IOException e) {
        }
        properties.setProperty("last-local", lastLocal? "true" : "false");
        properties.setProperty("server-name", serverName);
        properties.setProperty("server-address", serverAddress);
        try (FileOutputStream stream = new FileOutputStream(configFile)) {
            properties.store(stream, "ContinueButton config");
        } catch (IOException e) {
        }
    }

    static {
        File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "continuebutton");

        if (!configDir.exists()) {
            if (!configDir.mkdir()) {
            }
        }

        File configFile = new File(configDir, "config.properties");
        Properties properties = new Properties();

        if (configFile.exists()) {
            try (FileInputStream stream = new FileInputStream(configFile)) {
                properties.load(stream);
            } catch (IOException e) {
            }
        }

        lastLocal = ((String) properties.computeIfAbsent("last-local", (a) -> "true")).equals("true");
        serverName = ((String) properties.computeIfAbsent("server-name", (a) -> ""));
        serverAddress = ((String) properties.computeIfAbsent("server-address", (a) -> ""));

        try (FileOutputStream stream = new FileOutputStream(configFile)) {
            properties.store(stream, "ContinueButton config");
        } catch (IOException e) {
        }
    }
}
