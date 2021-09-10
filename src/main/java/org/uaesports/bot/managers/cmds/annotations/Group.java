package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Group/folder for subcommands
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Group {
    String name();
    String description();
}
