# KakuroWithSat
Solving the kakuro puzle using sat4j
## Dependencies
1. sat4j
2. guawa
3. java 8
## Current usage
``java -jar KakuroWithSat <input file path> verbose(optional)``

use verbose to see the actual cnf file that is generated before running the sat4j

the result will be in a newly created text file.
### Input file format:
- first all cells in the game should be enumarated
- each line represents a diffarent clue the line should be in the form <sum> <cell#1> ....<cell#i>
  
  example:
  
  ```
  6 1 2
  
  17 3 4 5 6
  
  3 7 8
  
  4 9 10
  
  14 11 12 13 14
  
  11 3 7
  
  8 4 8 11
  
  3 1 5
  
  18 2 6 9 13
  
  3 10 14
  ```
  
  clue number 9 got sum of 18 and got cells number 2,6,9 and 13 attached to it
