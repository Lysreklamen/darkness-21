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
#include <iomanip>

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
bool parseParameters(int argc, char * argv[], string & filename, bool & usePlaylist, int & startAtFrame, int & countdownSeconds);
int countdown(int seconds);

int main(int argc, char * argv[]) {
	string filename;
	bool usePlaylist;
	int startAtFrame;
	int countdownSeconds;
	if (!parseParameters(argc, argv, filename, usePlaylist, startAtFrame, countdownSeconds))
		return 1;
	
	loadChannelMapping("realChannelMapping.txt", channelMapping);
	
	initializeOla();

	if (countdownSeconds > 0) {
		countdown(countdownSeconds);
	}
	
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

bool parseParameters(int argc, char * argv[], string & filename, bool & usePlaylist, int & startAtFrame, int & countdownSeconds) {
	string helpText = "Usage:\n\t<program> <PGM file containing the frames> [--start <frameNumber>] [--countdown <seconds>]\n\t<program> --playlist <playlist file containing a PGM file on each line> [--countdown <seconds>]";
	filename = "";
	usePlaylist = false;
	startAtFrame = 0;
	countdownSeconds = 0;

	for (int i = 1; i < argc; ++i) {
		if (!strcmp(argv[i], "--playlist")) {
			usePlaylist = true;
		}
		else if (!strcmp(argv[i], "--start") || !strcmp(argv[i], "--countdown")) {
			if (i + 1 >= argc) {
				cout << helpText << endl;
				return false;
			}
			int value = atoi(argv[i + 1]); //TODO: Validate
			if (value < 0) {
				cout << "--start and --countdown must be nonnegative.\n" << helpText << endl;
				return false;
			}
			if (!strcmp(argv[i], "--start")) {
				startAtFrame = value;
			}
			else {
				if (value >= 360000) {
					cout << "--countdown must be less than 360000.\n" << helpText << endl;
					return false;
				}
				countdownSeconds = value;
			}
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
	while (getline(playlistFile, line)) {
		if (line.find_first_not_of(" \t") != string::npos) {
			filenames.push_back(line);
		}
	}
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

int countdown(int seconds) {
	const int NUM_DIGITS = 2;
	const int NUM_SEGMENTS = 7;
	const int UPPER_LEFT = 0;
	const int LOWER_LEFT = 1;
	const int BOTTOM = 2;
	const int LOWER_RIGHT = 3;
	const int UPPER_RIGHT = 4;
	const int TOP = 5;
	const int MIDDLE = 6;
	const int segmentChannels[NUM_DIGITS][NUM_SEGMENTS] = {
		{492, 491, 490, 489, 488, 487, 493}, //TODO
		{501, 500, 499, 498, 497, 496, 502} //TODO
	};
	vector<vector<int> > segments(10);
	segments[0].push_back(UPPER_LEFT);
	segments[0].push_back(LOWER_LEFT);
	segments[0].push_back(BOTTOM);
	segments[0].push_back(LOWER_RIGHT);
	segments[0].push_back(UPPER_RIGHT);
	segments[0].push_back(TOP);
	segments[1].push_back(LOWER_RIGHT);
	segments[1].push_back(UPPER_RIGHT);
	segments[2].push_back(TOP);
	segments[2].push_back(UPPER_RIGHT);
	segments[2].push_back(MIDDLE);
	segments[2].push_back(LOWER_LEFT);
	segments[2].push_back(BOTTOM);
	segments[3].push_back(TOP);
	segments[3].push_back(UPPER_RIGHT);
	segments[3].push_back(MIDDLE);
	segments[3].push_back(LOWER_RIGHT);
	segments[3].push_back(BOTTOM);
	segments[4].push_back(UPPER_LEFT);
	segments[4].push_back(MIDDLE);
	segments[4].push_back(UPPER_RIGHT);
	segments[4].push_back(LOWER_RIGHT);
	segments[5].push_back(TOP);
	segments[5].push_back(UPPER_LEFT);
	segments[5].push_back(MIDDLE);
	segments[5].push_back(LOWER_RIGHT);
	segments[5].push_back(BOTTOM);
	segments[6].push_back(TOP);
	segments[6].push_back(UPPER_LEFT);
	segments[6].push_back(LOWER_LEFT);
	segments[6].push_back(BOTTOM);
	segments[6].push_back(LOWER_RIGHT);
	segments[6].push_back(MIDDLE);
	segments[7].push_back(TOP);
	segments[7].push_back(UPPER_RIGHT);
	segments[7].push_back(LOWER_RIGHT);
	segments[8].push_back(TOP);
	segments[8].push_back(UPPER_LEFT);
	segments[8].push_back(LOWER_LEFT);
	segments[8].push_back(BOTTOM);
	segments[8].push_back(LOWER_RIGHT);
	segments[8].push_back(UPPER_RIGHT);
	segments[8].push_back(MIDDLE);
	segments[9].push_back(BOTTOM);
	segments[9].push_back(LOWER_RIGHT);
	segments[9].push_back(UPPER_RIGHT);
	segments[9].push_back(TOP);
	segments[9].push_back(UPPER_LEFT);
	segments[9].push_back(MIDDLE);

	unsigned long long startTimestamp = getUnixTimestamp();
	for (int t = seconds; t >= 0; t--) {
		int s = t % 60;
		int m = (t / 60) % 60;
		int h = t / 3600;
		int number = h > 0 ? h : (m > 0 ? m : s);
		int digits[] = {number / 10, number % 10};
		for (int d = 0; d < NUM_DIGITS; d++) {
			for (int i = 0; i < NUM_SEGMENTS; i++) {
				buffer.SetChannel(segmentChannels[d][i], 0);
			}
			vector<int> segmentsForDigit = segments[digits[d]];
			for (int i = 0; i < segmentsForDigit.size(); i++) {
				buffer.SetChannel(segmentChannels[d][segmentsForDigit[i]], 255);
			}
		}
		if (!ola_client.SendDmx(universe, buffer)) {
			cerr << "Send DMX failed" << endl;
			return 4;
		}
		cout << std::setfill('0');
		cout << "Countdown: " << number << " (" << std::setw(2) << h << ":" << std::setw(2) << m << ":" << std::setw(2) << s << ")" << endl;
		long long timeToWait = startTimestamp + 1000000 * (seconds + 1 - t) - getUnixTimestamp();
		if (timeToWait > 0) {
			usleep(timeToWait);
		}
	}
	return 0;
}
