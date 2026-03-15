### Software Design Documentation (SDD)

## Table of Contents
- [1. System Overview](#1-system-overview)
- [2. Architecture Design](#2-architecture-design)
  - [2.1 Architectural Pattern](#architectural-pattern)
- [3. Major System Components](#3-major-system-components)
  - [Model Layer](#model-layer)
  - [Logic Layer](#logic-layer)
  - [UI Layer](#ui-layer)
  - [Storage layer](#storage-layer)
- [4. UML Diagrams](#4-uml-diagrams)
  - [4.1 Class Diagrams](#class-diagrams)
  - [4.2 Sequence Diagrams](#sequence-diagrams)
  - [4.3 Use Case Diagrams](#use-case-diagrams)
- [5. Key Design Decisions](#5-key-design-decisions)
  - [5.1 Layered Architecture]( #layered-architecture)

## 1. System Overview
The shared expense tracker is an application that enables groups of users to record, manage and settle shared expenses.
It targets friend groups, housemates and small teams who need to split costs without manual calculation.
**The system allows users to:**
- Create and manage expense groups with named members
- Record expenses with a payer, amount, description and list of involved participants
- View a simplified debt summary for the user, showing how much they owe and are owed
- Mark debts as settled and reload all data locally between sessions 

## 2. Architecture Design
# Architectural Pattern
┌─────────────────────────────────────────────────────────┐
│                       UI Layer                          │
│         MainWindow | GroupPanel | ExpensePanel          │
│                      DebtPanel                          │
│                   (JavaFX + FXML)                       │
├─────────────────────────────────────────────────────────┤
│                      Logic Layer                        │
│          ExpenseManager | DebtCalculator                │
├─────────────────────────────────────────────────────────┤
│                      Model Layer                        │
│           Group | Person | Expense | Debt               │
├─────────────────────────────────────────────────────────┤
│                     Storage Layer                       │
│                       Storage                           │
└─────────────────────────────────────────────────────────┘
Each layer communicates only with adjacent layers. The UI layer never directly addresses storage and the storage layer has no knowledge of the UI. 
This separation makes the system easier to test and maintain. 

## 3. Major System Components
# Model Layer
The model layer contains pure data classes with no business logic or UI dependencies.

# Logic Layer
The logic layer contains all business logic and computation. It is independent of the UI.

# UI Layer
Built with JavaFX and FXML, each panel is a pair of a `.fxml` layout file and a Java controller class.

# Storage Layer
Handles reading and writing all application to local storage.

## 4. UML Diagrams
# Class Diagrams
┌──────────────────────────┐          ┌──────────────────────────────────────┐
│          Group           │          │               Expense                │
├──────────────────────────┤          ├──────────────────────────────────────┤
│ - name: String           │  1    *  │ - description: String                │
│ - members: List<Person>  │──────────│ - amount: double                     │
│ - expenses: List<Expense>│          │ - payer: Person                      │
├──────────────────────────┤          │ - participants: List<Person>         │
│ + addMember(p)           │          │ - splitType: SplitType               │
│ + removeMember(p)        │          │ - splitRatios: Map<Person, Double>   │
│ + addExpense(e)          │          ├──────────────────────────────────────┤
│ + removeExpense(e)       │          │ + getShareFor(p: Person): double     │
│ + rename(name)           │          │ + getSplitType(): SplitType          │
└──────────────────────────┘          └──────────────────────────────────────┘
│ *                                        │ *        │ *
│                                          │          │
▼                               payer 1    ▼          ▼ participants *
┌──────────────────────────┐          ┌──────────────────────────┐
│          Person          │          │           Debt           │
├──────────────────────────┤          ├──────────────────────────┤
│ - name: String           │          │ - debtor: Person         │
│ - balance: double        │◄─────────│ - creditor: Person       │
├──────────────────────────┤   2      │ - amount: double         │
│ + getName(): String      │          │ - settled: boolean       │
│ + getBalance(): double   │          ├──────────────────────────┤
│ + adjustBalance(delta)   │          │ + markSettled()          │
└──────────────────────────┘          │ + isSettled(): boolean   │
                                      └──────────────────────────┘
**Relationships:**
+ `Group` 1 — * `Expense` (one Group has many Expenses)
+ `Group` 1 — * `Person` (one Group has many Persons)
+ `Expense` * — 1 `Person` (many Expenses have one payer)
+ `Expense` * — * `Person` (many Expenses involve many Persons as participants)
+ `Debt` * — 2 `Person` (each Debt links one debtor and one creditor)

# Sequence Diagrams

User          ExpensePanel      ExpenseManager             DebtCalculator     Storage
|                 |                  |                           |               |
|--[fill form]--> |                  |                           |               |
|--[click Add]--> |                  |                           |               |
|                 |--addExpense()--> |                           |               |
|                 |                  |--validate()               |               |
|                 |                  |--new Expense()            |               |
|                 |                  |--group.addExpense(expense)|               |
|                 |                  |--computeBalances()------->|               |
|                 |                  |                           |--update()     |
|                 |                  |<-----------return debts   |               |
|                 |                  |--saveData()------------------------------>|
|                 |<--refreshUI()----|                           |               |
|<--updated list--|                  |                           |               |

# Use Case Diagrams

                    ┌─────────────────────────────────────────┐
                    │          Shared Expense Tracker         │
                    │                                         │
                    │  ○ Create Group                         │
                    │  ○ Rename Group                         │
                    │  ○ Add Member                           │
                    │  ○ Add Expense                          │
[User] ─────────────│  ○ Edit Expense                         │
                    │  ○ Delete Expense (with confirmation)   │
                    │  ○ View Expense List                    │
                    │  ○ View Balances                        │
                    │  ○ Mark Debt as Settled                 │
                    │  ○ Search Expenses                      │
                    │  ○ Choose Split Method                  │
                    │                                         │
                    │  ○ Auto-calculate Balances  ◄─[System]  │
                    │  ○ Auto-save Data           ◄─[System]  │
                    │  ○ Auto-load Data           ◄─[System]  │
                    └─────────────────────────────────────────┘

## 5. Key Design Decisions
# Layered Architecture
**Decision:** Adopt a strict layered architecture with no cross-layer dependencies (e.g. UI cannot call Storage directly).
**Rationale:** Separating concerns allows team members to work on different layers in parallel without major conflicts. 

----*End of document*----
