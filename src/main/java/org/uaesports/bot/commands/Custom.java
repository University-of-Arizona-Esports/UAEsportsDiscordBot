package org.uaesports.bot.commands;

import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.uaesports.bot.managers.cmds.Command;
import org.uaesports.bot.managers.cmds.annotations.Description;
import org.uaesports.bot.managers.cmds.annotations.Execute;
import org.uaesports.bot.managers.cmds.annotations.Name;

@Name("custom")
@Description("Command that uses custom parameters")
public class Custom extends Command {
    
    private HighLevelComponent[] components;
    
    public HighLevelComponent[] getComponents() {
        return components;
    }
    
    public void setComponents(HighLevelComponent[] components) {
        this.components = components;
    }
    
    @Execute
    public void call(SlashCommandInteraction sci) {
        sci.createImmediateResponder()
                .append("Test these buttons")
                .addComponents(components)
                .respond();
    }
    
}
