package com.eaw1805.www.commands;

/**
 * Holds the arguments for user-related commands.
 */
public class UserCommand {

    private String name;

    private String action;

    public UserCommand() {
        name = null;
        action = null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String thisName) {
        name = thisName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }
}
