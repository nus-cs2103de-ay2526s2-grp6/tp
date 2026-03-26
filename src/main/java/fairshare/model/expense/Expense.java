package fairshare.model.expense;

import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

import java.util.List;
import java.util.Objects;

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
        return this.payer;
    }

    public List<Person> getParticipants() {
        return this.participants;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getExpenseName() {
        return this.expenseName;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Expense)) {
            return false;
        }

        Expense otherExpense = (Expense) other;
        return this.expenseName.equals(otherExpense.expenseName)
                && this.amount == otherExpense.amount
                && this.payer.equals(otherExpense.payer)
                && this.participants.equals(otherExpense.participants)
                && this.tags.equals(otherExpense.tags);
    }

    public int hashCode() {
        // Any two person expenses with the same variables is assumed to be the same expense (same hashcode)
        return Objects.hash(expenseName, amount, payer, participants, tags);
    }
}
