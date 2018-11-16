package ir3;

public class Access3 extends Arg3 {
    String objectId;
    String fieldId;

    public Access3(String objectId, String fieldId) {
        this.objectId = objectId;
        this.fieldId = fieldId;
    }
    public String toString() {
        return objectId + "." + fieldId;
    }
}
