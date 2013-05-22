--Counts the number of samples for chrom 20 and pos 138004 that don't match the reference (0/0).
REGISTER pigGene.jar;

--Loads the input file.
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();

--Filters all lines that match chromosome '20'.
R2 = FILTER R1 BY chrom == '20';

--Filters all lines that match position '138004'.
R3 = FILTER R2 BY pos == 138004;

--Filters all lines that doesn't contain genotype information which matches the reference (0/0).
R4 = FILTER R3 BY SUBSTRING(genotype,0,3) != '0/0';

--Groups all lines by using the chromosome and position column.
R5 = GROUP R4 BY (chrom,pos);

--Selects the needed columns for the output. The grouped columns (chrom,pos) get flattened to separate them into two distinct columns. The COUNT operation counts the number of grouped elements for each group consisting of (chrom,pos).
R6 = FOREACH R5 GENERATE FLATTEN(group), COUNT(R4) as count;

--Stores the output.
STORE R6 INTO '$output1';
