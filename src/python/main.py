# -*- coding: utf-8 -*-


if __name__ == "__main__":
	print "Geonames"
	with open("../../data/cities15000.txt") as geonamesFile:
		lineNumber = 0
		for line in geonamesFile:
			lineNumber +=1

		print "%d lines read" % lineNumber
