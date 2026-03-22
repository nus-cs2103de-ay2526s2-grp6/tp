package fairshare.storage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fairshare.model.expense.Expense;
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

    private final String expenseName;
    private final double amount;
    private final TxtAdaptedPerson payer;
    private final List<TxtAdaptedPerson> shares;
    private final List<TxtAdaptedTag> tags;

    /**
     * COnstructs a {@code TxtAdaptedExpense} from an {@code Expense} model object.
     *
     * @param source the {@code Expense} to adapt; cannot be null.
     */
    public TxtAdaptedExpense(Expense source) {
        this.expenseName = source.getExpenseName();
        this.amount = source.getAmount();
        this.payer = new TxtAdaptedPerson(source.getPayer());
        this.shares = source.getParticipants().stream()
                .map(TxtAdaptedPerson::new)
                .collect(Collectors.toList());
        this.tags = source.getTags().stream()
                .map(TxtAdaptedTag::new)
                .collect(Collectors.toList());
    }

    /**
     * Constructs a {@code TxtAdaptedExpense} with all fields specified directly.
     * Used during deserialization.
     *
     * @param expenseName the expense name; cannot be null or empty.
     * @param amount      the expense amount; must be greater than 0.
     * @param payer       the adapted payer person; cannot be null.
     * @param shares      the list of adapted participant persons; cannot be null.
     * @param tags        the list of adapted tags; cannot be null.
     */
    public TxtAdaptedExpense(String expenseName, double amount,
                             TxtAdaptedPerson payer, List<TxtAdaptedPerson> shares,
                             List<TxtAdaptedTag> tags) {
        this.expenseName = expenseName;
        this.amount = amount;
        this.payer = payer;
        this.shares = shares;
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
    public List<TxtAdaptedPerson> getShares() {
        return shares;
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
        List<Person> participants = shares.stream()
                .map(TxtAdaptedPerson::toModelType)
                .collect(Collectors.toList());

        List<Tag> tagList = tags.stream()
                .map(TxtAdaptedTag::toModelType)
                .collect(Collectors.toList());

        return new Expense(expenseName, amount,
                payer.toModelType(), participants, tagList);
    }

    /**
     * Serializes this expense into a single pipe-delimited plain-text line.
     * Lists of shares and tags are joined with commas within their field.
     *
     * @return a string representation of this expense.
     */
    public String serialize() {
        String shareStr = shares.stream()
                .map(TxtAdaptedPerson::serialize)
                .collect(Collectors.joining(LIST_SEPARATOR));

        String tagsStr = tags.stream()
                .map(TxtAdaptedTag::serialize)
                .collect(Collectors.joining(LIST_SEPARATOR));

        return expenseName + "|"
                + amount + "|"
                + payer.serialize() + "|"
                + shareStr + "|"
                + tagsStr;
    }

    /**
     * Deserializes a pipe-delimited plain-text line into a
     * {@code TxtAdaptedExpense}.
     * Expected format: {@code description|amount|payer|share1,share2|tag1,tag2}
     *
     * @param line the line to parse; must follow the expected format.
     * @return the corresponding {@code TxtAdaptedExpense}.
     * @throws IllegalArgumentException if the line does not have exactly 5 fields.
     */
    public static TxtAdaptedExpense deserialize(String line) {
        String[] parts = line.split(FIELD_SEPARATOR, -1);

        if (parts.length != 5) {
            throw new IllegalArgumentException(
                    "Invalid expense format: " + line);
        }

        String description = parts[0].trim();
        double amount = Double.parseDouble(parts[1].trim());
        TxtAdaptedPerson payer = TxtAdaptedPerson.deserialize(parts[2]);

        List<TxtAdaptedPerson> shares = Arrays.stream(parts[3].split(LIST_SEPARATOR))
                .filter(s -> !s.isBlank())
                .map(TxtAdaptedPerson::deserialize)
                .collect(Collectors.toList());

        List<TxtAdaptedTag> tags = Arrays.stream(parts[4].split(LIST_SEPARATOR))
                .filter(s -> !s.isBlank())
                .map(TxtAdaptedTag::deserialize)
                .collect(Collectors.toList());

        return new TxtAdaptedExpense(description, amount, payer, shares, tags);
    }
}
