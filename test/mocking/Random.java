package mocking;

public class Random extends java.util.Random {
    private int nextInt;
    private float nextFloat;

    public Random() {
        this.nextInt = 0;
        this.nextFloat = 0;
    }

    @Override
    public int nextInt(int bound) {
        if (nextInt >= bound) nextInt = 0;
        return nextInt++;
    }

    @Override
    public float nextFloat() {
        if (nextFloat >= 1) nextFloat = 0;
        float currentFloat = nextFloat;
        nextFloat+=0.1f;
        return currentFloat;
    }
}
