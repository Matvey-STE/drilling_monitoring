package org.matveyvs;

import org.matveyvs.utils.RandomWellDataBaseCreator;

public class Main {
    public static void main(String[] args) {
        RandomWellDataBaseCreator creator = RandomWellDataBaseCreator.getInstance();
        var companyName = "Ariel";
        var fieldName = "Tevlinskoe";
        var clusterName = "50";
        var well = "3457";
        Double startPoint = 2130.00;
        Double finishPoint = 2200.00;

        creator.createRandomWellInformation(companyName,fieldName,clusterName,well,startPoint,finishPoint);
    }
}