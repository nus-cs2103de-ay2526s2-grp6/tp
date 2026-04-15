package fairshare.model.balance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fairshare.model.expense.Expense;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
import fairshare.model.person.Person;

/**
 * Utility class for calculating simplified debts from a list of expenses.
 */
public class BalanceCalculator {

    /**
     * Calculates the simplified list of balances (debts) for each group given a list of expenses.
     *
     * @param expenses The list of expenses.
     * @return A map where the key is the {@code Group} and the value is a list of {@code Balance} objects
     *     representing the final simplified debts.
     */
    public static Map<Group, List<Balance>> calculate(List<Expense> expenses) {
        assert expenses != null : "expenses should not be null";

        // Filter expenses by their respective groups
        Map<Group, List<Expense>> groupedExpenses = new HashMap<>();
        for (Expense expense : expenses) {
            Group expenseGroup = expense.getGroup();
            if (groupedExpenses.containsKey(expenseGroup)) {
                List<Expense> expenseList = groupedExpenses.get(expenseGroup);
                expenseList.add(expense);
            } else {
                List<Expense> expenseList = new ArrayList<>(List.of(expense));
                groupedExpenses.put(expenseGroup, expenseList);
            }
        }

        // Calculate final balances for each group
        Map<Group, List<Balance>> groupBalances = new HashMap<>();
        for (Map.Entry<Group, List<Expense>> entry : groupedExpenses.entrySet()) {
            Map<Person, Double> netAmts = calculateNetAmounts(entry.getValue());
            List<Balance> balances = generateBalances(netAmts);
            groupBalances.put(entry.getKey(), balances);
        }

        return groupBalances;
    }

    private static Map<Person, Double> calculateNetAmounts(List<Expense> expenses) {
        Map<Person, Double> balances = new HashMap<>();

        for (Expense expense : expenses) {
            double totalAmt = expense.getAmount();
            Person payer = expense.getPayer();
            Set<Participant> participants = expense.getParticipants();

            if (!participants.isEmpty()) {
                balances.put(payer, balances.getOrDefault(payer, 0d) + totalAmt);

                int totalShares = expense.getTotalShares();
                for (Participant participant : participants) {
                    Person person = participant.getPerson();
                    int shares = participant.getShares();
                    double splitAmt = (totalAmt / totalShares) * shares;
                    balances.put(person, balances.getOrDefault(person, 0d) - splitAmt);
                }
            }
        }

        return balances;
    }

    private static List<Balance> generateBalances(Map<Person, Double> balances) {
        List<Person> debtors = new ArrayList<>();
        List<Person> creditors = new ArrayList<>();

        for (Map.Entry<Person, Double> entry : balances.entrySet()) {
            if (entry.getValue() < -0.01) {
                debtors.add(entry.getKey());
            } else if (entry.getValue() > 0.01) {
                creditors.add(entry.getKey());
            }
        }

        return calculateSettlements(debtors, creditors, balances);
    }

    private static List<Balance> calculateSettlements(List<Person> debtors, List<Person> creditors,
                                       Map<Person, Double> balances) {
        List<Balance> finalBalances = new ArrayList<>();

        int debtorIdx = 0;
        int creditorIdx = 0;
        while (debtorIdx < debtors.size() && creditorIdx < creditors.size()) {
            Person debtor = debtors.get(debtorIdx);
            Person creditor = creditors.get(creditorIdx);

            double debtAmt = Math.abs(balances.get(debtor));
            double creditAmt = balances.get(creditor);

            double settledAmt = Math.min(debtAmt, creditAmt);

            finalBalances.add(new Balance(debtor, creditor, settledAmt));

            balances.put(debtor, balances.get(debtor) + settledAmt);
            balances.put(creditor, balances.get(creditor) - settledAmt);

            // Move on to next person
            if (Math.abs(balances.get(debtor)) < 0.01) {
                debtorIdx++;
            }
            if (Math.abs(balances.get(creditor)) < 0.01) {
                creditorIdx++;
            }
        }

        return finalBalances;
    }
}
