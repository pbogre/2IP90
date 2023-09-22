import java.util.Scanner;
import java.util.ArrayList;

class MadTrucker {
    ArrayList<Integer> depthFirstSearch(int choiceIndex, 
                                        ArrayList<Integer> choiceSequence,
                                        ArrayList<Integer> availableGasCans, 
                                        ArrayList<Integer> prohibitedStops, 
                                        int position) {
        int choice = availableGasCans.get(choiceIndex);
        position += choice;

        if (prohibitedStops.contains(position)) {
           depthFirstSearch(choiceIndex+1, choiceSequence, availableGasCans, prohibitedStops, position);
        } else {
            availableGasCans.remove(choiceIndex);
            choiceSequence.add(choiceIndex);
            depthFirstSearch(0, choiceSequence, availableGasCans, prohibitedStops, position);
        }


        return new ArrayList<>();
    }

    public static void main(String[] args) {
        MadTrucker madtrucker = new MadTrucker();
        Scanner scanner = new Scanner(System.in);

        int inputSize = scanner.nextInt();
        ArrayList<Integer> gasCans = new ArrayList<>();
        ArrayList<Integer> prohibitedStops = new ArrayList<>();

        for (int i = 0; i < inputSize; i++) {
            gasCans.add(scanner.nextInt());
        }

        for (int i = 0; i < inputSize - 1; i++) {
            prohibitedStops.add(scanner.nextInt());
        }

        ArrayList<Integer> a = madtrucker.depthFirstSearch(0, new ArrayList<Integer>(), gasCans, prohibitedStops, 0);

        scanner.close();
    }
}
