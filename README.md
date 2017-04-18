# Kleinbergs-Hits

# Relevant files:

>hits9169.java

# Compilation instructions:

>$ javac hits9169.java

# Instructions to run the HIITS algorithm program:

>$ java hits9169 iterations initialValue path_to_file_and_name

# Implementation and Limitations:

The algorithms have been implemented as guided by the notes.

# Initial Values:

0 --> 0

1 --> 1

-1 --> 1/N

-2 --> 1/ SquareRoot(N)

Default --> Value specified

# Iterations:

0 --> Precision for 5 digits after decimal

< 0 --> Precision with the number of digits corresponding to the positive value of number specified

\> 0 --> Number of iterations

# Issues:

1 or more extra iterations for the same input as the values are not rounded off for calculation but they are for the display

If precision is selected as -7, during display there may be offset of 1 digit as the digits after are rounded off some times. 
