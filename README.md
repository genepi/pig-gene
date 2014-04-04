Due to the vast progress in sequencing, data volumes in bioinformatics are increasing dramatically. Therefore, the aim of this work is the development of a graphical generation platform for Apache Pig scripts called PigGene to analyze data in a massively parallel way. Users are guided through the building process of scripts by an intuitive user interface and are able to generate workflow definitions for complex data queries on the fly. They can also re-use existing workflows within a new workflow definition. Generated scripts are ready to use and can be further executed directly on Cloudgene, a graphical MapReduce execution platform. PigGene supports especially inexperienced users in the area of bioinformatics.

My project is divided into two parts: 
 1. A collection of different pig scripts and modified storages located in the PigGene-Scripts folder
 2. A webapp that makes it possible to specify and combine different tasks, like filtering or joining. 
    These tasks are sent to a server which creates an Apache Pig script for the user.
