REGISTER pigGene.jar;
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
R2 = LOAD '$input2' USING pigGene.storage.reference.PigGeneStorageReferenceFile();
R3 = JOIN R1 BY (chrom,pos), R2 BY (chrom,pos);
STORE R3 INTO '$output1';
