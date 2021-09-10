package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.*;

/**
 * Enable a role for a command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(EnabledRoles.class)
public @interface EnableRole {
    long value();
}
