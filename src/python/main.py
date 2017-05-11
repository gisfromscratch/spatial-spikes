# -*- coding: utf-8 -*-


if __name__ == "__main__":
	print "Geonames"
	with open("../../data/cities15000.txt") as geonamesFile:
		lineNumber = 0
		for line in geonamesFile:
			lineNumber +=1
			tokens = line.split("\t")
			if (6 < len(tokens)):
				id = int(tokens[0])
				lat = float(tokens[4])
				lon = float(tokens[5])
				if (lat < -90 or 90 < lat):
					print "Feature %d has an invalid latitude!"
				if (lon < -180 or 180 < lon):
					print "Feature %d has an invalid longitude!"

		print "%d lines read" % lineNumber
