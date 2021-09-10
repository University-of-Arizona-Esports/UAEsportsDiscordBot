package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a method that is executed when a command is run.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Execute {
}
