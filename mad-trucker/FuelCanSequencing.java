import java.util.ArrayList;
import java.util.List;

public class FuelCanSequencing {
    public static void main(String[] args) {
        // Read input here
        int n = 20;
        int[] mileage = {20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9 ,8,7   ,6, 5, 4, 3, 2, 1};
        int[] unstoppableLocations = {209, 208, 207, 206, 205, 204, 203, 202, 201, 200, 199, 198, 197, 196, 195, 194, 193, 192, 191};

        // Populate 'mileage' and 'unstoppableLocations' arrays from input

        List<Integer> result = findValidSequence(n, mileage, unstoppableLocations, new ArrayList<>(), new boolean[n], 0);

        // Check if a valid sequence was found
        if (result != null) {
            // Print the result (valid sequence of fuel cans)
            for (int i : result) {
                System.out.print(i + " ");
            }
        } else {
            System.out.println("No valid sequence found.");
        }
    }

    private static List<Integer> findValidSequence(int n, int[] mileage, int[] unstoppableLocations, List<Integer> result, boolean[] used, int currentLocation) {
        // Base case: All fuel cans used
        if (result.size() == n) {
            return result;
        }

        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                // Check if using this can would lead to stopping at an unstoppable location
                boolean canUse = true;
                for (int j = 0; j < unstoppableLocations.length; j++) {
                    if (currentLocation + mileage[i] == unstoppableLocations[j]) {
                        canUse = false;
                        break;
                    }
                }

                if (canUse) {
                    used[i] = true;
                    result.add(i);

                    // Recursively try the next fuel can
                    List<Integer> validSequence = findValidSequence(n, mileage, unstoppableLocations, result, used, currentLocation + mileage[i]);

                    if (validSequence != null) {
                        return validSequence; // Return the valid sequence
                    }

                    // Backtrack
                    used[i] = false;
                    result.remove(result.size() - 1);
                }
            }
        }

        return null; // No valid sequence found
    }
}
