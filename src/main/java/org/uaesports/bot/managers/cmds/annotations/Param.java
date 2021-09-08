package org.uaesports.bot.managers.cmds.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Params.class)
public @interface Param {
    int index();
    String name();
    String description();
}
