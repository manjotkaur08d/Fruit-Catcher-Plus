# Fruit Catcher Plus (Core Java + Swing)

A simple desktop Java game built without game engines or external dependencies.

## Features
- 3 falling items at Level 1, increasing to 7 simultaneous items by Level 5
- 5 levels (0, 100, 200, 300, 400+ points)
- Apples, oranges, plums, bombs (Level 2+) and golden bonus fruits (Level 3+)
- A brief orange/yellow cartoon burst when a bomb hits the basket
- 5 lives; missed ordinary fruits or caught bombs cost 1 life; missed bombs are safe
- High score during the current game session, pause, restart

## Run
Install Java JDK 11 or newer. In a terminal *inside this folder*:

```powershell
javac Main.java GamePanel.java Basket.java Fruit.java
java Main
```

## Controls
- ENTER — start
- LEFT / RIGHT — move basket
- P — pause / resume
- R — restart after game over

## Files
- `Main.java` — Swing game window
- `GamePanel.java` — game loop, level logic, collision, burst drawing
- `Basket.java` — basket position, movement, drawing
- `Fruit.java` — item appearance, random types and movement

## Notes
All artwork is drawn with Java Graphics2D. No sound, images or additional installations are necessary beyond the JDK.
