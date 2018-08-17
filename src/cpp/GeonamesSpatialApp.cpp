#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

#include <stdlib.h>

using namespace std;

int bufferedRead(const char *filePath)
{
	static const size_t bufferSize = 16*1024;

	FILE *file = fopen(filePath, "r");
	if (NULL == file)
	{
		return 1;
	}
	if (ferror(file))
	{
		return 1;
	}

	long lineNumber = 0;
	char buffer[bufferSize];
	char *line;
	while (!feof(file))
	{
		fgets(buffer, bufferSize, file);
		line = strchr(buffer, '\n');
		if (line)
		{
			*line = '\0';
		}
		else
		{
			line = buffer;
		}

		lineNumber++;

		char *token  = strtok(line, "\t");
		for (int index = 0; token; index++, token = strtok(NULL, "\t"))
		{
			int id;
			float lat, lon;
			switch (index)
			{
				case 0:
					id = atoi(token);
					break;
				case 4:
					lat = atof(token);
					if (lat < -90 || 90 < lat)
					{
						cerr << "Feature " << id << " has an invalid latitude!" << endl;
					}
					break;
				case 5:
					lon = atof(token);
					if (lon < -180 || 180 < lon)
					{
						cerr << "Feature " << id << " has an invalid longitude!" << endl;
					}
					break;
			}
		}
	}

	fclose(file);

	cout << lineNumber << " lines read" << endl;
	return 0;
}

int lineRead(const char *filePath)
{
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
					#ifdef linux
					int id __attribute__((unused));
					#else
					int id;
					#endif
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
	return 0;
}

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
	return bufferedRead(filePath.c_str());
}
