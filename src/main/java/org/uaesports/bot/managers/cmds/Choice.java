package org.uaesports.bot.managers.cmds;

public interface Choice<T> {
    
    String getName();
    
    T getValue();
    
}
