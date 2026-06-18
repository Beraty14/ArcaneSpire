package models;

public class SettingsManager {
    private static SettingsManager instance;
    
    private int musicVolume = 10; // Reduced by 50% as requested
    private int sfxVolume = 30;   // Thunder reduced by 35%+ as requested

    private SettingsManager() {}

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = Math.max(0, Math.min(100, musicVolume));
    }

    public int getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(int sfxVolume) {
        this.sfxVolume = Math.max(0, Math.min(100, sfxVolume));
    }
}
