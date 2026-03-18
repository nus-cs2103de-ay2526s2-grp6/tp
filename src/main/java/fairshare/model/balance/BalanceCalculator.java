package fairshare.model.balance;

import fairshare.model.expense.Expense;
import fairshare.model.person.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for calculating debts from an expense list.
 */
public class BalanceCalculator {

    public static List<Balance> calculate(List<Expense> expenses) {
        Map<Person, Double> balances = calculateBalances(expenses);
        return simplifyBalances(balances);
    }

    private static Map<Person, Double> calculateBalances(List<Expense> expenses) {
        Map<Person, Double> balances = new HashMap<>();

        for (Expense expense : expenses) {
            double totalAmt = expense.getAmount();
            Person payer = expense.getPayer();
            List<Person> participants = expense.getParticipants();

            if (!participants.isEmpty()) {
                balances.put(payer, balances.getOrDefault(payer, 0d) + totalAmt);

                double splitAmt = totalAmt / participants.size();
                for (Person person : participants) {
                    balances.put(person, balances.getOrDefault(person, 0d) - splitAmt);
                }
            }
        }

        return balances;
    }

    private static List<Balance> simplifyBalances(Map<Person, Double> balances) {
        List<Person> debtors = new ArrayList<>();
        List<Person> creditors = new ArrayList<>();

        for (Map.Entry<Person, Double> entry : balances.entrySet()) {
            if (entry.getValue() < -0.01) {
                debtors.add(entry.getKey());
            } else if (entry.getValue() > 0.01) {
                creditors.add(entry.getKey());
            }
        }

        return generateSettlements(debtors, creditors, balances);
    }

    private static List<Balance> generateSettlements(List<Person> debtors, List<Person> creditors,
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
