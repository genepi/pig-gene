REGISTER pigGene.jar;
REGISTER SeqPig.jar;
REGISTER hadoop-bam-5.1.jar;
REGISTER sam-1.76.jar;
REGISTER picard-1.76.jar;
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
REGISTER asf;
