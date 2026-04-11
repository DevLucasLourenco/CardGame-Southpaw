# Southpaw Card Game — Card Standard (Official)

> This file is the authoritative source of truth for all card definitions,
> mechanics, and creation conventions in the Southpaw Card Game project.
> Every contributor and AI assistant working on this repository must follow
> these rules when creating or modifying any card-related code.

---

## Table of Contents

- [Visual Layout Reference](#visual-layout-reference)
- [Card Types](#card-types)
- [Mandatory Fields](#mandatory-fields)
- [Rarity Scale](#rarity-scale)
- [Stat Guidelines by Rarity](#stat-guidelines-by-rarity)
- [Agility Reference](#agility-reference)
- [Roster — Existing Cards](#roster--existing-cards)
- [Power (PowerSkill) Standard](#power-powerskill-standard)
- [Existing Powers](#existing-powers)
- [Spell Card Standard](#spell-card-standard)
- [Implementation Checklist](#implementation-checklist)
- [Code Conventions](#code-conventions)

---

## Visual Layout Reference

All cards follow this layout — inspired by Yu-Gi-Oh! structure:

```
┌─────────────────────────────────┐
│  ★ ★ ★ ☆ ☆     [RARIDADE]     │  ← 1–5 estrelas (rarity)
│  ┌─────────────────────────┐    │
│  │                         │    │
│  │      [ILUSTRAÇÃO]       │    │
│  │      do personagem      │    │
│  │                         │    │
│  └─────────────────────────┘    │
│  NOME DA CARTA                  │  ← name (ou nickName ≤ 15 chars)
│  Tipo: Paw Monster              │  ← tipo da carta
│  ─────────────────────────────  │
│  [Texto de lore / descrição     │  ← flavour text e descrição do poder
│   do personagem e sua habilidade│
│   única no campo de batalha]    │
│  ─────────────────────────────  │
│  PODER: <NomeDoPoder>           │  ← usePower() — nome da PowerSkill
│  ─────────────────────────────  │
│  ⚡ Elixir: 4                   │  ← elixirCost (custo de invocação)
│  ATK: 300        AGI: 2        │  ← attack / agility
│  HP:  3000 / 3000              │  ← life / maxLife
└─────────────────────────────────┘
```

> **Cartas de Magia** não possuem HP, ATK nem AGI — apenas Elixir e efeito.

---

## Card Types

| Tipo            | Classe Base Java       | Descrição                                                                                |
| --------------- | ---------------------- | ---------------------------------------------------------------------------------------- |
| **Paw Monster** | `PawCard` (abstract)   | Criaturas com HP, ATK, AGI, raridade e poder ativo. Ficam no campo após a invocação.     |
| **Spell Card**  | `SpellCard` (abstract) | Efeito instantâneo. Não ficam no campo. Custo em Elixir + efeito aplicado imediatamente. |

---

## Mandatory Fields

### Paw Monster — campos obrigatórios em `setCardDetails()`

| Campo           | Setter               | Tipo  | Regra                                                           |
| --------------- | -------------------- | ----- | --------------------------------------------------------------- |
| **HP**          | `setLife(int)`       | `int` | Vida inicial e máxima                                           |
| **ATK**         | `setAttack(int)`     | `int` | Dano base por ataque — NUNCA modificar `baseAttack` diretamente |
| **Agility**     | `setAgility(int)`    | `int` | 1 (lento) a 5 (rápido); define prioridade de turno              |
| **Rarity**      | `setRarity(int)`     | `int` | 1 a 5 — ver tabela abaixo                                       |
| **Elixir Cost** | `setElixirCost(int)` | `int` | Custo de invocação                                              |

> `baseAttack` é gerenciado internamente pela `PawCard`. Nunca defina-o diretamente.
> `maxLife` é definido automaticamente na primeira chamada de `setLife()`.

### Paw Monster — `usePower()` obrigatório

Todo `PawCard` que implementa `HasPower` deve:

1. Instanciar uma `PowerSkill` concreta passando `(getUser(), this, getElixirCost())`
2. Chamar `.Use()` — o controle de Elixir é feito dentro de `PowerSkill.Use()`

```java
@Override
public void usePower() {
    PowerSkill power = new NomeDoPoder(getUser(), this, getElixirCost());
    power.Use();
}
```

---

## Rarity Scale

| Estrelas | Tier         | Conceito                                            |
| -------- | ------------ | --------------------------------------------------- |
| ★☆☆☆☆    | **Comum**    | Barato, fácil de invocar, stats baixos              |
| ★★☆☆☆    | **Incomum**  | Custo moderado, poder útil em situações específicas |
| ★★★☆☆    | **Raro**     | Stats equilibrados, poder relevante                 |
| ★★★★☆    | **Épico**    | Alto custo, alto impacto, poder decisivo            |
| ★★★★★    | **Lendário** | Reservado para cartas únicas / boss / event cards   |

---

## Stat Guidelines by Rarity

| Raridade  | HP            | ATK       | AGI   | Elixir Cost |
| --------- | ------------- | --------- | ----- | ----------- |
| ★ (1)     | 400 – 700     | 50 – 150  | 5     | 1 – 2       |
| ★★ (2)    | 700 – 1.200   | 100 – 400 | 4 – 5 | 2 – 3       |
| ★★★ (3)   | 1.200 – 2.200 | 200 – 500 | 3 – 4 | 3 – 4       |
| ★★★★ (4)  | 2.200 – 4.000 | 300 – 700 | 2 – 3 | 4 – 5       |
| ★★★★★ (5) | 4.000+        | 700+      | 1 – 2 | 5 – 7       |

> Cartas com ATK alto devem ter HP proporcialmente baixo (glass cannon).
> Cartas com HP muito alto devem ter ATK e AGI compensatoriamente baixos (tank).

---

## Agility Reference

A agilidade determina **prioridade de turno** e **número de ações por round**
(ver `ShiftDeal.java`).

| AGI | Perfil                                               |
| --- | ---------------------------------------------------- |
| 5   | Ultra-rápido: age primeiro, múltiplas ações          |
| 4   | Rápido                                               |
| 3   | Equilibrado                                          |
| 2   | Lento                                                |
| 1   | Ultra-lento: age por último, tipicamente alto HP/ATK |

---

## Roster — Existing Cards

### Pawskeleton — ★★ Incomum

```
┌─────────────────────────────────┐
│  ★ ★ ☆ ☆ ☆     Incomum        │
│  ┌─────────────────────────┐    │
│  │       [Pawskeleton]     │    │
│  └─────────────────────────┘    │
│  Pawskeleton                    │
│  Tipo: Paw Monster              │
│  ─────────────────────────────  │
│  Um esqueleto felino veloz e    │
│  imprevisível. Aparenta ser     │
│  frágil, mas age antes que      │
│  qualquer adversário perceba.   │
│  ─────────────────────────────  │
│  PODER: — (a ser definido)      │
│  ─────────────────────────────  │
│  ⚡ Elixir: 2                   │
│  ATK: 100        AGI: 5        │
│  HP:  600 / 600                │
└─────────────────────────────────┘
```

| Campo       | Valor                  |
| ----------- | ---------------------- |
| HP          | 600                    |
| ATK         | 100                    |
| AGI         | 5                      |
| Rarity      | 2                      |
| Elixir Cost | 2                      |
| Poder       | _(a ser implementado)_ |
| Arquétipo   | Rápido / Disruptivo    |

---

### Pawclown — ★★★ Raro (Glass Cannon)

```
┌─────────────────────────────────┐
│  ★ ★ ★ ☆ ☆     Raro           │
│  ┌─────────────────────────┐    │
│  │        [Pawclown]       │    │
│  └─────────────────────────┘    │
│  Pawclown                       │
│  Tipo: Paw Monster              │
│  ─────────────────────────────  │
│  Um palhaço felino de alta      │
│  periculosidade. Fragil como    │
│  vidro, mas seu ataque confunde │
│  o oponente — e cria um clone   │
│  perfeito de si mesmo.          │
│  ─────────────────────────────  │
│  PODER: Doppelgangers           │
│  ─────────────────────────────  │
│  ⚡ Elixir: 3                   │
│  ATK: 400        AGI: 4        │
│  HP:  800 / 800                │
└─────────────────────────────────┘
```

| Campo       | Valor                                         |
| ----------- | --------------------------------------------- |
| HP          | 800                                           |
| ATK         | 400                                           |
| AGI         | 4                                             |
| Rarity      | 3                                             |
| Elixir Cost | 3                                             |
| Poder       | **Doppelgangers** — clona a si mesmo no campo |
| Arquétipo   | Glass Cannon / Controle                       |

---

### Pawbayle — ★★★ Raro (Balanced)

```
┌─────────────────────────────────┐
│  ★ ★ ★ ☆ ☆     Raro           │
│  ┌─────────────────────────┐    │
│  │        [Pawbayle]       │    │
│  └─────────────────────────┘    │
│  Pawbayle                       │
│  Tipo: Paw Monster              │
│  ─────────────────────────────  │
│  Um guerreiro felino equilibrado│
│  entre força e resistência.     │
│  Quando ameaçado, endurece seu  │
│  couro e absorve mais dano.     │
│  ─────────────────────────────  │
│  PODER: Tanker                  │
│  ─────────────────────────────  │
│  ⚡ Elixir: 3                   │
│  ATK: 200        AGI: 3        │
│  HP:  1800 / 1800              │
└─────────────────────────────────┘
```

| Campo       | Valor                                          |
| ----------- | ---------------------------------------------- |
| HP          | 1800                                           |
| ATK         | 200                                            |
| AGI         | 3                                              |
| Rarity      | 3                                              |
| Elixir Cost | 3                                              |
| Poder       | **Tanker** — aumenta HP atual e máximo em +20% |
| Arquétipo   | Equilibrado / Defensivo                        |

---

### Pawarrior — ★★★★ Épico (Tank)

```
┌─────────────────────────────────┐
│  ★ ★ ★ ★ ☆     Épico          │
│  ┌─────────────────────────┐    │
│  │       [Pawarrior]       │    │
│  └─────────────────────────┘    │
│  Pawarrior                      │
│  Tipo: Paw Monster              │
│  ─────────────────────────────  │
│  O guerreiro supremo dos felinos│
│  de batalha. Lento como uma     │
│  fortaleza, mas quando sua Fúria│
│  desperta, ninguém sobrevive    │
│  ao segundo golpe.              │
│  ─────────────────────────────  │
│  PODER: Rage                    │
│  ─────────────────────────────  │
│  ⚡ Elixir: 4                   │
│  ATK: 300        AGI: 2        │
│  HP:  3000 / 3000              │
└─────────────────────────────────┘
```

| Campo       | Valor                                                           |
| ----------- | --------------------------------------------------------------- |
| HP          | 3000                                                            |
| ATK         | 300                                                             |
| AGI         | 2                                                               |
| Rarity      | 4                                                               |
| Elixir Cost | 4                                                               |
| Poder       | **Rage** — dobra o ATK (baseado em `baseAttack`, anti-stacking) |
| Arquétipo   | Tank / Ofensivo                                                 |

---

## Power (PowerSkill) Standard

Cada poder é uma classe concreta em `models/powerEnviroment/` que herda `PowerSkill`.

### Regras obrigatórias

1. Construtor recebe `(User user, PawCard card, int elixirCost)` — repassar ao `super()`
2. Implementar `powerRule()` com o efeito da habilidade
3. **Não** verificar Elixir dentro de `powerRule()` — esse controle fica em `PowerSkill.Use()`
4. Efeitos que modificam ATK devem usar `getCard().getBaseAttack()` como base (nunca `getAttack()`) para ser idempotente e resistente a stacking
5. Documentar o arquétipo no Javadoc da classe: `Ofensivo | Defensivo | Controle | Suporte`

### Template de implementação

```java
/**
 * [Descrição breve do poder]
 * Arquétipo: Ofensivo | Defensivo | Controle | Suporte
 */
public class NomeDoPoder extends PowerSkill {

    public NomeDoPoder(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        // efeito aqui
    }
}
```

---

## Existing Powers

### Rage — Arquétipo: Ofensivo

| Campo         | Detalhe                                                       |
| ------------- | ------------------------------------------------------------- |
| Efeito        | Dobra o ATK da carta (`baseAttack × 2`)                       |
| Anti-stacking | Usa `baseAttack` como base — múltiplas ativações não acumulam |
| Usado por     | Pawarrior                                                     |

```java
getCard().setAttack(getCard().getBaseAttack() * 2);
```

---

### Tanker — Arquétipo: Defensivo

| Campo      | Detalhe                                         |
| ---------- | ----------------------------------------------- |
| Efeito     | Aumenta HP atual e `maxLife` em +20%            |
| Observação | Não restaura HP perdido — aplica sobre HP atual |
| Usado por  | Pawbayle                                        |

```java
int HPincreased = (int) (getCard().getLife() * 1.2);
getCard().setMaxLife(HPincreased);
getCard().setLife(HPincreased);
```

---

### Doppelgangers — Arquétipo: Controle / Clone

| Campo      | Detalhe                                                                    |
| ---------- | -------------------------------------------------------------------------- |
| Efeito     | Cria um clone da carta e o posiciona no campo via `forcePositionateCard()` |
| Observação | O clone não gasta Elixir adicional para entrar em campo                    |
| Usado por  | Pawclown                                                                   |

```java
PawCard clone = getCard().getClass()
    .getDeclaredConstructor(User.class)
    .newInstance(getUser());
clone.forcePositionateCard();
```

---

## Spell Card Standard

> Estado atual: **stub** (`SpellCard.java` — sem implementação).

Quando implementadas, Spell Cards devem seguir:

| Regra              | Detalhe                                            |
| ------------------ | -------------------------------------------------- |
| Herdam de          | `SpellCard` (que implementa `Card`)                |
| **Não** possuem    | HP, ATK, AGI — esses getters retornam 0 ou N/A     |
| Ficam no campo?    | **Não** — efeito é aplicado e a carta é descartada |
| `getUser()`        | Retorna o jogador que jogou a carta                |
| `setCardDetails()` | Define apenas `elixirCost` e metadados descritivos |
| `exportInfo()`     | Retorna uma linha descrevendo o efeito             |

---

## Implementation Checklist

Ao criar uma nova **Paw Monster**:

- [ ] Classe em `src/models/characters/` com prefixo `Paw`
- [ ] Herda de `PawCard`
- [ ] Implements `HasPower` (se tiver poder)
- [ ] `setCardDetails()` define: `life`, `attack`, `agility`, `rarity`, `elixirCost`
- [ ] `usePower()` instancia a `PowerSkill` e chama `.Use()`
- [ ] Stats dentro da faixa da raridade definida
- [ ] Registrada em `ManagePaws.java` para ser invocável no jogo

Ao criar uma nova **PowerSkill**:

- [ ] Classe em `src/models/powerEnviroment/`
- [ ] Herda de `PowerSkill`
- [ ] Construtor com `(User, PawCard, int elixirCost)` + `super()`
- [ ] Implementa `powerRule()` sem verificar Elixir
- [ ] Usa `baseAttack` (não `attack`) se modificar ATK
- [ ] Javadoc com arquétipo: `Ofensivo | Defensivo | Controle | Suporte`

---

## Code Conventions

| Convenção              | Regra                                                               |
| ---------------------- | ------------------------------------------------------------------- |
| Nomenclatura de cartas | `Paw` + identidade temática (ex: `Pawclown`, `Pawskeleton`)         |
| Apelido (nickname)     | Opcional, máximo **15 caracteres**                                  |
| `baseAttack`           | Nunca setar diretamente — gerenciado internamente por `PawCard`     |
| `maxLife`              | Definido automaticamente na primeira chamada de `setLife()`         |
| Elixir no poder        | Sempre descontado em `PowerSkill.Use()` — nunca em `powerRule()`    |
| Entry point de batalha | `MessageDispatcher` / `Battle.java` — não bypassar o fluxo de turno |
| Pacote de cartas       | `models.characters` para criaturas, `models.spells` para magias     |
| Pacote de poderes      | `models.powerEnviroment` (convenção existente no projeto)           |
