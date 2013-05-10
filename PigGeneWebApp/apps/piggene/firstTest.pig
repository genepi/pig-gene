REGISTER pigGene.jar;
R1 = LOAD '$input1' USING PigStorage('\t');
R2 = LOAD '$input2' USING PigStorage('\t');
R3 = JOIN R1 BY ($0), R2 BY ($0);
R4 = FILTER R3 BY $0 == d1;
STORE R4 INTO '$out';
