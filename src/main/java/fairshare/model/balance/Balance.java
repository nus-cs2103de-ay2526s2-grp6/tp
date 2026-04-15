package fairshare.model.balance;

import fairshare.model.person.Person;

/**
 * Represents a calculated settlement between two persons
 */
public class Balance {
    private Person debtor;
    private Person creditor;
    private double amount;

    /**
     * Creates an instance of {@code Balance} with the specified debtor, creditor, and amount.
     *
     * @param debtor The person who owes an amount.
     * @param creditor The person who is owed the amount.
     * @param amount The exact amount owed.
     */
    public Balance(Person debtor, Person creditor, double amount) {
        assert debtor != null : "debtor should not be null";
        assert creditor != null : "creditor should not be null";
        assert amount > 0 : "amount should be positive";

        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
    }

    /**
     * Returns the person who owes the amount.
     *
     * @return The debtor as a {@code Person}.
     */
    public Person getDebtor() {
        return this.debtor;
    }

    /**
     * Returns the person who is owed the amount.
     *
     * @return The creditor as a {@code Person}.
     */
    public Person getCreditor() {
        return this.creditor;
    }

    /**
     * Returns the exact monetary amount of this balance.
     *
     * @return The amount owed.
     */
    public double getAmount() {
        return this.amount;
    }
}
