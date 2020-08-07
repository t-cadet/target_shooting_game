# Target Shooting Game
A target shooting game in Java (a 2017 university project).

![Game Demo](https://github.com/tristanCadet/target_shooting_game/blob/master/Apercu_ecran_1920x1080.png)
Note: Parts of the game & code are in french.
## Features
- An interactive GUI for target shooting
- Multiple targets & their original drawings
- Higly customizable & randomizable target path
- Increasing difficulty
- Lootable buffs and weapons
- Dashboard to keep track of life, score, timer, weapons and buffs
- Launch menu
- Sound effects and soundtrack
## Getting started
You will need:
- **Eclipse**
- **Java** (I just tested it on Java 14 (august 2020) but higher or lower versions will probably work as well).

Clone the repo, open the project in Eclipse, go to the main file (src/view/Main.java) and hit run.
The game should launch on the main menu in full screen. 

Note: the sound files are in .wav format which makes them very heavy.

## Game Manual

Shoot the nasty targets to get some points without shooting the little girls.
If a target comes off screen you lose some HP.
The difficulty (speed, number of targets and target HP) increases according to the score.

You can loot weapons and buffs randomly when killing targets.
Change your weapon using the dropdown menu.

Weapons have a cooldown (CD) time.
If you try to shoot during CD it will produce an empty barrel noise.

The images of the targets change the first time you hit them, then you see the trace left
by the impact.

The program has a soundtrack, please activate the speakers.

Note: all sizes are relative, so the rendering should work for different screen resolutions.
