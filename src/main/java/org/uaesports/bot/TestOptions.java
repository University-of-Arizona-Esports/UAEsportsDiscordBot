package org.uaesports.bot;

import org.uaesports.bot.managers.cmds.Choice;

public enum TestOptions implements Choice<Integer> {
    
    OP_A(5),
    OP_B(9);
    
    private int value;
    
    TestOptions(int i) {
        value = i;
    }
    
    
    @Override
    public String getName() {
        return name();
    }
    
    @Override
    public Integer getValue() {
        return value;
    }
}
