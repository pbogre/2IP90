import java.util.*;

public class MadTrucker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int[] gasCans = new int[n];
        int[] unstoppableLocations = new int[n - 1];

        for(int i = 0; i < n; i++)
        {
            gasCans[i] = scanner.nextInt();
        }

        for(int i = 0; i < n - 1; i++)
        {
            unstoppableLocations[i] = scanner.nextInt();
        }

        scanner.close();

        Map<Pair, List<Integer>> memo = new HashMap<>();

        List<Integer> result = findValidSequence(n, gasCans, unstoppableLocations, new ArrayList<>(), memo, 0, 0);

        if (result != null) {
            for (int i : result) {
                System.out.print(i + " ");
            }
        } else {
            System.out.println("No valid sequence found.");
        }
        System.out.println(isValidSequence(gasCans, result, unstoppableLocations));
    }
    public static Boolean isValidSequence(int[] gasCans, List<Integer> sequence, int[] unstoppableLocations)
    {
        int location = 0;
        boolean exists;
        for(int n : sequence)
        {
            location += gasCans[n];
            for(int unstoppable : unstoppableLocations)
            {
                if(location == unstoppable)
                {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Integer> findValidSequence(int n, int[] gasCans, int[] unstoppableLocations, List<Integer> result, Map<Pair, List<Integer>> memo, int currentIndex, int currentLocation) {
        // Base case: All gas cans used
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

                    List<Integer> validSequence = findValidSequence(n, gasCans, unstoppableLocations, result, memo, currentIndex + 1, currentLocation + gasCans[i]);

                    if (validSequence != null) {
                        memo.put(currentState, validSequence);
                        return validSequence;
                    }

                    result.remove(result.size() - 1);
                }
            }
        }

        memo.put(currentState, null);
        return null;
    }

    private static class Pair {
        private int index;
        private int location;

        public Pair(int index, int location) {
            this.index = index;
            this.location = location;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return index == pair.index &&
                    location == pair.location;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, location);
        }
    }
}
