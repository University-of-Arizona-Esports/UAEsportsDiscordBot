package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.*;

/**
 * Holds info for a command parameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Params.class)
@Target(ElementType.METHOD)
public @interface Param {
    int index();
    String name();
    String description();
}
