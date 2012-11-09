/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to gain the rsNumber from the reference file and
 * write it into the corresponding  id-column of the sample file
 * if a match was possible. Keep all columns from the sample file
 * and add on additional column with the parsed genotype information. * 
 * 
 * call this script like this:
 * pig -param sample=GeneSamples/input/2exomes.vcf -param ref=GeneRefFile/00-All.vcf retrieveGenotype.pig
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;

sampleFile = LOAD '$sample' USING pigGene.PigGeneStorage();
refFile = LOAD '$ref' USING pigGene.PigGeneStorage();
refFiltered = FOREACH refFile GENERATE TRIM($0), ($1 + 0), TRIM($2);

/*
 * TODO: zuerst die Datentypen und referenznamen im Loader spezifizieren...
 */


joined = JOIN sampleFile BY ($0,$1), refFiltered BY ($0,$1);

/*
 * reordered = FOREACH joined GENERATE sampleFile::$0,sampleFile::$1,refFiltered::$2,sampleFile::$9;
 */

DUMP joined;