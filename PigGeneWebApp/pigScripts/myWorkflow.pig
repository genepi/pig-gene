//this is a dummy workflow that is used for testing purpose while developing the application
REGISTER pigGene.jar;
REGISTER dataFu.jar;

//input file
R1 = LOAD '$input.vcf' USING pigGene.storage.merged.PigGeneStorage();
R2 = FILTER R1 BY chrom == 12;
//here could be a script;
R3 = LOAD '$input2.txt' USING PigStorage(' ');
R4 = JOIN R2 BY (chrom,pos), R3 BY (chrom,pos);
R5 = FILTER R4 BY x == 2;
//another script;
STORE R5 INTO '$output.txt';
