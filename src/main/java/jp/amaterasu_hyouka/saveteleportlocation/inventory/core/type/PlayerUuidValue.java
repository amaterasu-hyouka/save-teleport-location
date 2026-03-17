package jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type;

public record PlayerUuidValue(
        String uuid,
        String name
) implements PlayerCategoryValue {
}
