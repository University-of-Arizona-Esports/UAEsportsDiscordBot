package org.uaesports.bot.managers.cmds.handlers;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;
import org.uaesports.bot.managers.cmds.Command;

import java.util.List;

/**
 * A portion of a command that could have multiple options.
 * This includes the top level command, and groups.
 */
public class GroupHandler implements NamedInteractionHandler {
    
    private String name;
    private List<NamedInteractionHandler> handlers;
    
    /**
     * @param name Name of this group.
     * @param handlers If this is the root handler, List of Groups/Subcommands on the base command.
     * If this is a command group, List of subcommands.
     */
    public GroupHandler(String name, List<NamedInteractionHandler> handlers) {
        this.name = name;
        this.handlers = handlers;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public void handle(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, Command instance) {
        var option = provider.getFirstOption().get();
        var name = option.getName();
        NamedInteractionHandler handler = handlers.stream()
                                                  .filter(h -> h.getName().equals(name))
                                                  .findFirst()
                                                  .get();
        // If this is the root group, pass the provider along.
        // If this is a real command group, provide the subcommand.
        handler.handle(sci, this.name == null ? provider : option, instance);
    }
    
}
