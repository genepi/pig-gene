/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script imports multiple samplefiles,
 * filters chromosomes and sorts the output
 * relation by CHROM POS REF ALT SAMPLE. 
 * 
 * call this script like this:
 * pig -param input=GeneSamples/in -param outOrder=GeneSamples/outOrder -param output=GeneSamples/out multipleSamples.pig
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;
samples = LOAD '$input' USING PigStorage('\t','-tagsource')
			AS (filename:chararray, chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, 
									format:chararray, exome:chararray);

/* filter header and chromosome & project unused columns */
samp = FILTER samples BY pigGene.IgnoreHeader(chrom);
sampf = FILTER samp BY pigGene.FilterChromosome(chrom);
sfu = FOREACH sampf GENERATE chrom, pos, ref, alt, filename;

/* ordering */
ordered = ORDER sfu BY chrom, pos, ref, alt, filename ASC;
STORE ordered INTO '$outOrder';

/* remove duplicates */
duplicates = FOREACH ordered GENERATE chrom, pos, ref, alt;
noDup = DISTINCT duplicates;
STORE noDup INTO '$output';