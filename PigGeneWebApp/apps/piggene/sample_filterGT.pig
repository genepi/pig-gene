--remove chromosomes that match 'M', 'X' or 'Y' and remove entries where GT is not '0/1' or '1/0'
REGISTER pigGene.jar;
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
R2 = FILTER R1 BY chrom != 'M' AND chrom != 'X' AND chrom != 'Y';
R3 = FILTER R2 BY (SUBSTRING(genotype,0,3) == '0/1') OR (SUBSTRING(genotype,0,3) == '1/0');
STORE R3 INTO '$output1';
