package dev.mattrm.mc.uhcplugin.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtils {
    public static File getResourceAsFile(String resourcePath) throws IOException {
        InputStream in = FileUtils.class.getResourceAsStream(resourcePath);
        if (in == null) {
            System.out.println("Could not load resource file");
            return null;
        }

        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            //copy stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    public static File exportResourceFile(String fileName, String outputDir, JavaPlugin plugin) throws IOException {
        Path dirPath = plugin.getDataFolder().toPath().resolve(outputDir);
        Files.createDirectories(dirPath);

        File file = new File(dirPath.toFile(), fileName);
        try (InputStream in = FileUtils.class.getResourceAsStream(fileName)) {
            if (in == null) {
                System.out.println("Could not load resource file");
                return null;
            }

            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return file;
    }
}
