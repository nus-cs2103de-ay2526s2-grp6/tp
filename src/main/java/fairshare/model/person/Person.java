package fairshare.model.person;

public class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        String myName = this.getName().trim();
        String theirName = otherPerson.getName().trim();

        return myName.equals(theirName);
    }

    public int hashCode() {
        // Any two person objects with the same name is assumed to be the same person (same hashcode)
        return this.getName().trim().hashCode();
    }
}
