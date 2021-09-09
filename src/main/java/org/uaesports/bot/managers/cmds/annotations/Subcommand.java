package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Make a command a subcommand
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Subcommand {
    String name();
    String description();
}
