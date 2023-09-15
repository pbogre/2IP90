import java.util.Scanner;

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

    boolean checkPalindrome(long[] digits) {
        boolean isPalindrome = true;
        int size = digits.length;
        for (int i = 0; i < size / 2; i++) {
           if (digits[i] != digits[size - 1 - i]) {
               isPalindrome = false;
               break;
           }
        }

        return isPalindrome;
    }

    public static void main(String[] args) {
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
            boolean isPalindrome = palindrome.checkPalindrome(digits);
            System.out.println(isPalindrome);
        }
    }
}
