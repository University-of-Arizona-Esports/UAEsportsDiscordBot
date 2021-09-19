package org.uaesports.bot.managers.components;

import org.javacord.api.interaction.MessageComponentInteraction;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispatches component interaction events to the handler methods.
 */
public class ComponentManager {
    
    private Map<String, ComponentMethod> handlers = new HashMap<>();
    
    /**
     * Adds all handler methods from a component handler instance.
     */
    public void add(ComponentGroup handler) {
        handler.getMethods().forEach((id, method) -> {
            if (handlers.containsKey(id)) {
                throw new IllegalStateException("Duplicate handler for component id: " + id);
            }
            handlers.put(id, method);
        });
    }
    
    /**
     * Dispatch an interaction to the associated handler method.
     */
    public final void handle(MessageComponentInteraction interaction) {
        var id = interaction.getCustomId();
        var method = handlers.getOrDefault(id, null);
        if (method != null) method.handle(interaction, id);
        // else we have no handler for this ID
    }
    
}
