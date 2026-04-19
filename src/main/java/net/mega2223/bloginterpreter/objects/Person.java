package net.mega2223.bloginterpreter.objects;

public class Person {
    String displayName;
    String uniqueName;
    String referenceLink;

    public Person(String displayName, String uniqueName, String referenceLink) {
        this.displayName = displayName;
        this.uniqueName = uniqueName;
        this.referenceLink = referenceLink;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public String getReferenceLink() {
        return referenceLink;
    }
}
