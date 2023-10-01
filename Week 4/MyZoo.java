import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyZoo {

    FoodsManager foodsManager;
    ContainmentsManager containmentsManager;
    AnimalsManager animalsManager;

    /**
     * Runs the program and gets the input
     */
    void run() {

        Scanner scanner = new Scanner(System.in);

        inputloop:
        while (scanner.hasNextInt()) {
            int command = scanner.nextInt();

            switch (command) {
                case 0: {       
                    int animalTypeIndex = scanner.nextInt() - 1;
                    AnimalType animalType = AnimalType.values()[animalTypeIndex];
                    String animalName = scanner.next();

                    Animal newAnimal = new Animal(animalName, animalType);

                    int containmentNumber = scanner.nextInt();

                    if (animalsManager.addAnimal(newAnimal) && containmentsManager.allocateAnimal(newAnimal, containmentNumber)) {
                        System.out.print(command + " ");
                    } else {
                        System.out.print(command + "! ");
                    }

                    break;
                }

                case 1: {
                    String animalName = scanner.next();
                    int containmentNumber = scanner.nextInt();

                    Animal selectedAnimal = animalsManager.getAnimalByName(animalName);

                    // animal doesn't exist
                    if (selectedAnimal == null) {
                        System.out.print(command + "! ");
                        break;
                    }

                    if (containmentsManager.removeAnimal(selectedAnimal) && containmentsManager.allocateAnimal(selectedAnimal, containmentNumber)) {
                        System.out.print(command + " ");
                    } else {
                        System.out.print(command + "! ");
                    }

                    break;
                }

                case 2: {
                    String animalName = scanner.next();

                    Animal selectedAnimal = animalsManager.getAnimalByName(animalName);

                    // animal doesn't exist
                    if (selectedAnimal == null) {
                        System.out.print(command + "! ");
                        break;
                    }

                    if (animalsManager.removeAnimal(selectedAnimal) && containmentsManager.removeAnimal(selectedAnimal)) {
                        System.out.print(command + " ");
                    } else {
                        System.out.print(command + "! ");
                    }

                    break;
                }

                case 3: {
                    int foodTypeIndex = scanner.nextInt() - 1;
                    FoodType foodType = FoodType.values()[foodTypeIndex];
                    int amount = scanner.nextInt();

                    if (foodsManager.addStock(foodType, amount)) {
                        System.out.print(command + " ");
                    } else {
                        System.out.print(command + "! ");
                    }

                    break;
                }

                case 4: {
                    int foodTypeIndex = scanner.nextInt() - 1;
                    FoodType foodType = FoodType.values()[foodTypeIndex];
                    int amount = scanner.nextInt();

                    int containmentNumber = scanner.nextInt();
                    Containment selectedContainment = containmentsManager.containments[containmentNumber];

                    if (foodsManager.feedContainment(foodType, amount, selectedContainment)) {
                        System.out.print(command + " ");
                    } else {
                        System.out.print(command + "! ");
                    }

                    break;
                }

                default:
                    break inputloop;
            }
        }
    }

    MyZoo() {
        this.foodsManager = new FoodsManager();
        this.containmentsManager = new ContainmentsManager();
        this.animalsManager = new AnimalsManager();
    }
}

enum ContainmentType {
    CAGE,
    OPEN_ENCLOSURE
}

enum AnimalType {
    LION,
    TIGER,
    LEOPARD,
    ZEBRA,
    ANTELOPE,
    GIRAFFE,
    BEAR
}

enum FoodType {
    HAY,
    CORN,
    GRAIN,
    CARROTS,
    CHICKEN,
    BEEF
}



// Managers

class FoodsManager {

    Map<FoodType, Integer> foodsStock;
    int stockLimit = 100;

    /**
     * 
     * @param food the type of food that will be added
     * @param amount the amount which shouldn't exceed 100
     * @return success of method (boolean)
     */
    boolean addStock(FoodType food, int amount) {

        int currentAmount = this.foodsStock.get(food);

        if (currentAmount + amount > stockLimit) {
            return false;
        }

        foodsStock.put(food, currentAmount + amount);

        return true;
    }
    /**
     * 
     * @param food the food that will be given
     * @param amount the amount to be given
     * @param containment the containment to be fed
     * @return success of method (boolean)
     */
    boolean feedContainment(FoodType food, int amount, Containment containment) {
        
        int currentAmount = this.foodsStock.get(food);

        // not enough food stock
        if (currentAmount - amount < 0) {
            return false;
        }

        // no animals in containment
        if (containment.containedAnimals.size() == 0) {
            return false;
        }

        // animals in containment cannot eat selected food
        if (!containment.canReceiveFood(food)) {
            return false;
        }

        foodsStock.put(food, currentAmount - amount);

        return true;
    }

    FoodsManager() {

        this.foodsStock = new HashMap<FoodType, Integer>();
        this.foodsStock.put(FoodType.HAY, 0);
        this.foodsStock.put(FoodType.CORN, 0);
        this.foodsStock.put(FoodType.GRAIN, 0);
        this.foodsStock.put(FoodType.CARROTS, 0);
        this.foodsStock.put(FoodType.CHICKEN, 0);
        this.foodsStock.put(FoodType.BEEF, 0);
    }
}

class ContainmentsManager {

    Containment[] containments;

    /**
     * 
     * @param animal that will be allocated
     * @param containmentNumber, the number of the cage/enclosure
     * @return success of method (boolean)
     */
    boolean allocateAnimal(Animal animal, int containmentNumber) {

        Containment chosenContainment = containments[containmentNumber];

        if (animal.canLiveWith(chosenContainment.containedAnimals) && chosenContainment.addAnimal(animal)) {

            return true;
        }

        return false;
    }

