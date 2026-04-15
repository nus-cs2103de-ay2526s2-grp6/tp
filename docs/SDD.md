# Software Design Documentation (SDD)

## Table of Contents
- [1. System Overview](#1-system-overview)
- [2. Architecture Design](#2-architecture-design)
    - [2.1 Architectural Pattern](#21-architectural-pattern)
- [3. Major System Components](#3-major-system-components)
    - [3.1 Model Layer](#31-model-layer)
    - [3.2 Logic Layer](#32-logic-layer)
    - [3.3 UI Layer](#33-ui-layer)
    - [3.4 Storage Layer](#34-storage-layer)
- [4. UML Diagrams](#4-uml-diagrams)
    - [4.1 Class Diagrams](#41-class-diagrams)
    - [4.2 Sequence Diagrams](#42-sequence-diagrams)
    - [4.3 Use Case Diagram](#43-use-case-diagram)
- [5. Key Design Decisions](#5-key-design-decisions)
    - [5.1 Layered Architecture](#51-layered-architecture)
    - [5.2 Command Pattern for Logic](#52-command-pattern-for-logic)
    - [5.3 Plain-Text Storage](#53-plain-text-storage)
    - [5.4 JavaFX ObservableList for UI Binding](#54-javafx-observablelist-for-ui-binding)
    - [5.5 Proportional Split Using Participant Shares](#55-proportional-split-using-participant-shares)
    - [5.6 Graceful Handling of Corrupted Storage Files](#56-graceful-handling-of-corrupted-storage-files)
- [6. Testing](#6-testing)
  - [6.1 Unit Testing](#61-unit-testing)
  - [6.2 Manual Testing](#62-manual-testing)
- [7. AI Use Declaration](#7-ai-use-declaration)

## 1. System Overview
The Shared Expense Tracker is an application that enables groups of users to record, manage and settle shared expenses.
It targets friend groups, housemates and small teams who need to split costs without manual calculation.

**The system allows users to:**
- Record expenses with a payer, amount, description, group and list of involved participants using a command-line style input
- Support equal split and proportional split (e.g. s/bob:2 s/mary:1 means bob pays 2/3, mary pays 1/3)
- Update existing expenses without deleting and recreating them
- Automatically calculate how much each member owes using a debt simplification algorithm
- View a simplified balance summary showing who owes whom and how much
- Filter expenses by name, payer, participant or tag
- Settle debts between two members using the settle command
- View a spending breakdown by tag as a pie chart
- View all groups categorised as Active or Settled in a popup window
- Persist all expense data locally between sessions
- Access a help window listing all available commands

## 2. Architecture Design
### 2.1 Architectural Pattern
![Architectural Pattern Diagram](architecture/ArchitecturalPatternDiagram.png)

Each layer communicates only with adjacent layers. The UI layer never directly addresses storage and the storage layer has no knowledge of the UI. This separation makes the system easier to test and maintain.

## 3. Major System Components
### 3.1 Model Layer
The model layer contains pure data classes with no business logic or UI dependencies. Key classes:
- `Expense` — stores expense name, amount, payer, group, participants, tags and expense type (EXPENSE or SETTLEMENT)
- `Person` — stores a person's name
- `Group` — stores a group's name
- `Tag` — stores a tag name
- `Balance` — represents a directional debt between two persons
- `BalanceCalculator` — calculates net balance of each user
- `ExpenseList` — wraps an `ObservableList<Expense>` for JavaFX binding
- `ModelManager` — implements `Model`, manages `ExpenseList` and
  `FilteredList`
- `Participant` — stores a person and their number of shares for proportional split expenses. Each expense has a list of participants with share values that determine how the cost is divided.

### 3.2 Logic Layer
The logic layer processes all user commands. It follows the Command Pattern — each command is parsed into a `Command` object and executed
against the `Model`.
- `LogicManager` — implements `Logic`, coordinates parsing and execution
- `FairShareParser` — parses the command word and delegates to the appropriate parser
- `AddCommandParser`, `DeleteCommandParser`, `FilterCommandParser`,
  `UpdateCommandParser`, `SettleCommandParser` — parse arguments for each command
- `AddCommand`, `DeleteCommand`, `FilterCommand`, `ListCommand`,
  `UpdateCommand`, `SettleCommand`, `HelpCommand`, `ExitCommand` — execute operations against the model
- `CommandResult` — wraps the feedback string and flags for help and exit returned after execution
- `ParserUtil` — utility class providing shared parsing methods such as tokenizing command arguments, extracting field values and validating input formats
- `UpdateCommand` — updates specific fields of an existing expense by index. Uses an `UpdateFields` inner class to hold only the fields that need changing.

### 3.3 UI Layer
Built with JavaFX and FXML. Each component loads its own `.fxml` file using `FXMLLoader` with `setController(this)` — no `fx:controller`attribute is used in any FXML file.

- `MainWindow` — root window, implements `Ui`, holds all sub-panels and wires them together. Refreshes all panels after every command.
- `Header` — displays the app logo, name, subtitle and a Groups button that opens the `GroupWindow`
- `ExpenseListPanel` — displays the filtered expense list as cards. Shows an empty state message when no expenses exist.
- `ExpenseCard` — renders a single expense's details including group badge, expense name, amount, payer, participant chips showing percentage and dollar amount, and tags. Colour coded by expense type and amount.
- `BalancePanel` — displays the simplified debt summary as cards with a "Who Owes What" section header
- `BalanceCard` — renders a single balance entry showing all debts for one person
- `TagPieChart` — displays a pie chart showing spending breakdown by tag, excluding settlements. Shows a "No expenses yet" label when empty.
- `StatusBar` — displays total expense count and total amount, excluding settlements, at the bottom of the expense list
- `GroupWindow` — a separate popup window showing all groups categorised as Active (outstanding balances) or Settled (all balances cleared). Refreshes automatically if open when a command is executed.
- `ResultDisplay` — shows command feedback messages in a monospace font with a blue left border
- `CommandBox` — accepts user text input, executes on Enter key or Send button click
- `HelpWindow` — separate popup window listing all available commands

### 3.4 Storage Layer
Handles reading and writing all expense data to a local plain-text file.
- `StorageManager` — implements `Storage`, delegates to
  `TxtFairShareStorage`
- `TxtFairShareStorage` — reads and writes to `data/expenses.txt`. Handles corrupted files by deleting them and throwing a
  `StorageException` so the app can start fresh with a warning.
- `TxtSerializableFairShare` — converts between `Expense` objects and text lines
- `TxtAdaptedExpense` — storage representation of an `Expense`, serialized as a pipe-delimited line including group and expense type
- `TxtAdaptedParticipant` — storage representation of a `Participant`, serialized as `name:shares` e.g. `bob:2`
- `TxtAdaptedPerson` — storage representation of a `Person`
- `TxtAdaptedTag` — storage representation of a `Tag`

## 4. UML Diagrams
### 4.1 Class Diagrams
The system is organized into four layers. Each layer's class diagram is shown below.

**UI Layer:**
![UI Class Diagram](architecture/UiClassDiagram.png)
- BalancePanel uses an Accordion grouped by group name,
  with each group expandable to show its balance cards.
  Active groups show an orange indicator, settled groups
  show a green indicator.
- TagPieChart is now called PieChart and supports toggling
  between spending by tag and spending by group
- PieChart uses fixed colours per tag/group so colours do
  not change on refresh

**Logic Layer:**
![Logic Class Diagram](architecture/LogicClassDiagram.png)

**Model Layer:**
![Model Class Diagram](architecture/ModelClassDiagram.png)

**Storage Layer:**
![Storage Class Diagram](architecture/StorageClassDiagram.png)

### 4.2 Sequence Diagrams
**Add Expense:**
![Add Expense Sequence Diagram](architecture/AddExpenseSequenceDiagram.png)

The sequence diagram above illustrates the flow when a user types
`add n/Lunch a/20.0 g/malaysia p/alice s/alice:1 s/bob:1 t/food`:
1. User types the command into `CommandBox`
2. `MainWindow` calls `execute()` on `LogicManager`
3. `LogicManager` passes the input to `FairShareParser`
4. `FairShareParser` creates `AddCommandParser` which parses the arguments and creates `AddCommand`
5. `LogicManager` calls `execute(model)` on `AddCommand`
6. `AddCommand` calls `addExpense()` on `Model`
7. `Model` calls `BalanceCalculator.calculate()` to recompute balances
8. `LogicManager` calls `saveFairShare()` on `Storage`
9. `MainWindow` refreshes `ExpenseListPanel`, `BalancePanel`
   (grouped by group), `PieChart`, `StatusBar` and `GroupWindow`
   (if open)

**Delete Expense:**
![Delete Expense Sequence Diagram](architecture/DeleteExpenseSequenceDiagram.png)

The sequence diagram above illustrates the flow when a user types
`delete 1`:
1. User types the command into `CommandBox`
2. `MainWindow` calls `execute()` on `LogicManager`
3. `LogicManager` passes the input to `FairShareParser`
4. `FairShareParser` creates `DeleteCommandParser` which parses the index argument and creates `DeleteCommand`
5. `LogicManager` calls `execute(model)` on `DeleteCommand`
6. `DeleteCommand` calls `deleteExpense(targetIndex)` on `Model`
7. `Model` calls `BalanceCalculator.calculate()` to recompute balances with the remaining expenses
8. `LogicManager` calls `saveFairShare()` on `Storage` to persist the updated state
9. `MainWindow` refreshes all UI panels and displays"Expense deleted"

**Update Expense:**
![Update Expense Sequence Diagram](architecture/UpdateExpenseSequenceDiagram.png)

The flow when a user types `update 1 a/50.0`:
1. User types the command into `CommandBox`
2. `MainWindow` calls `execute()` on `LogicManager`
3. `LogicManager` passes the input to `FairShareParser`
4. `FairShareParser` creates `UpdateCommandParser` which parses the index and fields, creating an `UpdateCommand` with an
   `UpdateFields` object containing only the changed fields
5. `LogicManager` calls `execute(model)` on `UpdateCommand`
6. `UpdateCommand` gets the target expense from the filtered list, creates a new `Expense` with the updated fields, and calls
   `model.updateExpense()`
7. `Model` recalculates balances via `BalanceCalculator`
8. `LogicManager` saves updated state via `Storage`
9. `MainWindow` refreshes all UI panels

### 4.3 Use Case Diagram
![Use Case Diagram](architecture/UseCaseDiagram.png)

The use case diagram above shows all interactions between actors and
the system in the current implementation.

**User** can:
- Add an expense specifying name, amount, group, payer, participants and tags
- Delete an expense by index
- Update an existing expense by index
- Settle a debt between two members
- List all expenses
- Filter expenses by group, name, payer, participant or tag
- View the balance summary showing who owes whom
- View spending breakdown by tag in the pie chart
- View all groups categorised as Active or Settled
- Open the help window to view all available commands

**System** automatically:
- Recalculates balances after every add, update, delete or settle
- Saves data to disk after every command
- Loads saved data on application startup
- Shows a warning if saved data was corrupted on startup

## 5. Key Design Decisions

### 5.1 Layered Architecture
**Decision:** Adopt a strict layered architecture (UI → Logic → Model → Storage) with no cross-layer dependencies.

**Rationale:** Separating concerns allows team members to work on different layers in parallel without conflicts and makes each layer independently testable.

### 5.2 Command Pattern for Logic
**Decision:** Each user action is encapsulated as a `Command` object parsed by a dedicated parser class.

**Rationale:** Adding new commands only requires creating a new`Command` and `Parser` class without modifying existing code, following the Open-Closed principle.

### 5.3 Plain-Text Storage
**Decision:** Persist data as a pipe-delimited `.txt` file using custom serialization.

**Rationale:** Simple to implement and debug without external dependencies. Each expense is stored as one line in the format
`group|name|amount|payer|participants|tags|expenseType`.

### 5.4 JavaFX ObservableList for UI Binding
**Decision:** Use `ObservableList` and `FilteredList` from JavaFX for the expense list.

**Rationale:** JavaFX `ListView` automatically reflects changes to an
`ObservableList`, reducing the need for manual UI refresh calls.

### 5.5 Proportional Split Using Participant Shares
**Decision:** Model split proportions as integer shares per participant rather than percentages or fixed amounts.

**Rationale:** Integer shares are simpler to input. `s/bob:2 s/mary:1` is more intuitive than `s/bob:66.67 s/mary:33.33`.
The fraction is computed at display time from the participant's shares divided by the total shares, shown as both a percentage and a dollar amount on the expense card.

### 5.6 Graceful Handling of Corrupted Storage Files
**Decision:** When the storage file cannot be parsed, delete it and start with an empty expense list rather than crashing.

**Rationale:** A corrupted file should not prevent the app from launching. The user is shown a warning message on startup so they are aware their previous data was lost.

### 5.7 FXML Controller Set in Java
**Decision:** All FXML files omit `fx:controller` and instead call`fxmlLoader.setController(this)` in the Java constructor.

**Rationale:** Setting the controller in Java avoids the "Controller value already specified" error that occurs when both the FXML attribute and Java call are used together. It also keeps the
controller wiring explicit and consistent across all UI components.

### 5.8 UI Refresh After Every Command
**Decision:** After every successful command, `MainWindow` explicitly refreshes `BalancePanel`, `TagPieChart`, `StatusBar` and
`GroupWindow` (if open), and updates the window title.

**Rationale:** While `ExpenseListPanel` updates automatically via`ObservableList` binding, the other panels derive computed data
(balances, tag totals, group status) that must be recalculated and re-rendered explicitly after each change.

### 5.9 Balance Panel Grouped by Group
**Decision:** Display balances in an accordion grouped by group name rather than a flat list.

**Rationale:** When multiple groups exist, a flat balance list is confusing since the same person may owe different amounts in different groups. Grouping by group name makes
it clear which debts belong to which trip or event.

## 6. Testing

### 6.1 Unit Testing
Unit tests are written using JUnit 5 and cover the Storage layer comprehensively. The following test classes are included:

**Storage Layer:**
- `TxtAdaptedGroupTest` — tests serialization, deserialization
  and model conversion of group data
- `TxtAdaptedPersonTest` — tests serialization, deserialization
  and model conversion of person data
- `TxtAdaptedTagTest` — tests serialization, deserialization
  and model conversion of tag data
- `TxtAdaptedParticipantTest` — tests serialization,
  deserialization and model conversion of participant data
  including share values
- `TxtAdaptedExpenseTest` — tests serialization, deserialization
  and model conversion of expense data including group,
  expense type and round-trip integrity
- `TxtSerializableFairShareTest` — tests saving and loading
  the full expense list to and from file, including empty lists,
  settlements and nested directories
- `TxtFairShareStorageTest` — tests the full storage pipeline
  including corrupted file handling, file deletion and data
  preservation across save and read cycles
- `StorageManagerTest` — tests the storage manager as the
  top-level storage interface

**Logic Layer:**
- `LogicManagerTest` — tests command execution, balance
  calculation and expense list retrieval
- `AddCommandTest`, `DeleteCommandTest`, `UpdateCommandTest`,
  `FilterCommandTest`, `ListCommandTest`, `SettleCommandTest`,
  `HelpCommandTest`, `ExitCommandTest`, `ClearCommandTest` —
  tests each command's execution logic against the model
- `FilterCommandParserTest` — tests parsing of filter command
  arguments
- `CommandResultTest` — tests the command result wrapper

To run all tests: `gradlew test`

### 6.2 Manual Testing

The UI layer was tested manually as JavaFX components require
a running application thread and cannot be unit tested without
a dedicated testing framework such as TestFX.

The following UI components were tested manually:

**Layout and Styling:**
- Header displays app logo, title and subtitle correctly
- Expense list panel shows cards with correct colour coding
  for normal expenses and settlements
- Balance panel accordion groups balances correctly by group
  with active and settled indicators
- Pie chart toggles correctly between tag and group views
  and updates after every command
- Status bar shows correct expense count and total
- Groups window shows correct active and settled groups

**Functionality:**
- Adding an expense updates the expense list, balance panel,
  pie chart, status bar and groups window correctly
- Deleting an expense updates all panels correctly
- Updating an expense updates all panels correctly
- Settling a debt updates all panels correctly
- Filtering expenses updates the expense list and pie chart
  correctly
- Running `list` after a filter restores all expenses
- Empty state message appears when expense list is empty
- Balance panel empty state appears when all debts are settled
- Groups window active and settled categorisation is correct
- Help window displays all available commands correctly
- Startup message displays on first launch
- Escape key clears the command box
- Groups window refreshes automatically when open

**Edge Cases Tested:**
- Corrupted data file on startup shows warning message and
  starts with empty list
- Group names are case-insensitive — e.g `JB` and `jb` are
  treated as the same group
- Settlements are excluded from pie chart and status bar totals
- Adding expenses to multiple groups shows all groups correctly
  in the balance panel and groups window

---

## 7. AI Use Declaration

**UI layer:**
Claude AI was used to organize and clean up the structure of the FXML files, namely to avoid repetition.
Additionally, Claude AI was used to help fix UI bugs and improve the overall UI: 
- the various panels not displaying properly
- implement insights window as a separate popup window 
- generate ideas on small UI improvements:
  - display group names labels in expense panel
  - display shares in labels in expense panel, with percentages and amounts shown 
