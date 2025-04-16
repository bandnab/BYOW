# 🧱 Java-Based Tile World Generator

This project is a modular, tile-based world generation engine written in Java. It simulates a 2D game world rendered via a custom tile engine and procedurally generated using rooms, hallways, and interactive input. The system supports real-time rendering, multiple input sources, and randomized terrain — all built with object-oriented design principles.

## ✨ Features

- 🎮 **Tile Rendering Engine**: Character-based tile rendering with color support using StdDraw.
- 🏗 **Procedural Map Generation**: Randomly generates rooms and hallways using seeded randomness.
- 🧭 **Flexible Input System**: Supports keyboard input, random input, and string-based commands.
- 💾 **Save/Load Support**: Input string mode mimics save/load behavior through deterministic state recreation.
- 🔀 **Custom Random Utilities**: Bernoulli, Gaussian, Poisson, and uniform distributions via `RandomUtils`.

## 📁 Project Structure

## 🖥️ How to Run

1. **Compile all Java files**:
   ```bash
   javac byow/**/*.java
2.java byow.Core.Main
3.java byow.Core.Main -s "n123sss:q"



 My Contribution
- Developed the procedural map generator using random room and hallway creation logic

- Built and debugged input abstractions for keyboard, random, and string-based controls

- Integrated rendering logic with the tile engine for real-time feedback

- Applied object-oriented principles to keep the code modular and extensible

🧠 Lessons Learned
- This project helped me strengthen my understanding of:

- Java system design and modular architecture

- Randomized algorithms and seed-based determinism

- Real-time rendering with minimal graphics libraries

- Managing game state across different input sources

🗂 Future Work
- Implement file-based save/load with serialization

- Add enemy AI or NPC pathing

- Build an inventory system or quest engine

- Port rendering from StdDraw to a GUI framework like JavaFX

