#!/bin/bash


if [ "$#" -lt 1 ];
then
	echo "Usage: $0 <moduleOfferingId> <studentId>..."
else
	MODULE_OFFERING_ID=$1

	echo "Module Offering Id: $1"
	for stu in "$@" 
	do
		if [ "$stu" != "$1" ];
		then
			echo "Adding student: $stu to the model"
		fi
	done
	mkdir classes/$1
	java -jar Model-Trainer.jar "$@"

fi
