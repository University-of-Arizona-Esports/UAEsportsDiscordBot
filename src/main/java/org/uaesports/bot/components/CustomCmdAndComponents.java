package org.uaesports.bot.components;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.uaesports.bot.managers.cmds.CommandHandler;
import org.uaesports.bot.managers.cmds.annotations.Description;
import org.uaesports.bot.managers.cmds.annotations.Execute;
import org.uaesports.bot.managers.cmds.annotations.Name;
import org.uaesports.bot.managers.components.ComponentHandler;
import org.uaesports.bot.managers.components.annotations.Component;
import org.uaesports.bot.managers.components.annotations.ComponentParam;

@Name("custom")
@Description("Command that uses custom parameters")
public class CustomCmdAndComponents implements CommandHandler, ComponentHandler {
    
    @Execute
    public void call(SlashCommandInteraction sci) {
        sci.createImmediateResponder()
           .setFlags(MessageFlag.EPHEMERAL)
           .append("Test these buttons")
           .addComponents(getComponents())
           .respond();
    }
    
    @Override
    public HighLevelComponent[] getComponents() {
        // Get the components that this class handles.
        // Or not, this could return nothing if you make components elsewhere.
        var row = ActionRow.of(
                Button.secondary("buttonA", "Option A"),
                Button.secondary("buttonB", "Option B")
        );
        return new ActionRow[] {row};
    }
    
    // Can add additional params if you define how to obtain it from the interaction
    // Optional second parameter (String componentId) is also supported
    @ComponentParam
    public User getUser(MessageComponentInteraction interaction) {
        return interaction.getUser();
    }
    
    @Override
    public void onException(MessageComponentInteraction interaction, String id, Throwable cause) {
        // Optional method: Exception occurred during a @ComponentParam method
    }
    
    // Handles interactions from components with the specified ID
    // User param is available due to @ComponentParam method above
    @Component("buttonA")
    public void optionA(MessageComponentInteraction interaction, User user) {
        interaction.createImmediateResponder()
                   .setFlags(MessageFlag.EPHEMERAL)
                   .append("Good choice ").append(user)
                   .respond();
    }
    
    @Component("buttonB")
    public void optionB(MessageComponentInteraction interaction) {
        interaction.createImmediateResponder()
                   .setFlags(MessageFlag.EPHEMERAL)
                   .append("This was the worse option")
                   .respond();
    }
    
}
