package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Holds the collection of @Params on a method
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {
    Param[] value();
}
