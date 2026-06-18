package models;

import java.util.ArrayList;
import java.util.List;

public class CardLibrary {
    public static Card getCard(String id) {
        LanguageManager lm = LanguageManager.getInstance();
        switch (id) {
            case "quickStrike": return new AttackCard("quickStrike", lm.getText("card_quickStrike"), 0, lm.getText("card_quickStrike_desc"), "card_quick_strike.png", 3, 1, 0);
            case "tacticalDefense": return new MagicCard("tacticalDefense", lm.getText("card_tacticalDefense"), 1, lm.getText("card_tacticalDefense_desc"), "card_tactical_defense.png", 0, 0, 4, 1);
            case "poisonDart": return new AttackCard("poisonDart", lm.getText("card_poisonDart"), 1, lm.getText("card_poisonDart_desc"), "card_poison_dart.png", 1, 1, 2);
            case "prepWork": return new MagicCard("prepWork", lm.getText("card_prepWork"), 1, lm.getText("card_prepWork_desc"), "card_prep_work.png", 0, 0, 0, 2);

            case "strike": return new AttackCard("strike", lm.getText("card_strike"), 1, lm.getText("card_strike_desc"), "card_strike.png.jpg", 6, 1, 0);
            case "heavyStrike": return new AttackCard("heavyStrike", lm.getText("card_heavyStrike"), 2, lm.getText("card_heavyStrike_desc"), "card_heavy_strike.png", 14, 1, 0);
            case "doubleSlash": return new AttackCard("doubleSlash", lm.getText("card_doubleSlash"), 1, lm.getText("card_doubleSlash_desc"), "card_double_slash.png", 4, 2, 0);
            case "poisonBlade": return new AttackCard("poisonBlade", lm.getText("card_poisonBlade"), 1, lm.getText("card_poisonBlade_desc"), "card_poison_blade.png", 4, 1, 3);
            case "fireball": return new AttackCard("fireball", lm.getText("card_fireball"), 2, lm.getText("card_fireball_desc"), "card_fireball.png.jpg", 12, 1, 0);
            case "inferno": return new AttackCard("inferno", lm.getText("card_inferno"), 3, lm.getText("card_inferno_desc"), "card_inferno.png", 20, 1, 0);
            
            case "block": return new DefenseCard("block", lm.getText("card_block"), 1, lm.getText("card_block_desc"), "card_block.png.jpg", 5);
            case "fortify": return new DefenseCard("fortify", lm.getText("card_fortify"), 2, lm.getText("card_fortify_desc"), "card_fortify.png", 12);
            
            case "heal": return new MagicCard("heal", lm.getText("card_heal"), 1, lm.getText("card_heal_desc"), "card_heal.png.jpg", 8, 0, 0);
            case "holyLight": return new MagicCard("holyLight", lm.getText("card_holyLight"), 2, lm.getText("card_holyLight_desc"), "card_holy_light.png", 15, 0, 0);
            case "vampiric": return new MagicCard("vampiric", lm.getText("card_vampiric"), 2, lm.getText("card_vampiric_desc"), "card_vampiric.png", 5, 8, 0);
            case "shieldBash": return new MagicCard("shieldBash", lm.getText("card_shieldBash"), 1, lm.getText("card_shieldBash_desc"), "card_shield_bash.png", 0, 3, 3);
            
            default: return new AttackCard("strike", lm.getText("card_strike"), 1, lm.getText("card_strike_desc"), "card_strike.png.jpg", 6, 1, 0);
        }
    }

    public static List<Card> getRandomCards(int count) {
        String[] allIds = {
            "heavyStrike", "doubleSlash", "poisonBlade", "fireball", "inferno",
            "fortify", "heal", "holyLight", "vampiric", "shieldBash",
            "quickStrike", "tacticalDefense", "poisonDart", "prepWork"
        };
        List<String> idList = new ArrayList<>(java.util.Arrays.asList(allIds));
        java.util.Collections.shuffle(idList);
        
        List<Card> result = new ArrayList<>();
        for (int i = 0; i < count && i < idList.size(); i++) {
            result.add(getCard(idList.get(i)));
        }
        return result;
    }
}
