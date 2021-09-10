package org.uaesports.bot.managers.cmds;

/**
 * Used on an enum to make a predefined set of choices as a parameter.
 */
public interface Choice {
    
    String getName();
    
    default int getValue() {
        return ((Enum<?>) this).ordinal();
    }
    
}