    /**
     * 
     * @param animal animal to be removed from its containment
     * @return success of method (boolean)
     */
    boolean removeAnimal(Animal animal) {
        for (Containment containment : this.containments) {
            if (containment.removeAnimal(animal)) {

                return true;
            }
        }

        return false;
    }

    ContainmentsManager() {
        
        this.containments = new Containment[15];

        // Set the first 0 to 9 to type cage and then 10 to 14 to type open enclosure
        for (int i = 0; i < 10; i++) {
            containments[i] = new Containment(ContainmentType.CAGE);
        }
        for (int i = 10; i < 15; i++) {
            containments[i] = new Containment(ContainmentType.OPEN_ENCLOSURE);
        }
    }
}

class AnimalsManager {

    Map<String, Animal> animals;

    private boolean checkExists(String animalName) {

        return animals.containsKey(animalName);
    }

    Animal getAnimalByName(String animalName) {

        return animals.get(animalName);
    }

    boolean addAnimal(Animal newAnimal) {

        if (this.checkExists(newAnimal.name)) {

            return false;
        }

        animals.put(newAnimal.name, newAnimal);

        return true;
    }

    boolean removeAnimal(Animal selectedAnimal) {

        if (!this.checkExists(selectedAnimal.name)) {

            return false;
        }

            animals.remove(selectedAnimal.name);

        return true;
    }

    AnimalsManager() {

        this.animals = new HashMap<String, Animal>();
    }
}



// Managed

class Containment {

    ContainmentType type;
    int capacity;
    ArrayList<Animal> containedAnimals;

    boolean addAnimal(Animal animal) {

        // animal can't live in cage
        if (animal.onlyOpenEnclosures && this.type.equals(ContainmentType.CAGE)) {

            return false;
        }

        // too many animals
        if (this.containedAnimals.size() + 1 > this.capacity) {

            return false;
        }

        containedAnimals.add(animal);

        return true;
    }

    boolean removeAnimal(Animal animal) {

        return containedAnimals.remove(animal);
    }

    boolean canReceiveFood(FoodType food) {

        for (Animal animal : containedAnimals) {
            if (!animal.canEat(food)) {
                return false;
            }
        }

        return true;
    }

    Containment(ContainmentType type) {

        this.type = type;
        this.containedAnimals = new ArrayList<Animal>();

        if (type.equals(ContainmentType.CAGE)) {
            this.capacity = 2;
        } else {
            this.capacity = 6;
        }
    }
}


class Animal {

    String name;
    AnimalType species;
    boolean onlyOpenEnclosures;
    AnimalType[] companions;
    FoodType[] diet;

    /**
     * 
     * @param proposedCompanions list of animals to check whether this animal can live with
     * @return whether or not the animal can live with the proposed companions (boolean)
     */
    boolean canLiveWith(ArrayList<Animal> proposedCompanions) {
        
        boolean canLiveWithCompanions = true;

        for (Animal proposedCompanion : proposedCompanions) {
            boolean canLiveWithCompanion = false;

            for (AnimalType preferredSpecies : this.companions) {
                if (proposedCompanion.species.equals(preferredSpecies)) {
                    canLiveWithCompanion = true;
                    break;
                }
            }

            if (!canLiveWithCompanion) {
                canLiveWithCompanions = false;
                break;
            }

        }

        return canLiveWithCompanions;
    }

    /**
     * 
     * @param proposedFood food to check whether the animal can eat it
     * @return whether or not the animal can eat the proposed food (boolean)
     */
    boolean canEat(FoodType proposedFood) {
        
        boolean isInDiet = false;

        for (FoodType preferredFood : diet) {
            if (preferredFood.equals(proposedFood)) {
                isInDiet = true;
                break;
            }
        }

        return isInDiet;
    }

    /**
     * 
     * @param name name of the animal
     * @param species species of the animal (AnimalType)
     * 
     * Constructor that sets all restrictions for the initialised animal using switch case to determine the animal
     */
    Animal(String name, AnimalType species) {

        this.name = name;
        this.species = species;

        // set containment preferences
        this.onlyOpenEnclosures = species.equals(AnimalType.ANTELOPE) || species.equals(AnimalType.ZEBRA) || species.equals(AnimalType.GIRAFFE);

        // set companion preferences
        switch (species) {
            case ANTELOPE:
            case ZEBRA:
            case GIRAFFE:
                this.companions = new AnimalType[3];
                companions[0] = AnimalType.ANTELOPE;
                companions[1] = AnimalType.ZEBRA;
                companions[2] = AnimalType.GIRAFFE;
                break;
            case TIGER:
            case LEOPARD:
            case BEAR:
                this.companions = new AnimalType[0];
                break;
            case LION:
                this.companions = new AnimalType[1];
                companions[0] = AnimalType.LION;
                break;
        }

        // set dietary preferences
        switch (species) {
            case ANTELOPE:
                this.diet = new FoodType[3];
                diet[0] = FoodType.HAY;
                diet[1] = FoodType.CORN;
                diet[2] = FoodType.GRAIN;
                break;
            case ZEBRA:
            case GIRAFFE:
                this.diet = new FoodType[4];
                diet[0] = FoodType.HAY;
                diet[1] = FoodType.CORN;
                diet[2] = FoodType.GRAIN;
                diet[3] = FoodType.CARROTS;
                break;
            case LION:
            case TIGER:
            case LEOPARD:
                this.diet = new FoodType[2];
                diet[0] = FoodType.CHICKEN;
                diet[1] = FoodType.BEEF;
                break;
            case BEAR:
                this.diet = new FoodType[3];
                diet[0] = FoodType.CARROTS;
                diet[1] = FoodType.CHICKEN;
                diet[2] = FoodType.BEEF;
                break;
        }
    }
}

