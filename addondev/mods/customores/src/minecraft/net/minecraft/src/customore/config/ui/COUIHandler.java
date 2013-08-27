package net.minecraft.src.customore.config.ui;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FCAddOnHandler;
import net.minecraft.src.GuiCreateWorld;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSelectWorld;
import net.minecraft.src.customore.config.COWorldConfig;
import net.minecraft.src.customore.config.ui.GuiCustomOreGenSettings.GuiOpenMenuButton;

public class COUIHandler {

    private static Object _worldCreationGui = null;
    private static Object _optionsGuiButton = null;

	public static void onClientTick() {

		Minecraft mc = Minecraft.getMinecraft();

		if (mc.theWorld == null && mc.currentScreen != null)
		{
			if (mc.currentScreen instanceof GuiCreateWorld)
			{
				if (_worldCreationGui == null)
				{
					_worldCreationGui = mc.currentScreen;
				}

				onWorldCreationMenuTick((GuiCreateWorld)mc.currentScreen);
			}
			else if (_worldCreationGui != null && (mc.currentScreen instanceof GuiSelectWorld || mc.currentScreen instanceof GuiMainMenu))
			{
				_worldCreationGui = null;
				onWorldCreationMenuTick((GuiCreateWorld)null);
			}
		}
		else if (_worldCreationGui != null)
		{
			_worldCreationGui = null;
			onWorldCreationMenuTick((GuiCreateWorld)null);
		}

	}
	
	public static void onWorldCreationMenuTick(GuiCreateWorld gui)
    {
        if (gui == null)
        {
            _optionsGuiButton = null;
        }
        else
        {
            if (_optionsGuiButton == null)
            {
                COWorldConfig.loadedOptionOverrides[0] = null;
                GuiCustomOreGenSettings button = new GuiCustomOreGenSettings(gui);
                _optionsGuiButton = new GuiOpenMenuButton(gui, 99, 0, 0, 150, 20, "Custom Ore Generation...", button);
            }

            GuiOpenMenuButton button1 = (GuiOpenMenuButton)_optionsGuiButton;
            
            
            Collection controlList = (Collection)gui.getButtonList();

            if (!controlList.contains(button1))
            {
                button1.xPosition = (gui.width - button1.getWidth()) / 2;
                button1.yPosition = 165;
                controlList.add(button1);
            }

        }
    }

}
