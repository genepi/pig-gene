REGISTER pigGene.jar;
R1 = LOAD '$6exomes.vcf' USING pigGene.PigGeneStorage();
R2 = FILTER R1 BY chrom == 12;
R3 = LOAD '$reference.vcf' USING pigGene.PigGeneStorage();
R4 = JOIN R2 BY (chrom,id), R3 BY (chrom,id);
STORE R4 INTO '$end';
