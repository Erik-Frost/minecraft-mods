# Minecraft Modding Guide

### How to make an item:
1. Register the item
2. Create item [model](https://minecraft.gamepedia.com/Model#Item_models) JSON in .../assets/corruptedsurvival/models/item/
3. Create item texture PNG in .../assets/corruptedsurvival/textures/item/
4. Add item [lang](https://minecraft.gamepedia.com/Language) translation mapping to en-us.json in .../assets/corruptedsurvival/lang/
5. Create [tags]((https://minecraft.gamepedia.com/Tag)) for item as needed
   - Referencing default minecraft item tag JSONs may be helpful
6. Create a reference to the item in ModItems?



### Referencing Default Minecraft:
For reference, default minecraft assets and data can be found inside the JAR files at %appdata%/.minecraft/versions/

### Resource Packs
[How to create a resource pack](https://minecraft.gamepedia.com/Tutorials/Creating_a_resource_pack)

[The file structure of a resource pack](https://minecraft.gamepedia.com/Resource_Pack#Folder_structure)

- [assets](https://minecraft.gamepedia.com/Resource_Pack)
  - [sounds.json](https://minecraft.gamepedia.com/Sounds.json)
    - A JSON file which tells the sound system what sound files to play when a sound event is triggered by one or more in-game events.
  - [blockstates](https://minecraft.gamepedia.com/Model#Block_states) ([?](https://minecraft.gamepedia.com/Block_states)) ([states](https://minecraft.gamepedia.com/Java_Edition_data_value#Block_states))
    - JSON files describing each block's variants and linking them to their corresponding models. (stairs, logs, barrles, etc...) 
    - These JSON files also describe multipart blocks, which are blocks that can be made up of more than one model at a time. (redstone dust, wood fence, etc...)
  - [font](https://minecraft.gamepedia.com/Resource_Pack#Fonts)
    - files for describing fonts
  - icons
    - ???
  - [lang](https://minecraft.gamepedia.com/Resource_Pack#Language) ([?](https://minecraft.gamepedia.com/Language))
    - JSON files mapping keys (items, blocks, entities, etc...) to values (translated names of those objects)
  - [models](https://minecraft.gamepedia.com/Resource_Pack#Models) ([?](https://minecraft.gamepedia.com/Model))
    - [item](https://minecraft.gamepedia.com/Model#Item_models)
      - JSON files describing the model of each item
    - [block](https://minecraft.gamepedia.com/Model#Block_models)
      - JSON files describing the model of each block
  - [particles](https://minecraft.gamepedia.com/Particles)
    - JSON files that appear to be mapping a single particle effect to one or more particle texture
  - [sounds](https://minecraft.gamepedia.com/Resource_Pack#Sounds) ([?](https://minecraft.gamepedia.com/Sound))
    - OGG files used in playing sounds and music
  - [shaders](https://minecraft.gamepedia.com/Resource_Pack#Shaders) ([?](https://minecraft.gamepedia.com/Shaders))
    - files used in shading.
  - [texts](https://minecraft.gamepedia.com/Resource_Pack#Texts)
    - TXT files used in the end dialogue, the credits and on the [titlescreen splash](https://minecraft.gamepedia.com/Splash)
  - [textures](https://minecraft.gamepedia.com/Resource_Pack#Textures)
    - [block](https://minecraft.gamepedia.com/List_of_block_textures)
      - PNG and MCMETA files used in texturing and animating every block
    - [colormap](https://minecraft.gamepedia.com/Tint)
      - PNG files used in mapping various shades of color (tree leaves, grass, etc...)
    - effect
      - A single PNG file used in dithering textures
    - entity
      - PNG and MCMETA files used in texturing the different entities
    - environment
      - PNG files used in texturing different parts of the environment (clouds, rain, snow, the moon, the sun and the end sky)
    - [font](https://minecraft.gamepedia.com/Language#Font)
      - PNG files used in texturing the font
    - gui
      - PNG files used in texturing the various widgets, icons, buttons, bars, toasts and backgrounds 
      - advancements
        - PNG files used in texturing the tabs, widgets and frame of the advancement menu
        - backgrounds
          - PNG files used in texturing the backgrounds of the various advancement menus
      - container
        - PNG files used in texturing the various container menus
        - creative_inventory
          - PNG files used in texturing the different parts of the creative inventory
      - presets
        - Maybe this PNG is used as the default title screen background???
      - title
        - PNG files used in texturing the startup and title screen logos
        - background
          - Placeholder PNGs used for texturing the titlescreen panorama?
  - [item](https://minecraft.gamepedia.com/Item)
    - 16x16 pixel PNG files used in texturing each item
  - [map](https://minecraft.gamepedia.com/Map)
    - PNG files used in texturing the background and icons of the map item
  - misc
    - A collection of miscelaneous PNG and MCMETA files used in texturing and animating wildly different things
      - enchanted item glint
      - forcefield on barriers
      - nausea visual overlay
      - pumpkin on head mask
      - shadow beneath entities
      - vignette visual overlay
      - unknown server and unknown pack default textures
      - I think underwater visual overlay?
  - [mob_effect](https://minecraft.gamepedia.com/Status_effect)
    - 18x18 pixel PNG files used as icons for each status effect
  - models
    - [armour](https://minecraft.gamepedia.com/Armor)
      - Two 64x32 pixel PNG files texturing each armour set
        - I think layer one is head and chest/arms
        - I think layer two is legs and feet
  - [painting](https://minecraft.gamepedia.com/Painting)
    - PNG files used in texturing painting entities. Their default sizes range from 1x1 blocks (16x16 pixels) to 4x4 blocks (64x64 pixels)
  - [particle](https://minecraft.gamepedia.com/Particles)
    - PNG files used in texturing particles. Their default sizes range from 8x8 pixels to 32x32 pixels

### Data Pack Structure
[How to create a data pack](https://minecraft.gamepedia.com/Tutorials/Creating_a_data_pack)

[The file structure of a data pack](https://minecraft.gamepedia.com/Data_Pack#Folder_structure)

- [data](https://minecraft.gamepedia.com/Data_Pack)
  - [advancements](https://minecraft.gamepedia.com/Advancement/JSON_format) ([?](https://minecraft.gamepedia.com/Advancement))
    - JSON files describing all the advancement and their parents/paths, icons, frames and triggers. Also has options for each advancement to be hidden, toasted, announced in chat and/or give rewards
  - [functions](https://minecraft.gamepedia.com/Function_(Java_Edition))
    - MCFUNCTION files describing custom functions
  - [loot_tables](https://minecraft.gamepedia.com/Loot_table)
    - blocks
      - JSON files describing what blocks drop when broken in different ways
    - entities
      - JSON files describing what entities drop when they die in different ways
    - chests
      - JSON files describing what items can populate what chests
    - gameplay
      - JSON files describing what items drop for fishing, piglin bartering, cat gifts and hero of the village rewards
  - [predicates](https://minecraft.gamepedia.com/Predicate)
    -  Technical JSON files that represent the conditions for loot tables
  - [recipes](https://minecraft.gamepedia.com/Recipe)
    - JSON files describing all the crafting recipes of items
  - [structures](https://minecraft.gamepedia.com/Structure_block_file_format) ([?](https://minecraft.gamepedia.com/Generated_structures))
    - NBT files describing the structures that get generated in Minecraft's world generation
  - [tags](https://minecraft.gamepedia.com/Tag)
    - JSON files grouping blocks, items, fluids, entities and functions together.
  - dimension_type
    - ???
  - [dimension](https://minecraft.gamepedia.com/Custom_dimension) ([?](https://minecraft.gamepedia.com/Dimension))
    - JSON files describing the various dimensions
  - [worldgen](https://minecraft.gamepedia.com/Custom_world_generation)
    - [biome](https://minecraft.gamepedia.com/Custom_world_generation#Biome) ([?](https://minecraft.gamepedia.com/Biome))
      - JSON files describing the various biomes
    - [configured_carver](https://minecraft.gamepedia.com/Custom_world_generation#Carvers)
      - JSON files used to add carved out regions of biomes, either replaced with air or liquid.
    - [configured_feature](https://minecraft.gamepedia.com/Custom_world_generation#Features)
      - JSON files describing unique elements of the world like trees, flowers, ore, etc.
    - [configured_structure_feature](https://minecraft.gamepedia.com/Custom_world_generation#Structure_features)
      - JSON files describing special types of features that generate a structure (nether fortress, stronghold, etc...)
    - [configured_surface_builder](https://minecraft.gamepedia.com/Custom_world_generation#Surface_builders)
      - JSON files controlling how the surface of the terrain is shaped and what blocks it is generated with.
    - [noise_settings](https://minecraft.gamepedia.com/Custom_world_generation#Noise_settings)
      - JSON files used for generating the shape of the terrain
    - [processor_list](https://minecraft.gamepedia.com/Custom_world_generation#Processor_lists)
      - JSON files used to affect blocks in structures,
    - [template_pool](https://minecraft.gamepedia.com/Custom_world_generation#Jigsaw_pools)
      - JSON files used to generate structures using [jigsaw blocks](https://minecraft.gamepedia.com/Jigsaw_Block)

	
	
	
### [NBT File](https://minecraft.gamepedia.com/NBT_format) Viewing & Creation
- [NBTExplorer](https://github.com/jaquadro/NBTExplorer) can be used to view NBT files 
- NBT structure files can be created in Minecraft in creative mode using [Structure Blocks](https://minecraft.gamepedia.com/Structure_Block) and [Structure Voids](https://minecraft.gamepedia.com/Structure_Void)
  - In java edition, the max size of a structure is 48x48x48 blocks



### MISC (for now)
Possibly for 3D modeling things https://blockbench.net/

data values https://minecraft.gamepedia.com/Java_Edition_data_value

Get nbt of the held item: /data get entity @s SelectedItem

The special properties for different items like a Compass or a bow can be found in ItemModelsProperties

to modify a vanilla object, use registry replacement

Find SRG names in build/createMCPtoSRG/output.tsrg


