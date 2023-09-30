import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyZoo {

    FoodsManager foodsManager;
    ContainmentsManager containmentsManager;
    AnimalsManager animalsManager;

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

    void addStock(FoodType food, int amount) {

        int currentAmount = this.foodsStock.get(food);

        if (currentAmount + amount > stockLimit) {
            // raise exception?
        }

        foodsStock.put(food, currentAmount + amount);
    }

    void useStock(FoodType food, int amount) {
        
        int currentAmount = this.foodsStock.get(food);

        if (currentAmount - amount < 0) {
            // raise exception?
        }

        foodsStock.put(food, currentAmount - amount);
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

    void allocateAnimal(Animal animal) {
        for (Containment containment : containments) {
            if (animal.canLiveWith(containment.containedAnimals)) {
                containment.addAnimal(animal);
                return;
            }
        }
        // throw? exception
    }

    void relocateAnimal(Animal animal, int containmentNumber) {
        Containment chosenContainment = containments[containmentNumber];
        if (animal.canLiveWith(chosenContainment.containedAnimals)) {
            // TODO vv does this add to the actual object or just the reference of it?
            chosenContainment.addAnimal(animal);
            return;
        }

        // raise exception
    }

    void removeAnimal(String animalName) {
        for (Containment containment : this.containments) {
            for (Animal animal : containment.containedAnimals) {
                if (animal.name.equals(animalName)) {
                    containment.removeAnimalWithName(animalName);
                }
            }
        }
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

    ArrayList<String> animalNames;

    void addAnimal(String newAnimalName) {

        // check if an animal with this name already exists
        for (String animalName : animalNames) {
            if (animalName.equals(newAnimalName)) {
                // throw exception?
            }
        }
        animalNames.add(newAnimalName);
    }

    AnimalsManager() {
        this.animalNames = new ArrayList<String>();
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

    void removeAnimalWithName(String animalName) {

        for (Animal animal : containedAnimals) {
            if (animal.name.equals(animalName)) {
                containedAnimals.remove(animal);
            }
        }
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

    // TODO test this
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

