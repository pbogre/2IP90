import java.util.*;

/**
 *
 * Gives the order in which the mad trucker should use
 * his gas cans in order to never run out of gas in
 * a no-stopping area
 *
 * User Manual:
 *
 * - Input:
 *   - Number of cans (integer) (n)
 *   - List of cans' mileages (between 1 and 10000) (size is n)
 *   - List of points where the trucker cannot stop to refuel (size is n - 1)
 *
 * - Output:
 *   - The indices of the gas cans that the trucker should use, in the order that
 *   they should use them in
 *
 *
 * @author Damyan Dimov
 * @ID 1933248
 * @author Pietro Bonaldo Gregori
 * @ID 1964542
 *
 */
public class MadTrucker {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] gasCans = new int[n];
        int[] unstoppableLocations = new int[n - 1];

        for (int i = 0; i < n; i++) {
            gasCans[i] = scanner.nextInt();
        }

        for (int i = 0; i < n - 1; i++) {
            unstoppableLocations[i] = scanner.nextInt();
        }

        scanner.close();

        // Map created for Caching/Memoization to store already visited paths 
        // and to reduce time complexity
        Map<Pair, List<Integer>> memo = new HashMap<>();
        List<Integer> result = findValidSequence(n, gasCans, unstoppableLocations, new ArrayList<>(), memo, 0, 0);

        if (result != null) {
            for (int i = 0; i < result.size() - 1; i++) {
                System.out.print(result.get(i) + " ");
            }

            int lastIndex = result.size() - 1;
            System.out.print(result.get(lastIndex));
        }
    }

    /**
     *
     * Finds the sequence of gas cans that will
     * result in the mad trucker not running out
     * of gas in a no-refueling area
     *
     * @param n number of gas cans
     * @param gasCans list of gas cans' mileages
     * @param unstoppableLocations list of points where the trucker cannot stop
     * @param result the indices of gas cans that are currently being explored
     * @param memo the memoization array used for optimization
     * @param currentIndex index of gas can that is being explored
     * @param currentLocation kilometers travelled by trucker in current recursion
     *
     * @return result the indices of gas cans in the order that the trucker should use them
     *
     */
    private static List<Integer> findValidSequence(int n, int[] gasCans, int[] unstoppableLocations, List<Integer> result, Map<Pair, List<Integer>> memo, int currentIndex, int currentLocation) {
        // Base case; returns the found result after all the cans are used
        if (result.size() == n) {
            return result;
        }

        Pair currentState = new Pair(currentIndex, currentLocation);
        if (memo.containsKey(currentState)) {
            return memo.get(currentState);
        }

        for (int i = 0; i < n; i++) {
            if (!result.contains(i)) {
                boolean canUse = true;
                for (int j = 0; j < unstoppableLocations.length; j++) {
                    if (currentLocation + gasCans[i] == unstoppableLocations[j]) {
                        canUse = false;
                        break;
                    }
                }

                if (canUse) {
                    result.add(i);

                    // Recursive call with updated params
                    List<Integer> validSequence = findValidSequence(n, gasCans, unstoppableLocations, result, memo, currentIndex + 1, currentLocation + gasCans[i]);

                    if (validSequence != null) {
                        memo.put(currentState, validSequence);
                        return validSequence;
                    }
                    
                    // Backtrack and remove elements if they make an invalid sequence
                    result.remove(result.size() - 1);
                }
            }
        }

        memo.put(currentState, null);
        return null;
    }

    // Class needed for describing pair type of currentIndex and currentLocation
    private static class Pair {
        private int index;
        private int location;

        public Pair(int index, int location) {
            this.index = index;
            this.location = location;
        }
    
        // Overriding of class equals to assure that 
        // two pairs are equal even if they are different instances
        @Override
        public boolean equals(Object o) {
            if (this == o) return true; // Compares normally when they're the same instance
            if (o == null || getClass() != o.getClass()) return false; //Check if they're different classes -> returns false
            Pair pair = (Pair) o; 
        // Casts the object o as pair therefore returning true 
        // if they're equal even if they're different instances
            return index == pair.index &&
                    location == pair.location;
        }

        // Overriding of hashCode method to ensure that 
        // equal different instances have the same hashcode generated
        @Override
        public int hashCode() {
            return Objects.hash(index, location);
        }
    }
}
