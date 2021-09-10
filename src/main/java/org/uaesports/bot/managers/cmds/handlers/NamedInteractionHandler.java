package org.uaesports.bot.managers.cmds.handlers;

/**
 * A part of a command that has a name, such as a subcommand or group.
 */
public interface NamedInteractionHandler extends InteractionHandler {
    
    String getName();
    
}
