------------------------------------------------------
Create: Blazing Hot 0.6.2
------------------------------------------------------

### Added:

* Modern Lamp Quad Panel

### Changed:

* Removed Nether Flora and Coal from Nether Compound Recipe
* Updated and reorganised textures of Modern Lamps
* Optimised Modern Lamp Panels

------------------------------------------------------
Create: Blazing Hot 0.6.1
------------------------------------------------------

### Fixed:
* Fixed Brass Carrot's Haste effect duration

------------------------------------------------------
Create: Blazing Hot 0.6
------------------------------------------------------

### Added:
* New fluids: Molten Zinc, Copper, Brass, Ancient Debris, Netherite, Andesite
* New melting in Blaze Mixer recipe for Ancient Debris, that gives Netherite at higher rates
* New mixing recipe for Molten Andesite, that gives Andesite Alloy at higher rates
* Mixing recipe for Molten Brass using Molten Copper and Zinc
* Blaze Mixing recipe for Netherite using Molten Ancient Debris and Gold
* Crafting recipe for Blaze Apple
* 17 new Advancements, including 3 challenges and one secret advancement
    * You may need to replace your Mixers, Blaze Mixers and Spouts to get some of the advancements
* New item tags:
  * `blazinghot:metal_carrots`
  * `blazinghot:metal_apples`
  * `blazinghot:stellar_metal_apples`
  * `blazinghot:enchanted_metal_apples`
  * `blazinghot:metal_food`
