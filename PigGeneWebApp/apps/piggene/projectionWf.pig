REGISTER pigGene.jar;
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
R2 = FOREACH R1 GENERATE $0;
STORE R2 INTO '$output1';
