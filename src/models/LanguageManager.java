package models;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private static LanguageManager instance;
    private String currentLanguage = "TR"; // TR, EN, ES

    private Map<String, Map<String, String>> dictionary;

    private LanguageManager() {
        dictionary = new HashMap<>();
        initTranslations();
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public void setLanguage(String lang) {
        if (lang.equals("TR") || lang.equals("EN") || lang.equals("ES")) {
            this.currentLanguage = lang;
        }
    }
    
    public String getLanguage() {
        return currentLanguage;
    }

    public String getText(String key) {
        if (dictionary.containsKey(currentLanguage) && dictionary.get(currentLanguage).containsKey(key)) {
            return dictionary.get(currentLanguage).get(key);
        }
        return key; // Fallback to key
    }

    private void initTranslations() {
        Map<String, String> tr = new HashMap<>();
        Map<String, String> en = new HashMap<>();
        Map<String, String> es = new HashMap<>();

        // Main Menu
        tr.put("title", "ARCANE SPIRE");
        en.put("title", "ARCANE SPIRE");
        es.put("title", "ARCANE SPIRE");

        tr.put("subtitle", "Kartların Çarpışması");
        en.put("subtitle", "Clash of Cards");
        es.put("subtitle", "Choque de Cartas");

        tr.put("new_game", "⚔  YENİ OYUN  ⚔");
        en.put("new_game", "⚔  NEW GAME  ⚔");
        es.put("new_game", "⚔  NUEVO JUEGO  ⚔");

        tr.put("continue", "DEVAM ET");
        en.put("continue", "CONTINUE");
        es.put("continue", "CONTINUAR");

        tr.put("how_to_play", "? NASIL OYNANIR");
        en.put("how_to_play", "? HOW TO PLAY");
        es.put("how_to_play", "? CÓMO JUGAR");
        
        tr.put("settings", "⚙ AYARLAR");
        en.put("settings", "⚙ SETTINGS");
        es.put("settings", "⚙ AJUSTES");

        tr.put("exit", "ÇIKIŞ");
        en.put("exit", "EXIT");
        es.put("exit", "SALIR");

        tr.put("exit_confirm", "Oyundan çıkmak istediğinize emin misiniz?");
        en.put("exit_confirm", "Are you sure you want to exit the game?");
        es.put("exit_confirm", "¿Estás seguro de que quieres salir del juego?");

        tr.put("yes", "EVET");
        en.put("yes", "YES");
        es.put("yes", "SÍ");

        tr.put("no", "HAYIR");
        en.put("no", "NO");
        es.put("no", "NO");

        // Settings Menu
        tr.put("settings_title", "AYARLAR");
        en.put("settings_title", "SETTINGS");
        es.put("settings_title", "AJUSTES");

        tr.put("language", "Dil:");
        en.put("language", "Language:");
        es.put("language", "Idioma:");

        tr.put("music_vol", "Müzik Sesi:");
        en.put("music_vol", "Music Vol:");
        es.put("music_vol", "Vol. Música:");

        tr.put("sfx_vol", "Efekt Sesi:");
        en.put("sfx_vol", "SFX Vol:");
        es.put("sfx_vol", "Vol. Efectos:");

        tr.put("back", "GERİ");
        en.put("back", "BACK");
        es.put("back", "VOLVER");

        // Battle
        tr.put("floor", "Kat");
        en.put("floor", "Floor");
        es.put("floor", "Piso");

        tr.put("end_turn", "⚔ TURU BİTİR");
        en.put("end_turn", "⚔ END TURN");
        es.put("end_turn", "⚔ TERMINAR TURNO");

        tr.put("your_turn", "SENİN TURUN");
        en.put("your_turn", "YOUR TURN");
        es.put("your_turn", "TU TURNO");

        tr.put("enemy_turn", "DÜŞMANIN TURU");
        en.put("enemy_turn", "ENEMY'S TURN");
        es.put("enemy_turn", "TURNO DEL ENEMIGO");

        tr.put("armor", "Zırhlandı!");
        en.put("armor", "Armored!");
        es.put("armor", "¡Blindado!");

        tr.put("victory", "⚔ ZAFER! ⚔");
        en.put("victory", "⚔ VICTORY! ⚔");
        es.put("victory", "⚔ ¡VICTORIA! ⚔");

        tr.put("victory_desc", "TEBRİKLER!\nArcane Spire fethedildi!\nKadim Ejderhayı yendin!\n\nKule artık sana boyun eğiyor...");
        en.put("victory_desc", "CONGRATULATIONS!\nArcane Spire is conquered!\nYou defeated the Ancient Dragon!\n\nThe tower now bows to you...");
        es.put("victory_desc", "¡FELICIDADES!\n¡Arcane Spire conquistada!\n¡Derrotaste al Dragón Antiguo!\n\nLa torre ahora se inclina ante ti...");

        tr.put("arena_cleared", "Arena Geçildi! +10 HP");
        en.put("arena_cleared", "Arena Cleared! +10 HP");
        es.put("arena_cleared", "¡Arena Completada! +10 HP");
        
        tr.put("choose_card", "Destene eklemek için bir kart seç:");
        en.put("choose_card", "Choose a card to add to your deck:");
        es.put("choose_card", "Elige una carta para añadir a tu mazo:");

        tr.put("game_over", "💀 Öldün...");
        en.put("game_over", "💀 You Died...");
        es.put("game_over", "💀 Has Muerto...");
        
        tr.put("game_over_desc", "Arcane Spire bir ruhu daha yuttu.");
        en.put("game_over_desc", "Arcane Spire consumed another soul.");
        es.put("game_over_desc", "Arcane Spire consumió otra alma.");

        tr.put("arena_word", "Arena: ");
        en.put("arena_word", "Arena: ");
        es.put("arena_word", "Arena: ");
        
        tr.put("turn_word", "Tur: ");
        en.put("turn_word", "Turn: ");
        es.put("turn_word", "Turno: ");
        
        tr.put("continue_btn", "DEVAM ET");
        en.put("continue_btn", "CONTINUE");
        es.put("continue_btn", "CONTINUAR");

        // How to Play Slides
        tr.put("htp_title_1", "ARCANE SPIRE'A HOŞ GELDİNİZ");
        en.put("htp_title_1", "WELCOME TO ARCANE SPIRE");
        es.put("htp_title_1", "BIENVENIDOS A ARCANE SPIRE");

        tr.put("htp_desc_1", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 14px; line-height: 1.6;'>"
            + "Arcane Spire, kart tabanlı ve sıra tabanlı (turn-based) bir taktiksel RPG oyunudur.<br><br>"
            + "Amacınız, her katındaki tehlikeli canavarları yenerek kulenin en üst katına tırmanmak ve zirvedeki kadim <b>Ejderha</b>'yı alt etmektir.<br><br>"
            + "Her tur elinize gelen kartları akıllıca oynayarak saldırmalı, savunma yapmalı ve hayatta kalmalısınız."
            + "</div></html>");
        en.put("htp_desc_1", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 14px; line-height: 1.6;'>"
            + "Arcane Spire is a card-based, turn-based tactical RPG.<br><br>"
            + "Your goal is to climb to the top floor of the tower by defeating dangerous monsters on each floor and slay the ancient <b>Dragon</b>.<br><br>"
            + "You must attack, defend, and survive by playing the cards dealt to you each turn wisely."
            + "</div></html>");
        es.put("htp_desc_1", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 14px; line-height: 1.6;'>"
            + "Arcane Spire es un juego de rol táctico basado en cartas y turnos.<br><br>"
            + "Tu objetivo es llegar al piso superior de la torre derrotando monstruos peligrosos en cada piso y acabar con el antiguo <b>Dragón</b>.<br><br>"
            + "Debes atacar, defender y sobrevivir jugando sabiamente las cartas que te tocan cada turno."
            + "</div></html>");

        tr.put("htp_title_2", "KAHRAMAN SINIFLARI");
        en.put("htp_title_2", "HERO CLASSES");
        es.put("htp_title_2", "CLASES DE HÉROE");

        tr.put("htp_desc_2", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.4;'>"
            + "Oyunda seçebileceğiniz benzersiz özelliklere sahip 4 kahraman sınıfı bulunur:<br><br>"
            + "🛡️ <font color='#FFD700'><b>Şövalye:</b></font> 90 HP ile başlar. Fiziksel saldırılarda rakibe <b>+1 hasar</b> ekler.<br>"
            + "🔮 <font color='#A330C9'><b>Büyücü:</b></font> 65 HP ile başlar. Tur başına <b>4 Enerji</b>ye sahiptir ve eline <b>6 Kart</b> çeker.<br>"
            + "🗡️ <font color='#30C97B'><b>Suikastçı:</b></font> 70 HP ile başlar. Düşmana verdiği tüm zehir etkilerine <b>+2 zehir</b> ekler.<br>"
            + "⛪ <font color='#4488FF'><b>Paladin:</b></font> 85 HP ile başlar. Kutsal gücüyle her tur başında kendi canını <b>+3 HP</b> iyileştirir."
            + "</div></html>");
        en.put("htp_desc_2", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.4;'>"
            + "There are 4 hero classes with unique abilities to choose from:<br><br>"
            + "🛡️ <font color='#FFD700'><b>Knight:</b></font> Starts with 90 HP. Adds <b>+1 damage</b> to physical attacks.<br>"
            + "🔮 <font color='#A330C9'><b>Mage:</b></font> Starts with 65 HP. Has <b>4 Energy</b> per turn and draws <b>6 Cards</b>.<br>"
            + "🗡️ <font color='#30C97B'><b>Rogue:</b></font> Starts with 70 HP. Adds <b>+2 poison</b> to all poison effects applied to the enemy.<br>"
            + "⛪ <font color='#4488FF'><b>Paladin:</b></font> Starts with 85 HP. Heals <b>+3 HP</b> at the start of each turn with holy power."
            + "</div></html>");
        es.put("htp_desc_2", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.4;'>"
            + "Hay 4 clases de héroes con habilidades únicas para elegir:<br><br>"
            + "🛡️ <font color='#FFD700'><b>Caballero:</b></font> Empieza con 90 HP. Añade <b>+1 de daño</b> a los ataques físicos.<br>"
            + "🔮 <font color='#A330C9'><b>Mago:</b></font> Empieza con 65 HP. Tiene <b>4 de Energía</b> y roba <b>6 Cartas</b>.<br>"
            + "🗡️ <font color='#30C97B'><b>Asesino:</b></font> Empieza con 70 HP. Añade <b>+2 de veneno</b> a todos los efectos aplicados.<br>"
            + "⛪ <font color='#4488FF'><b>Paladín:</b></font> Empieza con 85 HP. Cura <b>+3 HP</b> al inicio de cada turno."
            + "</div></html>");

        tr.put("htp_title_3", "KARTLAR VE ENERJİ");
        en.put("htp_title_3", "CARDS AND ENERGY");
        es.put("htp_title_3", "CARTAS Y ENERGÍA");

        tr.put("htp_desc_3", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.5;'>"
            + "Kart oynamak için sol üstteki <b>Enerji</b> havuzunu kullanırsınız. Enerjiniz her tur başında tamamen yenilenir.<br><br>"
            + "🔴 <font color='#FF4444'><b>Saldırı Kartları:</b></font> Düşmana doğrudan hasar verir. (Örn: Vuruş, Ağır Darbe, Ateş Topu)<br>"
            + "🔵 <font color='#44AAFF'><b>Savunma Kartları:</b></font> Düşman hasarını emen geçici <b>Zırh</b> kazanmanızı sağlar. (Zırh tur sonunda silinir)<br>"
            + "🟡 <font color='#FFBB22'><b>Büyü/Destek Kartları:</b></font> Can yenileme, zehir verme veya can çalma gibi özel yetenekler barındırır."
            + "</div></html>");
        en.put("htp_desc_3", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.5;'>"
            + "You use the <b>Energy</b> pool to play cards. Energy is fully restored at the start of each turn.<br><br>"
            + "🔴 <font color='#FF4444'><b>Attack Cards:</b></font> Deal direct damage to the enemy. (e.g. Strike, Heavy Strike, Fireball)<br>"
            + "🔵 <font color='#44AAFF'><b>Defense Cards:</b></font> Grant temporary <b>Armor</b> to absorb damage. (Armor is lost at end of turn)<br>"
            + "🟡 <font color='#FFBB22'><b>Magic/Support Cards:</b></font> Have special abilities like healing, poisoning, or lifesteal."
            + "</div></html>");
        es.put("htp_desc_3", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.5;'>"
            + "Usas la <b>Energía</b> para jugar cartas. La energía se restaura al inicio de cada turno.<br><br>"
            + "🔴 <font color='#FF4444'><b>Cartas de Ataque:</b></font> Infligen daño directo. (ej. Golpe, Golpe Pesado, Bola de Fuego)<br>"
            + "🔵 <font color='#44AAFF'><b>Cartas de Defensa:</b></font> Otorgan <b>Armadura</b> temporal. (Se pierde al final del turno)<br>"
            + "🟡 <font color='#FFBB22'><b>Cartas de Magia/Apoyo:</b></font> Tienen habilidades como curar, envenenar o robar vida."
            + "</div></html>");

        tr.put("htp_title_4", "KULE MUHAFIZININ BİLMECESİ");
        en.put("htp_title_4", "THE TOWER GUARD'S RIDDLE");
        es.put("htp_title_4", "EL ACERTIJO DEL GUARDIA");

        tr.put("htp_desc_4", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.6;'>"
            + "Kuleye ilk girdiğinizde kapıyı tutan kadim bir Muhafızla karşılaşırsınız.<br><br>"
            + "Muhafızın sorduğu bilmeceyi çözmek için <b>30 saniyeniz</b> vardır.<br><br>"
            + "✅ Bilmeceyi <b>doğru cevaplarsanız</b>, Muhafız size yol verir ve oyuna <b>Tam Canla</b> başlarsınız.<br>"
            + "❌ Bilmeceye <b>yanlış cevap verirseniz</b> veya süre biterse, <b>-10 HP ceza</b> ile oyuna başlarsınız!"
            + "</div></html>");
        en.put("htp_desc_4", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.6;'>"
            + "When you first enter the tower, you face an ancient Guard at the door.<br><br>"
            + "You have <b>30 seconds</b> to solve the riddle asked by the Guard.<br><br>"
            + "✅ If you answer <b>correctly</b>, the Guard steps aside and you start with <b>Full HP</b>.<br>"
            + "❌ If you answer <b>incorrectly</b> or time runs out, you start with a <b>-10 HP penalty</b>!"
            + "</div></html>");
        es.put("htp_desc_4", "<html><div style='text-align: center; font-family: Serif; color: #EADDC9; font-size: 13px; line-height: 1.6;'>"
            + "Cuando entras por primera vez, te enfrentas a un Guardia en la puerta.<br><br>"
            + "Tienes <b>30 segundos</b> para resolver el acertijo.<br><br>"
            + "✅ Si respondes <b>correctamente</b>, el Guardia te deja pasar con <b>HP Completa</b>.<br>"
            + "❌ Si respondes <b>incorrectamente</b>, empiezas con una <b>penalización de -10 HP</b>!"
            + "</div></html>");
            
        tr.put("prev_btn", "◀ ÖNCEKİ");
        en.put("prev_btn", "◀ PREVIOUS");
        es.put("prev_btn", "◀ ANTERIOR");
        
        tr.put("next_btn", "SONRAKİ ▶");
        en.put("next_btn", "NEXT ▶");
        es.put("next_btn", "SIGUIENTE ▶");
        
        tr.put("menu_btn_esc", "ANA MENÜ (ESC)");
        en.put("menu_btn_esc", "MAIN MENU (ESC)");
        es.put("menu_btn_esc", "MENÚ PRINCIPAL (ESC)");
        
        tr.put("start_btn_htp", "BAŞLA ⚔");
        en.put("start_btn_htp", "START ⚔");
        es.put("start_btn_htp", "EMPEZAR ⚔");

        // Character Select
        tr.put("char_select_title", "KAHRAMANINI SEÇ");
        en.put("char_select_title", "CHOOSE YOUR HERO");
        es.put("char_select_title", "ELIGE TU HÉROE");

        tr.put("hero_energy", "Enerji");
        en.put("hero_energy", "Energy");
        es.put("hero_energy", "Energía");

        tr.put("hero_cards", "Kart");
        en.put("hero_cards", "Cards");
        es.put("hero_cards", "Cartas");

        tr.put("hero_knight_name", "Şövalye");
        en.put("hero_knight_name", "Knight");
        es.put("hero_knight_name", "Caballero");

        tr.put("hero_knight_desc", "Fiziksel saldırılarına her zaman fazladan hasar ekler.");
        en.put("hero_knight_desc", "Always adds extra damage to physical attacks.");
        es.put("hero_knight_desc", "Siempre añade daño extra a los ataques físicos.");

        tr.put("hero_knight_lore", "Ağır zırhı ve devasa kılıcıyla savaş alanını domine eder. Savunması kadar hücumu da etkilidir.");
        en.put("hero_knight_lore", "Dominates the battlefield with heavy armor and a massive sword. His offense is as effective as his defense.");
        es.put("hero_knight_lore", "Domina el campo de batalla con armadura pesada y una espada masiva. Su ataque es tan eficaz como su defensa.");

        tr.put("hero_mage_name", "Büyücü");
        en.put("hero_mage_name", "Mage");
        es.put("hero_mage_name", "Mago");

        tr.put("hero_mage_desc", "Enerjisi ve çektiği kart sayısı diğerlerinden daha fazladır.");
        en.put("hero_mage_desc", "Has more energy and draws more cards than others.");
        es.put("hero_mage_desc", "Tiene más energía y roba más cartas que los demás.");

        tr.put("hero_mage_lore", "Antik sırların koruyucusu. Düşük canını, yıkıcı büyüleri ve yüksek kart kapasitesiyle telafi eder.");
        en.put("hero_mage_lore", "Keeper of ancient secrets. Compensates for low health with devastating spells and high card capacity.");
        es.put("hero_mage_lore", "Guardián de secretos antiguos. Compensa su baja salud con hechizos devastadores y alta capacidad de cartas.");

        tr.put("hero_rogue_name", "Suikastçı");
        en.put("hero_rogue_name", "Rogue");
        es.put("hero_rogue_name", "Asesino");

        tr.put("hero_rogue_desc", "Düşmana uyguladığı zehirlerin etkisi her zaman daha güçlüdür.");
        en.put("hero_rogue_desc", "The poisons applied to the enemy are always stronger.");
        es.put("hero_rogue_desc", "Los venenos aplicados al enemigo siempre son más fuertes.");

        tr.put("hero_rogue_lore", "Gölgelerde dans eder. Rakiplerini sinsi zehirler ve hızlı bıçak darbeleriyle yavaş yavaş ölüme sürükler.");
        en.put("hero_rogue_lore", "Dances in the shadows. Drags opponents to death slowly with sneaky poisons and swift knife strikes.");
        es.put("hero_rogue_lore", "Baila en las sombras. Arrastra a los oponentes a la muerte lentamente con venenos furtivos y rápidos golpes de cuchillo.");

        tr.put("hero_paladin_name", "Paladin");
        en.put("hero_paladin_name", "Paladin");
        es.put("hero_paladin_name", "Paladín");

        tr.put("hero_paladin_desc", "Kutsal gücü sayesinde her tur canını biraz yeniler.");
        en.put("hero_paladin_desc", "Regenerates some health every turn with holy power.");
        es.put("hero_paladin_desc", "Regenera algo de salud cada turno con su poder sagrado.");

        tr.put("hero_paladin_lore", "Işığın yorulmaz savaşçısı. Darbe aldıkça güçlenir ve inancıyla yaralarını sarar.");
        en.put("hero_paladin_lore", "Tireless warrior of light. Gets stronger as he takes hits and heals his wounds with faith.");
        es.put("hero_paladin_lore", "Guerrero incansable de la luz. Se hace más fuerte al recibir golpes y cura sus heridas con fe.");
        tr.put("start_adventure", "MACERAYA BAŞLA");
        en.put("start_adventure", "START ADVENTURE");
        es.put("start_adventure", "EMPEZAR AVENTURA");

        // Riddles
        tr.put("riddle_0_q", "\"Sabah 4 ayaklı, öğlen 2 ayaklı, akşam 3 ayaklı olan nedir?\"");
        tr.put("riddle_0_a", "insan,i̇nsan");
        en.put("riddle_0_q", "\"What walks on four legs in the morning, two legs at noon, and three legs in the evening?\"");
        en.put("riddle_0_a", "human,man,person");
        es.put("riddle_0_q", "\"¿Qué camina sobre cuatro patas por la mañana, dos al mediodía y tres por la noche?\"");
        es.put("riddle_0_a", "humano,hombre,persona");

        tr.put("riddle_1_q", "\"Beni kırarsan büyürüm, bana dokunursan ölürüm. Neyim?\"");
        tr.put("riddle_1_a", "sessizlik,sessızlık");
        en.put("riddle_1_q", "\"If you break me I grow, if you touch me I die. What am I?\"");
        en.put("riddle_1_a", "silence");
        es.put("riddle_1_q", "\"Si me rompes crezco, si me tocas muero. ¿Qué soy?\"");
        es.put("riddle_1_a", "silencio");

        tr.put("riddle_2_q", "\"Herkes beni alır ama kimse geri getiremez. Neyim?\"");
        tr.put("riddle_2_a", "zaman,vakit");
        en.put("riddle_2_q", "\"Everyone takes me but no one can bring me back. What am I?\"");
        en.put("riddle_2_a", "time");
        es.put("riddle_2_q", "\"Todos me toman pero nadie puede devolverme. ¿Qué soy?\"");
        es.put("riddle_2_a", "tiempo");

        tr.put("riddle_3_q", "\"Ne kadar çok alırsan, arkanda o kadar çok bırakırsın. Neyim?\"");
        tr.put("riddle_3_a", "adım,adim,ayak izi");
        en.put("riddle_3_q", "\"The more you take, the more you leave behind. What am I?\"");
        en.put("riddle_3_a", "step,footstep,steps");
        es.put("riddle_3_q", "\"Cuanto más tomas, más dejas atrás. ¿Qué soy?\"");
        es.put("riddle_3_a", "paso,pasos,huella");

        tr.put("riddle_4_q", "\"Gözüm var göremem, ağzım var konuşamam. Neyim?\"");
        tr.put("riddle_4_a", "resim,portre,tablo,fotoğraf");
        en.put("riddle_4_q", "\"I have eyes but cannot see, I have a mouth but cannot speak. What am I?\"");
        en.put("riddle_4_a", "picture,portrait,painting,photo");
        es.put("riddle_4_q", "\"Tengo ojos pero no veo, tengo boca pero no hablo. ¿Qué soy?\"");
        es.put("riddle_4_a", "foto,retrato,pintura,cuadro");

        tr.put("floor_1", "Zindan Girişi");
        en.put("floor_1", "Dungeon Entrance");
        es.put("floor_1", "Entrada de la Mazmorra");

        tr.put("floor_2", "Goblin Yuvası");
        en.put("floor_2", "Goblin Nest");
        es.put("floor_2", "Nido de Duendes");

        tr.put("floor_3", "Zehir Bataklığı");
        en.put("floor_3", "Poison Swamp");
        es.put("floor_3", "Pantano Venenoso");

        tr.put("floor_4", "Karanlık Salon");
        en.put("floor_4", "Dark Hall");
        es.put("floor_4", "Salón Oscuro");

        tr.put("floor_5", "Dövme Odası");
        en.put("floor_5", "Forge Room");
        es.put("floor_5", "Sala de Forja");

        tr.put("floor_6", "Ruhlar Koridoru");
        en.put("floor_6", "Corridor of Souls");
        es.put("floor_6", "Pasillo de las Almas");

        tr.put("floor_7", "Cehennem Kapısı");
        en.put("floor_7", "Hell's Gate");
        es.put("floor_7", "Puerta del Infierno");

        tr.put("floor_8", "Ölü Tapınak");
        en.put("floor_8", "Dead Temple");
        es.put("floor_8", "Templo Muerto");

        tr.put("floor_9", "Ejder Mağarası");
        en.put("floor_9", "Dragon Cave");
        es.put("floor_9", "Cueva del Dragón");

        tr.put("floor_10", "ZIRVE - Ejderhanın İni");
        en.put("floor_10", "PEAK - Dragon's Lair");
        es.put("floor_10", "CIMA - Guarida del Dragón");

        // Enemies
        tr.put("enemy_1", "İskelet Savaşçı");
        en.put("enemy_1", "Skeleton Warrior");
        es.put("enemy_1", "Guerrero Esqueleto");

        tr.put("enemy_2", "Goblin Akıncı");
        en.put("enemy_2", "Goblin Raider");
        es.put("enemy_2", "Incursor Duende");

        tr.put("enemy_3", "Toksik Balçık");
        en.put("enemy_3", "Toxic Slime");
        es.put("enemy_3", "Limo Tóxico");

        tr.put("enemy_4", "Vampir Lord");
        en.put("enemy_4", "Vampire Lord");
        es.put("enemy_4", "Señor Vampiro");

        tr.put("enemy_5", "Taş Golem");
        en.put("enemy_5", "Stone Golem");
        es.put("enemy_5", "Gólem de Piedra");

        tr.put("enemy_6", "Hayalet Wraith");
        en.put("enemy_6", "Ghost Wraith");
        es.put("enemy_6", "Espectro Fantasma");

        tr.put("enemy_7", "Büyük Şeytan");
        en.put("enemy_7", "Greater Demon");
        es.put("enemy_7", "Gran Demonio");

        tr.put("enemy_8", "Lich Kral");
        en.put("enemy_8", "Lich King");
        es.put("enemy_8", "Rey Exánime");

        tr.put("enemy_9", "Üç Başlı Hidra");
        en.put("enemy_9", "Three-Headed Hydra");
        es.put("enemy_9", "Hidra de Tres Cabezas");

        tr.put("enemy_10", "Kadim Ejderha");
        en.put("enemy_10", "Ancient Dragon");
        es.put("enemy_10", "Dragón Antiguo");

        // Cinematic
        tr.put("cinematic_hero", "Cesur");
        en.put("cinematic_hero", "Brave");
        es.put("cinematic_hero", "Valiente");

        tr.put("cinematic_line1", "Kulenin lanetini kırmak için yola koyuldun.");
        en.put("cinematic_line1", "You set out to break the tower's curse.");
        es.put("cinematic_line1", "Te propusiste romper la maldición de la torre.");

        tr.put("cinematic_line2", "Karanlık kuleye ulaştın ve kapıdan içeri adım attın.");
        en.put("cinematic_line2", "You reached the dark tower and stepped inside.");
        es.put("cinematic_line2", "Llegaste a la torre oscura y entraste.");

        tr.put("cinematic_line3", "Girdiğin an ağır demir kapı arkandan sertçe kapandı!");
        en.put("cinematic_line3", "The heavy iron door slammed shut behind you!");
        es.put("cinematic_line3", "¡La pesada puerta de hierro se cerró de golpe detrás de ti!");

        tr.put("cinematic_line4", "Gölgelerin içinden yaşlı bir muhafız belirdi:");
        en.put("cinematic_line4", "An old guard emerged from the shadows:");
        es.put("cinematic_line4", "Un viejo guardia surgió de las sombras:");

        tr.put("cinematic_line5", "\"Geçmek istiyorsan bilmecemi çöz!\"");
        en.put("cinematic_line5", "\"Solve my riddle if you wish to pass!\"");
        es.put("cinematic_line5", "\"¡Resuelve mi acertijo si deseas pasar!\"");

        tr.put("answer_btn", "Cevapla");
        en.put("answer_btn", "Answer");
        es.put("answer_btn", "Responder");

        tr.put("your_answer", "Cevabın: ");
        en.put("your_answer", "Your Answer: ");
        es.put("your_answer", "Tu Respuesta: ");

        tr.put("time_out_title", "Zaman Tükendi!");
        en.put("time_out_title", "Time Out!");
        es.put("time_out_title", "¡Tiempo Agotado!");

        tr.put("time_out_desc", "Süre Doldu!\nMuhafız seni geriye itti!\n(-10 HP ile başlıyorsun)");
        en.put("time_out_desc", "Time is up!\nThe guard pushed you back!\n(Starting with -10 HP)");
        es.put("time_out_desc", "¡Se acabó el tiempo!\n¡El guardia te empujó hacia atrás!\n(Empiezas con -10 HP)");

        tr.put("correct_ans_title", "Doğru Cevap!");
        en.put("correct_ans_title", "Correct Answer!");
        es.put("correct_ans_title", "¡Respuesta Correcta!");

        tr.put("correct_ans_desc", "Muhafız gülümsedi:\n\"Geçebilirsin,");
        en.put("correct_ans_desc", "The guard smiled:\n\"You may pass,");
        es.put("correct_ans_desc", "El guardia sonrió:\n\"Puedes pasar,");

        tr.put("wrong_ans_title", "Yanlış Cevap!");
        en.put("wrong_ans_title", "Wrong Answer!");
        es.put("wrong_ans_title", "¡Respuesta Incorrecta!");

        tr.put("wrong_ans_desc", "Muhafız başını salladı:\n\"Yanlış! Ama yine de girebilirsin...\"\n(-10 HP)");
        en.put("wrong_ans_desc", "The guard shook his head:\n\"Wrong! But you may still enter...\"\n(-10 HP)");
        es.put("wrong_ans_desc", "El guardia negó con la cabeza:\n\"¡Incorrecto! Pero aún así puedes entrar...\"\n(-10 HP)");

        // Common
        tr.put("cost", "Bedel");
        en.put("cost", "Cost");
        es.put("cost", "Costo");
        
        // Cards
        tr.put("card_quickStrike", "Hızlı Hücum");
        en.put("card_quickStrike", "Quick Strike");
        es.put("card_quickStrike", "Ataque Rápido");
        tr.put("card_quickStrike_desc", "3 hasar ver. Bedelsiz.");
        en.put("card_quickStrike_desc", "Deal 3 damage. Costs 0.");
        es.put("card_quickStrike_desc", "Inflige 3 daño. Cuesta 0.");

        tr.put("card_tacticalDefense", "Taktik Savunma");
        en.put("card_tacticalDefense", "Tactical Defense");
        es.put("card_tacticalDefense", "Defensa Táctica");
        tr.put("card_tacticalDefense_desc", "4 zırh kazan. 1 kart çek.");
        en.put("card_tacticalDefense_desc", "Gain 4 armor. Draw 1 card.");
        es.put("card_tacticalDefense_desc", "Gana 4 armadura. Roba 1 carta.");

        tr.put("card_poisonDart", "Zehirli Dart");
        en.put("card_poisonDart", "Poison Dart");
        es.put("card_poisonDart", "Dardo Venenoso");
        tr.put("card_poisonDart_desc", "1 hasar + 2 zehir ver.");
        en.put("card_poisonDart_desc", "Deal 1 damage + 2 poison.");
        es.put("card_poisonDart_desc", "1 daño + 2 veneno.");

        tr.put("card_prepWork", "Ön Hazırlık");
        en.put("card_prepWork", "Prep Work");
        es.put("card_prepWork", "Preparación");
        tr.put("card_prepWork_desc", "2 kart çek.");
        en.put("card_prepWork_desc", "Draw 2 cards.");
        es.put("card_prepWork_desc", "Roba 2 cartas.");

        tr.put("card_strike", "Kılıç Vuruşu");
        en.put("card_strike", "Sword Strike");
        es.put("card_strike", "Golpe de Espada");
        tr.put("card_strike_desc", "6 hasar ver.");
        en.put("card_strike_desc", "Deal 6 damage.");
        es.put("card_strike_desc", "Inflige 6 daño.");

        tr.put("card_heavyStrike", "Ağır Darbe");
        en.put("card_heavyStrike", "Heavy Strike");
        es.put("card_heavyStrike", "Golpe Pesado");
        tr.put("card_heavyStrike_desc", "14 hasar ver.");
        en.put("card_heavyStrike_desc", "Deal 14 damage.");
        es.put("card_heavyStrike_desc", "Inflige 14 daño.");

        tr.put("card_doubleSlash", "Çift Kesim");
        en.put("card_doubleSlash", "Double Slash");
        es.put("card_doubleSlash", "Doble Corte");
        tr.put("card_doubleSlash_desc", "2x4 hasar ver.");
        en.put("card_doubleSlash_desc", "Deal 2x4 damage.");
        es.put("card_doubleSlash_desc", "Inflige 2x4 daño.");

        tr.put("card_poisonBlade", "Zehirli Bıçak");
        en.put("card_poisonBlade", "Poison Blade");
        es.put("card_poisonBlade", "Hoja Venenosa");
        tr.put("card_poisonBlade_desc", "4 hasar + 3 zehir.");
        en.put("card_poisonBlade_desc", "4 damage + 3 poison.");
        es.put("card_poisonBlade_desc", "4 daño + 3 veneno.");

        tr.put("card_fireball", "Ateş Topu");
        en.put("card_fireball", "Fireball");
        es.put("card_fireball", "Bola de Fuego");
        tr.put("card_fireball_desc", "12 hasar ver.");
        en.put("card_fireball_desc", "Deal 12 damage.");
        es.put("card_fireball_desc", "Inflige 12 daño.");

        tr.put("card_inferno", "Cehennem Alevi");
        en.put("card_inferno", "Inferno");
        es.put("card_inferno", "Infierno");
        tr.put("card_inferno_desc", "20 hasar ver.");
        en.put("card_inferno_desc", "Deal 20 damage.");
        es.put("card_inferno_desc", "Inflige 20 daño.");
        
        tr.put("card_block", "Demir Kalkan");
        en.put("card_block", "Iron Shield");
        es.put("card_block", "Escudo de Hierro");
        tr.put("card_block_desc", "5 zırh kazan.");
        en.put("card_block_desc", "Gain 5 armor.");
        es.put("card_block_desc", "Gana 5 armadura.");

        tr.put("card_fortify", "Tahkimat");
        en.put("card_fortify", "Fortify");
        es.put("card_fortify", "Fortificar");
        tr.put("card_fortify_desc", "12 zırh kazan.");
        en.put("card_fortify_desc", "Gain 12 armor.");
        es.put("card_fortify_desc", "Gana 12 armadura.");

        tr.put("card_heal", "İyileşme");
        en.put("card_heal", "Heal");
        es.put("card_heal", "Curar");
        tr.put("card_heal_desc", "8 can iyileştir.");
        en.put("card_heal_desc", "Heal 8 HP.");
        es.put("card_heal_desc", "Cura 8 HP.");
        
        tr.put("card_holyLight", "Kutsal Işık");
        en.put("card_holyLight", "Holy Light");
        es.put("card_holyLight", "Luz Sagrada");
        tr.put("card_holyLight_desc", "15 can iyileştir.");
        en.put("card_holyLight_desc", "Heal 15 HP.");
        es.put("card_holyLight_desc", "Cura 15 HP.");

        tr.put("card_vampiric", "Kan Emici");
        en.put("card_vampiric", "Vampiric");
        es.put("card_vampiric", "Vampírico");
        tr.put("card_vampiric_desc", "8 hasar, 5 HP.");
        en.put("card_vampiric_desc", "8 damage, 5 heal.");
        es.put("card_vampiric_desc", "8 daño, 5 cura.");

        tr.put("card_shieldBash", "Kalkan Vuruşu");
        en.put("card_shieldBash", "Shield Bash");
        es.put("card_shieldBash", "Golpe de Escudo");
        tr.put("card_shieldBash_desc", "3 zırh + 3 hasar.");
        en.put("card_shieldBash_desc", "Gain 3 armor, deal 3 dmg.");
        es.put("card_shieldBash_desc", "3 armadura, 3 daño.");

        // Shop Translations
        tr.put("shop_title", "KULE MARKETİ");
        en.put("shop_title", "TOWER SHOP");
        es.put("shop_title", "TIENDA DE LA TORRE");

        tr.put("gold_text", "Altın: ");
        en.put("gold_text", "Gold: ");
        es.put("gold_text", "Oro: ");

        tr.put("leave_shop", "Sonraki Kat ➔");
        en.put("leave_shop", "Next Floor ➔");
        es.put("leave_shop", "Siguiente Piso ➔");

        tr.put("heal_potion", "Can İksiri (+25 HP)");
        en.put("heal_potion", "Health Potion (+25 HP)");
        es.put("heal_potion", "Poción de Vida (+25 HP)");

        tr.put("heal_potion_desc", "Mevcut canınızı 25 puan yeniler.");
        en.put("heal_potion_desc", "Restores 25 of your current HP.");
        es.put("heal_potion_desc", "Restaura 25 de tu HP actual.");

        tr.put("energy_upgrade", "Enerji Yükseltme (+1 Enerji)");
        en.put("energy_upgrade", "Energy Upgrade (+1 Energy)");
        es.put("energy_upgrade", "Aumento de Energía (+1 Energía)");

        tr.put("energy_upgrade_desc", "Maksimum enerjinizi kalıcı olarak 1 artırır.");
        en.put("energy_upgrade_desc", "Permanently increases your max energy by 1.");
        es.put("energy_upgrade_desc", "Aumenta permanentemente tu energía máxima en 1.");

        tr.put("insufficient_gold", "Yetersiz Altın!");
        en.put("insufficient_gold", "Insufficient Gold!");
        es.put("insufficient_gold", "¡Oro Insuficiente!");

        tr.put("bought", "Satıldı");
        en.put("bought", "Sold");
        es.put("bought", "Vendido");

        tr.put("gold_earned_msg", "Kazanılan Altın: +%d Altın!");
        en.put("gold_earned_msg", "Gold Earned: +%d Gold!");
        es.put("gold_earned_msg", "¡Oro Ganado: +%d Oro!");

        tr.put("shop_welcome", "Kadim tüccar fısıldar: 'Altın karşılığı güç veririm...'");
        en.put("shop_welcome", "The ancient merchant whispers: 'Power for gold...'");
        es.put("shop_welcome", "El mercader antiguo susurra: 'Poder por oro...'");

        tr.put("shop_restore_hp", "Canı Yeniler");
        en.put("shop_restore_hp", "Restore HP");
        es.put("shop_restore_hp", "Restaura HP");

        tr.put("shop_energy_boost", "Enerji Artışı");
        en.put("shop_energy_boost", "Energy Boost");
        es.put("shop_energy_boost", "Aumento de Energía");

        tr.put("shop_max_energy_plus_1", "Max Enerji +1");
        en.put("shop_max_energy_plus_1", "Max Energy +1");
        es.put("shop_max_energy_plus_1", "Máx. Energía +1");

        // Save System Translations
        tr.put("save_title", "OYUNU KAYDET");
        en.put("save_title", "SAVE GAME");
        es.put("save_title", "GUARDAR JUEGO");

        tr.put("load_title", "OYUNU YÜKLE");
        en.put("load_title", "LOAD GAME");
        es.put("load_title", "CARGAR JUEGO");

        tr.put("save_slot", "Kayıt Yuvası");
        en.put("save_slot", "Save Slot");
        es.put("save_slot", "Ranura de Guardado");

        tr.put("delete_btn", "SİL");
        en.put("delete_btn", "DELETE");
        es.put("delete_btn", "ELIMINAR");

        tr.put("delete_confirm", "Bu kayıt dosyasını kalıcı olarak silmek istediğinize emin misiniz?");
        en.put("delete_confirm", "Are you sure you want to permanently delete this save file?");
        es.put("delete_confirm", "¿Estás seguro de que quieres eliminar permanentemente este archivo de guardado?");

        tr.put("autosave_notif", "OTOMATİK KAYDEDİLİYOR...");
        en.put("autosave_notif", "AUTOSAVING...");
        es.put("autosave_notif", "GUARDANDO AUTOMÁTICAMENTE...");

        tr.put("quicksave_notif", "HIZLI KAYDEDİLDİ");
        en.put("quicksave_notif", "QUICK SAVED");
        es.put("quicksave_notif", "GUARDADO RÁPIDO");

        tr.put("quickload_notif", "HIZLI YÜKLENDİ");
        en.put("quickload_notif", "QUICK LOADED");
        es.put("quickload_notif", "CARGADO RÁPIDO");

        tr.put("play_time", "Oynama Süresi: ");
        en.put("play_time", "Play Time: ");
        es.put("play_time", "Tiempo de Juego: ");

        tr.put("last_saved", "Son Kayıt: ");
        en.put("last_saved", "Last Saved: ");
        es.put("last_saved", "Último Guardado: ");

        tr.put("empty_slot", "[ Boş Yuva ]");
        en.put("empty_slot", "[ Empty Slot ]");
        es.put("empty_slot", "[ Ranura Vacía ]");

        tr.put("pause_menu", "OYUN DURDURULDU");
        en.put("pause_menu", "GAME PAUSED");
        es.put("pause_menu", "JUEGO PAUSADO");

        tr.put("resume_btn", "OYUNA DÖN");
        en.put("resume_btn", "RESUME");
        es.put("resume_btn", "REANUDAR");

        tr.put("exit_menu_btn", "ANA MENÜYE DÖN");
        en.put("exit_menu_btn", "MAIN MENU");
        es.put("exit_menu_btn", "MENÚ PRINCIPAL");

        tr.put("unsaved_warning", "Kaydedilmemiş ilerlemeniz kaybolacak. Çıkmak istiyor musunuz?");
        en.put("unsaved_warning", "Your unsaved progress will be lost. Do you want to exit?");
        es.put("unsaved_warning", "Tu progreso no guardado se perderá. ¿Quieres salir?");

        tr.put("save_and_exit_btn", "KAYDET VE ÇIK");
        en.put("save_and_exit_btn", "SAVE & EXIT");
        es.put("save_and_exit_btn", "GUARDAR Y SALIR");

        tr.put("no_quicksave", "Hızlı kayıt bulunamadı! F5 ile kaydedin.");
        en.put("no_quicksave", "No quicksave found! Save with F5.");
        es.put("no_quicksave", "No se encontró guardado rápido. Guarda con F5.");

        dictionary.put("TR", tr);
        dictionary.put("EN", en);
        dictionary.put("ES", es);
    }
}
