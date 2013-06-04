REGISTER pigGene.jar;
REGISTER hadoop-bam-5.1.jar;
REGISTER sam-1.76.jar;
REGISTER picard-1.76.jar;
FF = LOAD '$input1' USING fi.aalto.seqpig.FastqUDFLoader();
top = FILTER FF BY tile >= 1000 AND tile < 2000;
sp = FOREACH top GENERATE UnalignedReadSplit(sequence, quality);
bases = FOREACH sp GENERATE FLATTEN($0);
first_10 = FILTER bases BY pos <= 10;
grpd_by_pos = GROUP first_10 BY pos;
result = FOREACH grpd_by_pos GENERATE group AS cycle, AVG($1.basequal);
sorted_cycle_avg = ORDER result BY cycle;
DUMP sorted_cycle_avg;
