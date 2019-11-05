# ONCESpark
This repository contains the source code for our stream-aware serial pattern mining algorithm, namely ONCESpark, which corresponds to an academic paper submitted to Future Generation Computer Systems. The source code for the work will be released here once the paper is accepted.

ONCEPSpark.scala and ONCEStreaming.scala work under the Spark platform, and ONCEStreaming.scala uses Sparkstreaming to process streaming data.The version of Spark is 1.6.0 and the version of Hadoop is 2.6.0

## Input:
a long sequence in the form of: [(s1,time1),(s2,time2),...]

episodes:(s1,s2,...), e.g., (2, 68, 65) or ('A','B','C').

time constraint: an interger number, it equals $t_n$-$t_1$, where the $t_1$ and $t_n$ are the timestamps for the first and last element in an episode, respectively.

## Output:
The code will output the frequency of the specified sequence that satisfies the time constraint in the input data set.

## Other information
For more information, please refer to the homepage for the corresponding author, Prof. Hui at http://lihuixidian.github.io.
