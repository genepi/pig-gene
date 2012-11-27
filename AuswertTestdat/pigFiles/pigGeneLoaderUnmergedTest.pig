/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to test my own
 * Storage implementation of 
 * the Unmerged Files.
 * 
 * call this script like this:
 * pig -param input=GeneSamples/in/sample1.vcf pigGeneLoaderUnmergedTest.pig
 * 
 * @author: Clemens Banas
 */


REGISTER pigGene.jar;

in = LOAD '$input' USING pigGene.PigGeneStorageUnmerged(); 
filt = FOREACH in GENERATE pos,ref,file;
DUMP filt;