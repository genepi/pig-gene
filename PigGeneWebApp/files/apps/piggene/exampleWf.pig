REGISTER pigGene.jar;
REGISTER SeqPig.jar;
REGISTER hadoop-bam-6.0.jar;
REGISTER sam-1.93.jar;
REGISTER picard-1.93.jar;
REGISTER variant-1.93.jar;
REGISTER tribble-1.93.jar;
REGISTER commons-jexl-2.1.1.jar;

--exampleWf
--description of the example workflow

--
in = LOAD '$sample' USING pigGene.storage.merged.PigGeneStorage();
--
in2 = LOAD '$sample2' USING pigGene.storage.merged.PigGeneStorage();
--
filtered = FILTER in BY chrom == 20;
--
out = JOIN filtered BY (chrom, pos), in2 BY (chrom, pos);
--
STORE out INTO '$output';