/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to extract the geneinfo 
 * from the info column.
 * 
 * call this script like this:
 * pig -param input=GeneRefFile/00-All.vcf -param output=GeneSamples/output extractGeneInfoReference.pig
 * 
 * @author: Clemens Banas
 */
 
 REGISTER pigGene.jar;
 
 in = LOAD '$input' USING PigStorage('\t') AS (REFchrom:chararray, REFpos:long, REFid:chararray, REFr:chararray, REFalt:chararray, REFqual:double, REFfilt:chararray, REFinfo:chararray);
 out = FOREACH in GENERATE pigGene.ExtractGeneInfo(REFinfo);
 
 STORE out INTO '$output';