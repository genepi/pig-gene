--example workflow
REGISTER pigGene.jar;
R1 = LOAD '$input.txt' USING PigStorage('\t');
R2 = FILTER R1 BY chrom == 12;
STORE R2 INTO '$output.txt';
