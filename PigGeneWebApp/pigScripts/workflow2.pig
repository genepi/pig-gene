REGISTER pigGene.jar;
R1 = LOAD '$input.txt' USING pigGene.PigGeneStorage();
R2 = FILTER R1 BY chrom == 12;
STORE R2 INTO '$output.txt';
