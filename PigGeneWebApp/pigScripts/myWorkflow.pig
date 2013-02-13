REGISTER pigGene.jar;

//my comment
R1 = LOAD '$input.txt' USING pigGene.PigGeneStorage();

//blub ist super!
R2 = FILTER R1 BY chrom == 12;
R3 = LOAD '$reference.vcf' USING pigGene.PigGeneStorage();
R4 = JOIN R2 BY (chrom,pos), R3 BY (chrom,pos);

//test
STORE R4 INTO '$output';

//test it
REGISTER asdsdg;
