2023-04-02 303 - Touchstone model: created model, texture is broken

2023-04-02 302 - Lantern Model Working: Still need sprite and lighting work but this will work for now

2023-04-01 301 - Create Lantern Model: Began work on lantern model. After implementing, there is a texture error.

2023-01-15 Update build.gradle: 
2023-01-14 235 - Final final tweaks: Finished adjusting ghost effect. Added more to the lang file but some translation keys are still broken.

2023-01-14 234 - More tweaks: Eliminated a number of crashes and finalized potion recipes. Brewing stand should now be working, and the touchstone deducts levels when being used now.

2023-01-13 233 - Brewing now fully armed and operational: 
2023-01-12 232 - Brewing Logic of tickloop not firing: 
2023-01-12 231 - Fixed Missing Changes: Slot Numbers can now be fully adjusted using TouchstoneTile properties instead of manually adjusting all the methods.

2023-01-12 230 - Wrapping up Touchstone Rework: Removed a number of slots from the touchstone. Added in slots for brewing. Brewing is not working, going to take a second look at comparison logic. Enchanting is sticking stuff in the wrong slot.

2023-01-12 229 - More Work on Crafting and Brewing: 
2023-01-11 228 - Can't use table to craft: Will need to do a workaround, probably using the touchstone.

2023-01-10 227 - Wrapped up documentation and recipes: Added potion recipes and tweaked final crafting table recipes.

2023-01-04 226 - Finish Resource Prep: 
2022-12-28 225 - Create Bindingstone image: Made a texture for the bindingstone, texture is a compressed image generated from blender. Source model in matsrc.

2022-12-27 224 - Create Wounded Effect Symbol: 
2022-12-27 223 - Enhance Ressickness Image: 
2022-12-20 222 - Create Textures and ResSick effect: Created Resurrection Sickness programming and a texture for the Ghost Effect.

2022-12-17 221 - Update Localization: 
2022-12-17 220 - StraightDeathtoGhsot Working: Tested ghost effect from DeathHandler is working. SetGhost from the Lantern will also cancel fire effects now.

2022-12-16 219 - Import Success: Code for returning items to player now working from lantern. Need to fine tune and test code for death without wounded status.

2022-12-16 218 - More Work on Enchantment Logic: Inventory update problem related to code only running server side. Re-arranged code to fix. ScanAndSaveItems function is hanging up when it runs. Related to storing items as a list. Will try as an array.

2022-12-14 217 - Need to finish importing logic into lantern: Worked on merging remember item logic with lantern. Inventory not getting cleared atm, also need to refer to death inventory items, NOT player inventory items.

2022-12-13 216 - BodyDrop Breathrough!: No TP. Managed to replicated body drop. Need to purge inventory. Currently only working on lantern to enter ghost state. Need to add to DeathManager.

2022-11-30 215 - Trying to get TP working: Player still not teleporting after respawn. May need to look at avoiding respawn all together.

2022-11-28 214 - Repair Tier 2 Touchstone: 
2022-11-21 213 - Asset Creation: 
2022-11-16 212 - Crafting Recipes Functioning: Still need assets for in inventory

2022-11-16 211 - Continue work on loot tables: 
2022-11-09 210 - Address GhostEventHandler crash: Removed function to disable inventory as it was causing a crash. GhostEventHandler is re-enabled.

2022-11-07 209 - Started work on loot tables: 
2022-11-07 208 - Added Crafting Recipes: Added jsons to resources folder for crafting, as well as a few intermediary items for the recipes. Need assets for them still.

2022-11-06 207 - Added Invulnerability while ghost: 
2022-11-06 206 - Fixed Lantern Issue: Can now fully spirit walk. Protections for spirit walking are not yet implemented properly.

2022-11-06 205 - Fixed looping issue: No longer stuck in deathloop on first death. Lantern doesn't exit ghost mode when clicking on touchstone however, may need to move the handling of that to DeathHandler. Also encountered strange bug where recovering items from body and then right clicking on touchstone with lantern deletes enchanted items. Need to reproduce.

2022-11-06 204 - Loop Error: Player gets stuck in respawn loop after death.

2022-11-06 Update Documentation: 
2022-11-06 Fix missing notes?: 
2022-11-06 Update Documentation: 
2022-11-06 203 - Fixed CTD on startup: Crash was caused by shorthanding effects before they're loaded. Fixed by referring to effects directly instead of creating variables. NOTE: Hud Suppression from the ghost effect is still leading to a crash. This object is still disabled until I address that issue.

2022-11-06 202 - Investigating CTD: Disabled GhostEvent Handler, need to find the source of CTD.

2022-11-05 201 - Register GhostHandler: Ghost Event Handler was not registered until now. Encountering issues with launching. Eclipse reports build path is set up incorrectly.

2022-11-05 Update InventoryHandler.java: 
2022-11-05 Merge pull request #3 from SubjectDeltaV/100-Phase-II-Functional: Fix Missing Commits
2022-11-05 Delete CHANGELOG.md: 
2022-11-05 Delete CHANGELOG.md: 
2022-11-05 Revert "Update Documentation": This reverts commit 673d8fba3e1855efd56bc7f8dac33051df8d0534.

