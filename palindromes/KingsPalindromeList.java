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
 * @author Damyan Dimov
 * @ID <ID STUDENT 1>
 * @author Pietro Bonaldo Gregori
 * @ID <ID STUDENT 2>
 * 
 */
class KingsPalindromeList {
    Scanner scanner = new Scanner(System.in);

    /**
     * 
     * Gets and parses user input
     *
     * @return Array where first element is task number,
     * and all next elements are the list of numbers
     *
     */
    long[] getInput() {
        long task = scanner.nextLong();
        int listSize = scanner.nextInt();
        long[] input = new long[listSize + 1];
        input[0] = task;

        for(int i = 0; i < listSize; i++) {
            input[i + 1] = scanner.nextLong();
        }

        return input;
    }

    /**
     *
     * Counts digits of a number
     *
     * @param number A number of type long
     * @return Number of digits in given number
     *
     */
    int countDigits(long number) {
        int count = 0;

        // note that we do not need to
        // act on a copy of the number 
        // because Java methods do not
        // modify the parameter
        while (number != 0) {
            number /= 10;
            count++;
        }

        return count;
    }

    /**
     *
     * Converts a long to an array of its digits
     *
     * @param number A number of type long
     * @return Array consisting of each digit in the given number
     *
     */
    int[] convertLongToArray(long number) {
        int count = countDigits(number);
        int[] digits = new int[count];

        // Start at largest and divide from there 
        // to keep order of digits
        long magnitude = (long)Math.pow(10, count - 1);

        for (int i = 0; i < count; i++) {
            // get number up to the current magnitude 
            // level by taking the floor of division,
            // then get the remainder using mod 10 
            // to get only the last digit of remaining
            // number
            digits[i] = (int)Math.floorDiv(number, magnitude) % 10;
            magnitude /= 10;
        }

        return digits;
    }

    /**
     *
     * Converts array of digits to long of its concatenation
     *
     * @param digits An array of digits 
     * @return Number equal to concatenation of given 
     * array of digits
     *
     */
    long convertArrayToLong(int[] digits) {
        long number = 0;

        // For each digit append to number
        // the digit multiplied by 10 to the 
        // power of its magnitude, which 
        // we retrieve by using the current 
        // iteration number
        //
        // Example:
        // 1234 = 1000 + 200 + 30 + 4
        for(int i = 0; i < digits.length; i++) {
            long magnitude = (long)Math.pow(10, digits.length - 1 - i);
            number += digits[i] * magnitude;
        }

        return number;
    }

    /**
     *
     * Fix a corrupt number in the list,
     * as per the King's request
     *
     * @param digits An array of digits
     * @return The next highest palindrome, or the 
     * number itself if it is a palindrome
     *
     */
    long getNextPalindrome(int[] digits){
        if (digits.length == 1) {
            return digits[0];
        }

        // middleIndex also works as size of each half
        // (this is used throughout the method)
        int middleIndex = Math.floorDiv(digits.length, 2);
        int[] leftHalfDigits = new int[middleIndex];
        int[] rightHalfDigits = new int[middleIndex];
 
        for (int i = 0; i < middleIndex; i++) {
            leftHalfDigits[i] = digits[i];
            rightHalfDigits[i] = digits[middleIndex + i + 1];
        }

        int[] inversedLeftHalfDigits = new int[middleIndex];

        for (int i = middleIndex - 1; i >= 0; i--) {
           inversedLeftHalfDigits[middleIndex - i - 1] = leftHalfDigits[i]; 
        }

        long inversedLeftHalf = convertArrayToLong(inversedLeftHalfDigits);
        long rightHalf = convertArrayToLong(rightHalfDigits);
        int[] nearestPalindromeDigits = new int[digits.length];

        // Fill middle digit
        nearestPalindromeDigits[middleIndex] = digits[middleIndex];

        // if the new right half (invered left half) is
        // smaller than  the right half, we increase 
        // middle digit to get make the new palindrome
        // greater than the given number
        if (inversedLeftHalf < rightHalf) {
             nearestPalindromeDigits[middleIndex]++;
        }

        // Fill left half
        for (int i = 0; i < middleIndex; i++) {
            nearestPalindromeDigits[i] = leftHalfDigits[i];
        }

        // Fill right half
        for (int i = 0; i < middleIndex; i++) {
            nearestPalindromeDigits[i + middleIndex + 1] = inversedLeftHalfDigits[i];
        }

        long nearestPalindrome = convertArrayToLong(nearestPalindromeDigits);

        return nearestPalindrome;
    }

    public static void main(String[] args) 
    {
        KingsPalindromeList palindrome = new KingsPalindromeList();
        long[] input = palindrome.getInput();
        int task = (int)input[0];

        int listSize = input.length - 1;
        long[] list = new long[listSize];

        // Fill list with all elements in 
        // input array except first one, 
        // which is the task number
        for(int i = 0; i < listSize; i++) {
            list[i] = input[i + 1];
        }

        for (long number : list) {
            int[] digits = palindrome.convertLongToArray(number);
            long fixedPalindrome = palindrome.getNextPalindrome(digits);
            System.out.print(fixedPalindrome + " ");
        }
    }
}
