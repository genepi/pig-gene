register pigGene.jar;

A = LOAD '$input' USING pigGene.PigGeneLoader();
DUMP A;