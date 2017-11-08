package me.paul.triangles;

public enum Gravity {
    OFF, SIMPLE, TRUE, POINT, MULTI_POINT;
    private static Gravity[] values = values();
    public Gravity next()
    {
        return values[(this.ordinal() + 1) % values.length];
    }
}
