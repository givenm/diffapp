# diffapp

# Completed All requirements except:

# Requirement 6
•	Properties of original or modified that are collections (arrays, sets, lists, maps etc.) must be taken into consideration when calculating and applying diffs
Reason for not completing: This requirs consideration of how data is store in each type of Collection. Careful consideration of the instantiation of each type of collection is a cumbersome task as for 1 interface, you get so many implementations of that interface and covering them is a cumbersome task since users who use the library can choose a JDK implememntation or even create their own.

# Requirement 7
•	Cyclic relationships in original or modified should be taken into consideration when calculating and applying diffs
Reason: Ran out of time to finally implement this but ths is done by using tail recursion.

# Numbering of results needs some more debugging to make it work.
