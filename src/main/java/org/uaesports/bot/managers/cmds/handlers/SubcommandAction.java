package org.uaesports.bot.managers.cmds.handlers;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;
import org.uaesports.bot.managers.cmds.CommandHandler;

/**
 * A portion of a command that is named.
 */
public class SubcommandAction implements NamedInteractionHandler {
    
    private String name;
    private CommandAction action;
    
    public SubcommandAction(String name, CommandAction action) {
        this.name = name;
        this.action = action;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void handle(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, CommandHandler instance) {
        action.handle(sci, provider.getFirstOption().orElse(null), instance);
    }
    
}
