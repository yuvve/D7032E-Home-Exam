package common.point_salad;


public enum ManifestMetadata {
    MANIFEST_FILENAME("PointSaladManifest.json"),
    CARDS_FIELD("Cards"),
    ARGS_FIELD("Args"),
    POINTS_FIELD("Points"),
    NAME_FIELD("Name");

    private final String value;

    ManifestMetadata(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}