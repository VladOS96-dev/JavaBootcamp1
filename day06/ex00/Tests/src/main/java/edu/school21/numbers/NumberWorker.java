package edu.school21.numbers;


import java.util.stream.IntStream;

public class NumberWorker {

    public boolean isPrime(int number) {
        if (number < 2) {
            throw new IllegalNumberException("Number must be greater than 1");
        }
        if (number == 2) return true;
        if (number % 2 == 0) return false;
        return IntStream.rangeClosed(3, (int) Math.sqrt(number))
                .filter(n -> n % 2 != 0)
                .noneMatch(n -> number % n == 0);
    }

    public int digitsSum(int number) {
        return String.valueOf(Math.abs(number))
                .chars()
                .map(Character::getNumericValue)
                .sum();
    }
}