* New Metal Carrots and Apples:
  * Zinc - focused on movement (Speed, Slow Falling)
  * Copper - focused on water (Water Breathing, Dolphin's Grace)
  * Brass - focused on building (Haste, Jump Boost)
  * Enchanted Netherite Apple - combines and enhances the power of Enchanted Blaze Apple and Enchanted Golden Apple
* Compatibility recipes for melting Rods, Wires and Zinc Sheet from [Create: Crafts & Additions](https://modrinth.com/mod/createaddition)
* [Forge] Information about Nether Lava in JEI

### Changed:
* Modern Lamp Panels can now be waterlogged
* Increased chance of getting Blaze Powder when crushing Blaze Gold Rods from 30% to 50%
* Several recipes now use tags instead of specific ingredients
* Updated textures for existing Molten Metals
* Updated textures for existing Metal Apples
* Blaze Arrow's damage is now multiplied when hitting the target rather than when being shot
* Changed the icon of Blazing Building Blocks creative tab back to White Modern Lamp
* Adjusted effects of existing Metal Apples
* Changed recipes for Modern Lamp Blocks:
    * They now require 1 Blaze Gold Rod, 1 Glowstone and 3 Glass blocks (or Stained Glass for colored lamps)
* [Fabric] Blaze Mixing recipes should no longer show before regular Mixing recipes when using EMI
* [Fabric] Unified some translations between EMI and JEI
* [Fabric] Molten Blaze Gold source blocks now turn into Obsidian when touching water

### Removed:
* Metal Block melting recipes

### Fixed:
* Fixed mod .jar files using `blazinghot` name instead of `create_blazing_hot`
* Modern Lamp Panels no longer get destroyed by fluids when facing up or down
* Blaze Arrow is now in the `minecraft:arrows` entity type tag and grants the "Take Aim" advancement
* Moved Blaze Mixer's fuel slot in recipe viewers to avoid being hidden by ingredients
* [Forge] Fixed Iron Carrot crafting recipe using wrong tag for iron nuggets
* [Forge] Fixed Blaze Mixer having incorrect fuel capacity
* [Forge] Fixed Modern Lamp dyeing recipes using wrong tags for dyes
* [Forge] Pumping fuel out of Blaze Mixer while processing a recipe no longer crashes the game

------------------------------------------------------
Create: Blazing Hot 0.5
------------------------------------------------------

### Added:

* **⚒ Forge support**
* Regular mixing recipe for melting Blaze Gold Rods
* Configuration options for toggling effect tooltips for custom food
* Toggleable effect tooltips for vanilla Golden Apples
* New configuration options for blaze mixing

### Changed:

* Reduced melting time with Blaze Mixer
* Reduces fuel costs for melting with Blaze Mixer
* Extended processing time of Molten Blaze Gold mixing and blaze mixing recipe
* Melting with Mechanical Mixer is now 3 times longer compared to Blaze Mixer (previously 5)
* Renamed `fuel` property in Blaze Mixing recipes to `blazinghot:fuel`
* Nether Sprouts can now be used when crafting Nether Compound
* Updated textures for Blaze Gold Ingot, Nugget, Sheet and Block
* Modern Lamp crafting recipe now returns 2 lamps instead of 1
* Blaze Arrow crafting recipe now returns 4 arrows instead of 2
* Reduced the amount of Nether Essence required to obtain Molten Blaze Gold from 4 to 2
* Reduced the amount of fluid when filling Metal Carrots and Apples from 8 nuggets or ingots to 6
* Adjusted sequenced assembly recipes for Enchanted Metal Apples:
  * Increased the amount of loops from 4 to 6
  * Reduced amount of fluid required per loop from 9 ingots to 6

### Fixed:

* Blaze mixing recipes now consume the correct amount of fuel
* Blaze mixing recipes' speed is no longer affected by the fuel
  * This only applies to recipes exclusive to the Blaze Mixer
* Melting time no longer caps at 512 ticks (25.6 seconds)

------------------------------------------------------
Create: Blazing Hot 0.4
------------------------------------------------------

### Added:

- Blaze Mixer:
    - A Mixer capable of holding fluids (fuel)
    - All recipes from Mechanical Mixer can be applied in the Blaze Mixer
    - When there is enough fuel present, the Blaze Mixer will work twice as fast
    - There are some recipes exclusive to the Blaze Mixer
- Blaze Mixing (no support for REI yet, use [EMI](https://modrinth.com/mod/emi) or [JEI](https://modrinth.com/mod/jei)
  to see the recipes)
- Blaze Whisk (crafting ingredient)
- Melting recipes for Blaze Gold Rod
- Separate creative tab for building blocks
- Nether Lava
- More efficient Blaze Mixing recipes for Molten Blaze Gold

### Changed:

- Drastically increased melting time for metals in the Mechanical Mixer
    - Melting with fueled Blaze Mixer is 5 times faster than in the Mechanical Mixer
- Moved Blaze Gold Block and Modern Lamps to the new creative tab
- Made Molten Gold and Molten Iron behave like lava:
    - Their source blocks turn into Obsidian when touching water, and their tick rate has been reduced from `5` and `15`
      to `30`
    - You can generate Cobblestone faster using Nether Lava.
    - In the future, these metals will have some new unique
      interactions with other fluids
- New changelog format
- New version naming scheme:
    - Removed build number from published files
    - Added mod platform
    - Example: `0.3-build.89+1.20.1` -> `0.4+fabric-mc1.20.1`

### Removed:

- Iron Block, Gold Block and Blaze Gold Block melting recipes in the Mechanical Mixer
    - These blocks can still be melted in the Blaze Mixer

------------------------------------------------------
Create: Blazing Hot 0.3
------------------------------------------------------

### Added:

- Molten Iron
- Iron Carrot, Iron Apple, Stellar Iron Apple, Enchanted Iron Apple
- Recipes for melting Blaze Gold in the Mixer
- Blaze Casing

### Changed:

- Slightly adjusted some recipes
- Extended Stellar Blaze Apple's and Enchanted Blaze Apple's Regeneration effect to 20 seconds

### Fixed:

- Blaze Gold Block no longer emits light

------------------------------------------------------
Create: Blazing Hot 0.2
------------------------------------------------------

### Added:

- Colored variants of Modern Lamps and Modern Lamp Panels

### Changed:

- Updated textures for White Modern Lamp and White Modern Lamp Panel

------------------------------------------------------
Create: Blazing Hot 0.2
------------------------------------------------------

### Added:

- Blaze Apple, Stellar Blaze Apple, Enchanted Blaze Apple

### Changed:

- Renamed:
    - `Modern Lamp` -> `White Modern Lamp`
    - `Modern Lamp Panel` -> `White Modern Lamp Panel`
- Added new tooltips to Modern Lamps
- Added redstone functionality of Modern Redstone Lamps to regular Modern Lamps
- ID changes:
    - `blazinghot:modern_lamp` -> `blazinghot:white_modern_lamp`
    - `blazinghot:modern_lamp_panel` -> `blazinghot:white_modern_lamp_panel`
- Updated Blaze Carrot tooltip
- Blaze Gold Blocks can now be used in the Beacon base
- Blaze Gold Ingots can now be used in Beacons

### Removed:

- Modern Redstone Lamp
- Modern Redstone Lamp Panel

------------------------------------------------------
Create: Blazing Hot 0.1.1
------------------------------------------------------

### Changed:

- Blaze Carrots now extinguish after being eaten
- Blaze Carrots no longer give Fire Resistance
- Molten Blaze Gold and Molten Gold no longer use Tinker's Construct's textures
- Gilded Stellar Golden Apple's rarity is now Rare instead of Epic

### Fixed:

- Effect tooltips now show the amplifiers of the effects (#2)
- Removed Cobblestone milling into Stone Dust recipe due to conflicts with Cobblestone crushing into Gravel recipe (#3)

### Removed:

- Rail Grass Slab

------------------------------------------------------
Create: Blazing Hot 0.1
------------------------------------------------------

### Molten Metals

- Currently: Gold, Blaze Gold
- Gold can be melted in superheated mixing
- Blaze Gold can be acquired by mixing Molten Gold and **Nether Essence**
- Molten Gold can replace lava in cobblestone generators to make the process faster
- Molten Blaze Gold creates Netherrack when touching water

### Nether Essence

- Obtained by haunting Nether Compound
- Nether Compound can be obtained by mixing some nether and overworld resources

### Blaze Gold:

- Comes in six forms: liquid, nuggets, ingots, blocks, sheets and rods
- Blaze Gold Rods can be crushed into Blaze Powder and crafted into Blaze Arrows that deal more damage in the Nether
- Blaze Gold Nuggets and Molten Blaze Gold can be used to craft Blaze Carrot that grant 10 seconds of Fire Resistance

### Misc:

- Added Stellar Golden Apple, which is obtained by deploying a Nether Star on a Golden Apple
- Added recipe for Enchanted Golden Apple that uses the Stellar Golden Apple
- Added Stone Dust, Netherrack Dust and Soul Dust
- Added filling recipes for Golden Carrots, Golden Apples and Glistering Melons
- Added toggleable Modern Lamps, Modern Lamp Panels and their redstone-powered variants.
- Added Rail Grass (a scuffed grass slab)
