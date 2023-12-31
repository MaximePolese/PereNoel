import java.util.ArrayList;

public class Main {

    public static double[][] allPoints = {
            {45.171112, 5.695952},
            {45.183152, 5.699386},
            {45.174115, 5.711106},
            {45.176123, 5.722083},
            {45.184301, 5.719791},
            {45.184252, 5.730698},
            {45.170588, 5.716664},
            {45.193702, 5.691028},
            {45.165641, 5.739938},
            {45.178718, 5.744940},
            {45.176857, 5.762518},
            {45.188512, 5.767172},
            {45.174017, 5.706729},
            {45.174458, 5.687902},
            {45.185110, 5.733667},
            {45.185702, 5.734507},
            {45.184726, 5.734666},
            {45.184438, 5.733735},
            {45.184902, 5.735256},
            {45.174812, 5.698095},
            {45.169851, 5.695723},
            {45.180943, 5.698965},
            {45.176205, 5.692165},
            {45.171244, 5.689872}
    };

    public static void displayAllPoints() {
        for (int i = 0; i < allPoints.length; i++) {
            for (int j = 0; j < allPoints[i].length; j++) {
                System.out.println(allPoints[i][j]);
            }
        }
    }

    public static double calcDistance(int pointA, int pointB) {
        double lat1 = allPoints[pointA][0];
        double lon1 = allPoints[pointA][1];
        double lat2 = allPoints[pointB][0];
        double lon2 = allPoints[pointB][1];

        double R = 6371e3; // metres
        double φ1 = lat1 * Math.PI / 180; // φ, λ in radians
        double φ2 = lat2 * Math.PI / 180;
        double Δφ = (lat2 - lat1) * Math.PI / 180;
        double Δλ = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c / 1000; // in km
        return d;
    }

    public static double[][] distances = new double[allPoints.length][allPoints.length];

    public static void calcAllDistances() {
        for (int i = 0; i < allPoints.length; i++) {
            for (int j = i; j < allPoints.length; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else {
                    distances[i][j] = calcDistance(i, j);
                    distances[j][i] = calcDistance(i, j);
                }
                System.out.println("La distance entre les points " + i + " et " + j + " est égale à " + distances[i][j] + " km");
            }
        }
    }

    public static ArrayList<Integer> path = new ArrayList<>(allPoints.length);
    public static boolean[] visited = new boolean[allPoints.length];

    public static int findNextMove(int lastPoint) {
        double shortestWay = 1000;
        int nextIndex = 1000;
        for (int i = 0; i < allPoints.length; i++) {
            if (distances[lastPoint][i] > 0 && !visited[i]) {
                if (distances[lastPoint][i] < shortestWay) {
                    shortestWay = distances[lastPoint][i];
                    nextIndex = i;
                }
            }
        }
        visited[lastPoint] = true;
        System.out.println(shortestWay);
        path.add(nextIndex);
        System.out.println(path);
        return nextIndex;
    }

    public static void findShortestPath(int startPoint) {
        path.add(startPoint);
        int nextPoint = startPoint;
        for (int i = 0; i < (allPoints.length - 1); i++) {
            nextPoint = findNextMove(nextPoint);
        }
    }

    public static double calcTotalkm(ArrayList<Integer> chmin) {
        double tot = 0;
        for (int i = 0; i < chmin.size() - 1; i++) {
            tot += calcDistance(chmin.get(i), chmin.get(i + 1));
        }
        return tot;
    }

    public static double[][] pheromone = new double[allPoints.length][allPoints.length];

    public static void initPhero() {
        for (int i = 0; i < allPoints.length; i++) {
            for (int j = 0; j < allPoints[i].length; j++) {
                pheromone[i][j] = 0;
            }
        }
    }

    public static void fillPhero() {
        for (int i = 0; i < allPoints.length; i++) {
            findShortestPath(i);
            for (int j = 0; j < (path.size() - 1); j++) {
                pheromone[path.get(j)][path.get(j + 1)] += 1;
            }
            path.clear();
            clearVisited();
        }
    }

    private static void clearVisited() {
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
    }

    public static boolean[] visitedPhero = new boolean[allPoints.length];

    public static double getPheroOnRoad(int pointA, int pointB) {
        return pheromone[pointA][pointB] + pheromone[pointB][pointA];
    }

    public static int findNextAntMove(int lastPoint) {
        double pheroMax = 0;
        int nextIndex = 1000;
        for (int i = 0; i < allPoints.length; i++) {
            if (!visitedPhero[i]) {
                if (getPheroOnRoad(lastPoint, i) >= pheroMax) {
                    pheroMax = getPheroOnRoad(lastPoint, i);
                    nextIndex = i;
                }
            }
        }
        visitedPhero[lastPoint] = true;
        path.add(nextIndex);
        System.out.println(path);
        return nextIndex;
    }

    public static void findShortestAntPath(int startPoint) {
        initPhero();
        fillPhero();
        path.add(startPoint);
        int nextPoint = startPoint;
        for (int i = 0; i < (allPoints.length - 1); i++) {
            nextPoint = findNextAntMove(nextPoint);
        }
    }

    public static void main(String[] args) {
        calcAllDistances();
//        findShortestPath(0);
        findShortestAntPath(0);
        System.out.println(calcTotalkm(path));
    }
}
