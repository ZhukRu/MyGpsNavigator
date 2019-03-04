import api.GpsNavigator;
import api.Path;
import impl.Road;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        final GpsNavigator navigator = new MyGpsNavigator();
        //navigator.readData("/Users/ruslanzhuk/Downloads/test");
        navigator.readData("D:\\Gps\\road_map.txt");
        final Path path = navigator.findPath("MINSK", "BREST");
        System.out.println(path);
    }

    private static class MyGpsNavigator implements GpsNavigator {

        private HashSet<String> fromCities;
        private HashSet<String> toCities;
        private ArrayList<String> allCities;
        private ArrayList<Road> roads;
        private int[][] graph;
        private ArrayList<String> resultPath;
        private int resultCost;
        private String roadInFile;

        @Override
        public void readData(String filePath) {
            // Read data from file.
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            roads = new ArrayList<>();
            allCities = new ArrayList<>();

            while (true) {
                try {
                    if (!((roadInFile = reader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] str = roadInFile.split(" ");
                String fromCity = str[0];
                int fromCityIndex = -1;
                String toCity = str[1];
                int toCityIndex = -1;
                int tempPaсh = Integer.parseInt(str[2]);
                int tempCost = Integer.parseInt(str[3]);
                if (tempPaсh < 0 || tempCost < 0) {
                    System.out.println("Длина участка или цена проезда по нему не может быть отрицательным !");
                } else {
                    int cost = tempPaсh * tempCost;
                    if (allCities.indexOf(fromCity) == -1) {
                        allCities.add(fromCity);
                        fromCityIndex = allCities.indexOf(fromCity);
                    } else {
                        fromCityIndex = allCities.indexOf(fromCity);
                    }
                    if (allCities.indexOf(toCity) == -1) {
                        allCities.add(toCity);
                        toCityIndex = allCities.indexOf(toCity);
                    } else {
                        toCityIndex = allCities.indexOf(toCity);
                    }
                    Road road = new Road(fromCity, toCity, fromCityIndex, toCityIndex, cost);
                    roads.add(road);
                }
            }

            graph = new int[roads.size() - 1][roads.size() - 1];

            for (int i = 0; i < roads.size() - 1; i++) {
                for (int j = 0; j < roads.size() - 1; j++) {
                    graph[i][j] = -1;
                }
                graph[i][i] = 0;
            }

            for (int i = 0; i < roads.size(); i++) {
                Road currentRoad = roads.get(i);
                graph[currentRoad.getFromCityIndex()][currentRoad.getToCityIndex()] = currentRoad.getCost();
            }
        }

        @Override
        public Path findPath(String pointA, String pointB) {
            fromCities = new HashSet<>();
            toCities = new HashSet<>();
            resultPath = new ArrayList<>();
            resultCost = 0;

            for (Road r : roads) {
                fromCities.add(r.getFromCity());
                toCities.add(r.getToCity());
            }

            if (fromCities.contains(pointA)) {

                if (toCities.contains(pointB)) {

                    int maxPath = Integer.MAX_VALUE / 2;
                    int sumCities = allCities.size();
                    int[] minPath = new int[sumCities];
                    int startCityIndex = allCities.indexOf(pointA);
                    int stopCityIndex = allCities.indexOf(pointB);
                    minPath[startCityIndex] = 0;
                    boolean[] visited = new boolean[sumCities];
                    int[] visitedCityInPath = new int[sumCities];
                    int[] shortestPath = new int[sumCities];
                    Arrays.fill(shortestPath, maxPath);
                    shortestPath[startCityIndex] = 0;

                    resultPath.add(allCities.get(startCityIndex));
                    for (; ; ) {

                        int currentCity = -1;

                        for (int nextCity = 0; nextCity < sumCities; nextCity++) {

                            if (!visited[nextCity] && shortestPath[nextCity] < maxPath &&
                                    (currentCity == -1 || shortestPath[currentCity] > shortestPath[nextCity])) {
                                currentCity = nextCity;
                            }

                        }

                        if (currentCity == -1) break;

                        visited[currentCity] = true;
                        visitedCityInPath[currentCity] = currentCity;

                        for (int nextCity = 0; nextCity < sumCities; nextCity++) {

                            if (!visited[nextCity] && 0 < graph[currentCity][nextCity] && graph[currentCity][nextCity] < maxPath) {

                                if (shortestPath[nextCity] > shortestPath[currentCity] + graph[currentCity][nextCity]) {
                                    shortestPath[nextCity] = shortestPath[currentCity] + graph[currentCity][nextCity];
                                }
                            }

                        }
                    }
                    resultCost = shortestPath[stopCityIndex];
                    int temp = 0;
                    resultPath.add(allCities.get(stopCityIndex));
                } else {
                    resultPath.add("В данный город нет маршрута!");
                }
            } else {
                resultPath.add("Из данного города нет маршрутов!");
            }

            // System.out.println(Arrays.deepToString(roads.toArray()));
            return new Path(resultPath, resultCost);

        }
    }
}
