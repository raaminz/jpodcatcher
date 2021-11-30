package com.raminzare.jpodcatcher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

class NumberGeneratorTest {


    @RepeatedTest(value = 100 , name = "generatedNumber {currentRepetition}")
    void generateNumber_shouldGeneratePositiveIntegers(RepetitionInfo info){
        Assertions.assertTrue(NumberGenerator.generateNumber() > 0);
    }

}