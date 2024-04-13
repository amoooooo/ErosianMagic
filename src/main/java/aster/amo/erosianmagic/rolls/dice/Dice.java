package aster.amo.erosianmagic.rolls.dice;

import net.minecraft.util.RandomSource;

import java.util.Random;

public class Dice {
    public static final Dice D4 = new Dice(4);
    public static final Dice D6 = new Dice(6);
    public static final Dice D8 = new Dice(8);
    public static final Dice D10 = new Dice(10);
    public static final Dice D12 = new Dice(12);
    public static final Dice D20 = new Dice(20);
    public static final Dice D100 = new Dice(100);
    private final int sides;

    public Dice(int sides) {
        this.sides = sides;
    }

    public int getSides() {
        return sides;
    }

    public int roll(RandomSource randomSource, float luck, int bonus) {
        int result = 0;

        // Determine the base roll
        int baseRoll = randomSource.nextInt(sides) + 1;

        // Calculate the range expansion factor based on luck
        float rangeExpansion = 1 + (luck / 10.0f);

        // Expand the range of possible outcomes
        int expandedMax = Math.round(sides * rangeExpansion);

        // Re-roll if the base roll exceeds the original range
        while (baseRoll > sides) {
            baseRoll = randomSource.nextInt(sides) + 1;
        }

        // Roll the dice within the expanded range
        int expandedRoll = randomSource.nextInt(expandedMax) + 1;

        // Map the expanded roll back to the original range
        result = Math.min(expandedRoll, sides);

        // Add the bonus
        result += bonus;

        return result;
    }

    public static int rollMultiple(RandomSource randomSource, Dice dice, int count, float luck, int bonus) {
        int result = 0;
        for (int i = 0; i < count; i++) {
            result += dice.roll(randomSource, luck, bonus);
        }
        return result;
    }
}
