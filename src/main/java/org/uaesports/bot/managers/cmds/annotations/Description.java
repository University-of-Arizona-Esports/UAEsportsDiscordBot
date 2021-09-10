package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description of a command
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String value();
}
