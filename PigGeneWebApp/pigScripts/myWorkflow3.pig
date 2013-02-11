REGISTER pigGene.jar;
R1 = LOAD '$input.txt' USING pigGene.PigGeneStorage();
R2 = FILTER R1 BY chrom == 13;
R3 = LOAD '$reference.vcf' USING pigGene.PigGeneStorage();
R4 = JOIN R2 BY (chrom,pos), R3 BY (chrom,pos);
STORE R4 INTO '$output';
asf = LOAD '$asdf' USING pigGene.PigGeneStorage();
