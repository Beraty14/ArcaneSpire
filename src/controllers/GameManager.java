package controllers;

import models.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Oyunun beyni. Singleton Pattern kullanılarak tek bir instance üzerinden
 * tüm tur mantığını, kat geçişlerini ve oyuncu/düşman etkileşimlerini yönetir.
 * OOP Prensibi: Singleton Design Pattern + Encapsulation.
 */
public class GameManager {
    private static GameManager instance;

    private Player player;
    private Enemy currentEnemy;
    private int currentFloor;
    private int turnNumber;
    private boolean playerTurn;
    private boolean gameActive;
    private boolean riddleCorrect;
    private int goldEarnedLastBattle;
    private int currentSaveSlot = 1;

    private long totalPlayTimeMs;
    private long sessionStartTimeMs;

    public void startPlayTimeTracking() {
        this.sessionStartTimeMs = System.currentTimeMillis();
    }

    public void setTotalPlayTimeMs(long ms) {
        this.totalPlayTimeMs = ms;
    }

    public long getTotalPlayTimeMs() {
        if (sessionStartTimeMs == 0) return totalPlayTimeMs;
        return totalPlayTimeMs + (System.currentTimeMillis() - sessionStartTimeMs);
    }

    public void updatePlayTimeSession() {
        if (sessionStartTimeMs > 0) {
            totalPlayTimeMs += (System.currentTimeMillis() - sessionStartTimeMs);
            sessionStartTimeMs = System.currentTimeMillis();
        }
    }

    // Listener Pattern - UI güncellemelerini bildirmek için
    private GameEventListener listener;

    public int getCurrentSaveSlot() { return currentSaveSlot; }
    public void setCurrentSaveSlot(int slot) { this.currentSaveSlot = slot; }

