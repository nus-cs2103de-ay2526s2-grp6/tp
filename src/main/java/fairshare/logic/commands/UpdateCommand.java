package fairshare.logic.commands;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;
import fairshare.model.expense.Expense;
import fairshare.model.expense.Participant;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

import java.util.List;
import java.util.Optional;

public class UpdateCommand extends Command {
    private int expenseIndex;
    private UpdateFields updateFields;
    private static final String MESSAGE_SUCCESS = "Update success";
    private static final String MESSAGE_INVALID_INDEX = "Cannot update an expense that is not in the list.";


    public UpdateCommand(int expenseIndex, UpdateFields updateFields) {
        this.expenseIndex = expenseIndex;
        this.updateFields = updateFields;
    }

    public CommandResult execute(Model model) throws CommandException {
        try {
            List<Expense> displayedExpenseList = model.getFilteredExpenseList();
            Expense targetExpense = displayedExpenseList.get(expenseIndex);
            Expense updatedExpense = createUpdatedExpense(targetExpense, updateFields);
            model.updateExpense(targetExpense, updatedExpense);

            return new CommandResult(MESSAGE_SUCCESS, false, false);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }
    }

    private Expense createUpdatedExpense(Expense targetExpense, UpdateFields updateFields) {
        String expenseName = updateFields.getExpenseName().orElse(targetExpense.getExpenseName());
        double amount = updateFields.getAmount().orElse(targetExpense.getAmount());
        Person payer = updateFields.getPayer().orElse(targetExpense.getPayer());
        List<Participant> participants = updateFields.getParticipants().orElse(targetExpense.getParticipants());
        List<Tag> tags = updateFields.getTags().orElse(targetExpense.getTags());

        return new Expense(expenseName, amount, payer, participants, tags);
    }


    public static class UpdateFields {
        private String expenseName;
        private Double amount;
        private Person payer;
        private List<Participant> participants;
        private List<Tag> tags;

        public void setExpenseName(String expenseName) {
            this.expenseName = expenseName;
        }

        public Optional<String> getExpenseName() {
            return Optional.ofNullable(this.expenseName);
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public Optional<Double> getAmount() {
            return Optional.ofNullable(this.amount);
        }

        public void setPayer(Person payer) {
            this.payer = payer;
        }

        public Optional<Person> getPayer() {
            return Optional.ofNullable(this.payer);
        }

        public void setParticipants(List<Participant> participants) {
            this.participants = participants;
        }

        public Optional<List<Participant>> getParticipants() {
            return Optional.ofNullable(this.participants);
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public Optional<List<Tag>> getTags() {
            return Optional.ofNullable(this.tags);
        }

        public boolean isEmpty() {
            return (this.expenseName == null) && (this.amount == null) && (this.payer == null)
                    && (this.participants == null) && (this.tags == null);
        }
    }
}
