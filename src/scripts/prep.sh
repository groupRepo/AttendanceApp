#!/bin/bash
if [ "$#" -ne "2" ];
then
	echo "Usage ./prep.sh <zipfile> <studentid>"
else
	#path to the zip file
	ZIP_FILE=$1
	#path to temp directory
	TEMP_DIR=ext_pic
	#student id
	STUDENT_ID=$2
	#student dir
	STUDENT_DIR=students/$STUDENT_ID

	#unzip files to  TEMP_DIR
	echo "Unziping uploaded zip file: $ZIP_FILE"
	unzip $ZIP_FILE -d $TEMP_DIR

	#create the dir for the student pics
	echo "Creating student directory: $STUDENT_DIR"
	mkdir $STUDENT_DIR

	#process images and put them into the student folder
	echo "Processing images"
	java -jar Image-Processor.jar $TEMP_DIR $STUDENT_DIR

	#clean up
	echo "Removing temp files"
	rm -r $TEMP_DIR/*
	rm $ZIP_FILE
fi



