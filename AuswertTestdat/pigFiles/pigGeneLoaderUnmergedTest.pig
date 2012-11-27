/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to test my own
 * Storage implementation of 
 * the Unmerged Files.
 * 
 * call this script like this:
 * pig -param input=GeneSamples/input/indelTest.txt pigGeneLoaderUnmergedTest.pig
 * 
 * @author: Clemens Banas
 */


REGISTER pigGene.jar;

in = LOAD '$input' USING pigGene.PigGeneStorageUnmerged();
DUMP in;