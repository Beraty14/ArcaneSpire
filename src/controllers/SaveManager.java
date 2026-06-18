package controllers;
 
import java.io.*;
import java.util.Properties;
import models.Player;
import models.Card;
import models.CardLibrary;
 
public class SaveManager {
    public static class SaveDetails {
        public String heroClass;
        public int floor;
        public int hp;
        public int gold;
        public int maxEnergy;
        public long playTimeMs;
        public String lastSaved;
        public String saveType; // AUTO, MANUAL, QUICK
        public boolean exists;
    }
 
    public interface SaveListener {
        void onGameSaved(String message);
    }
 
    private static SaveListener saveListener;
 
    public static void setSaveListener(SaveListener listener) {
        saveListener = listener;
    }
 
    private static final String SAVE_DIR = "sav/";

    public static String getSaveFileName(int slot) {
        File dir = new File("sav");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (slot == 0) return SAVE_DIR + "savegame_quicksave.properties";
        return SAVE_DIR + "savegame_" + slot + ".properties";
    }
 
    public static void saveGame(GameManager gm, int slot) {
        saveGame(gm, slot, slot == 0 ? "QUICK" : "AUTO");
    }
 
    public static void saveGame(GameManager gm, int slot, String saveType) {
        if (gm.getPlayer() == null) return;
        gm.updatePlayTimeSession();
 
        Properties props = new Properties();
        props.setProperty("heroClass", gm.getPlayer().getHeroClass().name());
        props.setProperty("hp", String.valueOf(gm.getPlayer().getCurrentHp()));
        props.setProperty("floor", String.valueOf(gm.getCurrentFloor()));
        props.setProperty("gold", String.valueOf(gm.getPlayer().getGold()));
        props.setProperty("maxEnergy", String.valueOf(gm.getPlayer().getMaxEnergy()));
        props.setProperty("playTimeMs", String.valueOf(gm.getTotalPlayTimeMs()));
        props.setProperty("saveType", saveType);
 
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        props.setProperty("lastSaved", sdf.format(new java.util.Date()));
 
        // Save deck as comma-separated card IDs
        StringBuilder sb = new StringBuilder();
        java.util.List<Card> deck = gm.getPlayer().getDeck();
        for (int i = 0; i < deck.size(); i++) {
            sb.append(deck.get(i).getId());
            if (i < deck.size() - 1) sb.append(",");
        }
        props.setProperty("deck", sb.toString());
        
        try (FileOutputStream out = new FileOutputStream(getSaveFileName(slot))) {
            props.store(out, "ArcaneSpire Save File Slot " + slot + " Type " + saveType);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        if (saveListener != null) {
            models.LanguageManager lm = models.LanguageManager.getInstance();
            String msg = lm.getText("autosave_notif");
            if ("MANUAL".equals(saveType)) {
                msg = lm.getText("save_title") + " ✔";
            } else if ("QUICK".equals(saveType)) {
                msg = lm.getText("quicksave_notif") + " ✔";
            }
            saveListener.onGameSaved(msg);
        }
    }
 
    public static boolean loadGame(GameManager gm, int slot) {
        File file = new File(getSaveFileName(slot));
        if (!file.exists()) return false;
 
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
            Player.HeroClass hClass = Player.HeroClass.valueOf(props.getProperty("heroClass"));
            int hp = Integer.parseInt(props.getProperty("hp"));
            int floor = Integer.parseInt(props.getProperty("floor"));
            int gold = props.containsKey("gold") ? Integer.parseInt(props.getProperty("gold")) : 10;
            int maxEnergy = props.containsKey("maxEnergy") ? Integer.parseInt(props.getProperty("maxEnergy")) : (hClass == Player.HeroClass.MAGE ? 4 : 3);
            long playTimeMs = props.containsKey("playTimeMs") ? Long.parseLong(props.getProperty("playTimeMs")) : 0L;
 
            gm.setTotalPlayTimeMs(playTimeMs);
            gm.loadGameState(hClass, floor, hp, true);
 
            if (gm.getPlayer() != null) {
                gm.getPlayer().setGold(gold);
                gm.getPlayer().setMaxEnergy(maxEnergy);
 
                // Load deck from save file if it exists
                if (props.containsKey("deck")) {
                    String deckStr = props.getProperty("deck");
                    if (deckStr != null && !deckStr.trim().isEmpty()) {
                        String[] cardIds = deckStr.split(",");
                        gm.getPlayer().getDeck().clear();
                        for (String id : cardIds) {
                            gm.getPlayer().getDeck().add(CardLibrary.getCard(id.trim()));
                        }
                        gm.getPlayer().shuffleDeck();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean hasSaveGame(int slot) {
        return new File(getSaveFileName(slot)).exists();
    }
 
    public static boolean hasAnySaveGame() {
        return hasSaveGame(1) || hasSaveGame(2) || hasSaveGame(3) || hasSaveGame(4) || hasSaveGame(5);
    }
 
    public static boolean deleteSaveGame(int slot) {
        File file = new File(getSaveFileName(slot));
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
 
    public static SaveDetails getSaveDetails(int slot) {
        SaveDetails details = new SaveDetails();
        File file = new File(getSaveFileName(slot));
        if (!file.exists()) {
            details.exists = false;
            return details;
        }
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
            details.exists = true;
            details.heroClass = props.getProperty("heroClass");
            details.floor = Integer.parseInt(props.getProperty("floor"));
            details.hp = Integer.parseInt(props.getProperty("hp"));
            details.gold = props.containsKey("gold") ? Integer.parseInt(props.getProperty("gold")) : 10;
            details.maxEnergy = props.containsKey("maxEnergy") ? Integer.parseInt(props.getProperty("maxEnergy")) : 3;
            details.playTimeMs = props.containsKey("playTimeMs") ? Long.parseLong(props.getProperty("playTimeMs")) : 0L;
            details.lastSaved = props.getProperty("lastSaved");
            details.saveType = props.getProperty("saveType");
        } catch (Exception e) {
            details.exists = false;
        }
        return details;
    }
}
