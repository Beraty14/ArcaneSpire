package controllers;

import java.io.File;
import java.io.FileWriter;

public class AudioPlayer {
    private static Process playProcess;
    private static Process sfxProcess;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopMusic();
            if (sfxProcess != null) sfxProcess.destroyForcibly();
            try {
                // Ensure no orphaned script engines remain playing audio when game exits
                Runtime.getRuntime().exec("taskkill /F /IM cscript.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM wscript.exe /T");
            } catch (Exception e) {}
        }));
    }

    private static String getBasePath() {
        String bp = System.getProperty("user.dir");
        if (!bp.endsWith("ArcaneSpire")) {
            bp += java.io.File.separator + "ArcaneSpire";
        }
        return bp;
    }

    private static String currentMusic = null;

    public static void playMusic(String musicFileName) {
        if (musicFileName.equals(currentMusic) && playProcess != null) {
            try {
                if (playProcess.isAlive()) return; // Zaten çalıyorsa baştan başlatma
            } catch (NoSuchMethodError e) {
                return; // Eski Java sürümlerinde hata verirse yine de devam etme
            }
        }
        
        String basePath = getBasePath();
        stopMusic(); // Müzik zaten çalıyorsa durdur
        try {
            // VBS betiğini oluştur
            File vbs = new File(basePath + File.separator + "src" + File.separator + "audio" + File.separator + "play.vbs");
            vbs.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(vbs);
            fw.write("Set WMP = CreateObject(\"WMPlayer.OCX\")\n");
            fw.write("WMP.URL = WScript.Arguments(0)\n");
            fw.write("WMP.settings.setMode \"loop\", True\n"); // Döngü modu açık
            fw.write("WMP.settings.volume = " + models.SettingsManager.getInstance().getMusicVolume() + "\n"); // Arkaplan müziği ses seviyesi
            fw.write("WMP.controls.play\n");
            fw.write("While True\n");
            fw.write("  WScript.Sleep 1000\n");
            fw.write("Wend\n");
            fw.close();

            // Dosya yolunu oluştur
            String audioPath = basePath + File.separator + "src" + File.separator + "audio" + File.separator + musicFileName;
            
            // Gizli olarak VBScript çalıştır
            ProcessBuilder pb = new ProcessBuilder("cscript.exe", "//nologo", vbs.getAbsolutePath(), audioPath);
            playProcess = pb.start();
            currentMusic = musicFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playBattleMusic() {
        playMusic("battle_music.mp4");
    }

    public static void playMenuMusic() {
        playMusic("menu_music.mp3");
    }

    public static void playSFX(String sfxFileName) {
        String basePath = getBasePath();
        if (sfxProcess != null) {
            sfxProcess.destroy(); // Önceki efekti kes
        }
        try {
            File vbs = new File(basePath + File.separator + "src" + File.separator + "audio" + File.separator + "play_sfx.vbs");
            vbs.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(vbs);
            fw.write("Set WMP = CreateObject(\"WMPlayer.OCX\")\n");
            fw.write("WMP.URL = WScript.Arguments(0)\n");
            fw.write("WMP.settings.volume = " + models.SettingsManager.getInstance().getSfxVolume() + "\n"); // SFX ses seviyesi
            fw.write("WMP.controls.play\n");
            fw.write("While WMP.playState <> 1\n");
            fw.write("  WScript.Sleep 50\n");
            fw.write("Wend\n");
            fw.close();

            String audioPath = basePath + File.separator + "src" + File.separator + "audio" + File.separator + sfxFileName;
            ProcessBuilder pb = new ProcessBuilder("cscript.exe", "//nologo", vbs.getAbsolutePath(), audioPath);
            sfxProcess = pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        if (playProcess != null) {
            playProcess.destroyForcibly(); // Windows Media Player VBS işlemini zorla sonlandırır
            playProcess = null;
            currentMusic = null;
        }
    }

    public static void refreshMusicVolume() {
        if (currentMusic != null) {
            String temp = currentMusic;
            stopMusic(); // Stop and clear
            playMusic(temp); // Play again with new volume
        }
    }
}
