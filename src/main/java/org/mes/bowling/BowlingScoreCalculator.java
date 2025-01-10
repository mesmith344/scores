package org.mes.bowling;

import org.mes.bowling.exception.InvalidRollCharacterException;
import org.mes.bowling.exception.InvalidRollSequenceException;

import java.util.*;

public class BowlingScoreCalculator {
    private static final char STRIKE = 'X';
    private static final char SPARE = '/';
    private static final char FOUL = 'F'; // equivalent to a '0' for scoring
    private static final char MISS = '-'; // equivalent to a '0' for scoring

    private static final Character[] VALID_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', STRIKE, SPARE, FOUL, MISS};
    private static final List<Character> VALID_CHARS_LIST = Arrays.asList(VALID_CHARS);

    private BowlingScoreCalculator() {};

    public static List<Integer> calculateScores(char[] rollChars) {
        List<Integer> scores = new ArrayList<>();

        List<Roll> rolls = ingestRollChars(rollChars);
        if (rolls.isEmpty()) { return scores; }
        int numRolls = rolls.size();

        // find which frames are present in the rolls
        Set<Integer> framesPresent = new TreeSet<>(); // use TreeSet to order set by frame number
        for (Roll roll : rolls) { framesPresent.add(roll.getFrame()); }

        int startIndex;
        for (int currentFrame: framesPresent) {
            startIndex = 0;
            while (rolls.get(startIndex).getFrame() != currentFrame) {
                startIndex++;
            }
            // startIndex is now the index of the first roll in the current frame

            if (startIndex == numRolls - 1) {
                // the first roll in the current frame is the last roll in the list,
                // which means we don't have enough rolls to define this frame's score
                scores.add(null);
                continue;
            }

            // if we get here, there must be at least one roll after the first roll in the frame
            Roll firstRollInFrame = rolls.get(startIndex);
            Roll rollAfterFirstRoll = rolls.get(startIndex + 1);
            if (!firstRollInFrame.isStrike() && !rollAfterFirstRoll.isSpare()) {
                // this frame has two rolls with no strikes or spares
                scores.add(firstRollInFrame.getPins() + rollAfterFirstRoll.getPins());
                continue;
            }

            if (!firstRollInFrame.isStrike() && rollAfterFirstRoll.isSpare()) {
                // this frame has a spare, so we need a third roll to define the score
                if (startIndex == numRolls - 2) {
                    // no third roll exists
                    scores.add(null);
                    continue;
                } else {
                    // a third roll exists
                    Roll secondRollAfterFirstRoll = rolls.get(startIndex + 2);
                    scores.add(firstRollInFrame.getPins() + rollAfterFirstRoll.getPins() + secondRollAfterFirstRoll.getPins());
                    continue;
                }
            }

            if (firstRollInFrame.isStrike()) {
                // this frame has a strike, so we need two more rolls to define the score
                if (startIndex >= numRolls - 2) {
                    // we don't have enough rolls to define the score
                    scores.add(null);
                    // continue is not needed here
                } else {
                    // we have enough rolls to define the score
                    Roll secondRollAfterFirstRoll = rolls.get(startIndex + 2);
                    scores.add(firstRollInFrame.getPins() + rollAfterFirstRoll.getPins() + secondRollAfterFirstRoll.getPins());
                    // continue is not needed here
                }
            }
        } // iterate over all present frames

        return scores;
    }

    public static List<Roll> ingestRollChars(char[] rollChars) {
        List<Roll> rolls = new ArrayList<>();
        if (rollChars.length == 0) { return rolls; }

        int currentFrame = 1;
        int currentRollInFrame = 1;
        int remainingPins = 10;

        for(char rollChar: rollChars) {
            if (!isValidRollCharacter(rollChar)) {
                throw new InvalidRollCharacterException("'" + rollChar + "' is not a valid character for bowling scores.");
            }

            // rules for frames 1 through 9
            if (currentFrame < 10) {
                if (currentRollInFrame != 1 && rollChar == STRIKE) {
                    throw new InvalidRollSequenceException("Strikes may only happen on the first roll of frames 1 through 9.");
                }

                if (currentRollInFrame != 2 && rollChar == SPARE) {
                    throw new InvalidRollSequenceException("Spares may only happen on the second roll of frames 1 through 9.");
                }
            }

            // strikes
            if (rollChar==STRIKE) {
                rolls.add(new Roll(currentFrame, currentRollInFrame, 10, true, false, false));

                if (currentFrame<10) currentFrame++;

                continue;
            }

            // spares
            if (rollChar==SPARE) {
                rolls.add(new Roll(currentFrame, currentRollInFrame, remainingPins, false, true, false));

                if (currentFrame<10) currentFrame++;
                currentRollInFrame = 1;
                remainingPins = 10;

                continue;
            }

            // neither a strike nor a spare
            int pinsToppled = numberOfPinsToppled(rollChar);
            rolls.add(new Roll(currentFrame, currentRollInFrame, pinsToppled, false, false, false));
            if (currentRollInFrame == 1) {
                currentRollInFrame = 2;
                remainingPins = 10 - pinsToppled;
            } else {
                if (currentFrame<10) currentFrame++;
                currentRollInFrame = 1;
                remainingPins = 10;
            }


        } // rollChars

        return rolls;
    }

    private static boolean isValidRollCharacter(Character roll) {
        return VALID_CHARS_LIST.contains(roll);
    }

    // this method is never called for spares ('X') or strikes ('/')
    private static int numberOfPinsToppled(char rollChar) {
        return switch (rollChar) {
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            default -> 0; // this covers '0', 'F' (foul), and '-' (miss)
        };
    }
}
