package fairshare.model.expense;

import java.util.List;
import java.util.Objects;

import fairshare.model.group.Group;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

/**
 * Represents an expense in the shared expense tracker (FairShare)
 */
public class Expense {
    private Group group;
    private String expenseName;
    private double amount;
    private Person payer;
    private List<Participant> participants;
    private List<Tag> tags;

    /**
     * Creates an instance of {@code Expense} with the specified name, amount, payer, participants, and tags
     *
     * @param group The group the expense belongs to.
     * @param expenseName The name of the expense.
     * @param amount The amount (cost) of the expense.
     * @param payer The person who paid for the expense.
     * @param participants The list of participants sharing the cost of this expense.
     * @param tags The list of categorical tags associated with this expense.
     */
    public Expense(Group group, String expenseName, double amount,
                   Person payer, List<Participant> participants, List<Tag> tags) {
        this.group = group;
        this.expenseName = expenseName;
        this.amount = amount;
        this.payer = payer;
        this.participants = participants;
        this.tags = tags;
    }

    /**
     * Returns the person who paid for the expense
     *
     * @return The payer as a {@code Person}.
     */
    public Person getPayer() {
        return this.payer;
    }

    /**
     * Returns the list of participants involved in this expense.
     *
     * @return A list of {@code Participant} objects.
     */
    public List<Participant> getParticipants() {
        return this.participants;
    }

    /**
     * Returns the monetary cost of the expense.
     *
     * @return The expense cost as a double.
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Returns the name of the expense.
     *
     * @return The expense name as a string.
     */
    public String getExpenseName() {
        return this.expenseName;
    }

    /**
     * Returns the list of tags categorized under this expense.
     *
     * @return A list of {@code Tag} objects.
     */
    public List<Tag> getTags() {
        return this.tags;
    }

    /**
     * Calculates the total number of shares across all participants in this expense.
     *
     * @return The sum of all participant shares.
     */
    public int getTotalShares() {
        int shares = 0;
        for (Participant p : participants) {
            shares += p.getShares();
        }
        return shares;
    }

    /**
     * Returns the group this expense belongs to.
     *
     * @return The group as a {@code Group} object.
     */
    public Group getGroup() {
        return this.group;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Expense)) {
            return false;
        }

        Expense otherExpense = (Expense) other;
        return this.group.equals(otherExpense.group)
                && this.expenseName.equals(otherExpense.expenseName)
                && this.amount == otherExpense.amount
                && this.payer.equals(otherExpense.payer)
                && this.participants.equals(otherExpense.participants)
                && this.tags.equals(otherExpense.tags);
    }

    @Override
    public int hashCode() {
        // Any two person expenses with the same variables is assumed to be the same expense (same hashcode)
        return Objects.hash(expenseName, amount, payer, participants, tags);
    }
}
