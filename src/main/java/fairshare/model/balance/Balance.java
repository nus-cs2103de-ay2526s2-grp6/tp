package fairshare.model.balance;

import fairshare.model.person.Person;

public class Balance {
    private Person debtor;
    private Person creditor;
    private double amount;

    public Balance(Person debtor, Person creditor, double amount) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
    }

    public Person getDebtor() {
        return this.debtor;
    }

    public Person getCreditor() {
        return this.creditor;
    }

    public double getAmount() {
        return this.amount;
    }
}
