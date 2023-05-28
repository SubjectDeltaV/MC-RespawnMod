package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.gui.TouchstoneMenu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
/**
 * Initializer for our Menus
 * @author Mount
 *
 */

public class MenuTypesInit 
{
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, spiritw.MODID);
	
	public static final RegistryObject<MenuType<TouchstoneMenu>> TOUCHSTONE_MENU = registerMenuType(TouchstoneMenu::new, "touchstone_menu");
	
	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name)
	{
		return MENUS.register(name, () -> IForgeMenuType.create(factory));
	}
	
	public static void register(IEventBus eventBus)
	{
		MENUS.register(eventBus);
	}
}
