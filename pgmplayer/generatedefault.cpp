#include "pgmlib.h"
#include <iostream>
#include <vector>
#include <map>

using namespace std;

int main() {
	map<int, vector<int> > channelMapping;
	loadChannelMapping("realChannelMapping.txt", channelMapping);
	int numFrames;
	unsigned char ** frames = loadFramesFromPgm("../simulator/sequences/uka17/Default.pgm", channelMapping, &numFrames);
	// 14 boxes, 18 channels per box, first channel is 2 (which is at index 1 in the frame array)
	for (int b = 0; b < 14; b++) {
		for (int i = 0; i < 18; i++) {
			cout << (int) frames[0][b * 18 + i + 1];
			if (i != 17) {
				cout << ",";
			}
		}
		cout << endl;
	}
	return 0;
}
