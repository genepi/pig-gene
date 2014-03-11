REGISTER pigGene.jar;
REGISTER SeqPig.jar;
REGISTER hadoop-bam-5.1.jar;
REGISTER sam-1.76.jar;
REGISTER picard-1.76.jar;
R2 = FILTER R1 BY a == b;
R3 = FOREACH R2 GENERATE a,c;
R2 = FILTER R1 BY a == b;
R3 = FOREACH R2 GENERATE a,c;
