# SouthPaw — Card Game

> powered by Lucas Lourenço

A turn-based card battle game written in Java. Two players summon creature cards called **Paws**, spend Elixir to place and power them up, and fight to drain the opponent's Elixir to zero.

---

## Table of Contents

- [Game Mechanics](#game-mechanics)
- [Architecture](#architecture)
- [Package Overview](#package-overview)
- [Classes & Responsibilities](#classes--responsibilities)
  - [Contracts (Interfaces)](#contracts-interfaces)
  - [PawCard (Base)](#pawcard-base)
  - [Characters](#characters)
  - [Powers](#powers)
  - [Users](#users)
  - [Spells](#spells)
  - [Service Layer](#service-layer)
- [Game Flow](#game-flow)
- [How to Run](#how-to-run)
- [Bug Fixes Applied](#bug-fixes-applied)
- [Roadmap](#roadmap)
- [Evaluation](#evaluation)

---

## Game Mechanics

| Concept              | Description                                                                                                 |
| -------------------- | ----------------------------------------------------------------------------------------------------------- |
| **Elixir**           | Each player starts with **10 Elixir**. Summoning a card or using a power costs Elixir.                      |
| **Losing Condition** | A player loses when their Elixir reaches **0**.                                                             |
| **Direct Attack**    | When an enemy Paw attacks the player directly, its Elixir Cost is deducted from the target player's Elixir. |
| **Card (Paw)**       | Each Paw has HP, Attack, Agility, Rarity and an Elixir Cost.                                                |
| **Agility**          | Determines action order. Higher Agility = more actions per round and priority in turn order.                |
| **Powers**           | Some Paws implement `hasPower` and can activate a special ability (spending additional Elixir).             |

---

## Architecture

```
src/
├── models/
│   ├── contracts/
│   │   ├── Card.java           # Interface: all cards must implement
│   │   └── HasPower.java       # Interface: cards with activatable power
│   ├── characters/
│   │   ├── pawbase/
│   │   │   ├── PawCard.java    # Abstract base for all Paw cards
│   │   │   └── ManagePaws.java # Registry of cards available for invocation
│   │   ├── Pawarrior.java      # Tank - Rage power (doubles attack)
│   │   ├── Pawbayle.java       # Balanced - Tanker power (HP +20%)
│   │   ├── Pawclown.java       # Glass cannon - Doppelgangers power (clone itself)
│   │   └── Pawskeleton.java    # Fast & cheap - power stub
│   ├── powerEnviroment/
│   │   ├── PowerSkill.java     # Abstract: handles elixir check + deduction
│   │   ├── Rage.java           # Doubles baseAttack (stacking-safe)
│   │   ├── Tanker.java         # Increases HP by 20% (updates maxLife too)
│   │   └── Doppelgangers.java  # Clones the card on the field for free
│   ├── spells/
│   │   └── SpellCard.java      # Stub for future spell cards
│   └── users/
│       ├── User.java           # Player: name, elixir, active paws
│       └── Statistic.java      # Tracks damage, kills, powers used
└── service/
    ├── battle/
    │   ├── against/
    │   │   └── Battle.java     # ★ Main entry point: local 2-player game
    │   └── simulated/
    │       └── simulatedBattle1v1.java  # Dev test scenario (no input needed)
    └── event/
        ├── By.java             # Enum: AGILITY | ATTACK
        ├── EventDeal.java      # Turn menus, attack logic, game-over detection
        ├── PartyDetection.java # Maps users → their active cards
        └── ShiftDeal.java      # Turn order calculation by Agility
```

---

## Package Overview

| Package                     | Role                                                             |
| --------------------------- | ---------------------------------------------------------------- |
| `models.contracts`          | Define the **Card** and **hasPower** contracts                   |
| `models.characters.pawbase` | `PawCard` abstract base class; `managePaws` card registry        |
| `models.characters`         | Concrete Paw implementations                                     |
| `models.powerEnviroment`    | `PowerSkill` abstract class and concrete powers                  |
| `models.spells`             | Stub for future spell cards                                      |
| `models.users`              | `User` (name, elixir, active paws) and `statistic` (placeholder) |
| `service.event`             | Game event handling, menus, turn ordering                        |
| `service.battle`            | Entry points: interactive vs. simulated battle                   |

---

## Classes & Responsibilities

### Contracts (Interfaces)

#### `Card` — `models/contracts/Card.java`

Defines the common interface for every card in the game.

| Method                    | Purpose                                                   |
| ------------------------- | --------------------------------------------------------- |
| `getAttack()`             | Returns the card's attack value                           |
| `getName()`               | Returns name or nickname                                  |
| `getElixirCost()`         | Returns the cost to summon                                |
| `getUser()`               | Returns the owning User                                   |
| `getAgility()`            | Returns agility (affects turn order)                      |
| `exportInfo()`            | Returns a formatted single-line summary                   |
| `getCardDetails(boolean)` | Returns full card details, optionally printing to console |
| `setCardDetails()`        | Called by constructor to set stats                        |

#### `hasPower` — `models/contracts/hasPower.java`

Marker interface for cards that have an activatable power.

```java
void usePower();
```

---

### PawCard (Base) — `models/characters/pawbase/PawCard.java`

Abstract base class that all creature cards extend. Implements `Card`.

**Fields:**

| Field        | Type    | Description                                           |
| ------------ | ------- | ----------------------------------------------------- |
| `name`       | String  | Set to the class simple name automatically            |
| `nickName`   | String  | Optional (max 15 chars); overrides `name` in display  |
| `attack`     | int     | Damage dealt per hit                                  |
| `agility`    | int     | Determines turn order and number of actions per round |
| `life`       | int     | Current HP                                            |
| `maxLife`    | int     | Maximum HP (set on first `setLife()` call)            |
| `elixirCost` | int     | Cost to place card on the field                       |
| `rarity`     | int     | 1–5 scale                                             |
| `alive`      | boolean | Whether the card is still alive                       |
| `onTheField` | boolean | Whether the card has been summoned                    |

**Key Methods:**

| Method                     | Behavior                                                                                              |
| -------------------------- | ----------------------------------------------------------------------------------------------------- |
| `positionateCard()`        | Checks Elixir, deducts cost, sets card as on-field and alive, adds to owner's `pawUnderControl`       |
| `attackEnemy(PawCard)`     | Deducts attacker's `attack` from enemy's life; `dyingState()` is triggered internally via `setLife()` |
| `receiveDamage(Card)`      | Deducts attacker's `attack` from own life via `setLife()` (which triggers `dyingState()`)             |
| `dyingState()` _(private)_ | If `life <= 0`: sets `alive=false`, removes from `pawUnderControl`, prints death message              |
| `setLife(int)`             | Sets HP; if `maxLife == 0` sets it as well; then calls `dyingState()`                                 |
| `getCardDetails(boolean)`  | Human-readable block with HP, attack, agility, rarity, elixir cost                                    |
| `exportInfo()`             | Compact table-row format for the battle state display                                                 |

**Constructors:**

- `PawCard(User user)` — places card under the user with auto-named name.
- `PawCard(User user, String nickname)` — same but with a nickname (≤15 chars).

---

### Characters

#### `Pawarrior` — Rarity 4 / Elixir 4

High HP tank with the **Rage** power.

| Stat        | Value                   |
| ----------- | ----------------------- |
| HP          | 3000                    |
| Attack      | 300                     |
| Agility     | 2                       |
| Elixir Cost | 4                       |
| Power       | `rage` — doubles attack |

#### `Pawbayle` — Rarity 3 / Elixir 3

Balanced mid-tier card. Power not yet implemented.

| Stat    | Value |
| ------- | ----- |
| HP      | 1800  |
| Attack  | 200   |
| Agility | 3     |

#### `Pawskeleton` — Rarity 2 / Elixir 2

Fast and cheap; low HP but high agility.

| Stat    | Value |
| ------- | ----- |
| HP      | 600   |
| Attack  | 100   |
| Agility | 5     |

#### `Pawclown` — Rarity ? / Elixir ?

Placeholder card. No stats defined yet (all 0). Awaiting implementation.

---

### Powers — `models/powerEnviroment/`

#### `PowerSkill` (abstract)

Base class for all powers. Holds `user`, `card`, and `elixirCost`.

`Use()` — validates and deducts the Elixir cost, then calls `powerRule()`.

| Concrete Class  | Effect                                   |
| --------------- | ---------------------------------------- |
| `rage`          | Doubles the card's Attack (`attack * 2`) |
| `tanker`        | Increases current HP and maxLife by 20%  |
| `doppelgangers` | _(Not implemented — stub)_               |

> `Power.java` — legacy/orphaned class, not connected to the current power system.

---

### Users — `models/users/`

#### `User`

Represents a player.

| Field             | Default | Description                       |
| ----------------- | ------- | --------------------------------- |
| `name`            | —       | Player name                       |
| `elixir`          | 10      | Available Elixir pool             |
| `pawUnderControl` | `[]`    | Live cards currently on the field |
| `statistic`       | new     | Statistics tracker (stub)         |

#### `statistic`

Tracks `pawsDefeated` and `totalDamageInflicted` per user. Currently not updated by the battle logic.

---

### Spells — `models/spells/spellCard.java`

Abstract stub that implements `Card`. No concrete spells implemented yet.

---

### Service Layer

#### `By` (enum)

Ordering criteria: `AGILITY`, `ATTACK`.

#### `PartyDetection`

Builds a `Map<User, List<Card>>` snapshot of each player's active cards at battle start.

#### `shiftDeal`

Calculates turn order based on card stats.

| Method                                | Purpose                                                                                                                                                                                     |
| ------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `iterThroughtBy(By)`                  | Returns all cards sorted by the given criterion (descending)                                                                                                                                |
| `actionOrderingByAgility(List<Card>)` | Expands turn list: higher agility cards act more often. A card with agility 5 and min-agility 2 acts ~2–3 times per cycle. Cards with the same agility group are shuffled for tie-breaking. |
| `shuffleActionOrder(List<Card>)`      | Groups by agility level and randomizes within each group                                                                                                                                    |

**Agility Algorithm:**
Each card acts `ceil(ownAgility / minAgility)` times per cycle. Example: agility=5 vs min=2 → acts twice (5→3→1, added twice). Cards are then shuffled within agility tiers.

#### `eventDeal`

Central event/menu handler.

| Method                        | Purpose                                                                                         |
| ----------------------------- | ----------------------------------------------------------------------------------------------- |
| `inicializationFirstEvent()`  | Prints game banner on startup                                                                   |
| `insertUsers(User...)`        | Registers users for this event session                                                          |
| `generalBattleState()`        | Prints a formatted table showing each player's Elixir and active Paws                           |
| `FirstMenu()`                 | Pre-battle setup loop per user: choose paws to invoke, review state                             |
| `ChooseMonsterToInvoke(User)` | Shows available paws (from `managePaws`), reads choice via Scanner, instantiates via reflection |
| `ChooseMenu(User)`            | _(Stub)_ — intended as the per-turn action menu                                                 |

#### `managePaws`

Registry of cards available for invocation. Currently lists: `Pawarrior`, `Pawskeleton`, `Pawclown`.

#### `battle` — Interactive Entry Point

Reads player names, runs `FirstMenu`, then iterates the game loop until a player's Elixir reaches 0.

#### `simulatedBattle1v1` — Automated Test Scenario

Hard-coded battle between two users (Lucas vs. Fulano). Useful for testing the agility ordering and `generalBattleState()` display without interactive input.

---

## Game Flow

```
battle.main()
  │
  ├─ Read player names
  ├─ Create User1, User2
  ├─ PartyDetection.insertUsers()
  ├─ eventDeal.insertUsers()
  │
  ├─ FirstMenu() [per user]
  │   └─ Loop:
  │       ├─ A) ChooseMonsterToInvoke → PawCard.positionateCard()
  │       └─ B) generalBattleState()
  │
  └─ Game Loop [until a player's elixir <= 0]
      └─ ChooseMenu(user) [per user — stub, to be implemented]
```

---

## How to Run

> O projeto usa **Gradle** com **LibGDX** (backend LWJGL3). É necessário Java 17+ instalado.

### Pré-requisitos

| Requisito | Versão testada | Link                                              |
| --------- | -------------- | ------------------------------------------------- |
| Java JDK  | 21.0.9 LTS     | https://adoptium.net/temurin/releases/?version=21 |

O Gradle Wrapper já está incluso no repositório — não é preciso instalar o Gradle separadamente.

### Inicializar o jogo

```powershell
# Dentro da pasta "Card Game"
cd "Card Game"

# Compila e abre a janela do jogo (1600 × 950 px, 60 fps)
.\gradlew.bat desktop:run
```

A janela é criada pela seguinte linha em `DesktopLauncher.java`:

```java
new Lwjgl3Application(new SouthPawGame(), config);
```

### Apenas compilar (sem abrir)

```powershell
.\gradlew.bat desktop:compileJava
```

### Configurações da janela (DesktopLauncher.java)

| Parâmetro | Valor         |
| --------- | ------------- |
| Resolução | 1600 × 950 px |
| FPS alvo  | 60            |
| VSync     | Ativado       |
| Título    | SouthPaw      |

### Estrutura de assets

As artes das cartas devem estar em `assets/cards/` com o nome exato da classe Java:

```
assets/
└── cards/
    ├── Pawarrior.png
    ├── Pawbayle.png
    ├── Pawclown.png
    └── Pawskeleton.png
```

---

## Bug Fixes Applied

| #   | File               | Bug                                                                                                                                             | Fix                                                                                       |
| --- | ------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| 1   | `Pawbayle.java`    | `setCardDetails()` threw `UnsupportedOperationException` on construction (instant crash)                                                        | Implemented stats: HP 1800, Attack 200, Agility 3, Rarity 3, Elixir 3                     |
| 2   | `Pawskeleton.java` | `usePower()` threw `UnsupportedOperationException`                                                                                              | Replaced with empty stub (TODO)                                                           |
| 3   | `PawCard.java`     | `receiveDamage()` called `dyingState()` twice (once via `setLife()`, once explicitly)                                                           | Removed the redundant explicit call                                                       |
| 4   | `PawCard.java`     | No `setMaxLife()` accessor — `tanker` could not update maxLife                                                                                  | Added `setMaxLife(int)`                                                                   |
| 5   | `tanker.java`      | `powerRule()` increased `life` but not `maxLife`; display would show `HP: 3000/3600` (max < current)                                            | Now updates both `maxLife` and `life`                                                     |
| 6   | `PowerSkill.java`  | `Use()` called `powerRule()` without checking or deducting Elixir                                                                               | Added Elixir check + deduction before `powerRule()`                                       |
| 7   | `shiftDeal.java`   | `getMinNumber()` returned `0` when any card had agility 0; in `actionOrderingByAgility`, subtracting 0 from agility caused an **infinite loop** | Rewrote to use `Integer.MAX_VALUE` as initial min, skip zero-agility cards, fallback to 1 |
| 8   | `battle.java`      | `userListage` was never populated with `user1`/`user2`; the game loop iterated over an empty list                                               | Added `userListage.add(user1)` and `userListage.add(user2)`                               |
| 9   | `battle.java`      | `while (!true)` = `while (false)` — the do-while body ran exactly once then immediately exited                                                  | Replaced with a `gameOver` flag loop that checks `user.getElixir() <= 0`                  |

---

## Roadmap

### P0 — Stability & Correctness (Immediate)

- [ ] **Implement `ChooseMenu(User)`** in `eventDeal`: the per-turn action menu (attack, use power, pass). Currently a no-op stub that makes the game loop run forever.
- [ ] **Implement direct player attack**: when no paws block, an attacking Paw should deduct its `elixirCost` from the enemy player's Elixir.
- [ ] **Implement `Pawclown` stats**: currently all stats are 0.
- [ ] **Implement `doppelgangers.powerRule()`**: card duplication logic.
- [ ] **Implement `Pawbayle.usePower()`**: design and assign a power.
- [ ] **Wire `statistic` tracking**: `pawsDefeated` and `totalDamageInflicted` are never updated; hook into `dyingState()` and `attackEnemy()`.

### P1 — Gameplay Completeness

- [ ] **Game-over detection in `shiftDeal`/`battle`**: formally expose a `isGameOver()` method driven by Elixir state.
- [ ] **Rage is permanent and stackable**: activating `rage` multiple times permanently doubles attack each time. Add a `powered` flag or store base attack to prevent stacking.
- [ ] **Add `Deck` concept**: players should have a deck of cards to draw from, not just free-pick from `managePaws`.
- [ ] **Add `spellCard` implementations**: at least one concrete spell (e.g., heal, buff, AOE).
- [ ] **Complete simulation mode**: `simulatedBattle1v1` should run a full auto-battle loop, not just print the state.
- [ ] **Two-Scanner conflict**: `battle.java` and `eventDeal` each create their own `Scanner(System.in)`; pass a single Scanner instance instead.

### P2 — Code Quality & Conventions

- [ ] **Rename classes to PascalCase**: `managePaws` → `ManagePaws`, `statistic` → `Statistic`, `rage` → `Rage`, `tanker` → `Tanker`, `doppelgangers` → `Doppelgangers`, `partyDeal` → `PartyDeal`, `spellCard` → `SpellCard`, `hasPower` → `HasPower`.
- [ ] **Fix package typo**: `powerEnviroment` → `powerEnvironment`.
- [ ] **Remove orphaned `Power.java`**: not connected to the live power system (`PowerSkill` is used); either integrate or delete.
- [ ] **Move `partyDeal` and `ActionBy`**: both are package-private inner classes in another file; they should be their own files or proper inner classes.
- [ ] **Add a build tool** (Maven or Gradle): the current manual `javac` workflow does not scale.

### P3 — Architecture & Features

- [ ] **Save / load game state**: serialize `User` + cards to JSON/file.
- [ ] **Multiplayer over network**: abstract `battle.java` into a session model that supports remote players.
- [ ] **Card Balance System**: introduce a balance config file so stats can be tuned without recompiling.
- [ ] **UI Layer**: migrate the console print blocks to a proper display abstraction (or a simple Swing/JavaFX view).

---

## Evaluation

### State Before Bug Fixes — 4.0 / 10

| Criterion        | Notes                                                                                                                                                                    |
| ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Architecture** | Clean layered design: interfaces → abstract base → concrete cards → service. Good foundation.                                                                            |
| **Correctness**  | 9 bugs, 2 of which cause immediate crashes (`Pawbayle` construction, infinite loop). The interactive game loop was completely broken (empty user list + `while(false)`). |
| **Completeness** | ~40% implemented. Core combat mechanics exist but the interactive loop, spells, statistic tracking, and half the powers are stubs.                                       |
| **Conventions**  | Several class names violate Java PascalCase. Package name has a typo. Two `Scanner` instances on `System.in`.                                                            |
| **Patterns**     | Good use of interfaces, abstract classes, Strategy pattern for powers, reflection for dynamic card instantiation.                                                        |

### State After Bug Fixes — 5.5 / 10

| Criterion        | Notes                                                                                                                                 |
| ---------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| **Architecture** | Unchanged — still solid.                                                                                                              |
| **Correctness**  | All 9 bugs resolved. No more crashes on start. Game loop now runs and terminates correctly. Power system now deducts Elixir properly. |
| **Completeness** | ~45%. The fixes unblock the game from running; the remaining stubs are roadmap items, not bugs.                                       |
| **Conventions**  | Noted in roadmap; not renamed in this pass to avoid breaking all imports.                                                             |
| **Patterns**     | Unchanged — the good patterns remain.                                                                                                 |

> The jump from 4.0 → 5.5 is bounded by the amount of unimplemented features (Deck system, turn loop, spells, `Pawclown`, statistics). A fully implemented game with the existing architecture would score **7.5–8.5 / 10**.
