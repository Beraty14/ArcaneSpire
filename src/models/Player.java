package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends Character {
    public enum HeroClass {
        KNIGHT, MAGE, ROGUE, PALADIN
    }

    private HeroClass heroClass;
    private int maxEnergy;
    private int currentEnergy;
    private int drawCount;
    private List<Card> deck;
    private List<Card> hand;
    private List<Card> discardPile;
    private int gold;
    private List<Card> guaranteedCardsForNextBattle = new ArrayList<>();

    public Player(String name, int maxHp, int maxEnergy, String imagePath, HeroClass heroClass, int drawCount) {
        super(name, maxHp, imagePath);
        this.heroClass = heroClass;
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
        this.drawCount = drawCount;
        this.deck = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.gold = 10; // Starting gold
    }

    public HeroClass getHeroClass() { return heroClass; }
    public int getMaxEnergy() { return maxEnergy; }
    public int getCurrentEnergy() { return currentEnergy; }
    public int getDrawCount() { return drawCount; }
    public List<Card> getHand() { return hand; }
    public List<Card> getDeck() { return deck; }
    public List<Card> getDiscardPile() { return discardPile; }
    public int getGold() { return gold; }
    public void addGold(int amount) { if (amount > 0) this.gold += amount; }
    public void setGold(int gold) { this.gold = Math.max(0, gold); }
    public boolean spendGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }

    public void setMaxEnergy(int maxEnergy) { this.maxEnergy = maxEnergy; }
    public void increaseMaxEnergy(int amount) {
        if (amount > 0) {
            this.maxEnergy += amount;
            this.currentEnergy = Math.min(this.currentEnergy + amount, this.maxEnergy);
        }
    }

    public void resetEnergy() {
        this.currentEnergy = this.maxEnergy;
    }

    public boolean spendEnergy(int amount) {
        if (this.currentEnergy >= amount) {
            this.currentEnergy -= amount;
            return true;
        }
        return false;
    }

    public void addCardToDeck(Card card) {
        this.deck.add(card);
    }

    public void addGuaranteedCard(Card card) {
        if (card != null) {
            this.guaranteedCardsForNextBattle.add(card);
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck);
        for (Card c : guaranteedCardsForNextBattle) {
            this.deck.remove(c);
            this.deck.add(0, c);
        }
        guaranteedCardsForNextBattle.clear();
    }

    public void drawCards(int count) {
        for (int i = 0; i < count; i++) {
            if (deck.isEmpty()) {
                deck.addAll(discardPile);
                discardPile.clear();
                Collections.shuffle(deck);
            }
            if (deck.isEmpty()) {
                break;
            }

            // El çeşitliliğini korumak için aynı ID'ye sahip kartların eldeki sayısını sınırla (en fazla 2 adet)
            int chosenIndex = -1;
            for (int k = 0; k < deck.size(); k++) {
                Card candidate = deck.get(k);
                int duplicateCount = 0;
                for (Card inHand : hand) {
                    if (inHand.getId().equals(candidate.getId())) {
                        duplicateCount++;
                    }
                }
                if (duplicateCount < 2) {
                    chosenIndex = k;
                    break;
                }
            }

            if (chosenIndex != -1) {
                hand.add(deck.remove(chosenIndex));
            } else {
                hand.add(deck.remove(0));
            }
        }
    }

    public void discardHand() {
        discardPile.addAll(hand);
        hand.clear();
    }

    public void applyStartOfTurnPassives() {
        if (heroClass == HeroClass.PALADIN) {
            this.heal(3);
        }
    }

    public void initializeDefaultDeck() {
        deck.clear();
        for (int i = 0; i < 4; i++) {
            deck.add(CardLibrary.getCard("strike"));
        }
        for (int i = 0; i < 4; i++) {
            deck.add(CardLibrary.getCard("block"));
        }
        deck.add(CardLibrary.getCard("fireball"));
        deck.add(CardLibrary.getCard("heal"));
        shuffleDeck();
    }
}
