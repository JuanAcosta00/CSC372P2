# Project 2 for CSC 372
This repository is for a custom language that has similar structure to python. This repository comes with a transpiler that I made that will transform the code into Java code, which can then be combiled and ran normally.

To run transpiler as a parser, first run javac *.java in the project directory, then run java Translator with no arguments

To run transpiler on a file, first run ==javac *.java== if you haven't already, then run ==java Translator fileName.txt== with just the file name as an argument, this will make a new file with the same name in java. You can then run ==javac *.java== to compile the newly created file, and run the new file using ==Java filename==.

## [Link to grammar for language](https://github.com/JuanAcosta00/CSC372P2/blob/master/grammar.md)