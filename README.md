<div align="center">
  <div>
    <img src='https://takutotofu.s-ul.eu/ggc7MFq8' alt='dark castle'/>
  </div>
  <h3 align="center">A JavaFX School Project</h3>

  <div align="center">
     This project has been developped during my second year at <a href='https://www.helb-prigogine.be/' target="_blank"><b>HELB - Ilya Prigogine</b></a> 2023-2024.
  </div>
  </br>
</div>

## 📋 <a name="table">Table of Contents</a>

1. 🏢 [Introduction](#introduction)
2. 🎮 [Game Description](#game-description)
3. 🔋 [Additional Feature](#features)
4. 🤸 [Quick Start](#quick-start)
5. 🕸️ [Code to Copy](#snippets)
6. 🔗 [Assets](#links)
7. 🚀 [More](#more)

## <a name="introduction">🏢 Introduction</a>

<p align='justify'>
  Welcome to the JavaFX project for the academic year 2023-2024. This project involves developing an arcade game titled HELBTower. 
  In this game, you will immerse yourself in a fantastical world where a brave hero fights against the forces of evil to save his kingdom. 
  Your mission is to enter the court of the ancients, collect magical treasures, and avoid or confront the evil guardians that emerge from the castle's towers.
</p>

## <a name="game-description">🎮 Game Description</a>

### 1️⃣ Game Space

The castle courtyard is a two-dimensional play area, bordered by obstacles that characters cannot pass through. This courtyard is divided into squares, each of which can contain different game elements.

### 2️⃣ Characters

- **The Hero**: Controlled by the player, he must collect all the coins to advance to the next level.
- **The Guardians**: Opponents of the hero, they move according to different behaviors. There are four types of guardians: orange, blue, purple, and red, each with specific AI.

### 3️⃣ Coins

Each square not occupied by another element contains a coin to be collected. The player must collect all the coins to win and move to the next level.

### 4️⃣ Walls and Towers

- **Walls**: Fixed obstacles that characters cannot pass through.
- **Towers**: Dwellings of the guardians. There are four towers, each housing a specific guardian. Guardians emerge from their towers once 25% of the coins are collected.

### 5️⃣ The Guardians

- **Orange Guardian**: Moves randomly.
- **Blue Guardian**: Systematically explores all squares.
- **Purple Guardian**: Moves from tower to tower, increasing speed with each coin collected by the hero.
- **Red Guardian**: Constantly pursues the hero.

### 6️⃣ Potions and Magic Capes

- **Potions**: Temporarily increase the hero's speed.
- **Magic Capes**: Allow the hero to pass through a wall once.

### 7️⃣ Game Periods

The game has three periods: morning, day, and evening, affecting the guardians' speed.

### 8️⃣ Teleporters

Four teleporters placed at the edges of the courtyard allow the hero to quickly move from one edge to another.

### 9️⃣ Level System and Game End

The game ends when the hero is caught by a guardian. An end-game message will be displayed, showing the best score, which is saved even after the program is closed.

### 🔟 Interactive Commands – Cheat Codes

- `0`: Reset the game.
- `1`: Set game period to morning.
- `2`: Set game period to day.
- `3`: Set game period to evening.
- `4`: Add a random potion.
- `5`: Add a random magic cape.
- `6`: Add a random guardian.
- `r`: Stop the red guardian.
- `b`: Stop the blue guardian.
- `m`: Stop the purple guardian.
- `o`: Stop the orange guardian.
- `s`: Enable/Disable additional feature.

## <a name="features">🔋 Additional Feature</a>

### 👉 The Chronometer

The **Chronometer** is an additional feature inspired by the <a href='https://fr.wikipedia.org/wiki/Prince_of_Persia'>***Prince of Persia***</a> franchise. When the player collects an hourglass, it slows down the guardians and moves the player back in time.

#### How It Works

- **Activation**: Press `S` to enable the feature, which generates hourglasses on the game board.
- **Effect**: Collecting an hourglass moves the player back a few squares and temporarily slows down the guardians. After the effect duration, the guardians' speed returns to normal.

## Conclusion

The HELBTower project is an opportunity to apply advanced Java and game development concepts. By following the guidelines and adhering to the constraints, you will develop an engaging game while demonstrating your skills in object-oriented programming. Good luck with your development journey, and may your hero triumph over the darkness!
