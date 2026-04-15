package fairshare.storage;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

/**
 * A plain-text-friendly representation of a {@code Expense}.
 * Each expense is serialized as a single pipe-delimited line in the format:
 * {@code expenseName|amount|payerName|share1,share2,...|tag1,tag2,...}
 */
public class TxtAdaptedExpense {
    private static final String FIELD_SEPARATOR = "\\|"; //for splitting
    private static final String LIST_SEPARATOR = ","; //for splitting lists

    private final TxtAdaptedGroup group;
    private final String expenseName;
    private final double amount;
    private final TxtAdaptedPerson payer;
    private final Set<TxtAdaptedParticipant> participants;
    private final Set<TxtAdaptedTag> tags;
    private final ExpenseType expenseType;

    /**
     * Constructs a {@code TxtAdaptedExpense} from an {@code Expense} model object.
     *
     * @param source the {@code Expense} to adapt; cannot be null.
     */
    public TxtAdaptedExpense(Expense source) {
        this.group = new TxtAdaptedGroup(source.getGroup());
        this.expenseName = source.getExpenseName();
        this.amount = source.getAmount();
        this.payer = new TxtAdaptedPerson(source.getPayer());
        this.participants = source.getParticipants().stream()
                .map(TxtAdaptedParticipant::new)
                .collect(Collectors.toSet());
        this.tags = source.getTags().stream()
                .map(TxtAdaptedTag::new)
                .collect(Collectors.toSet());
        this.expenseType = source.getExpenseType();
    }

    /**
     * Constructs a {@code TxtAdaptedExpense} with all fields specified directly.
     * Used during deserialization.
     *
     * @param expenseName the expense name; cannot be null or empty.
     * @param amount the expense amount; must be greater than 0.
     * @param payer the adapted payer person; cannot be null.
     * @param participants the list of adapted participant persons; cannot be null.
     * @param tags the list of adapted tags; cannot be null.
     */
    public TxtAdaptedExpense(TxtAdaptedGroup group, String expenseName, double amount, TxtAdaptedPerson payer,
                             Set<TxtAdaptedParticipant> participants, Set<TxtAdaptedTag> tags,
                             ExpenseType expenseType) {
        this.group = group;
        this.expenseName = expenseName;
        this.amount = amount;
        this.payer = payer;
        this.participants = participants;
        this.tags = tags;
        this.expenseType = expenseType;
    }

    /**
     * Returns the expense name.
     *
     * @return the expense name string.
     */
    public String getExpenseName() {
        return expenseName;
    }

    /**
     * Returns the amount of this expense.
     *
     * @return the amount as a double.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the payer of this expense.
     *
     * @return the {@code TxtAdaptedPerson} representing the payer.
     */
    public TxtAdaptedPerson getPayer() {
        return payer;
    }

    /**
     * Returns the set of participants sharing this expense.
     *
     * @return a set of {@code TxtAdaptedPerson}.
     */
    public Set<TxtAdaptedParticipant> getParticipants() {
        return participants;
    }

    /**
     * Returns the set of tags associated with this expense.
     *
     * @return a set of {@code TxtAdaptedTag}.
     */
    public Set<TxtAdaptedTag> getTags() {
        return tags;
    }

    /**
     * Converts this adapted expense back into an {@code Expense} model object.
     *
     * @return the corresponding {@code Expense}.
     */
    public Expense toModelType() {
        Group group = this.group.toModelType();
        Person payer = this.payer.toModelType();

        Set<Participant> participants = this.participants.stream()
                .map(TxtAdaptedParticipant::toModelType)
                .collect(Collectors.toSet());

        Set<Tag> tagSet = tags.stream()
                .map(TxtAdaptedTag::toModelType)
                .collect(Collectors.toSet());

        return new Expense(group, expenseName, amount, payer, participants, tagSet, expenseType);
    }

    /**
     * Serializes this expense into a single pipe-delimited plain-text line.
     * Lists of shares and tags are joined with commas within their field.
     *
     * @return a string representation of this expense.
     */
    public String serialize() {
        String groupStr = group.serialize();

        String payerStr = payer.serialize();

        String participantsStr = participants.stream()
                .sorted((p1, p2)
                        -> p1.getName().compareToIgnoreCase(p2.getName()))
                .map(TxtAdaptedParticipant::serialize)
                .collect(Collectors.joining(LIST_SEPARATOR));

        String tagsStr = tags.stream()
                .map(TxtAdaptedTag::serialize)
                .sorted()
                .collect(Collectors.joining(LIST_SEPARATOR));

        String expenseTypeStr = expenseType.name();

        return groupStr + "|"
                + expenseName + "|"
                + amount + "|"
                + payerStr + "|"
                + participantsStr + "|"
                + tagsStr + "|"
                + expenseTypeStr;
    }

    /**
     * Deserializes a pipe-delimited plain-text line into a
     * {@code TxtAdaptedExpense}.
     * Expected format: {@code group|description|amount|payer|share1,share2|tag1,tag2}
     *
     * @param line the line to parse; must follow the expected format.
     * @return the corresponding {@code TxtAdaptedExpense}.
     * @throws IllegalArgumentException if the line does not have exactly 5 fields.
     */
    public static TxtAdaptedExpense deserialize(String line) {
        String[] parts = line.split(FIELD_SEPARATOR, -1);

        if (parts.length != 7) {
            throw new IllegalArgumentException(
                    "Invalid expense format: " + line);
        }

        TxtAdaptedGroup group = TxtAdaptedGroup.deserialize(parts[0]);
        String expenseName = parts[1].trim();
        double amount = Double.parseDouble(parts[2].trim());
        TxtAdaptedPerson payer = TxtAdaptedPerson.deserialize(parts[3]);

        Set<TxtAdaptedParticipant> participants = Arrays.stream(parts[4].split(LIST_SEPARATOR))
                .filter(s -> !s.isBlank())
                .map(TxtAdaptedParticipant::deserialize)
                .collect(Collectors.toSet());

        Set<TxtAdaptedTag> tags = Arrays.stream(parts[5].split(LIST_SEPARATOR))
               .filter(s -> !s.isBlank())
               .map(TxtAdaptedTag::deserialize)
               .collect(Collectors.toSet());

        ExpenseType expenseType = ExpenseType.valueOf(parts[6]);

        return new TxtAdaptedExpense(group, expenseName, amount, payer, participants, tags, expenseType);
    }
}
