REGISTER pigGene.jar;
R1 = LOAD '$input' USING pigGene.storage.merged.PigGeneStorage();
R2 = FILTER R1 BY pos == 68922;
STORE R2 INTO '$output';
