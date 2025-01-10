package org.mes.bowling;

import org.junit.Test;
import org.mes.bowling.exception.InvalidRollCharacterException;
import org.mes.bowling.exception.InvalidRollSequenceException;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class BowlingScoreCalculatorTest {

    @Test(expected = InvalidRollCharacterException.class)
    public void ingestRollChars_invalidInputCharacter_exceptionThrown() {
        char[] sequenceWithInvalidRollCharacter = {'4', '3', 'Q', '5'};
        BowlingScoreCalculator.ingestRollChars(sequenceWithInvalidRollCharacter);
    }

    @Test(expected = InvalidRollSequenceException.class)
    public void ingestRollChars_invalidSequenceWithSpareOnFirstRoll_exceptionThrown() {
        char[] sequenceWithSpareOnFirstRoll = {'/', '3', '5'};
        BowlingScoreCalculator.ingestRollChars(sequenceWithSpareOnFirstRoll);
    }

    @Test(expected = InvalidRollSequenceException.class)
    public void ingestRollChars_invalidSequenceWithStrikeOnSecondRoll_exceptionThrown() {
        char[] sequenceWithSpareOnFirstRoll = {'1', 'X', '5'};
        BowlingScoreCalculator.ingestRollChars(sequenceWithSpareOnFirstRoll);
    }

    @Test(expected = InvalidRollSequenceException.class)
    public void ingestRollChars_invalidSequenceWithSpareAfterStrike_exceptionThrown() {
        char[] sequenceWithSpareOnFirstRoll = {'X', '/', '5'};
        BowlingScoreCalculator.ingestRollChars(sequenceWithSpareOnFirstRoll);
    }

    @Test
    public void ingestRollChars_emptySequence_resultsInEmptyRollsList() {
        char[] emptySequence = {};
        assertTrue(BowlingScoreCalculator.ingestRollChars(emptySequence).isEmpty());
    }

    @Test
    public void ingestRollChars_validSequence_correctResult() {
        char[] sequence = {'4', '/', 'X', '8'};
        List<Roll> rolls = BowlingScoreCalculator.ingestRollChars(sequence);
        assertEquals(4, rolls.size());

        Roll roll = rolls.get(0);
        assertEquals(1, roll.getFrame());
        assertEquals(1, roll.getRollInFrame());
        assertEquals(4, roll.getPins());
        assertFalse(roll.isStrike());
        assertFalse(roll.isSpare());

        roll = rolls.get(1);
        assertEquals(1, roll.getFrame());
        assertEquals(2, roll.getRollInFrame());
        assertEquals(6, roll.getPins());
        assertFalse(roll.isStrike());
        assertTrue(roll.isSpare());

        roll = rolls.get(2);
        assertEquals(2, roll.getFrame());
        assertEquals(1, roll.getRollInFrame());
        assertEquals(10, roll.getPins());
        assertTrue(roll.isStrike());
        assertFalse(roll.isSpare());

        roll = rolls.get(3);
        assertEquals(3, roll.getFrame());
        assertEquals(1, roll.getRollInFrame());
        assertEquals(8, roll.getPins());
        assertFalse(roll.isStrike());
        assertFalse(roll.isSpare());
    }

    @Test
    public void calculateScores_emptySequence_resultsInEmptyScoresList() {
        char[] sequence = {};
        assertTrue(BowlingScoreCalculator.calculateScores(sequence).isEmpty());
    }

    @Test
    public void calculateScores_singleRoll_resultsInScoresListWithSingleNull() {
        char[] sequence1 = {'3'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence1);
        assertEquals(1, scores.size());
        assertNull(scores.get(0));

        char[] sequence2 = {'X'};
        scores = BowlingScoreCalculator.calculateScores(sequence2);
        assertEquals(1, scores.size());
        assertNull(scores.get(0));
    }

    @Test
    public void calculateScores_sequenceWithoutSparesOrStrikes_correctResult() {
        char[] sequence = {'0', '1', '2', '3', '4', '5'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(3, scores.size());
        assertEquals(1, (int) scores.get(0));
        assertEquals(5, (int) scores.get(1));
        assertEquals(9, (int) scores.get(2));
    }

    @Test
    public void calculateScores_sequenceWithUndefinedSpare_correctResult() {
        char[] sequence = {'0', '/'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(1, scores.size());
        assertNull(scores.get(0));
    }

    @Test
    public void calculateScores_sequenceWithDefinedSpare_correctResult() {
        char[] sequence = {'5', '/', '5'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(2, scores.size());
        assertEquals(15, (int) scores.get(0));
        assertNull(scores.get(1));
    }

    @Test
    public void calculateScores_sequenceWithUndefinedStrike_correctResult() {
        char[] sequence = {'X', '3'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(2, scores.size());
        assertNull(scores.get(0));
        assertNull(scores.get(1));
    }

    @Test
    public void calculateScores_sequenceWithDefinedStrike_correctResult() {
        char[] sequence = {'X', '3', '5'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(2, scores.size());
        assertEquals(18, (int) scores.get(0));
        assertEquals(8, (int) scores.get(1));
    }

    @Test
    public void calculateScores_sequenceWithDefinedStrikeAndUndefinedSpare_correctResult() {
        char[] sequence = {'X', '8', '/'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(2, scores.size());
        assertEquals(20, (int) scores.get(0));
        assertNull(scores.get(1));
    }

    @Test
    public void calculateScores_firstExampleFromCodingExercise_correctResult() {
        char[] sequence = {'4', '5', 'X', '8'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(3, scores.size());
        assertEquals(9, (int) scores.get(0));
        assertNull(scores.get(1));
        assertNull(scores.get(2));
    }

    @Test
    public void calculateScores_secondExampleFromCodingExercise_correctResult() {
        char[] sequence = {'4', '5', 'X', '8', '1'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(3, scores.size());
        assertEquals(9, (int) scores.get(0));
        assertEquals(19, (int) scores.get(1));
        assertEquals(9, (int) scores.get(2));
    }

    @Test
    public void calculateScores_threeStrikes_correctResult() {
        char[] sequence = {'X', 'X', 'X'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(3, scores.size());
        assertEquals(30, (int) scores.get(0));
        assertNull(scores.get(1));
        assertNull(scores.get(2));
    }

    @Test
    public void calculateScores_nineStrikes_correctResult() {
        char[] sequence = {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(9, scores.size());
        assertEquals(30, (int) scores.get(0));
        assertEquals(30, (int) scores.get(1));
        assertEquals(30, (int) scores.get(2));
        assertEquals(30, (int) scores.get(3));
        assertEquals(30, (int) scores.get(4));
        assertEquals(30, (int) scores.get(5));
        assertEquals(30, (int) scores.get(6));
        assertNull(scores.get(7));
        assertNull(scores.get(8));
    }

    @Test
    public void calculateScores_nineStrikesAndTwoGutters_correctResult() {
        char[] sequence = {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '-', '-'};  // '-' indicates a miss, which is equivalent to a '0'
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(10, scores.size());
        assertEquals(30, (int) scores.get(0));
        assertEquals(30, (int) scores.get(1));
        assertEquals(30, (int) scores.get(2));
        assertEquals(30, (int) scores.get(3));
        assertEquals(30, (int) scores.get(4));
        assertEquals(30, (int) scores.get(5));
        assertEquals(30, (int) scores.get(6));
        assertEquals(20, (int) scores.get(7));
        assertEquals(10, (int) scores.get(8));
        assertEquals(0, (int) scores.get(9));
    }

    @Test
    public void calculateScores_tenStrikesAndTwoGutters_correctResult() {
        char[] sequence = {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '-', '-'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(10, scores.size());
        assertEquals(30, (int) scores.get(0));
        assertEquals(30, (int) scores.get(1));
        assertEquals(30, (int) scores.get(2));
        assertEquals(30, (int) scores.get(3));
        assertEquals(30, (int) scores.get(4));
        assertEquals(30, (int) scores.get(5));
        assertEquals(30, (int) scores.get(6));
        assertEquals(30, (int) scores.get(7));
        assertEquals(20, (int) scores.get(8));
        assertEquals(10, (int) scores.get(9));
    }

    @Test
    public void calculateScores_elevenStrikesAndAGutter_correctResult() {
        char[] sequence = {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '-'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(10, scores.size());
        assertEquals(30, (int) scores.get(0));
        assertEquals(30, (int) scores.get(1));
        assertEquals(30, (int) scores.get(2));
        assertEquals(30, (int) scores.get(3));
        assertEquals(30, (int) scores.get(4));
        assertEquals(30, (int) scores.get(5));
        assertEquals(30, (int) scores.get(6));
        assertEquals(30, (int) scores.get(7));
        assertEquals(30, (int) scores.get(8));
        assertEquals(20, (int) scores.get(9));
    }

    @Test
    public void calculateScores_perfectGame_correctResult() {
        char[] sequence = {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(10, scores.size());
        assertEquals(30, (int) scores.get(0));
        assertEquals(30, (int) scores.get(1));
        assertEquals(30, (int) scores.get(2));
        assertEquals(30, (int) scores.get(3));
        assertEquals(30, (int) scores.get(4));
        assertEquals(30, (int) scores.get(5));
        assertEquals(30, (int) scores.get(6));
        assertEquals(30, (int) scores.get(7));
        assertEquals(30, (int) scores.get(8));
        assertEquals(30, (int) scores.get(9));
    }

    @Test
    public void calculateScores_spareInTheTenthFrame_correctResult() {
        char[] sequence = {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '9', '/', 'X'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(10, scores.size());
        assertEquals(30, (int) scores.get(0));
        assertEquals(30, (int) scores.get(1));
        assertEquals(30, (int) scores.get(2));
        assertEquals(30, (int) scores.get(3));
        assertEquals(30, (int) scores.get(4));
        assertEquals(30, (int) scores.get(5));
        assertEquals(30, (int) scores.get(6));
        assertEquals(29, (int) scores.get(7));
        assertEquals(20, (int) scores.get(8));
        assertEquals(20, (int) scores.get(9));
    }

    @Test
    public void calculateScores_missInTheTenthFrame_correctResult() {
        char[] sequence = {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '9', '-'};
        List<Integer> scores = BowlingScoreCalculator.calculateScores(sequence);
        assertEquals(10, scores.size());
        assertEquals(30, (int) scores.get(0));
        assertEquals(30, (int) scores.get(1));
        assertEquals(30, (int) scores.get(2));
        assertEquals(30, (int) scores.get(3));
        assertEquals(30, (int) scores.get(4));
        assertEquals(30, (int) scores.get(5));
        assertEquals(30, (int) scores.get(6));
        assertEquals(29, (int) scores.get(7));
        assertEquals(19, (int) scores.get(8));
        assertEquals(9, (int) scores.get(9));
    }
}
