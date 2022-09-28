2022-09-28 126 - Attempt to add dependencies: 
2022-09-27 Update Gameplay.one: 
2022-09-27 Add dependencies: Added depency on corpse mod. Need to execute build to pull the code and add Metadata

2022-09-26 124 - Add Revival Sickness to Second Wind: Added Revival Sickness to second wind effect.

2022-09-26 123 - Fix Crash on Load: Fixed crash on startup. Made a capitalization error in PotionInit

2022-09-26 122 - Resolve CTD: Should fix CTD on pause/save. Did so by changing items to cure Revival Sickness from null to a new dev only potion that removes all effects.

2022-09-26 121 - Revival Sickness Implementation: First pass on Revival Sickness done. It includes multiple severities.

2022-09-24 120 - Minor Changes, started revival sickness work: Toned Darkness from 10 down to 5. Created new Mob Effect Revival Sickness

2022-09-23 119 - Reworked Second Wind: Reworked second wind programming to avoid ctd on player death. Now mirrors inventoryHandler logic.

2022-09-23 118 - Create Second Wind: Second Wind Logic now in place. Events should register the mob that killed you. Taking down that mob will remove the effect.

2022-09-23 117 - Force Pose: Force Player into "Dying" Pose

2022-09-23 116 - Fixed Hunger Death: Secondary Effects now working as expected. Hunger killing player, tuned down to intensity of 25.

2022-09-23 115 - Cleaned up code: Removed separation between damage protection while wounded and death protection while wounded. All immunities while wounded are outright immunity to Damage.

2022-09-22 114 - Attempt to Fix Mobs still killing: Attempt to resolve issue related to mobs being able to kill you in a wounded state. Moved event checking for living entity death to same event handler.

2022-09-22 113 - Tweaked Logic: Fixed Issue related to Screen Jerkiness due to Slowness and Darkness getting applied every second. Removed Timer from Wounded Effect. Timeout will now be based on food in hunger bar.

2022-09-22 112 - Reworked Cure Logic: Potion will now target ONLY the Wounded Effect. Note that it is now immune to milk.

2022-09-22 111 - Added Revive Potion: Added new Revive Potion. Potion not curing Effect yet. Will need some custom code in the effect.

2022-09-22 110 Protections and (in)abilities: Wounded Effect won't trigger from drowning, suffocation, lava, falling out of the world or starvation. Wounded players are now immune from damage from Falling Blocks, Magma Blocks, Lightning and Poison. Wounded players cannot be killed by Projectiles or Generic Damage

2022-09-21 109 - Effect is working: The effects effects are working now. TODO: Create Texture for effect.

2022-09-21 108 - Fixed Errors preventing registration: Fixed crash on death, and the following crashes when I fixed the crash on death. Effect now correctly applying but isn't applying any of it's effects.

2022-09-21 107 - Worked on Crashes: Fixed Crash on Startup. Still crashing on player death.

2022-09-21 106: Edits to DeathHandler: 
2022-09-20 105: Finished Wounded Effect: Completed Work on Bleeded Effect, added new event handler and renamed existing handler, we will keep events organized in different handlers.

2022-09-20 104 - Create Wounded Effect: Created New Effect "Wounded" and created the MobEffect registry.

2022-09-18 Move Notes Into Repo: 
2022-09-17 103: Fixed Respawn Bug: Respawning Fixed. Enchantment working as intended.

2022-09-17 102: Fixed Events: Events are now working. Was missing a registry. Dynamic events have to registered the old way. Running into errors checking for enchantment. Enchantment debug still WIP. Respawn is broken as of this commit.

2022-09-10 101: Program Enchantment: Added Event Logic for Spiritbound Enchantment. Took inspiration from standalone mode below, but required substantial rewrites to do correctly. https://github.com/vova7865/Soulbound/blob/master/src/main/java/com/vova7865/soulbound/Events.java

2022-09-08 011: Create Enchantment Registry: Created Enchantment Registry and new blank Enchantment Spiritbound.

2022-09-07 010: Fix Touchstone Render Issues: Using Deepslate Model for now

2022-09-07 009: Create Block Registry: Created Block Registry, Added Touchstone Block. Block texture not pulling from vanilla deepslate.

2022-09-07 008: Fix Render Issue: Had to move a folder around, structure was not built correctly the first time

2022-09-07 007: Create Item Registry: Created Item Registry, creative mode tab, and lantern item. PNG not rendering in game. Investigate.

2022-09-07 006: New Enviro setup: Tested to world and paused. No crashes.

2022-09-07 005: Reset and Start Over: 
2022-09-06 004: Added generic item and tab: Added Creative Mode Tab for items and generic items Lantern. These changes generating a crash. Probably a programming error.

2022-09-06 003: Finished Defining Touchstone: Fixed Issue with MODID being uppercase. Finished writing out definitions for touchstone. Predictably, item does not appear in menu but there is a stable launch to world.

2022-09-04 002: Continued work on Touchstone: creating definitions for touchstone block

2022-09-04 001 Block Init and Touchstone Creation: Created Block Initialization code. Added new Block TOUCHSTONE, and created method for auto registering BlockItems

2022-09-04 Update CHANGELOG.md: 
2022-09-03 Update Mod metadata: 
2022-09-03 Recreate Changelog: 
2022-09-03 Finalized Initial Setup: 
2022-09-03 recreate changelog: 
2022-09-03 Deleted & rebuilt project files: 
2022-09-03 Fixed missing files: Reobtained missing files and reran Eclipse Setup

2022-09-03 Configure workspace for Eclipse with Gradle: 
2022-09-03 Create CHANGELOG.md: 
2022-09-03 Setup workspace with gradle: 
2022-08-27 Installation of MDK for MC latest: 
2022-08-27 Initial commit: 
