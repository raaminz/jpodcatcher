package com.raminzare.jpodcatcher.internal;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicTestTest {

    @TestFactory
    Stream<DynamicTest> checkEvenNumbers(){
        return IntStream.iterate(2, n -> n + 2).limit(10)
                .mapToObj(n -> DynamicTest.dynamicTest("test" + n,
                        () -> assertEquals(0, n % 2)));
    }


}
