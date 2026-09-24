package entity;

public record StorageLocation(String zone, int shelf, int slot) {
    public StorageLocation {
        if(zone==null || zone.isBlank()){ // we want to reject the zone if either it is null or blank
            throw new IllegalArgumentException("Zone cannot be empty");
        }
        if(slot<1 || slot >999){
            throw new IllegalArgumentException("Slot cannot be outside bounds 1-999");
        }
        if(shelf<1 || shelf >999){
            throw new IllegalArgumentException("Shelf cannot be outside bounds 1-999");
        }
        zone.trim().toUpperCase();
    }
    public String display() {

        return "%s / shelf %d / slot %d"
                .formatted(zone, shelf, slot);
    }

    public String key() {

        return "%s:%d:%d"
                .formatted(zone, shelf, slot);
    }
}
