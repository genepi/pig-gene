//this is a dummy workflow that is used for testing purpose while developing the application1
REGISTER pigGene.jar;
REGISTER dataFu.jar;
R1 = LOAD '$input.vcf' USING pigGene.PigGeneStorage();

//filter
R2 = FILTER R1 BY chrom == 12;
R3 = LOAD '$input2.txt' USING PigStorage(' ');

//join
R4 = JOIN R2 BY (chrom,pos), R3 BY (chrom,pos);
R3 = FILTER R4 BY x==2;

//comment for user script...
R9 = FILTER R5 BY chrom==1;
STORE R9 INTO output2.txt;
STORE R5 INTO '$output.txt';
