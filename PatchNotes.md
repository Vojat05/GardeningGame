# Patch Notes

Only major changes are documented here<br><br>
<b>+</b> == New feature<br>
<b>-</b> == Removed feature<br>
<b>*</b> == Modified feature<br><br>

EXE file is made for every update documented here<br>

## 20/1/2023
<ul>
    <li> + Method to stop the game loop</li>
    <li> + Player and textures for player</li>
</ul>

## 23/1/2023
<ul>
    <li> * Method for setting the texture image was made useful for multiple objects</li>
</ul>

## 24/1/2023
<ul>
    <li> * Finished the change of textures for each side of the player</li>
</ul>

## 26/1/2023
<ul>
    <li> + Added the Player class for better readability</li>
</ul>

## 28/1/2023
<ul>
    <li> + Added the Flower class to make each flower an object with specific values</li>
</ul>

## 30/1/2023
<ul>
    <li> + Added the mouse listener for placing the flowers</li>
    <li> + Added an error list enum for errors</li>
    <li> + Added the method for summoning flowers on mouse click</li>
</ul>

## 3/2/2023
<ul>
    <li> + Added the flower dying mechanism</li>
    <li> + Added a 2 dimensional map for writing the positions and types of flowers into</li>
</ul>

## 4/2/2023
<ul>
    <li> + Added a way to keep plants alive by watering them</li>
</ul>

## 5/2/2023
<ul>
    <li> + Added the player inventory with 2 flowers and a watering can</li>
</ul>

## 14/2/2023
<ul>
    <li> + Added the inventory panel. To open visible inventory press T</li>
</ul>

## 17/2/2023
<ul>
    <li> + Added the game menu to select what you want before playing the actual game itself</li>
</ul>

## 28/2/2023
<ul>
    <li> * Changed the button textures</li>
    <li> * Changed the game logo in main menu</li>
</ul>

## 11/3/2023
<ul>
    <li> + Added a functional settings menu</li>
</ul>

## 17/3/2023
<ul>
    <li> * Made the "Restore Controls" button functional</li>
</ul>

## 19/3/2023
<ul>
    <li> + Added a background music while in-game</li>
</ul>

## 25/3/2023
<ul>
    <li> * Changed the movement system to vector movement system</li>
    <li> + Added the posibility for a player to walk diagonally</li>
</ul>

## 26/3/2023
<ul>
    <li> + Added the option to save the game progress</li>
    <li> + Added the option to load a save file</li>
</ul>

## 11/4/2023
<ul>
    <li> + Added the grass animation system</li>
</ul>

## 13/4/2023
<ul>
    <li> * Fixed the screen size issue, it wasn't always just in 1920 x 1080</li>
    <li> * Reworked the flower and grass repaint system to fix the game saving issue</li>
</ul>

## 14/4/2023
<ul>
    <li> + Added water depleting mechanic</li>
    <li> + Added a well to refill player's water can</li>
    <li> + Added the players reach</li>
    <li> + Added the house texture</li>
    <li> * Modified turning that the player stays rotated in his last movement direction</li>
</ul>

## 16/4/2023
<ul>
    <li> + Added collisions</li>
    <li> + Added house entering mechanic</li>
</ul>

## 17/4/2023
<ul>
    <li> + Added the option to pause the game</li>
    <li> - Removed button border from every button with custom picture texture</li>
</ul>

## 15/6/2023
<ul>
    <li> + Added the option to save your game to a specific save slot from the bed inside the house</li>
    <li> + Added the house interior parts</li>
    <li> * Modified the games access to flowers to be more abstract</li>
    <li> * Now the load button loads its own save file</li>
</ul>

## 1/7/2023
<ul>
    <li> + Added the fence texture and usage</li>
</ul>

## 8/7/2023
<ul>
    <li> + Added fence poles on the garden side</li>
</ul>

## 9/7/2023
<ul>
    <li> * Changed the in-game Map system from 0 - 9 integers to 49 - 126 char values (ASCII Table)</li>
    <li> * Fixed the collision issue introduced with the new map</li>
