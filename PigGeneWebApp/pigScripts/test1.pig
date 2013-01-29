REGISTER pigGene.jar;
R1 = LOAD '$abc' USING pigGene.PigGeneStorage();
R2 = FILTER R1 BY chrom == 12;
