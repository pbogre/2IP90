/*import java.util.*;

public class TruckFuelingOptimized {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the number of fuel cans
        int numCans = scanner.nextInt();
        int[] mileage = new int[numCans];

        // Input the mileage of each individual can
        for (int i = 0; i < numCans; i++) {
            mileage[i] = scanner.nextInt();
        }

        // Input the locations where stopping is not allowed
        Set<Integer> prohibitedLocations = new HashSet<>();
        for (int i = 0; i < numCans - 1; i++) {
            prohibitedLocations.add(scanner.nextInt());
        }

        scanner.close();

        List<Integer> fuelSequence = findValidFuelSequence(mileage, prohibitedLocations);

        // Output the sequence of fuel cans to use
        for (int canIndex : fuelSequence) {
            System.out.print(canIndex + " ");
        }
    }

    private static List<Integer> findValidFuelSequence(int[] mileage, Set<Integer> prohibitedLocations) {
        int numCans = mileage.length;
        int totalMileage = Arrays.stream(mileage).sum();
        int[] dp = new int[1 << numCans];
        int[] parent = new int[1 << numCans];

        for (int i = 0; i < (1 << numCans); i++) {
            dp[i] = -1; // Initialize with an invalid value
        }

        dp[0] = 0; // Starting point

        for (int mask = 0; mask < (1 << numCans); mask++) {
            if (dp[mask] == -1) continue;

            for (int i = 0; i < numCans; i++) {
                if ((mask & (1 << i)) == 0) {
                    int nextMask = mask | (1 << i);
                    int nextLocation = dp[mask] + mileage[i];

                    if (!prohibitedLocations.contains(nextLocation)) {
                        if (dp[nextMask] == -1 || dp[nextMask] > nextLocation) {
                            dp[nextMask] = nextLocation;
                            parent[nextMask] = i;
                        }
                    }
                }
            }
        }

        // Reconstruct the sequence
        List<Integer> fuelSequence = new ArrayList<>();
        int mask = (1 << numCans) - 1;
        while (mask > 0) {
            int selectedCan = parent[mask];
            fuelSequence.add(selectedCan);
            mask ^= (1 << selectedCan);
        }

        Collections.reverse(fuelSequence);
        return fuelSequence;
    }
}
*/
