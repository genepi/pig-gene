--Removes chromosomes that match 'M', 'X' or 'Y' and removes entries where genotype does not match '0/1' or '1/0'.
REGISTER pigGene.jar;
REGISTER SeqPig.jar;
REGISTER hadoop-bam-6.0.jar;
REGISTER sam-1.93.jar;
REGISTER picard-1.93.jar;
REGISTER variant-1.93.jar;
REGISTER tribble-1.93.jar;
REGISTER commons-jexl-2.1.1.jar;
--Loads the vcf-input file.
R100 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
--Filters all lines where chromosome does not match one of 'M', 'X', 'Y'.
R2000 = FILTER R1 BY chrom != 'M' AND chrom != 'X' AND chrom != 'Y';
--Filters all lines where the genotype information matches '0/1' or '1/0'.
R390 = FILTER R2 BY (SUBSTRING(genotype,0,3) == '0/1') OR (SUBSTRING(genotype,0,3) == '1/0');