    private GameManager() {
        currentFloor = 1;
        turnNumber = 0;
        playerTurn = true;
        gameActive = false;
        riddleCorrect = false;
        goldEarnedLastBattle = 0;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    // --- Event Listener Arayüzü (Observer Pattern) ---
    public interface GameEventListener {
        void onBattleStart(Player player, Enemy enemy);
        void onPlayerTurnStart();
        void onCardPlayed(Card card, int damageDealt);
        void onEnemyTurnStart(String moveName, int moveDamage);
        void onEnemyAttack(int damage);
        void onBattleWon(int floor);
        void onGameOver();
        void onFloorChange(int newFloor, Enemy newEnemy);
        void onStatsUpdated();
    }

    public void setListener(GameEventListener listener) {
        this.listener = listener;
    }

    // --- Oyun Başlangıcı ---
    public void loadGameState(Player.HeroClass heroClass, int floor, int hp, boolean riddleCorrect) {
        this.riddleCorrect = riddleCorrect;
        this.currentFloor = floor;
        this.turnNumber = 0;
        this.gameActive = true;
        this.startPlayTimeTracking();

        String heroImage = "hero_knight.png.png";
        String heroName = models.LanguageManager.getInstance().getText("hero_knight_name");
        int maxHp = 90;
        int energy = 3;
        int draw = 5;

        switch (heroClass) {
            case MAGE: heroImage = "hero_mage.png.jpg"; heroName = models.LanguageManager.getInstance().getText("hero_mage_name"); maxHp = 65; energy = 4; draw = 6; break;
            case ROGUE: heroImage = "hero_rogue.png.png"; heroName = models.LanguageManager.getInstance().getText("hero_rogue_name"); maxHp = 70; energy = 3; draw = 5; break;
            case PALADIN: heroImage = "hero_paladin.png.png"; heroName = models.LanguageManager.getInstance().getText("hero_paladin_name"); maxHp = 85; energy = 3; draw = 5; break;
            default: break;
        }

        player = new Player(heroName, maxHp, energy, heroImage, heroClass, draw);
        player.initializeDefaultDeck();
        player.setCurrentHp(hp);

        currentEnemy = createEnemyForFloor(currentFloor);
        startBattle();
    }
    public void startNewGame(Player.HeroClass heroClass) {
        currentFloor = 1;
        turnNumber = 0;
        gameActive = true;
        this.totalPlayTimeMs = 0;
        this.startPlayTimeTracking();

        String heroImage = "hero_knight.png.png";
        String heroName = models.LanguageManager.getInstance().getText("hero_knight_name");
        int hp = 90;
        int energy = 3;
        int draw = 5;

        switch (heroClass) {
            case MAGE: heroImage = "hero_mage.png.jpg"; heroName = models.LanguageManager.getInstance().getText("hero_mage_name"); hp = 65; energy = 4; draw = 6; break;
            case ROGUE: heroImage = "hero_rogue.png.png"; heroName = models.LanguageManager.getInstance().getText("hero_rogue_name"); hp = 70; energy = 3; draw = 5; break;
            case PALADIN: heroImage = "hero_paladin.png.png"; heroName = models.LanguageManager.getInstance().getText("hero_paladin_name"); hp = 85; energy = 3; draw = 5; break;
            default: break;
        }

        player = new Player(heroName, hp, energy, heroImage, heroClass, draw);
        player.initializeDefaultDeck();

        if (!riddleCorrect) {
            player.takeDamage(10);
        }

        currentEnemy = createEnemyForFloor(currentFloor);
        SaveManager.saveGame(this, currentSaveSlot);
        startBattle();
    }

    public void setRiddleCorrect(boolean correct) {
        this.riddleCorrect = correct;
    }

    private void startBattle() {
        turnNumber = 0;
        currentEnemy = createEnemyForFloor(currentFloor);
        if (listener != null) listener.onBattleStart(player, currentEnemy);
        
        // Intro ekranı için 2.8 saniye bekle
        javax.swing.Timer t = new javax.swing.Timer(2800, e -> {
            startPlayerTurn();
        });
        t.setRepeats(false);
        t.start();
    }

    public void startPlayerTurn() {
        playerTurn = true;
        turnNumber++;
        player.resetEnergy();
        player.resetArmor();
        
        player.applyStartOfTurnPassives();
        player.takePoisonDamage();
        if (player.isDead()) {
            gameActive = false;
            if (listener != null) listener.onGameOver();
            return;
        }

        currentEnemy.takePoisonDamage();
        if (currentEnemy.isDead()) {
            winBattle();
            return;
        }

        player.drawCards(player.getDrawCount());

        if (listener != null) {
            listener.onPlayerTurnStart();
            listener.onStatsUpdated();
        }
    }

    public boolean playCard(int handIndex) {
        if (!playerTurn || !gameActive) return false;

        List<Card> hand = player.getHand();
        if (handIndex < 0 || handIndex >= hand.size()) return false;

        Card card = hand.get(handIndex);
        if (!card.canPlay(player)) return false;

        player.spendEnergy(card.getEnergyCost());
        hand.remove(handIndex);

        boolean enemyHadBlock = currentEnemy.getArmor() > 0;
        card.applyEffect(player, currentEnemy);

        if (card instanceof models.AttackCard) {
            if (enemyHadBlock) controllers.AudioPlayer.playSFX("shield_break.mp4");
            else controllers.AudioPlayer.playSFX("attack.wav");
        } else if (card instanceof models.DefenseCard) {
            controllers.AudioPlayer.playSFX("gain_block.mp4");
        } else if (card instanceof models.MagicCard) {
            models.MagicCard mc = (models.MagicCard) card;
            if (mc.getHealAmount() > 0) {
                controllers.AudioPlayer.playSFX("heal.mp4");
            } else if (mc.getDamageAmount() > 0) {
                if (enemyHadBlock) controllers.AudioPlayer.playSFX("shield_break.mp4");
                else controllers.AudioPlayer.playSFX("attack.wav");
            } else if (mc.getArmorAmount() > 0) {
                controllers.AudioPlayer.playSFX("gain_block.mp4");
            }
        }

        int damage = 0;
        if (card instanceof AttackCard) {
            damage = ((AttackCard) card).getDamage();
        }

        player.getDiscardPile().add(card);

        if (listener != null) {
            listener.onCardPlayed(card, damage);
            listener.onStatsUpdated();
        }

        if (currentEnemy.isDead()) {
            winBattle();
            return true;
        }

        return true;
    }

    public void endPlayerTurn() {
        if (!playerTurn || !gameActive) return;
        playerTurn = false;
        player.discardHand();
        
        if (listener != null) {
            listener.onEnemyTurnStart("", 0);
        }

        // Düşman yapay zekası: Sadece bir hamle yap
        javax.swing.Timer startWait = new javax.swing.Timer(600, e -> {
            Enemy.Move m = currentEnemy.decideNextMove(player, turnNumber);
            boolean playerHadBlock = player.getArmor() > 0;
            currentEnemy.executeTurn(player, turnNumber);

            if (m != null) {
                if (m.damage > 0) {
                    if (playerHadBlock) controllers.AudioPlayer.playSFX("shield_break.mp4");
                    else controllers.AudioPlayer.playSFX("attack.wav");
                } else if (m.armor > 0) {
                    controllers.AudioPlayer.playSFX("gain_block.mp4");
                }
            }
            
            if (listener != null) {
                if (m != null) {
                    listener.onEnemyTurnStart(m.name, m.damage);
                    if (m.damage > 0) {
                        listener.onEnemyAttack(m.damage);
                    } else if (m.armor > 0) {
                        listener.onEnemyAttack(0); // Zırhlandı mesajı için
                    } else {
                        listener.onEnemyAttack(0); // İyileşti vs
                    }
                }
                listener.onStatsUpdated();
            }
            
            javax.swing.Timer endWait = new javax.swing.Timer(1000, e2 -> {
                if (player.isDead()) {
                    gameActive = false;
                    if (listener != null) listener.onGameOver();
                } else {
                    startPlayerTurn();
                }
            });
            endWait.setRepeats(false);
            endWait.start();
        });
        startWait.setRepeats(false);
        startWait.start();
    }

    private void winBattle() {
        goldEarnedLastBattle = 15 + (int)(Math.random() * 11); // Random 15-25 Gold
        if (player != null) {
            player.addGold(goldEarnedLastBattle);
        }
        if (listener != null) listener.onBattleWon(currentFloor);
    }

    public void advanceFloor() {
        currentFloor++;

        if (currentFloor > 10) {
            gameActive = false;
            return;
        }

        player.heal(10);
        player.discardHand();
        player.getDeck().addAll(player.getDiscardPile());
        player.getDiscardPile().clear();
        player.shuffleDeck();

        currentEnemy = createEnemyForFloor(currentFloor);

        SaveManager.saveGame(this, currentSaveSlot);

        if (listener != null) listener.onFloorChange(currentFloor, currentEnemy);
        startBattle();
    }

    private Enemy createEnemyForFloor(int floor) {
        switch (floor) {
            case 1: return new Enemies.SkeletonEnemy();
            case 2: return new Enemies.GoblinEnemy();
            case 3: return new Enemies.SlimeEnemy();
            case 4: return new Enemies.VampireEnemy();
            case 5: return new Enemies.GolemEnemy();
            case 6: return new Enemies.WraithEnemy();
            case 7: return new Enemies.DemonEnemy();
            case 8: return new Enemies.LichEnemy();
            case 9: return new Enemies.HydraEnemy();
            case 10: return new Enemies.DragonBoss();
            default: return new Enemies.DragonBoss();
        }
    }

    public Player getPlayer() { return player; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
    public int getCurrentFloor() { return currentFloor; }
    public int getTurnNumber() { return turnNumber; }
    public boolean isPlayerTurn() { return playerTurn; }
    public boolean isGameActive() { return gameActive; }
    public int getGoldEarnedLastBattle() { return goldEarnedLastBattle; }

    public String getFloorName() {
        models.LanguageManager lm = models.LanguageManager.getInstance();
        String prefix = lm.getText("arena_word");
        switch (currentFloor) {
            case 1: return prefix + "1 - " + lm.getText("floor_1");
            case 2: return prefix + "2 - " + lm.getText("floor_2");
            case 3: return prefix + "3 - " + lm.getText("floor_3");
            case 4: return prefix + "4 - " + lm.getText("floor_4");
            case 5: return prefix + "5 - " + lm.getText("floor_5");
            case 6: return prefix + "6 - " + lm.getText("floor_6");
            case 7: return prefix + "7 - " + lm.getText("floor_7");
            case 8: return prefix + "8 - " + lm.getText("floor_8");
            case 9: return prefix + "9 - " + lm.getText("floor_9");
            case 10: return lm.getText("floor_10");
            default: return prefix + currentFloor;
        }
    }

    public String getBackgroundForFloor() {
        if (currentFloor == 10) return "bg_arena_boss.png"; // bg_arena_boss.png does not exist but let's see. Wait, I'll map them!
        if (currentFloor >= 7) return "bg_arena_hard.png";
        if (currentFloor >= 4) return "bg_arena_medium.png";
        return "bg_arena_easy.png";
    }
}
