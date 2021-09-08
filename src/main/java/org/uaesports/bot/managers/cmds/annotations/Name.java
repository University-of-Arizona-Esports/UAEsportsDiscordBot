package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Holds the name of a command.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    
    String value();
    
}
