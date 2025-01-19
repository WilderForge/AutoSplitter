package com.wildermods.autosplitter.config;

import com.badlogic.gdx.scenes.scene2d.ui.RuntimeSkin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.wildermods.wilderforge.api.modLoadingV1.config.ModConfigurationEntryBuilder;
import com.worldwalkergames.ui.NiceButton;
import com.worldwalkergames.ui.NiceLabel;

public class AutosplitterModConfigurationEntryBuilder extends ModConfigurationEntryBuilder {

	public AutosplitterModConfigurationEntryBuilder(ConfigurationUIContext context) {
		super(context);
	}

	@Override
	public void buildValueSpan(ConfigurationUIEntryContext context) {
		if(context.getField().getName().equals("autosplit")) {
			buildAutosplitterSection(context);
		}
		else if(context.getField().getName().equals("host")) {
			buildServletSection(context);
		}
		super.buildValueSpan(context);
	}
	
	private void buildAutosplitterSection(ConfigurationUIEntryContext context) {
		Table table = context.fieldTable;
		String settingsText = gameStrings.ui("autosplitter.ui.configure.section.splitter.settings");
		RuntimeSkin skin = context.popup.getDependencies().skin;
		NiceButton button = new NiceButton(settingsText, skin, "bigDialogButton");
		table.add(
			new NiceLabel(settingsText, skin, "display-regular-subHeader", "darkText")
		).colspan(4).prefHeight(Value.percentHeight(1.5f, button));
		table.row();
	}

	private void buildServletSection(ConfigurationUIEntryContext context) {
		Table table = context.fieldTable;
		RuntimeSkin skin = context.popup.getDependencies().skin;
		String buttonText = gameStrings.ui("autosplitter.ui.configure.button.status");
		String settingsText = gameStrings.ui("autosplitter.ui.configure.section.server.settings");
		NiceButton button = new NiceButton(buttonText, skin, "bigDialogButton");
		table.add().prefHeight(Value.percentHeight(1.5f, button));
		table.row();
		table.add(
			new NiceLabel(settingsText, skin, "display-regular-subHeader", "darkText")
		).colspan(4).prefHeight(Value.percentHeight(1.5f, button));
		table.row();
		table.add(button).colspan(4).fillX();
		table.row();
	}
	
}
