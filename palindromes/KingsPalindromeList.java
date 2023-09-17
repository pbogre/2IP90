import java.util.Scanner;

/**
 * Reads a list of numbers, and can reconstruct the corresponding list of Palindromes,
 * produce the size of the largest magic set, and the content of that magic set.
 * 
 * Usage:
 * TODO: Documentation
 * (define what X refers to in methods  / variables)
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

        for (int i = 0; i < listSize; i++) {
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
        for (int i = 0; i < digits.length; i++) {
            long magnitude = (long)Math.pow(10, digits.length - 1 - i);
            number += digits[i] * magnitude;
        }

        return number;
    }

    /**
     *
     * Gets next palindrome greater than or equal to 
     * given number (expressed as array of digits)
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

    /**
     *
     * Gets an element's index in given array
     *
     * @param element Long element you are looking for
     * @param array Long array to search from
     * @return Index of element in array, or -1 if it is not in array
     *
     */
    int getElementIndex(long element, long[] array) {

        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return i;
            }
        }

        return -1;
    }

    /**
     *
     * Mathematically removes first and last digit from number
     *
     * @param number Long number to shave
     * @return Given long number without first and last digits
     *
     */
    long shaveNumber(long number) {

        // Remove first digit 
        number %= (int)Math.pow(10, (int)Math.log10(number));
        // Remove last digit
        number -= number % 10;
        number /= 10;

        return number;
    }

    /**
     *
     * Gets size of largest magic set in given list
     * of palindromes
     *
     * @param array Long array of palindromes
     * @return Size of largest magic set found in given array
     *
     */
    int getLargestMagicSetSize(long[] array) {

        int largestMagicSetSize = 0;

        for (int i = 0; i < array.length; i++) {
            long currentNumber = array[i];
            int currentMagicSetSize = 0;
            int currentNumberSize = countDigits(currentNumber);

            for (int j = 0; j <= Math.floorDiv(currentNumberSize, 2); j++) {
                if (getElementIndex(currentNumber, array) != -1) {
                    currentMagicSetSize++;
                }

                currentNumber = shaveNumber(currentNumber);
            }

            if (currentMagicSetSize > largestMagicSetSize) {
                largestMagicSetSize = currentMagicSetSize;
            }
        }

        return largestMagicSetSize;
    }

    /**
     *
     * Gets largest palindrome in largest magic set
     * of the given array
     *
     * @param array Long array of palindromes
     * @return Largest palindrome of largest magic set as type long
     *
     */
    long getXOfLargestMagicSet(long[] array) {

        long largestX = 0;
        int largestMagicSetSize = 0;

        for (int i = 0; i < array.length; i++) {
            long currentNumber = array[i];
            int currentMagicSetSize = 0;
            int currentNumberSize = countDigits(currentNumber);

            for (int j = 0; j <= Math.floorDiv(currentNumberSize, 2); j++) {
                if (getElementIndex(currentNumber, array) != -1) {
                    currentMagicSetSize++;
                }

                currentNumber = shaveNumber(currentNumber);
            }

            if (currentMagicSetSize > largestMagicSetSize) {
                largestMagicSetSize = currentMagicSetSize;
                largestX = array[i];
            }
        }
         
        return largestX;
    }

    /**
     *
     * Gets the contents of the largest magic set 
     * found in the given array
     *
     * @param array Long array of palindromes
     * @return Long array consisting of elements in 
     * largest magic set found, or largest element 
     * in array if no magic set is found
     *
     */
    long[] getLargestMagicSet(long[] array) {

        long largestX = getXOfLargestMagicSet(array);
        long largestXSize = countDigits(largestX);

        int largestMagicSetSize = getLargestMagicSetSize(array);
        long[] largestMagicSet = new long[largestMagicSetSize];

        // If the largest magic set consists only of 1 element, 
        // then there is no magic set
        // In that case return only the largest element in the array
        if (largestMagicSetSize == 1) {
            long largestElement = 0;

            for (long element : array) {
                if (element > largestElement) {
                    largestElement = element;
                }
            }

            largestMagicSet[0] = largestElement;
            return largestMagicSet;
        }

        int currentIndex = 0;
        long currentNumber = largestX;
        
        for (int i = 0; i <= Math.floorDiv(largestXSize, 2); i++) {
            if (currentIndex >= largestMagicSetSize) {
                break;
            }

            int currentNumberIndex = getElementIndex(currentNumber, array);

            if (currentNumberIndex != -1) {
                largestMagicSet[currentIndex] = currentNumber;
                currentIndex++;
            }

            currentNumber = shaveNumber(currentNumber);
        }

        return largestMagicSet;
    }

    public static void main(String[] args) {

        KingsPalindromeList palindrome = new KingsPalindromeList();

        long[] input = palindrome.getInput();
        int task = (int)input[0];

        int listSize = input.length - 1;
        long[] list = new long[listSize];

        // Fill list with all elements in 
        // input array except first one, 
        // which is the task number
        for (int i = 0; i < listSize; i++) {
            list[i] = input[i + 1];
        }

        // No need to check task here because 
        // we need correct list regardless of task
        long[] fixedList = new long[listSize];

        for (int i = 0; i < listSize; i++) {
            int[] digits = palindrome.convertLongToArray(list[i]);
            long fixedPalindrome = palindrome.getNextPalindrome(digits);
            fixedList[i] = fixedPalindrome;

            // Output done inside correcting loop for better performance
            if (task == 1) {
                // Avoid trailing spaces
                if (i < listSize - 1) {
                    System.out.print(fixedPalindrome + " ");
                } else {
                    System.out.print(fixedPalindrome);
                }
            }
        }

        if (task == 1) {
            return;
        }

        if (task == 2) {
            int largestMagicSetSize = palindrome.getLargestMagicSetSize(fixedList);
            System.out.println(largestMagicSetSize);
            return;
        }

        // If the program got here, the task has to be 3 (assuming correct input)
        // Therefore no need to check if task is 3

        long[] largestMagicSet = palindrome.getLargestMagicSet(fixedList);

        // Backward loop because, by nature of magic sets, 
        // the largestMagicSet array will always be sorted in 
        // descending order, but we want to print it in ascending
        for (int i = largestMagicSet.length - 1; i >= 0; i--) {
            // Avoid trailing spaces
            if (i > 0) {
                System.out.print(largestMagicSet[i] + " ");
            } else {
                System.out.print(largestMagicSet[i]);
            }
        }
    }
}
