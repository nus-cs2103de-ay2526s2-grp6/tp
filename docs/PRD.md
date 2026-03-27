# Product Requirement Documentation (PRD)

## Table of Contents
- [Product Overview](#product-overview)
- [Problem Statement](#problem-statement)
- [Target Users/Stakeholders](#target-usersstakeholders)
  - [Target Users](#target-users)
  - [Stakeholders](#stakeholders)
- [User Stories](#user-stories-)
  - [MUST-HAVE](#must-have)
  - [SHOULD-HAVE](#should-have)
- [Functional Requirements](#functional-requirements)
- [Non-Functional Requirements](#non-functional-requirements)

## Product Overview
This shared expense tracker is an application that helps groups manage and track shared expenses. 
Users can create groups, add members and record expenses by specifying the amount, the payer and the participants involved. 
The system automatically calculates how much each member owes and the most efficient way to settle debts.
Additional features may include debt simplification, uneven split methods (e.g. 60-40 or 70-30), multi-currency support and basic analytics to show a breakdown of expenses.

## Problem Statement
**To enable groups to handle shared expenses and debts in a more efficient and systematic manner, reducing the need for manual calculations, ultimately minimising confusion when settling payments.**

## Target Users/Stakeholders
### Target Users:
- Friend groups managing shared expenses during trips, outings or shared living arrangements.
- Housemates splitting recurring household bills such as rent, utilities and groceries.
- Event organisers tracking costs shared among participants.
- Small teams or colleagues splitting work-related costs (e.g. team lunches, office supplies).

### Stakeholders:
- End-users: the individuals within a group who add, view and settle expenses.
- Group admin: the user who creates and manages the group, adds members and maintains the group records.

## User stories 
### MUST-HAVE:
1. As a user, I want to be able to specify who exactly is involved in an expense so that people do not pay for things that they did not share.
2. As a user, I want the system to automatically calculate how much each person owes, so that I do not have to manually compute the split.
3. As a user, I want to be able to view a list of all shared expenses so that I can verify if the records are correct.
4. As a user, I want to view how much I owe or am owed in total, so that I can settle my debts easily.
5. As a user, I want my group’s shared expense data to be automatically saved so that I do not lose my records whenever the session ends.

### SHOULD-HAVE:
1. As a user, I want to be able to delete an expense so that I can fix mistakes in an added expense.
2. As a user, I want to be able to update an existing expense so that I do not have to delete and recreate an entire entry due to trivial mistakes.
3. As a user, I want to choose between equal split and custom split methods, so that expenses can reflect real-life arrangements.
4. As a user, I want to rename a group, so that I can better identify it later.
5. As a user, I want to be able to search for expenses by name or keyword so that I can quickly locate an expense without scrolling through a long list.

## Functional Requirements
1. The system should allow a user to create a new expense group with a unique name.
2. The system should allow a user to add members to a group by specifying their name.
3. The system should allow a user to record an expense by specifying a description, total amount, payer and the list of members involved.
4. The system should allow a user to view all expenses recorded within a group.
5. The system should allow a user to delete an existing expense from a group.
6. The system should prompt the user for confirmation before permanently deleting an expense.
7. The system should allow a user to edit the details of an existing expense.
8. The system should automatically compute each member's net balance (amount owed or to be received) after every expense is added, edited or deleted.
9. The system should display a simplified debt summary showing who owes whom and how much.
10. The system should allow a user to mark a specific debt between two members as settled, updating balances accordingly. 
11. The system should support equal split and custom split (by percentage or fixed amount) when recording an expense.
12. The system should allow a user to rename an existing group.
13. The system should allow a user to search for expenses within a group by entering a keyword or description.
14. The system should automatically store all data after every change and reload it during the application's next launch.

## Non-Functional Requirements
1. The application should load and display a group with up to 200 recorded expenses within 2 seconds.
2. The system should support up to 20 members per group and up to 500 expense records per group.
3. All debt and balance calculations should be accurate to 2 decimal places, with no rounding errors exceeding $0.01 per transaction.
