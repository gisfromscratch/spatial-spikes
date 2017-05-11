# -*- coding: utf-8 -*-


if __name__ == "__main__":
	print "Geonames"
	with open("../data/cities15000.txt") as geonamesFile:
		for line in geonamesFile:
			print line
