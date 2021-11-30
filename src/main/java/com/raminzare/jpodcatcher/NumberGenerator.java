package com.raminzare.jpodcatcher;

import java.util.Random;

public class NumberGenerator {

    static Random random = new Random();

    public static int generateNumber(){
        return random.nextInt(0, Integer.MAX_VALUE);
    }
}
