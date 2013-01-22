REGISTER pigGene.jar;
as = LOAD '$se' USING pigGene.PigGeneStorage();
sd = LOAD '$as' USING pigGene.PigGeneStorage();
