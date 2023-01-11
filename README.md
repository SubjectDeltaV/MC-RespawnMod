# MC-RespawnMod
A Minecraft mod that re-imageines respawning. Tired of your choices being keep all items or get stuck in a death loop while you desparately try to get your items back?
This mod is probably for you then. 

This mod depends on the Corpse Mod. Please ensure you have it in your load order when testing.
JEI/NEI is reccomended for recipe lookup until the recipes are set. I will list the ingrediants below. 

NOTICE: THIS MOD IS IN EARLY BETA! Expect bugs and lots of changes while balance and functionality is iterated upon!

## How it Works
As of the current version, there are a few outcomes when your health reaches zero. 

In most cases, you will be put into a wounded or downed state when your health reaches zero. Your visibility and movement will be severely limited.
You can save yourself with a drinkable REVIVE potion or a splash potion of REVIVE from a friend. In a wounded state, your hunger will tick away, when it reaches zero,
you will experience a vanilla death. 

You can avoid that if you choose to try a SPIRITWALK. Using your Spirit Lantern, right click when downed to be put into a Ghost state. In this state, you are 
invulnerable, but have limited vision. You must navigate to a TOUCHSTONE block and right click on it, this will return you to a normal state and, if you have items with
the right enchantment, those items will be returned to you. All other items will be safely stored in a corpse. Note that some death types (drowning, suffocation, starvation,
falling into the void) will skip the wounded state and initiate a spiritwalk right away. While in a spiritwalk, the experience you had on death is your health. You must
navigate back to your touchstone BEFORE your xp reaches zero. If it does, you will experience a vanilla death.

True death just means you will respawn normally in a bed and must retrieve your items the old fashioned way from your corpse. 

## The Enchantment
The SPIRITBOUND Enchantment is the current way to ensure you keep your items after a succesfull spiritwalk.

The FIRST LEVEL of SPIRITBOUND can be obtained from a placed touchstone. Simply run your items through the touchstone and they will return enchanted.
Note the item limit of the touchstone is currently 4 for this level of the enchantment. While you can obtain this enchantment from the enchanting table,
it will cost you experience, AND you must still run your items through the touchstone to ensure they are returned to you. In the future, you will be able
to increase the capacity of the TOUHCSTONE by upgrading the touchstone (not yet implemented)

The SECOND LEVEL of SPIRITBOUND is not currently functional. Once implemented it will be obtainable through your SPIRIT LANTERN, once upgraded (also not
implemented). The planned capacity of the lantern is 8, as opposed to the 4 of the TOUCHSTONE. You can also combine the two for a total of twelve enchanted 
items. Since you will always have your lantern on your person, items enchanted through it will be returned to you regardless of the touchstone you use.

Future levels of the enchantment are planned, each with more convenience, but more rarity.

## Recipes & Drops
The SPIRIT LANTERN and TOUCHSTONE have some unique items that must be crafted before you can craft them.

SOUL SAND can now be obtained from skeleton kills (temporary)

SOUL GEM = 8 ZOMBIE FLESH + 1 ENDER PEARL

BINDINGSTONE = SOUL GEM + STONE

SOUL STONE = SOUL SAND + DEEPSLATE

SPIRIT LANTERN = IRON INGOT + BINDINGSTONE

TOUCHSTONE = FLINT + BONE + SOUL STONE

The splash potion can be crafted from the crafting table

INACTIVE REVIVE POTION = Water Bottle + Zombie Flesh + Gun Powder

REVIVE POTION = INACTIVE REVIVE + SPIRIT LANTERN (You will keep the lantern)

The drinkable can only be made in the brewing stand (not yet implemented)