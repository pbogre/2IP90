import java.util.Scanner;

/**
 * Reads a list of numbers, and can reconstruct the corresponding list of Palindromes,
 * produce the size of the largest magic set, and the content of that magic set.
 * 
 * Usage:
 * TODO: Documentation
 * 
 * END TODO
 * 
 * @author <NAME STUDENT 1>
 * @ID <ID STUDENT 1>
 * @author Pietro Bonaldo Gregori
 * @ID <ID STUDENT 2>
 * 
 */
class KingsPalindromeList {
    Scanner scanner = new Scanner(System.in);

    long[] getInput() {
        long task = scanner.nextLong();
        int size = scanner.nextInt();
        long[] input = new long[size + 1];
        input[0] = task;

        for(int i = 0; i < size; i++) {
            input[i + 1] = scanner.nextLong();
        }

        return input;
    }

    int countDigits(long number) {
        int count = 0;
        long copy = number;

        while (copy != 0) {
            copy /= 10;
            count++;
        }

        return count;
    }

    long[] longToArray(long number, int count) {
        long[] digits = new long[count];

        // Start at largest and divide from there 
        // to keep order of digits
        long magnitude = (long)Math.pow(10, count - 1);
        for (int i = 0; i < count; i++) {
            digits[i] = Math.floorDiv(number, magnitude) % 10;
            magnitude /= 10;
        }

        return digits;
    }

    long arrayToLong(long[] digits) {
        long number = 0;

        for(int i = 0; i < digits.length; i++) {
            number += digits[i] * Math.pow(10, digits.length - 1 - i);
        }

        return number;
    }

    long getNearestPalindrome(long[] digits){

        if (digits.length == 1) {
            return digits[0];
        }

        int middleIndex = Math.floorDiv(digits.length, 2);
        long middleDigit = digits[middleIndex];
        long[] leftHalf = new long[middleIndex];
        long[] rightHalf = new long[middleIndex];
 
        for (int i = 0; i < middleIndex; i++) {
            leftHalf[i] = digits[i];
            rightHalf[i] = digits[middleIndex + i + 1];
        }

        long[] inversedLeftHalf = new long[leftHalf.length];

        for (int i = leftHalf.length - 1; i >= 0; i--) {
           inversedLeftHalf[leftHalf.length - i - 1] = leftHalf[i]; 
        }

        long inversedLeftHalfConcat = arrayToLong(inversedLeftHalf);
        long rightHalfConcat = arrayToLong(rightHalf);

        if (inversedLeftHalfConcat <= rightHalfConcat) {
            middleDigit++;
        }

        long[] nearestPalindromeArray = new long[digits.length];

        for (int i = 0; i < digits.length; i++) {
            if (i < middleIndex) {
                nearestPalindromeArray[i] = leftHalf[i];
            }
            else if (i == middleIndex) {
                nearestPalindromeArray[i] = middleDigit;
            }
            else {
                nearestPalindromeArray[i] = inversedLeftHalf[i - middleIndex - 1];
            }
        }

        return arrayToLong(nearestPalindromeArray);
    }

    public static void main(String[] args) 
    {
        KingsPalindromeList palindrome = new KingsPalindromeList();
        long[] input = palindrome.getInput();
        int task = (int)input[0];
        int size = input.length - 1;

        long[] list = new long[size];
        for(int i = 0; i < size; i++) {
            list[i] = input[i + 1];
        }

        for (long number : list) {
            int count = palindrome.countDigits(number);
            long[] digits = palindrome.longToArray(number, count);
            long fixedPalindrome = palindrome.getNearestPalindrome(digits);
            System.out.print(fixedPalindrome + " ");
        }
    }
}
