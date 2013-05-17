--Counts the number of samples for chrom 20 and pos 138004 that don't match the reference (0/0).
REGISTER pigGene.jar;
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
R2 = FILTER R1 BY chrom == '20';
R3 = FILTER R2 BY pos == 138004;
R4 = FILTER R3 BY SUBSTRING(genotype,0,3) != '0/0';
R5 = GROUP R4 BY (chrom,pos);
R6 = FOREACH R5 GENERATE FLATTEN(group), COUNT(R4) as count;
STORE R6 INTO '$output1';
