package org.matveyvs;

import org.matveyvs.utils.RandomWellDataBaseCreator;

public class Main {
    public static void main(String[] args) {
        RandomWellDataBaseCreator wellCreator = RandomWellDataBaseCreator.getInstance();
        wellCreator.createRandomDataForTests();

    }
}
