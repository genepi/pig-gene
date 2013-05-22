--Removes chromosomes that match 'M', 'X' or 'Y' and removes entries where genotype does not match '0/1' or '1/0'.
REGISTER pigGene.jar;

--Loads the vcf-input file.
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();

--Filters all lines where chromosome does not match one of 'M', 'X', 'Y'.
R2 = FILTER R1 BY chrom != 'M' AND chrom != 'X' AND chrom != 'Y';

--Filters all lines where the genotype information matches '0/1' or '1/0'.
R3 = FILTER R2 BY (SUBSTRING(genotype,0,3) == '0/1') OR (SUBSTRING(genotype,0,3) == '1/0');

--Stores the output.
STORE R3 INTO '$output1';
