--Joins a vcf-file with the vcf-reference-file and removes all entries, where there exists an rsNumber.
REGISTER pigGene.jar;
REGISTER SeqPig.jar;
REGISTER hadoop-bam-6.0.jar;
REGISTER sam-1.93.jar;
REGISTER picard-1.93.jar;
REGISTER variant-1.93.jar;
REGISTER tribble-1.93.jar;
REGISTER commons-jexl-2.1.1.jar;
--Loads the vcf-input file.
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
--Loads the vcf-reference file.
R2 = LOAD '$input2' USING pigGene.storage.reference.PigGeneStorageReferenceFile();
--Performs a LEFT join between the two loaded relations. The joint columns are chrom and pos.
R3 = JOIN R1 BY (chrom,pos) LEFT, R2 BY (chrom,pos);
--Selects the needed columns
R4 = FOREACH R3 GENERATE R1::chrom .. R1::persID, R2::id;
--Filters all lines where the id column is null. That means: where the join was not able to match the two relations.
R5 = FILTER R4 BY R2::id is null;
--Filters the needed columns.
R6 = FOREACH R5 GENERATE R1::chrom .. R1::persID;
--Stores the output.
STORE R6 INTO '$output1';
