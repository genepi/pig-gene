REGISTER pigGene.jar;
R1 = LOAD '$input1' USING pigGene.storage.reference.PigGeneStorageReferenceFile();
R2 = FILTER R1 BY chrom == '12';
STORE R2 INTO '$output1';
