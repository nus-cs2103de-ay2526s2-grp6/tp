package fairshare.model.expense;

import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

import java.util.List;

public class Expense {
    private String expenseName;
    private double amount;
    private Person payer;
    private List<Person> participants;
    private List<Tag> tags;

    public Expense(String expenseName, double amount,
                   Person payer, List<Person> participants, List<Tag> tags) {
        this.expenseName = expenseName;
        this.amount = amount;
        this.payer = payer;
        this.participants = participants;
        this.tags = tags;
    }

    public Person getPayer() {
        return payer;
    }

    public List<Person> getParticipants() {
        return participants;
    }

    public double getAmount() {
        return amount;
    }
}
