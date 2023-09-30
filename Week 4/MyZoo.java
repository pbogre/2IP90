import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class MyZoo {

    FoodsManager foodsManager;
    ContainmentsManager containmentsManager;
    AnimalsManager animalsManager;

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

                    if (containmentsManager.allocateAnimal(selectedAnimal, containmentNumber)) {
                        System.out.print(command + " ");
                    } else {
                        System.out.print(command + "! ");
                    }

                    break;
                }

                case 2: {
                    String animalName = scanner.next();

                    Animal selectedAnimal = animalsManager.getAnimalByName(animalName);

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

    boolean addStock(FoodType food, int amount) {

        int currentAmount = this.foodsStock.get(food);

        if (currentAmount + amount > stockLimit) {
            return false;
        }

        foodsStock.put(food, currentAmount + amount);

        return true;
    }

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

    boolean allocateAnimal(Animal animal, int containmentNumber) {

        Containment chosenContainment = containments[containmentNumber];

        if (animal.canLiveWith(chosenContainment.containedAnimals)) {
            chosenContainment.addAnimal(animal);

            return true;
        }

        return false;
    }

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

        if (this.checkExists(selectedAnimal.name)) {
            animals.remove(selectedAnimal.name);

            return true;
        }

        return false;
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

    void addAnimal(Animal animal) {

        containedAnimals.add(animal);
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

    boolean canLiveWith(ArrayList<Animal> possibleCompanions) {
        
        boolean canLiveWithCompanions = true;

        for (Animal possibleCompanion : possibleCompanions) {
            boolean canLiveWithCompanion = false;

            for (AnimalType preferredSpecies : this.companions) {
                if (possibleCompanion.species.equals(preferredSpecies)) {
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

    boolean canEat(FoodType food) {
        
        boolean isInDiet = false;

        for (FoodType possibleFood : diet) {
            if (possibleFood.equals(food)) {
                isInDiet = true;
                break;
            }
        }

        return isInDiet;
    }

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

