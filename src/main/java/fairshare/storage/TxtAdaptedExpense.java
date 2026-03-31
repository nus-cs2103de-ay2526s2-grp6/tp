package fairshare.storage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fairshare.model.expense.Expense;
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
    private final List<TxtAdaptedParticipant> participants;
    private final List<TxtAdaptedTag> tags;

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
                .toList();
        this.tags = source.getTags().stream()
                .map(TxtAdaptedTag::new)
                .toList();
    }

    /**
     * Constructs a {@code TxtAdaptedExpense} with all fields specified directly.
     * Used during deserialization.
     *
     * @param expenseName the expense name; cannot be null or empty.
     * @param amount      the expense amount; must be greater than 0.
     * @param payer       the adapted payer person; cannot be null.
     * @param participants the list of adapted participant persons; cannot be null.
     * @param tags        the list of adapted tags; cannot be null.
     */
    public TxtAdaptedExpense(TxtAdaptedGroup group, String expenseName, double amount, TxtAdaptedPerson payer,
                             List<TxtAdaptedParticipant> participants, List<TxtAdaptedTag> tags) {
        this.group = group;
        this.expenseName = expenseName;
        this.amount = amount;
        this.payer = payer;
        this.participants = participants;
        this.tags = tags;
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
     * Returns the list of participants sharing this expense.
     *
     * @return a list of {@code TxtAdaptedPerson}.
     */
    public List<TxtAdaptedParticipant> getParticipants() {
        return participants;
    }

    /**
     * Returns the list of tags associated with this expense.
     *
     * @return a list of {@code TxtAdaptedTag}.
     */
    public List<TxtAdaptedTag> getTags() {
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

        List<Participant> participants = this.participants.stream()
                .map(TxtAdaptedParticipant::toModelType)
                .toList();

        List<Tag> tagList = tags.stream()
                .map(TxtAdaptedTag::toModelType)
                .collect(Collectors.toList());

        return new Expense(group, expenseName, amount, payer, participants, tagList);
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
                .map(TxtAdaptedParticipant::serialize)
                .collect(Collectors.joining(LIST_SEPARATOR));

        String tagsStr = tags.stream()
                .map(TxtAdaptedTag::serialize)
                .collect(Collectors.joining(LIST_SEPARATOR));

        return groupStr + "|"
                + expenseName + "|"
                + amount + "|"
                + payerStr + "|"
                + participantsStr + "|"
                + tagsStr;
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

        if (parts.length != 6) {
            throw new IllegalArgumentException(
                    "Invalid expense format: " + line);
        }

        TxtAdaptedGroup group = TxtAdaptedGroup.deserialize(parts[0]);
        String expenseName = parts[1].trim();
        double amount = Double.parseDouble(parts[2].trim());
        TxtAdaptedPerson payer = TxtAdaptedPerson.deserialize(parts[3]);

        List<TxtAdaptedParticipant> participants = Arrays.stream(parts[4].split(LIST_SEPARATOR))
                .filter(s -> !s.isBlank())
                .map(TxtAdaptedParticipant::deserialize)
                .toList();

        List<TxtAdaptedTag> tags = Arrays.stream(parts[5].split(LIST_SEPARATOR))
                .filter(s -> !s.isBlank())
                .map(TxtAdaptedTag::deserialize)
                .toList();

        return new TxtAdaptedExpense(group, expenseName, amount, payer, participants, tags);
    }
}
