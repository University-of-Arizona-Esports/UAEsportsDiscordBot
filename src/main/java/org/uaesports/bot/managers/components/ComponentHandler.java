package org.uaesports.bot.managers.components;

import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.uaesports.bot.managers.components.annotations.ComponentParam;

public interface ComponentHandler {
    
    /**
     * Optional method. Convenience method to get the components that this
     * handler uses.
     */
    HighLevelComponent[] getComponents();
    
    /**
     * Called when a {@link ComponentParam} conversion throws an exception.
     */
    default void onException(MessageComponentInteraction interaction, String id, Throwable cause) { }
    
}
