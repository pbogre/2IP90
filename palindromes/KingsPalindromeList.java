import java.util.Scanner;

/**
 * Reads a list of numbers, and can reconstruct the corresponding list of Palindromes,
 * produce the size of the largest magic set, and the content of that magic set.
 * 
 * 
 * User Manual:
 * - The program reads 3 lines of input; the first line selects which task is going to be executed, either 1, 2 or 3; 
 *   The second line is the input which indicates the number list's size that is going to be provided;
 *   And the third and final line provides the list of numbers which need to be manipulated, i.e. task 1 gets the closest greater or equal to
 *   palindrome to each number; task 2 provides the maximum magic set size of palindromes that contain themselves and finally,
 *   task 3 provides the actual magic set, printed in ascending order.
 * - All numbers provided are positive integers and the numbers that need to be manipulated can be 5000 at maximum and have at most 17 digits;
 * - The program prints different outputs depending on the task number selected:
 *   *Task 1: Prints the "fixed" list with actual palindromes;
 *   *Task 2: Prints the largest magic set size i.e. one number;
 *   *Task 3: Prints the elements of the largest magic set in ascending order;
 *
 *   (As stated in the exercises, X is the largest number of the largest magic set given and we will refer to it as such)
 * 
 *
 *
 * - Test case for Task 1:
 *   Input:
 *   1
 *   5
 *   241 424 546 654 120
 *   Output: 242 424 555 656 121 (As expected)
 * 
 * - Test case for Task 2:
 *   Input:
 *   2
 *   4
 *   4 242 1224221 6
 *   Output: 3 (As expected)
 * 
 * - Test case for Task 3:
 *   Input:
 *   3
 *   5
 *   8 179 4718174 847 244
 *   Output: 8 181 4718174 (As expected)
 * 
 * - Test case for Task 1 with large numbers (type long test)
 *   Input:
 *   1
 *   3
 *   82709456723563412 17890456325563412 67823451784563412
 *   Output: 827094575490728 178904565409871 678234525432876 (As expected) {Long size is 18 digits}
 * 
 * - Test case for Task 2 with trailing/leading zeros in number
 *   Input:
 *   2
 *   3
 *   100 200 8
 *   Output: 1 (As expected)
 * 
 * - Test case for Task 3 with 2 magic sets of the same size
 *   Input:
 *   3
 *   4
 *   1 121 8 181
 *   Output: 8 181 (As expected)
 *
 * 
 * @author Damyan Dimov
 * @ID <1933248>
 * @author Pietro Bonaldo Gregori
 * @ID 1964542
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

        // inverse the left half as that is what will
        // be put on the right hand side of the "next" palindrome
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
     * Mathematically removes first and last digit from number,
     * including leading and trailing zero's
     *
     * @param number Long number to shave
     * @return Given long number without first, last, and leading/trailing zero digits
     *
     */
    long shaveNumber(long number) {

        // Remove first digit and all leading zero's
        number %= (int)Math.pow(10, (int)Math.log10(number));
        // Remove last digit and all trailing zero's
        number -= number % 10;
        while (number % 10 == 0 && number != 0) {
            number /= 10;
        }
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
            int currentNumberSize = countDigits(currentNumber);
            int currentMagicSetSize = 1;

            while (currentNumberSize > 0) {
                // we can start by shaving because we know
                // that the first number will be inside of the
                // palindrome list
                // we must check the number size because you
                // can't shave single digits
                if (currentNumberSize > 1) {
                    currentNumber = shaveNumber(currentNumber);
                    currentNumberSize = countDigits(currentNumber);
                } else {
                    break;
                }

                // if shaved number is in array, increment currentMagicSetSize
                if (getElementIndex(currentNumber, array) != -1) {
                    currentMagicSetSize++;
                }
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
            int currentNumberSize = countDigits(currentNumber);
            int currentMagicSetSize = 0;

            while (currentNumberSize > 0) {
                // we can start by shaving because we know
                // that the first number will be inside of the
                // palindrome list
                // we must check the number size because you
                // can't shave single digits
                if (currentNumberSize > 1) {
                    currentNumber = shaveNumber(currentNumber);
                    currentNumberSize = countDigits(currentNumber);
                } else {
                    break;
                }

                // if shaved number is in array, increment currentMagicSetSize
                if (getElementIndex(currentNumber, array) != -1) {
                    currentMagicSetSize++;
                }
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
        int largestMagicSetSize = getLargestMagicSetSize(array);
        long[] largestMagicSet = new long[largestMagicSetSize];

        largestMagicSet[0] = largestX;

        // If the largest magic set consists only of 1 element, 
        // then there is no magic set
        // In that case return only the largest element in the whole array
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

        int currentIndex = 1;
        long currentNumber = largestX;
        int currentNumberSize = countDigits(currentNumber);
        
        while (currentNumberSize > 0) {
            // this check is not required but improves performance
            if (currentIndex >= largestMagicSetSize) {
                break;
            }

            // we can start by shaving because we know
            // that the first number will be inside of the
            // palindrome list
            // we must check the number size because you
            // can't shave single digits
            if (currentNumberSize > 1) {
                currentNumber = shaveNumber(currentNumber);
                currentNumberSize = countDigits(currentNumber);
            } else {
                break;
            }

            // if shaved number is in array, add it to largestMagicSet
            // at location currentIndex, then increment currentIndex
            if (getElementIndex(currentNumber, array) != -1) {
                largestMagicSet[currentIndex] = currentNumber;
                currentIndex++;
            }
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
