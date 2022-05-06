package com.liu.mod.blocks;

import net.minecraft.util.StringRepresentable;

public enum ControlPart implements StringRepresentable {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    private ControlPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
