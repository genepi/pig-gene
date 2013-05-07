REGISTER pigGene.jar;
R1 = LOAD '$in1' USING pigGene.storage.merged.PigGeneStorage();
R2 = LOAD '$in2' USING pigGene.storage.merged.PigGeneStorage();
R3 = FILTER asdf BY asdf;
STORE sdf INTO '$out1';
STORE wet INTO '$out2';