2022-11-05 Create CHANGELOG.md: 
2022-11-05 Delete CHANGELOG.md: 
2022-11-05 Merge branch 'main' of https://github.com/SubjectDeltaV/MC-RespawnMod: 
2022-11-05 Update Documentation: 
2022-11-05 Merge pull request #2 from SubjectDeltaV/100-Phase-II-Functional: 100 phase II functional
2022-11-05 157 - Finished Implementing Ghost: Finished working on ghost work according to chart (See notebook). Also did general cleanup, clearing todo tasks and removing unused imports.

2022-11-05 Update Documentation: Created a flowchart for understanding and mapping out ghost status and remembering items.

2022-11-04 156 - Ghost State Online: Ghost State is working. To keep the enchantment working will need to re-implement inside the lantern.

2022-11-02 Merge branch '100-Phase-II-Functional' of https://github.com/SubjectDeltaV/MC-RespawnMod into 100-Phase-II-Functional: 
2022-11-02 155 - Started Spirit Walking: Began Work on actual spirit walking implementation. Added new "Ghost" Mob Effect and "Resurrection Sickness" Mob Effect Also changed Lantern Init to include new Lantern Class. All Mob Effects are now included in ModEffects for easier access. This will be an improvement on accessing the init file directly.

2022-10-30 154 - Fixed Enchant and Event: Items are now correctly transferring to player. WARNING: Items can currently be lost if you log out before retrieving them from the Touchstone.

2022-10-30 154 - Fixed Enchant and Event: Items are now correctly transferring to player. WARNING: Items can currently be lost if you log out before retrieving them from the Touchstone.

2022-10-26 153 - Sort of Working: Enchant is restoring items from touchstone, but not removing them from corpse.

2022-10-26 152 - Work on Enchant: 
2022-10-24 151 - Enchantment is kind of working: No more crashes, touchstone still not reacting to memorized items. Items are getting memorized correctly, issue is with how the program is comparing the objects.

2022-10-24 150 - Respawning working now: 
2022-10-24 149 - Attempting to Fix Enchant: For Some Reason, most of the items are coming through as null

2022-10-24 148 - More Work on Enchant: Event is firing but not recording items.

2022-10-22 147 - Worked on Re-enabling Enchant: Reworked level 1 enchantment and added lantern to scanning list.

2022-10-20 146 - Move Buttons on UI: Buttons are now closer to where they should be on ui. Players will now be automatically registered to Touchstone block. Buttons still causing CTD on use.

2022-10-18 145 - Add Buttons: Added buttons for Touchstone UI

2022-10-18 144 - Add logic for moving items to block: Added function to be called by a button by the player that moves items from the last death that have the correct enchantment and match items already stored in the touchstone.

2022-10-17 143 - Drops now ignores remembered items: Drops will not eject items saved or enchanted, they will be destroyed with the block.

2022-10-17 142 - Fixed Player Toolbar Loading: Toolbar now loads correctly when accessing Touchstones.

2022-10-17 141 - Barred placing/taking: Can no longer freely take items from save spots, or place items in save spots OR the output slot.

2022-10-17 140 - Touchstone enchanting behaving correctly: Items now enchant and make a copy into slots 3-6. Will need logic to destroy all items in slots 2-6 on Block.drosp(). Still need to fix toolbar.

2022-10-15 139 - Addressed crash: 
2022-10-14 138 - Fixed Sizing Issue: UI is now correctly sized. Crashing after 5 slots for some reason.

2022-10-11 137 - Unblocked: Fixed roadblock on Touchstone. Error in writing of cast. Block register was still set to cast to a new block instead of an instance of TouchstoneBlock class. UI for touchstone is not appropriately sized. Will need to correct.

2022-10-08 136 - Still broken: 
2022-10-08 135 - Started Review of Code: 
2022-10-08 134 - Tested Touchstone, Menu is not working: Can't get touchstone inventory to open, despite several rework attempts. Will need to go through the entire set of classes and locate the problem.

2022-10-06 133 - wrapped up implementation on Touchstone: All required entities should be functional, but still need a gui texture.

2022-10-05 132 - Started Implementation of touchstone: 
2022-10-03 131 - Finished Tick logic for Touchstone: Still need to get the block to the implementation phase so it can be tested.

2022-10-03 130 - More Touchstone Work: 
2022-10-02 129 - Trying to Implement touchstone functionality: Roadblock on touchstone logic. Unable to locate IInventory interface from tutorial.

2022-09-29 128 - Touchstone is now a Tile Entity: Trying to implement Touchstone functionality. No event exists that allows me to check block status. Will probably need to either implement custom event or make implementation for lesser enchantment entirely inside Touchstone. TODO: Add checker to make sure enchantment in Inventory Handler checks the enchantment level once the lesser version is implemented.

2022-09-28 127 - Added Dependency, Began Retrofit of Enchantment: Began altering event to force users to interact with touchstone to retrieve their items. Needs finishing still.

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

2022-09-08 Merge pull request #1 from SubjectDeltaV/000-Phase-1-Basic: Met Phase 1 goal (100 dev series)
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
