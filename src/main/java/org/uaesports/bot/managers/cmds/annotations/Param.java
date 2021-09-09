package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Holds info for a command parameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Params.class)
public @interface Param {
    int index();
    String name();
    String description();
}
