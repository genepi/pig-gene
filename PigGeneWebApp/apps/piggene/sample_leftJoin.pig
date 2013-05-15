--join vcf file with reference file and remove all entries, where there exists an rsNumber.
REGISTER pigGene.jar;
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
R2 = LOAD '$input2' USING pigGene.storage.reference.PigGeneStorageReferenceFile();
R3 = JOIN R1 BY (chrom,pos) LEFT, R2 BY (chrom,pos);
R4 = FOREACH R3 GENERATE R1::chrom .. R1::persID, R2::id;
R5 = FILTER R4 BY R2::id is null;
R6 = FOREACH R5 GENERATE R1::chrom .. R1::persID;
STORE R6 INTO '$output1';
