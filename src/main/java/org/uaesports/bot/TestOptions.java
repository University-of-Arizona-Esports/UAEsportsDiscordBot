package org.uaesports.bot;

import org.uaesports.bot.managers.cmds.Choice;

public enum TestOptions implements Choice {
    
    OP_A("Option A"),
    OP_B("Option B");
    
    private String name;
    
    TestOptions(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name();
    }
    
}
