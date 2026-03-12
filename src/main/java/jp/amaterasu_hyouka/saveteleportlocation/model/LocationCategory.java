package jp.amaterasu_hyouka.saveteleportlocation.model;

public record LocationCategory(
        Integer id,
        String name,
        String materialName,
        Integer priority
) {
    public LocationCategory(String name, String materialName) {
        this(
                null,
                name,
                materialName,
                null
        );
    }
}
