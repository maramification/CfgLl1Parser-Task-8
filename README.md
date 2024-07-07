# CfgLl1Parser-Task-8

## German University in Cairo
### Department of Computer Science
### Assoc. Prof. Haythem O. Ismail

### CSEN1002 Compilers Lab, Spring Term 2024
**Task 8: LL(1) Parsing**

## Overview
This project involves implementing an LL(1) parser using pushdown automata (PDA) and predictive parsing tables. The goal is to construct the predictive parsing table for a given context-free grammar (CFG), construct the PDA equivalent to the CFG, and implement an LL(1) parser that uses the table and PDA to parse input strings.

## Objective
Implement the LL(1) parsing algorithm that includes:
1. Constructing the predictive parsing table for the given CFG.
2. Constructing the PDA equivalent to the CFG.
3. Implementing an LL(1) parser that uses the table and PDA to direct its decisions.

Given an input string w, the parser should signal an error if w ∉ L(G) and produce a derivation of w from S if w ∈ L(G).

## Requirements

1. **Assumptions:**
   - The set V of variables consists of upper-case English letters.
   - The start variable is the symbol S.
   - The set Σ of terminals consists of lower-case English letters (excluding 'e').
   - The letter "e" represents ε.
   - Only LL(1) CFGs are considered.

2. **Implementation:**
   - **Class Constructor `CfgLl1Parser`:**
     - Takes a single parameter, a string description of a CFG together with First and Follow sets.
     - A string encoding a CFG is of the form `V#T#R#I#O`:
       - V: String representation of the set of variables; a semicolon-separated sequence of upper-case English letters, starting with S.
       - T: String representation of the set of terminals; a semicolon-separated sequence of alphabetically sorted lower-case English letters.
       - R: String representation of the set of rules. R is a semicolon-separated sequence of pairs. Each pair represents the largest set of rules with the same left-hand side. Pairs are of the form `i/j` where i is a variable of V and j is a string representation of the set of right-hand sides—a comma-separated sequence of strings. These pairs are sorted by the common left-hand side i based on the ordering of V.
       - I: String representation of the First set of each rule. I is a semicolon-separated sequence of pairs. Pairs are of the form `i/j` where i is a variable of V and j is the string representation of the First sets of each right-hand side of a rule for i—a comma-separated sequence of strings. These sets appear in the same order as the corresponding rules and are concatenations of the symbols making up the represented set. These pairs are sorted by the common left-hand side i based on the ordering of V.
       - O: String representation of the Follow set of each variable. O is a semicolon-separated sequence of pairs. Pairs are of the form `i/j` where i is a variable of V and j is the string representation of the Follow set of i. These sets are encoded by concatenations of the symbols making up the represented set. These pairs are sorted by the common left-hand side i based on the ordering of V.
     - Example CFG:
       ```
       G1 = ({S,T}, {a,c,i}, R, S)
       R: 
       S → iST | ε
       T → cS | a
       ```
       - Encoded as: `S;T#a;c;i#S/iST,e;T/cS,a#S/i,e;T/c,a#S/$ac;T/$ac`

   - **Method `parse`:**
     - Takes an input string w and returns a string encoding a left-most derivation of w in G.
     - In case w ∉ L(G), the derivation ends with “ERROR.”
     - The `parse` method constructs a PDA equivalent to G and uses the PDA together with the LL(1) parsing table to reach its decision.
     - A string encoding a derivation is a semicolon-separated sequence of items. Each item is a sentential form representing a step in the derivation. The first item is S. If w ∈ L(G), the last item is w; otherwise, it is ERROR.
     - Example:
       - Given G1, on input string `iiac`, `parse` should return: `S; iST; iiSTT; iiTT; iiaT; iiacS; iiac`
       - On input string `iia`, `parse` should return: `S; iST; iiSTT; iiTT; iiaT; ERROR`

For any further details or clarifications, refer to the lab manual or contact the course instructor.
