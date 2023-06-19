package business.models.boats;

public enum BoatType {
    Windsurf, Optimist, Laser, PatiCatala, HobieDragoon, HobieCat, Unknown;

    public static BoatType getEnumValue(String value) {
        return switch (value) {
            case "Optimist" -> BoatType.Optimist;
            case "Windsurf" -> BoatType.Windsurf;
            case "Laser" -> BoatType.Laser;
            case "Patí Català" -> BoatType.PatiCatala;
            case "HobieDragoon" -> BoatType.HobieDragoon;
            case "HobieCat" -> BoatType.HobieCat;
            default -> BoatType.Unknown;
        };
    }
}
