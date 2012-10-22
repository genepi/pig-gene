/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script imports multiple samplefiles,
 * filters chromosomes and sorts the output
 * relation by CHROM POS REF ALT SAMPLE. 
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;
sample1 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
s1 = FILTER sample1 BY pigGene.IgnoreHeader(chrom);

sample2 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
s2 = FILTER sample2 BY pigGene.IgnoreHeader(chrom);

sample3 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
s3 = FILTER sample3 BY pigGene.IgnoreHeader(chrom);

sample4 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
s4 = FILTER sample4 BY pigGene.IgnoreHeader(chrom);

/* filter chromosome */
s1f = FILTER s1 BY pigGene.FilterChromosome(chrom);
s2f = FILTER s2 BY pigGene.FilterChromosome(chrom);
s3f = FILTER s3 BY pigGene.FilterChromosome(chrom);
s4f = FILTER s4 BY pigGene.FilterChromosome(chrom);

/* add sample identifier */
s1fu = FOREACH s1f GENERATE chrom, pos, ref, alt, 1;
s2fu = FOREACH s2f GENERATE chrom, pos, ref, alt, 2;
s3fu = FOREACH s3f GENERATE chrom, pos, ref, alt, 3;
s4fu = FOREACH s4f GENERATE chrom, pos, ref, alt, 4;

/* union of the four relations */
megaUnion = UNION s1fu, s2fu, s3fu, s4fu;
STORE megaUnion INTO 'GeneSamples/outUnion';

/* ordering */
ordered = ORDER megaUnion BY chrom, pos, ref, alt, $4 ASC;
STORE ordered INTO 'GeneSamples/outOrdered';

/* remove duplicates */
duplicates = FOREACH ordered GENERATE chrom, pos, ref, alt;
noDup = DISTINCT duplicates;
STORE noDup INTO 'GeneSamples/out';