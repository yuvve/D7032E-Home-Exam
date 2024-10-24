package assets.impl;

import assets.IResource;

public enum PointSaladResource implements IResource {
    PEPPER("PEPPER"),
    LETTUCE("LETTUCE"),
    CARROT("CARROT"),
    CABBAGE("CABBAGE"),
    ONION("ONION"),
    TOMATO("TOMATO");

    private final String type;

    PointSaladResource(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    // ENUM has an equals method that compares the object reference,
    // so we don't need to (and cannot) override IResource's equals method
    // @Override
    // public boolean equals(Object obj) ...

    @Override
    public String represent() {
        return type.substring(0, 3);
    }
}
