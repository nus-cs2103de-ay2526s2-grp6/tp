# API Documentation: Shared Expense Tracker

This document provides a high-level overview of the internal component interfaces and their interactions.

---
## Table of Contents
- [1. Logic Component](#1-logic-component)
  - [Key Methods](#11-key-methods)
- [2. Model Component](#2-model-component)
  - [2.1 Key Methods](#21-key-methods)
- [3. Storage Component](#3-storage-component)
  - [3.1 Key Methods](#31-key-methods)
- [4. Custom Exceptions](#4-custom-exceptions)

## 1. Logic Component

### 1.1 Key Methods
- `execute(String userInput)` — Returns `CommandResult`, throws
  `ParseException`, `CommandException`. Parses and executes the
  command against the Model, then saves state to Storage.
- `getFilteredExpenseList()` — Returns `ObservableList<Expense>`.
  Provides the current filtered expense list for the UI.
- `calculateBalances()` — Returns `List<Balance>`. Returns the
  simplified debt settlement list.

## 2. Model Component

### 2.1 Key Methods
- `addExpense(Expense expense)` — Adds expense to the list.
- `deleteExpense(int index)` — Removes expense at the given index,
  returns the deleted `Expense`.
- `filterExpenses(Predicate<Expense> predicate)` — Filters the
  visible expense list.
- `getFilteredExpenseList()` — Returns `ObservableList<Expense>`.
- `calculateBalances()` — Returns `List<Balance>`.

## 3. Storage Component

### 3.1 Key Methods
- `readExpenseTracker()` — Returns `List<Expense>`, throws
  `StorageException`. Reads from `data/expenses.txt`.
- `saveExpenseTracker(List<Expense> expenses)` — throws
  `StorageException`. Writes to `data/expenses.txt` in
  pipe-delimited format.

## 4. Custom Exceptions
- `ParseException` — thrown when user input cannot be parsed
- `CommandException` — thrown when a valid command fails to execute
- `StorageException` — thrown when file read/write fails
- `UiException` — thrown when an FXML file cannot be loaded
