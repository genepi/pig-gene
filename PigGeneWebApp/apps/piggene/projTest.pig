REGISTER pigGene.jar;
R1 = LOAD '$input2' USING PigStorage('\t');
R2 = FOREACH R1 GENERATE $0,$1;
R3 = FILTER R2 BY $0 == '12';
STORE R3 INTO '$output1';
