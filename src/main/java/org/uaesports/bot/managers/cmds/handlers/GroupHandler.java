package org.uaesports.bot.managers.cmds.handlers;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

import java.util.List;

/**
 * A portion of a command that could have multiple options.
 * This includes the top level command, and groups.
 */
public class GroupHandler implements NamedInteractionHandler {
    
    private String name;
    private List<NamedInteractionHandler> handlers;
    
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
    public void handle(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, Object instance) {
        var option = provider.getFirstOption().get();
        var name = option.getName();
        NamedInteractionHandler handler = handlers.stream()
                                                  .filter(h -> h.getName().equals(name))
                                                  .findFirst()
                                                  .get();
        handler.handle(sci, provider, instance);
    }
    
}
