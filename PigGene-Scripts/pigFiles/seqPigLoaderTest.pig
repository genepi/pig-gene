/** usage:
  * pig -param fastq=/home/clemens/hadoop/input/Exome1_1.fastq seqPigLoaderTest.pig
  */

REGISTER SeqPig.jar;
REGISTER hadoop-bam-6.0.jar;
REGISTER sam-1.93.jar;
REGISTER picard-1.93.jar;
REGISTER variant-1.93.jar;
REGISTER tribble-1.93.jar;
REGISTER commons-jexl-2.1.1.jar;

R1 = LOAD '$fastq' USING fi.aalto.seqpig.io.FastqLoader();

FastQout = LIMIT R1 3;

DUMP FastQout;
DUMP BAMout;