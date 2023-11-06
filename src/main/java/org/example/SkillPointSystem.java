package org.example;

import java.util.Random;

public class SkillPointSystem {
    private int skillPoints;

    private Random random;

    public SkillPointSystem() {
        this.skillPoints = 0;
        this.random = new Random();
    }

    public void addSkillPoint() {
        int chance = random.nextInt(2);  // Generates a random number between 0 (inclusive) and 4 (exclusive)
        if (chance == 0) {  // If the random number is 0, add a skill point
            this.skillPoints += 1;
        }
    }

    public boolean useSkillPoint() {
        if (this.skillPoints > 0) {
            this.skillPoints -= 1;
            return true;
        }
        return false;
    }

    public int getSkillPoints() {
        return this.skillPoints;
    }
}
