/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script that uses a custom loader to retrieve
 * the inputdata in a typed format and with named columns.
 * 
 * call this script like this:
 * pig -param input=GeneSamples/input/typedInformation.txt loadTypedInformation.pig
 * 
 * @author: Clemens Banas
 */

REGISTER pigGene.jar;
in = LOAD '$input' USING pigGene.PigGeneTestStorage();
DUMP in;