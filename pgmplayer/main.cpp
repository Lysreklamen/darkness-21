#include "pgmlib.h"
#include <cstdlib>
#include <map>
#include <vector>
#include <string>
#include <fstream>
#include <algorithm>
#include <ola/DmxBuffer.h>
#include <ola/Logging.h>
#include <ola/StreamingClient.h>
#include <sys/time.h>
#include <ctime>

#include <unistd.h>
#include <string.h>
#include <iostream>

using namespace std;

static const int FRAME_SIZE = 512;
static const int MAX_PIXEL_VALUE = 255;

static unsigned int universe = 0;
static ola::StreamingClient ola_client;
static ola::DmxBuffer buffer;
static map<int, vector<int> > channelMapping;

int initializeOla();
string getNextFrameFilenameFromPlaylist(string playlistFilename, string currentFrameFilename);
int loadAndPlayPgm(string filename, int startAtFrame);
bool parseParameters(int argc, char * argv[], string & filename, bool & usePlaylist, int & startAtFrame);

int main(int argc, char * argv[]) {
	string filename;
	bool usePlaylist;
	int startAtFrame;
	if (!parseParameters(argc, argv, filename, usePlaylist, startAtFrame))
		return 1;
	
	loadChannelMapping("realChannelMapping.txt", channelMapping);
	
	initializeOla();
	
	if (usePlaylist) {
		string currentFrameFilename;
		while (true) {
			currentFrameFilename = getNextFrameFilenameFromPlaylist(filename, currentFrameFilename);
			if (currentFrameFilename == "")
				break;
			loadAndPlayPgm(currentFrameFilename, startAtFrame);
		}
	}
	else {
		loadAndPlayPgm(filename, startAtFrame);
	}
	
	ola_client.Stop();
	return 0;
}

bool parseParameters(int argc, char * argv[], string & filename, bool & usePlaylist, int & startAtFrame) {
	string helpText = "Usage:\n\t<program> <PGM file containing the frames> [--start <frameNumber]\n\t<program> --playlist <playlist file containing a PGM file on each line>";
	filename = "";
	usePlaylist = false;
	startAtFrame = 0;
	for (int i = 1; i < argc; ++i) {
		if (!strcmp(argv[i], "--playlist"))
			usePlaylist = true;
		else if (!strcmp(argv[i], "--start")) {
			if (i + 1 >= argc) {
				cout << helpText << endl;
				return false;
			}
			startAtFrame = atoi(argv[i + 1]);
			++i;
		}
		else {
			if (filename != "") {
				cout << helpText << endl;
				return false;
			}
			filename = argv[i];
		}
	}
	if (filename == "") {
		cout << helpText << endl;
		return false;
	}
	return true;
}

static unsigned long long getUnixTimestamp() {
	struct timeval tv;
	struct timezone tz;
	struct tm *tm;
	gettimeofday(&tv, &tz);
	tm = localtime(&tv.tv_sec);

	return mktime(tm) * 1000000 + tv.tv_usec;
}

int initializeOla() {
	cout << "Initializing OLA..." << endl;
	ola::InitLogging(ola::OLA_LOG_WARN, ola::OLA_LOG_STDERR);
	buffer.Blackout();
	
	// Set up the client - this connects to the server
	if (!ola_client.Setup()) {
		cerr << "Setup failed" << endl;
		return 3;
	}
	cout << "Initialized" << endl;
	if (!ola_client.SendDmx(universe, buffer)) {
		cerr << "Send DMX failed" << endl;
		return 4;
	}
	cout << "Cleared; waiting" << endl;
	//usleep(8000000);
	cout << "Done waiting" << endl;
}

string getNextFrameFilenameFromPlaylist(string playlistFilename, string currentFrameFilename) {
	cout << "Reading playlist file " << playlistFilename;
	if (currentFrameFilename == "")
		cout << " (no current frame file)" << endl;
	else
		cout << " (current frame file is " << currentFrameFilename << ")" << endl;
	
	string line;
	vector<string> filenames;
	ifstream playlistFile(playlistFilename.c_str());
	while (getline(playlistFile, line))
		filenames.push_back(line);
	if (filenames.size() == 0) {
		cout << "Playlist file is empty or nonexistent" << endl;
		return "";
	}
	vector<string>::iterator found = find(filenames.begin(), filenames.end(), currentFrameFilename);
	if (found == filenames.end()) {
		cout << "Current frame file name not found in playlist; picking first file name from playlist: " << filenames[0] << endl;
		return filenames[0];
	}
	if (++found == filenames.end())
		found = filenames.begin();
	cout << "Picking next file name from playlist: " << *found << endl;
	return *found;
}

int loadAndPlayPgm(string filename, int startAtFrame) {
	int numFrames;
	unsigned char ** frames = loadFramesFromPgm(filename.c_str(), channelMapping, &numFrames);
	if (!frames) {
		cerr << "Failed to load frame file" << endl;
		return 2;
	}
	cout << "Loaded frame file " << filename << "; playing from frame " << startAtFrame << "..." << endl;

	unsigned long long startTimestamp = getUnixTimestamp();
	for (int f = startAtFrame; f < numFrames; ++f) {
		for (int i = 0; i < FRAME_SIZE; ++i)
			buffer.SetChannel(i, frames[f][i]);
		if (!ola_client.SendDmx(universe, buffer)) {
			cerr << "Send DMX failed" << endl;
			return 4;
		}
		if (f % 20 == 19) {
			cout << "Sent frame #" << f << endl;
			cout << "(Current frame file is " << filename << ")" << endl;
		}

		long long timeToWait = 50000 * (f - startAtFrame) + startTimestamp - getUnixTimestamp();

		if (timeToWait > 0) { usleep(timeToWait);}
	}
	
	for (int f = 0; f < numFrames; ++f)
		delete[] frames[f];
	delete[] frames;
	return 0;
}

