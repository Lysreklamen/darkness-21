#include "pgmlib.h"
#include <iostream>
#include <fstream>

using namespace std;

static const int FRAME_SIZE = 512;
static const int MAX_PIXEL_VALUE = 255;

void storeFramesToPgm(unsigned char ** frames, int numFrames, const char * filename) {
	ofstream file(filename);
	file << "P2\n" << FRAME_SIZE << " " << numFrames << "\n" << MAX_PIXEL_VALUE << endl;
	for (int f = 0; f < numFrames; ++f) {
		for (int i = 1; i < FRAME_SIZE + 1; ++i) // The frames start at index 1
			file << (int)frames[f][i] << ' ';
		file << endl;
	}
}

unsigned char ** loadFramesFromPgm(const char * filename, map<int, vector<int> > channelMapping, int * numFrames) {
	ifstream file(filename);
	string magicWord;
	int width, height, maxPixelValue;
	file >> magicWord;
	if (magicWord != "P2") {
		cerr << "Not a PGM file; magic word was '" << magicWord << "' (expected 'P2')" << endl;
		return NULL;
	}
	file >> width >> height >> maxPixelValue;
	if (width != FRAME_SIZE) {
		cerr << "Picture width was " << width << " (expected " << FRAME_SIZE << ")" << endl;
		return NULL;
	}
	if (maxPixelValue != MAX_PIXEL_VALUE) {
		cerr << "Max pixel value was " << maxPixelValue << " (expected " << MAX_PIXEL_VALUE << ")" << endl;
		return NULL;
	}
	
	*numFrames = height;
	unsigned char ** frames = new unsigned char * [*numFrames];
	unsigned char * readBuffer = new unsigned char[FRAME_SIZE];
	for (int f = 0; f < *numFrames; ++f) {
		frames[f] = new unsigned char[FRAME_SIZE];
		// We copy the values in two passes - first, channel i from the source is copied to channel i in the target, then, all mapped channels are copied.
		// This is a simple way to ensure that a channel that is the target of a mapping will not get overwritten by the value from the source channel at the same index.
		for (int i = 0; i < FRAME_SIZE; ++i) {
			unsigned int pixelValue;
			file >> pixelValue;
			frames[f][i] = readBuffer[i] = (unsigned char)pixelValue;
		}
		for (map<int, vector<int> >::iterator it = channelMapping.begin(); it != channelMapping.end(); ++it)
			for (unsigned int i = 0; i < it->second.size(); ++i)
				frames[f][it->second[i]] = readBuffer[it->first];
	}
	delete[] readBuffer;
	return frames;
}

void loadChannelMapping(const char * filename, map<int, vector<int> > & channelMapping) {
	ifstream file(filename);
	int from, to;
	int count = 0;
	const vector<int> emptyVector;
	while (true) {
		if (!(file >> from)) {
			cout << "Loaded " << count << " channel mappings from " << filename << endl;
			return;
		}
		if (!(file >> to)) {
			cerr << "Error: File contained an odd number of integers; the last integer is skipped" << endl;
			cout << "Loaded " << count << " channel mappings from " << filename << endl;
			return;
		}
		--from;
		--to;
		if (channelMapping.find(from) == channelMapping.end())
			channelMapping[from] = emptyVector;
		channelMapping[from].push_back(to);
		++count;
	}
}

