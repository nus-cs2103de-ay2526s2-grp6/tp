package fairshare.model.expense;

import fairshare.model.person.Person;

public class Participant {
    private Person person;
    private int shares;

    public Participant(Person person, int shares) {
        this.person = person;
        this.shares = shares;
    }

    public Person getPerson() {
        return this.person;
    }

    public int getShares() {
        return this.shares;
    }
}
