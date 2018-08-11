#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;

int main(int argc, char **args)
{
	string defaultFilePath = "../../data/cities15000.txt";
	
	string filePath;
	if (argc == 3 && "-f" == string(args[1]))
	{
		filePath = args[2];
	}
	else
	{
		filePath = defaultFilePath;
	}
	
	cout << "Geonames" << endl;
	ifstream geonamesFile(filePath);
	if (geonamesFile)
	{
		string line;
		long lineNumber = 0;
		while (!geonamesFile.eof())
		{
			if (getline(geonamesFile, line))
			{
				lineNumber++;
				stringstream lineStream(line);
				string token;
				for (int index = 0; getline(lineStream, token, '\t'); index++)
				{
					int id __attribute__((unused));
					float lat, lon;
					switch (index)
					{
						case 0:
							id = stoi(token);
							break;
						case 4:
							lat = stof(token);
							if (lat < -90 || 90 < lat)
							{
								cerr << "Feature " << id << " has an invalid latitude!" << endl;
							}
							break;
						case 5:
							lon = stof(token);
							if (lon < -180 || 180 < lon)
							{
								cerr << "Feature " << id << " has an invalid longitude!" << endl;
							}
							break;
					}
				}
			}
		}
		
		cout << lineNumber << " lines read" << endl;
	}
}
