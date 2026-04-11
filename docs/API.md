# API Documentation: Shared Expense Tracker

This document provides a high-level overview of the internal component
interfaces and their interactions.

---
## Table of Contents
- [1. Logic Component](#1-logic-component)
  - [1.1 Key Methods](#11-key-methods)
- [2. Model Component](#2-model-component)
  - [2.1 Key Methods](#21-key-methods)
- [3. Storage Component](#3-storage-component)
  - [3.1 Key Methods](#31-key-methods)
- [4. UI Component](#4-ui-component)
  - [4.1 Key Methods](#41-key-methods)
- [5. Custom Exceptions](#5-custom-exceptions)
- [6. Storage Format](#6-storage-format)
- [7. Supported Commands](#7-supported-commands)

## 1. Logic Component

### 1.1 Key Methods
- `execute(String userInput)` — Returns `CommandResult`, throws
  `ParseException`, `CommandException`. Parses and executes the
  command against the Model, then saves state to Storage.
  Supported commands: `add`, `delete`, `update`, `filter`, `list`,
  `settle`, `help`, `exit`
- `getFilteredExpenseList()` — Returns `ObservableList<Expense>`.
  Provides the current filtered expense list for the UI.
- `getExpenseList()` — Returns `List<Expense>`. Provides the full
  unfiltered expense list used by `TagPieChart`, `StatusBar` and
  `GroupWindow`.
- `calculateBalances()` — Returns `Map<Group, List<Balance>>`.
  Returns the simplified debt settlement list grouped by group,
  used by BalancePanel and GroupWindow.

## 2. Model Component

### 2.1 Key Methods
- `addExpense(Expense expense)` — Adds expense to the list and
  recalculates balances.
- `deleteExpense(int index)` — Removes expense at the given index
  and recalculates balances. Returns the deleted `Expense`.
- `updateExpense(int index, Expense updatedExpense)` — Replaces the
  expense at the given index with the updated expense and
  recalculates balances.
- `filterExpenses(Predicate<Expense> predicate)` — Filters the
  visible expense list.
- `getFilteredExpenseList()` — Returns `ObservableList<Expense>`.
- `getExpenseList()` — Returns the full unfiltered
  `List<Expense>`.
- `calculateBalances()` — Returns `List<Balance>`. Computes the
  simplified debt settlement list from all expenses.

## 3. Storage Component

### 3.1 Key Methods
- `readFairShare()` — Returns `List<Expense>`, throws
  `StorageException`. Reads from `data/expenses.txt`. If the file
  is missing, returns an empty list. If the file is corrupted,
  deletes it and throws `StorageException` with a warning message.
- `saveFairShare(List<Expense> expenses)` — throws
  `StorageException`. Writes all expenses to `data/expenses.txt`
  in pipe-delimited format. Creates the `data/` directory if it
  does not exist.
- `getFairShareFilePath()` — Returns `Path`. Returns the path to
  the storage file.

## 4. UI Component

### 4.1 Key Methods

**`MainWindow`**
- `fillInnerParts()` — Instantiates and injects all UI subcomponents
  into their FXML placeholders.
- `start(Stage primaryStage)` — Shows the primary stage.
- `showStartupMessage(String message)` — Displays a message in the
  result display on startup, used to warn about corrupted data.

**`ExpenseListPanel`**
- `ExpenseListPanel(ObservableList<Expense> expenses)` — Constructs
  the panel bound to the given observable list. Automatically shows
  an empty state message when the list is empty.

**`BalancePanel`**
- `refresh(groupBalances: Map<Group, List<Balance>>)` —
  Re-renders the accordion with updated balance data
  grouped by group.

**`PieChart`** 
- `refresh(expenses: List<Expense>)` — Re-renders the pie
  chart with updated expense data. Supports toggle between
  tag view and group view. Uses fixed colours per label so
  colours do not change on refresh. Excludes settlements.
  Shows a no-data label when empty.
**`StatusBar`**
- `refresh(List<Expense> expenses)` — Updates the expense count and
  total amount. Excludes settlements.

**`GroupWindow`**
- `show(List<Expense> expenses, List<Balance> balances)` — Opens
  the group window and refreshes its content.
- `refreshIfShowing(List<Expense> expenses, List<Balance> balances)`
  — Refreshes the group window only if it is currently open.

**`Header`**
- `setOnGroupsClicked(Runnable handler)` — Sets the handler called
  when the Groups button is clicked.

## 5. Custom Exceptions
- `ParseException` — thrown when user input cannot be parsed into a
  valid command
- `CommandException` — thrown when a valid command fails to execute
  against the model
- `StorageException` — thrown when file read or write fails, or when
  the file is corrupted
- `UiException` — thrown when an FXML file cannot be loaded by
  `FXMLLoader`

## 6. Storage Format
Each expense is stored as one pipe-delimited line:
```
group|expenseName|amount|payerName|participant1:shares,participant2:shares|tag1,tag2|expenseType
```

Example (normal expense):
```
malaysia|Lunch|40.0|alice|bob:2,mary:1|food|EXPENSE
```

Example (settlement):
```
malaysia|Settlement|10.0|alice|bob:1||SETTLEMENT
```

Fields:
- `group` — group name
- `expenseName` — expense description
- `amount` — total amount as a decimal
- `payerName` — name of the person who paid
- `participants` — comma-separated list of `name:shares` pairs
- `tags` — comma-separated tag names, empty if none
- `expenseType` — either `EXPENSE` or `SETTLEMENT`

## 7. Supported Commands
- Add expense --> `add n/NAME a/AMOUNT g/GROUP p/PAYER s/PERSON:SHARES... [t/TAG...]`
  - e.g. `add n/Lunch a/40.0 g/malaysia p/alice s/bob:2 s/mary:1 t/food`
- Delete expense --> `delete INDEX`
  - e.g. `delete 1`
- Update expense --> `update INDEX [n/NAME] [a/AMOUNT] [p/PAYER] [s/PERSON:SHARES...] [t/TAG...]`
  - e.g. `update 1 a/50.0`
- Filter expenses --> `filter [g/GROUP] [n/NAME] [p/PAYER] [s/PERSON] [t/TAG]`
  - e.g. `filter g/malaysia`
- List all expenses --> `list`
- Settle debt --> `settle g/GROUP p/PAYER r/RECEIVER a/AMOUNT`
  - e.g. `settle g/malaysia p/alice r/bob a/10.0`
- Help --> `help`
- Exit --> `exit`
