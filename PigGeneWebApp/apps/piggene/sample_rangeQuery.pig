--This workflow performs a range query for 20:1-10000 (chrom 20 : pos 1-10000) and saves the result
REGISTER pigGene.jar;

--load input file
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();

--load reference file to match the rsNumber
R2 = LOAD '$input2' USING pigGene.storage.merged.PigGeneStorage();

--select all entries which match chrom 20
R3 = FILTER R1 BY chrom == '20';

--select all entries with pos greater or equal 1
R4 = FILTER R3 BY pos >= '1';

--select all entries with pos less or equal 10000
R5 = FILTER R4 BY pos <= '10000';

--select all entries of the reference file which match chrom 20
R6 = FILTER R2 BY chrom == '20';

--multiple different filtering options can be combined using the AND keyword
R7 = FILTER R6 BY pos >= '1' AND pos <= '10000';

--combines two relations if the value in the columns chrom AND pos are identically
R8 = JOIN R7 BY (chrom,pos), R5 BY (chrom,pos);
R9 = FOREACH R8 GENERATE abc, def, ghi;
STORE R9 INTO '$output1';
