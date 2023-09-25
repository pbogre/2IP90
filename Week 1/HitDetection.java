import java.util.Scanner;

/**
 * Detects if a point hits any of two circles.
 *
 * User Manual:
 * - Input:
 *     - First Circle's X coordinate (double)
 *     - First Circle's Y coordinate (double)
 *     - First Circle's Radius (double) (non-negative)
 *     - Second Circle's X coordinate (double)
 *     - Second Circle's Y coordinate (double)
 *     - Second Circle's Radius (double) (non-negative)
 *     - Point's X coordinate
 *     - Point's Y coordinate
 *
 * - Input Format:
 *     - You may separate each input using a whitespace
 *       or a newline character 
 *       - e.g. 1:
 *         1 2 3 1 2 3 1 2
 *       - e.g. 2:
 *         1 2 3
 *         1 2 3
 *         1 2
 *       - e.g. 3:
 *         1 2
 *         3 1 2
 *         3 1 
 *         2
 *
 * - Output:
 *     - You will get a message stating whether the point you 
 *     specified is contained in either, both, or neither circles given
 *     - If you insert a negative radius, you will get an error and the 
 *     program will exit
 *
 * @author Stefan Birca
 * @ID <ID STUDENT 1>
 * @author Pietro Bonaldo Gregori
 * @ID 1964542
 *
 */
class HitDetection {
    
    Scanner scanner = new Scanner(System.in);

    public void run() {

        // Initialize first circle class based on user input
        double firstCircleX = scanner.nextDouble();
        double firstCircleY = scanner.nextDouble();
        double firstCircleRadius = scanner.nextDouble();
        Circle firstCircle = new Circle(firstCircleX, firstCircleY, firstCircleRadius);

        // Initialize second circle class based on user input
        double secondCircleX = scanner.nextDouble();
        double secondCircleY = scanner.nextDouble();
        double secondCircleRadius = scanner.nextDouble();
        Circle secondCircle = new Circle(secondCircleX, secondCircleY, secondCircleRadius);

        // If either of the given radii is negative
        // print error and exit
        if (firstCircleRadius < 0 || secondCircleRadius < 0) {
            System.out.println("input error");
            return;
        }

        // Get point's coordinates from user input
        double pointX = scanner.nextDouble();
        double pointY = scanner.nextDouble();

        // Call detectHitFromPoint method from the two Circle objects
        // and store the returned boolean
        boolean firstHit = firstCircle.detectHitFromPoint(pointX, pointY);
        boolean secondHit = secondCircle.detectHitFromPoint(pointX, pointY);

        // If point hits either circle but not both: print, exit
        if (firstHit ^ secondHit) {
            // Uses ternary operator:
            // firstHit is true? Then use "first", otherwise "second"
            System.out.println("The point hits the " + (firstHit ? "first" : "second") + " circle");
            return;
        }

        // If point hits both circles: print, exit
        if (firstHit && secondHit) {
            System.out.println("The point hits both circles");
            return;
        }

        // If the program is still running here, 
        // the point does not hit either circle
        // as none of the previous conditions 
        // were met
        System.out.println("The point does not hit either circle");
    }

    // Method to close scanner from main method
    public void closeScanner() {
        scanner.close();
    }

    public static void main(String[] args) {
        HitDetection hitDetection = new HitDetection();
        hitDetection.run();
        hitDetection.closeScanner();
    }
}

/*
 * Circle class containing the circle's attributes
 * and the detectHitFromPoint method to determine 
 * whether a given point is inside of the circle
 */
class Circle {

    double x;
    double y;
    double radius;

    // Initialize a Circle object given its 
    // X and Y coordinates and its radius
    Circle(double x, double y, double radius) {

        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    /**
    * Returns whether or not the specified point is 
    * inside of the circle
    *
    * If the distance between a point and a circle's
    * center is lower than or equal to the circle's radius,
    * than that point is inside of the circle in a cartesian plane
    * (if the point is on the circle's border, that also counts as a hit)
    *
    * Pythagora's theorem can be used to find the distance squared, (a^2 + b^2 = c^2)
    * then the radius is squared to be able to compare it to the distance
    * without have to take the square root of anything, which is more optimal
    */
    boolean detectHitFromPoint(double pointX, double pointY) {

        double differenceX = this.x - pointX;
        double differenceY = this.y - pointY;
        double distanceSquared = Math.pow(differenceX, 2) + Math.pow(differenceY, 2);
        double radiusSquared = Math.pow(this.radius, 2);

        // Return the boolean value of whether the 
        // distance between the point and the circle's center (squared)
        // is lower than or equal to the radius of the circle (squared)
        // If true, then the point is in the circle
        // If false, then the point is outside of the circle
        return distanceSquared <= radiusSquared;
    }
}
