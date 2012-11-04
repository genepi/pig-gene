/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to test my selfwritten
 * Loader Function. 
 * 
 * call this script like this:
 * pig -param input=GeneSamples/input/readInput.txt readInput.pig
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;
data = LOAD '$input' USING pigGene.PigGeneStorage();
DUMP data;

/*
 * Wieso kommt dieser Fehler??
 * ERROR 1069: Problem resolving class version numbers for class pigGene.PigGeneStorage
 */