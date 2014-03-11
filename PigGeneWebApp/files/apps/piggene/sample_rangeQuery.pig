--Performs a range query for 20:1-1000000 (chrom 20 : pos 1-1000000) and extracts the quality of the genotype.
REGISTER pigGene.jar;

--Loads the vcf-input file.
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();

--Loads the vcf-reference file.
R2 = LOAD '$input2' USING pigGene.storage.reference.PigGeneStorageReferenceFile();

--Filters all lines that match chromosome '20'.
R3 = FILTER R1 BY chrom == '20';

--Filters all lines where position is larger or equal than 1.
R4 = FILTER R3 BY pos >= 1;

--Filters all lines where position is smaller or equal than 1000000.
R5 = FILTER R4 BY pos <= 1000000;

--Filters all lines that match chromosome '20'.
R6 = FILTER R2 BY chrom == '20';

--Filters all lines where position is larger or equal than 1 and position is smaller or equal than 1000000.
R7 = FILTER R6 BY pos >= 1 AND pos <= 1000000;

--Joins the filtered relations. The join columns are chrom and pos.
R8 = JOIN R5 BY (chrom,pos), R7 BY (chrom,pos);

--Selects the needed columns and extracts the quality of the genotype.
R9 = FOREACH R8 GENERATE R7::id, R7::pos, pigGene.UDFs.ExtractQuality(R5::genotype), R5::genotype, R5::persID;

--Stores the output.
STORE R9 INTO '$output1';
