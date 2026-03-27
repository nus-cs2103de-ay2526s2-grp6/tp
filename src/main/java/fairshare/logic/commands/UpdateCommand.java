package fairshare.logic.commands;

import java.util.List;
import java.util.Optional;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;
import fairshare.model.expense.Expense;
import fairshare.model.expense.Participant;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

/**
 * Represents a command to update a specific expense in the list.
 */
public class UpdateCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Update success";
    private static final String MESSAGE_INVALID_INDEX = "Cannot update an expense that is not in the list.";
    private int expenseIndex;
    private UpdateFields updateFields;

    /**
     * Creates an instance of {@code UpdateCommand}.
     *
     * @param expenseIndex The zero-based index of the expense to update in the filtered expense list.
     * @param updateFields The new data to update the expense with.
     */
    public UpdateCommand(int expenseIndex, UpdateFields updateFields) {
        this.expenseIndex = expenseIndex;
        this.updateFields = updateFields;
    }

    /**
     * Executes the update command by replacing the target expense with an updated expense.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     * @throws CommandException If the provided index is out of bounds of the filtered expense list.
     */
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

    /**
     * Represents a container for storing the details to update an expense with.
     */
    public static class UpdateFields {
        private String expenseName;
        private Double amount;
        private Person payer;
        private List<Participant> participants;
        private List<Tag> tags;

        /**
         * Sets the new expense name.
         *
         * @param expenseName The updated name string.
         */
        public void setExpenseName(String expenseName) {
            this.expenseName = expenseName;
        }

        /**
         * Returns the updated expense name, if any.
         *
         * @return An {@code Optional} containing the new name, or empty if none.
         */
        public Optional<String> getExpenseName() {
            return Optional.ofNullable(this.expenseName);
        }

        /**
         * Sets the new amount (cost) for the expense.
         *
         * @param amount The updated amount.
         */
        public void setAmount(double amount) {
            this.amount = amount;
        }

        /**
         * Returns the updated amount, if any.
         *
         * @return An {@code Optional} containing the new amount, or empty if none.
         */
        public Optional<Double> getAmount() {
            return Optional.ofNullable(this.amount);
        }

        /**
         * Sets the payer for the expense.
         *
         * @param payer The updated {@code Person} who paid.
         */
        public void setPayer(Person payer) {
            this.payer = payer;
        }

        /**
         * Returns the updated payer, if any.
         *
         * @return An {@code Optional} containing the new payer, or empty if none.
         */
        public Optional<Person> getPayer() {
            return Optional.ofNullable(this.payer);
        }

        /**
         * Sets the new list of participants for the expense.
         *
         * @param participants The updated list of {@code Participant} objects.
         */
        public void setParticipants(List<Participant> participants) {
            this.participants = participants;
        }

        /**
         * Returns the updated participant list, if any.
         *
         * @return An {@code Optional} containing the new participant list, or empty if none.
         */
        public Optional<List<Participant>> getParticipants() {
            return Optional.ofNullable(this.participants);
        }

        /**
         * Sets the new list of tags associated with the expense.
         *
         * @param tags The updated list of {@code Tag} objects.
         */
        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        /**
         * Returns the updated list of tags, if any.
         *
         * @return An {@code Optional} containing the new tag list, or empty if none.
         */
        public Optional<List<Tag>> getTags() {
            return Optional.ofNullable(this.tags);
        }

        /**
         * Checks if any fields have been provided for the update.
         *
         * @return True if all fields are null, otherwise false.
         */
        public boolean isEmpty() {
            return (this.expenseName == null) && (this.amount == null) && (this.payer == null)
                    && (this.participants == null) && (this.tags == null);
        }
    }
}
