package persistence;

public abstract class Paths {
    private static final String BOATS_L = "./datasets/Boats/boatsL.txt";
    private static final String BOATS_M = "./datasets/Boats/boatsM.txt";
    private static final String BOATS_S = "./datasets/Boats/boatsS.txt";
    private static final String BOATS_XS = "./datasets/Boats/boatsXS.txt";
    private static final String BOATS_XXS = "./datasets/Boats/boatsXXS.txt";
    private static final String SAILORS_L = "./datasets/Sailors/sailorsL.txt";
    private static final String SAILORS_M = "./datasets/Sailors/sailorsM.txt";
    private static final String SAILORS_S = "./datasets/Sailors/sailorsS.txt";
    private static final String SAILORS_XS = "./datasets/Sailors/sailorsXS.txt";
    private static final String SAILORS_XXS= "./datasets/Sailors/sailorsXXS.txt";

    public static String getBoatsDataset(String datasetSize) {
        return getPath(datasetSize, BOATS_L, BOATS_M, BOATS_S, BOATS_XS, BOATS_XXS);
    }

    public static String getSailorsDataset(String datasetSize) {
        return getPath(datasetSize, SAILORS_L, SAILORS_M, SAILORS_S, SAILORS_XS, SAILORS_XXS);
    }

    private static String getPath(String datasetSize, String l, String m, String s, String xs, String xxs) {
        switch (datasetSize) {
            case "L" -> {
                return l;
            }
            case "M" -> {
                return m;
            }
            case "S" -> {
                return s;
            }
            case "XS" -> {
                return xs;
            }
            case "XXS" -> {
                return xxs;
            }
            default -> {
                return null;
            }
        }
    }
}