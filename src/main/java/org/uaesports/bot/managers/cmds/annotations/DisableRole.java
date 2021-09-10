package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.*;

/**
 * Disable a role for a command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DisabledRoles.class)
public @interface DisableRole {
    long value();
}
