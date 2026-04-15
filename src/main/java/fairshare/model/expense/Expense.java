package fairshare.model.expense;

import java.util.Objects;
import java.util.Set;

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
    private Set<Participant> participants;
    private Set<Tag> tags;
    private ExpenseType expenseType;

    /**
     * Creates an instance of {@code Expense} with the specified name, amount, payer, participants, and tags
     *
     * @param group The group the expense belongs to.
     * @param expenseName The name of the expense.
     * @param amount The amount (cost) of the expense.
     * @param payer The person who paid for the expense.
     * @param participants The set of participants sharing the cost of this expense.
     * @param tags The set of categorical tags associated with this expense.
     */
    public Expense(Group group, String expenseName, double amount, Person payer,
                   Set<Participant> participants, Set<Tag> tags, ExpenseType expenseType) {
        assert group != null : "group should not be null";
        assert expenseName != null && !expenseName.isBlank() : "expense name should not be null or empty";
        assert amount >= 0 : "amount should be non-negative";
        assert participants != null && !participants.isEmpty() : "participants should not be null or empty";
        assert tags != null : "tags should not be null, should be empty set if no tags";
        assert expenseType != null : "expense type should not be null";

        this.group = group;
        this.expenseName = expenseName;
        this.amount = amount;
        this.payer = payer;
        this.participants = participants;
        this.tags = tags;
        this.expenseType = expenseType;
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
     * Returns the set of participants involved in this expense.
     *
     * @return A set of {@code Participant} objects.
     */
    public Set<Participant> getParticipants() {
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
     * Returns the set of tags categorized under this expense.
     *
     * @return A set of {@code Tag} objects.
     */
    public Set<Tag> getTags() {
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

    /**
     * Returns the type of this expense.
     *
     * @return The expense type as a {@code ExpenseType} object.
     */
    public ExpenseType getExpenseType() {
        return this.expenseType;
    }

    /**
     * Creates a special {@code Expense} representing a settlement between two persons.
     * A settlement is an expense with one participant and a "settlement" tag.
     *
     * @param group The {@code Group} the settlement belongs to.
     * @param payer The {@code Person} paying off the debt.
     * @param receiver The {@code Person} receiving the payment.
     * @param amount The amount being paid.
     * @return An {@code Expense} object configured as a settlement.
     */
    public static Expense createSettlement(Group group, Person payer, Person receiver, double amount) {
        String settlementName = "Settlement";
        Participant receive = new Participant(receiver, 1);

        return new Expense(group, settlementName, amount, payer, Set.of(receive),
                Set.of(), ExpenseType.SETTLEMENT);
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
        return Objects.hash(group, expenseName, amount, payer, participants, tags);
    }
}