</ul>

## 18/7/2023
<ul>
    <li> * Changed the window resize option to set the game window fullscreen if the display resolution is FullHD</li>
</ul>

## 19/7/2023
<ul>
    <li> + Added error visualization</li>
</ul>

## 29/7/2023
<ul>
    <li> + Added the tentacle flower</li>
</ul>

## 29/8/2023
<ul>
    <li> + Added a HP system</li>
    <li> + Added birds that deal damage by pooping on the player</li>
</ul>

## 30/8/2023
<ul>
    <li> * The flowers now have a random grass texture underneath, so they can be transparent</li>
    <li> + Added wall paintings to the house interior</li>
    <li> + Added an option to reload last save after death</li>
    <li> + Added damage and death sound effects</li>
</ul>

## 31/8/2023
<ul>
    <li> + Added a wardrobe into the house</li>
    <li> * Birds now flap with their wings while flying</li>
    <li> * The grave stone can be covered in bird shit</li>
    <li> + Added a cactus<li>
</ul>

## 1/9/2023
<ul>
    <li> * The player now has the ability to change between 2 outfits by interacting with the closet inside the house</li>
    <li> * Refilling water is now also possible by left clicking the well with the water selected</li>
</ul>

## 10/9/2023
<ul>
    <li> + Added some house interior such as a table, TV, chairs and a couch on which a player can sit by pressing RMB</li>
    <li> * All currently accessible flowers have all 3 textures</li>
    <li> * The wardrobe texture changed to better fit into the perspective</li>
    <li> * The save menu was updated to fit the current alert design</li>
<ul>

## 25/9/2023
<ul>
    <li> + Added a minimum resolution protection. If the user has a device display with a resolution lower than FullHD, the game won't start</li>
    <li> + Added a heart symbol into the HP gauge</li>
</ul>

## 26/9/2023
<ul>
    <li> + Added different language files</li>
    <li> * Added the tutorial box on the first start of a game instance</li>
</ul>

## 2/10/2023
<ul>
    <li> + Added the tile space</li>
    <li> + Added the tile texture</li>
    <li> * Fixed the tile movement speed glitch</li>
    <li> * Errors now use the texture pack font</li>
    <li> + Added a JSON config file for the language and texture pack files</li>
</ul>

## 4/10/2023
<ul>
    <li> * Changed player hotbar for items</li>
</ul>

## 5/10/2023
<ul>
    <li> + Added a player stamina system</li>
    <li> + Added custom icons for the inventory objects</li>
</ul>

## 6/10/2023
<ul>
    <li> * Quick side movement change fixed</li>
    <li> + Added day / night cycle</li>
</ul>

## 7/10/2023
<ul>
    <li> * Day / Night fadeout effect is now smooth</li>
    <li> * Day / Night length in seconds can now be set in the config file</li>
    <li> * Tile movement speed is now applied only when the character feet are on the tile square</li>
    <li> * Mouse & keyboard input is improved, no longer has issues with registering clicks</li>
</ul>

## 8/10/2023
<ul>
    <li> * Night is now darker</li>
</ul>

## 11/10/2023
<ul>
    <li> + Selecting slots in the inventory by clicking 1...10 hotbar keys</li>
    <li> + Added rain that waters flowers for you</li>
</ul>

## 15/10/2023
<ul>
    <li> * Player light is now functional</li>
</ul>

## 27/10/2023
<ul>
    <li> + Added .exe game launcher made in C</li>
    <li> + Added Build.exe to build the game yourself made in C</li>
    <li> + Added game icon</li>
    <li> * Save menu hover and selected slot effect fixed</li>
    <li> * Night lamp is now a gradient circle</li>
</ul>

## 6/11/2023
<ul>
    <li> * The run script in C closes the terminal on default, but keeps it open with the "cmd" argument</li>
</ul>

## 3/12/2023
<ul>
    <li> * Fixed the night circle size</li>
</ul>
