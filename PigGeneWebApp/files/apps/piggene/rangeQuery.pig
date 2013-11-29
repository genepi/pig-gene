--Performs a range query for 20:1-1000000 (chrom 20 : pos 1-1000000), extracts the quality of the genotype column.
REGISTER pigGene.jar;

--Loads the vcf-input file.
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();

--Loads the vcf-reference file.
R2 = LOAD '$input2' USING pigGene.storage.reference.PigGeneStorageReferenceFile();

--Filters all lines that match chromosome '20'.
R3 = FILTER R1 BY chrom == '20';

--Filters all lines where position is larger or equal than 1 and position is smaller or equal than 1000000.
R4 = FILTER R3 BY pos >= 1 AND pos <= 1000000;

--Filters all lines that match chromosome '20'.
R5 = FILTER R2 BY chrom == '20';

--Filters all lines where position is larger or equal than 1 and position is smaller or equal than 1000000.
R6 = FILTER R5 BY pos >= 1 AND pos <= 1000000;

--Joins the filtered relations. The join columns are chrom and pos.
R7 = JOIN R4 BY (chrom,pos), R6 BY (chrom,pos);

--Selects the needed columns and extracts the quality of the genotype.
R8 = FOREACH R7 GENERATE R6::chrom, R6::pos, R6::id, R4::genotype, pigGene.UDFs.ExtractQuality(R4::genotype), R4::persID;

--Stores the output.
STORE R8 INTO '$output1';
