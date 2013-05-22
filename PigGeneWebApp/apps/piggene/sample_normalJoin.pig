--Joins a vcf-file with the vcf-reference-file.
REGISTER pigGene.jar;

--Loads the vcf-input file.
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();

--Loads the vcf-reference file.
R2 = LOAD '$input2' USING pigGene.storage.reference.PigGeneStorageReferenceFile();

--Joins the two loaded relations. The join columns are chrom and pos.
R3 = JOIN R1 BY (chrom,pos), R2 BY (chrom,pos);

--Selects the needed columns.
R4 = FOREACH R3 GENERATE R1::chrom, R1::pos, R2::id, R1::ref .. R1::persID;

--Stores the output.
STORE R4 INTO '$output1';
