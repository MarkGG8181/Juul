import net.minecraft.client.main.Main;

import java.io.File;

/**
 * @author Liticane
 * Allows using existing .minecraft directory during testing.
 */
@SuppressWarnings("ALL")
public class Start {
    // I am tired of copying around assets every single time.
    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("../../natives").getAbsolutePath());

        final String appdataDirectory = System.getenv("APPDATA");

        File runDir = new File(appdataDirectory != null ? appdataDirectory : System.getProperty("user.home", "."), ".minecraft/");

        File gameDir = new File(runDir, ".");
        File assetsDir = new File(runDir, "assets/");

        Main.main(new String[]{
                "--version", "1.8",
                "--accessToken", "0",
                "--assetIndex", "1.8",
                "--userProperties", "{}",
                "--gameDir", gameDir.getAbsolutePath(),
                "--assetsDir", assetsDir.getAbsolutePath()
        });
    }
}