REGISTER pigGene.jar;
REGISTER hadoop-bam-5.1.jar;
REGISTER sam-1.76.jar;
REGISTER picard-1.76.jar;
REGISTER SeqPig.jar;
R1 = LOAD '$input1' USING fi.aalto.seqpig.BamUDFLoader('yes');
STORE R1 INTO '$output1';
