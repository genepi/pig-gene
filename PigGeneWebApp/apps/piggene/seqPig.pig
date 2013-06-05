REGISTER pigGene.jar;
REGISTER SeqPig.jar;
REGISTER hadoop-bam-5.1.jar;
REGISTER sam-1.76.jar;
REGISTER picard-1.76.jar;
reads = LOAD '$input1' USING fi.aalto.seqpig.io.FastqLoader();
REGISTER piggybank.jar;
REGISTER datafu-0.0.9.jar;
-- parameters:
-- ?* inputpath
-- ?* outputpath
-- ?* parallelism (optional)




-- Needs datafu for the variance function: https://github.com/linkedin/datafu
-- Needs piggybank for the string LENGTH




%default parallelism 1
set default_parallel $parallelism;




-- speculative execution isn't helping us much, so turn it off
set mapred.map.tasks.speculative.execution false;
set mapred.reduce.tasks.speculative.execution false;




-- Use in-memory aggregation, as possible
set pig.exec.mapPartAgg true;
set pig.exec.mapPartAgg.minReduction 5;




-- new experimental Pig feature -- generates specialized typed classes for the tuples
set pig.schematuple on;




define VAR datafu.pig.stats.VAR();
-- Calculate min, the 25th, 50th, 75th percentiles, and the max
define Quantile datafu.pig.stats.Quantile('5');
-- define StreamingQuantile datafu.pig.stats.StreamingQuantile('5');
define STRLEN org.apache.pig.piggybank.evaluation.string.LENGTH();




--
-- ? start of script
--




-- ? import reads
-- reads = load '$inputpath' using fi.aalto.seqpig.io.FastqLoader();
reads_by_bases = FOREACH reads GENERATE fi.aalto.seqpig.UnalignedReadSplit(sequence, quality);




------- read stats




-- read length
read_len = FOREACH reads GENERATE STRLEN(sequence);
read_len_counts = FOREACH (GROUP read_len BY $0) GENERATE group AS len, COUNT_STAR($1) as count;




-- per sequence avg base quality
-- read_q = FOREACH reads_by_bases GENERATE ROUND(AVG($0.basequal)) as read_qual;
-- read_q_counts = FOREACH (GROUP read_q BY read_qual) GENERATE group as avg_read_qual, COUNT_STAR($1) as count;




read_seq_qual = FOREACH reads GENERATE quality;
avgbase_qual_counts = FOREACH (GROUP read_seq_qual ALL) GENERATE fi.aalto.seqpig.stats.AvgBaseQualCounts($1.$0);
formatted_avgbase_qual_counts = FOREACH avgbase_qual_counts GENERATE fi.aalto.seqpig.stats.FormatAvgBaseQualCounts($0);








------- generate output




-- STORE read_len_counts INTO '$outputpath/read_len_counts';
-- STORE read_q_counts INTO '$outputpath/read_q_counts';
-- STORE formatted_avgbase_qual_counts INTO '$outputpath/read_q_counts';
STORE read_len_counts INTO '$output1';
STORE formatted_avgbase_qual_counts INTO '$output2';
