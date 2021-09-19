package org.uaesports.bot.managers.cmds;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public interface CommandHandler {
    
    /**
     * Called when an exception occurs while parsing a parameter.
     * @param sci Interaction object.
     * @param provider Option for the current command (use one of the getByName methods with paramName on this object to get parameter values)
     * @param paramName Name of the parameter that failed.
     * @param cause The exception that was thrown while parsing the parameter.
     */
    default void onInvalidParameter(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, String paramName, Throwable cause) { }
    
}
