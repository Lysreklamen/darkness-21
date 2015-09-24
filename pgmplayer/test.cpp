#include "../pgmlib.h"
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

#include <iostream>

using namespace std;

static const int FRAME_SIZE = 512;
static const int MAX_PIXEL_VALUE = 255;

static unsigned int universe = 0;
static ola::StreamingClient ola_client;
static ola::DmxBuffer buffer;
static map<int, vector<int> > channelMapping;

int initializeOla();
int loadAndPlayPgm(string filename, int startAtFrame);

int main(int argc, char * argv[]) {
	initializeOla();
	for (int i = 0; i < FRAME_SIZE; ++i) {
		buffer.SetChannel(i, (unsigned char) 255);
	}
	if (!ola_client.SendDmx(universe, buffer)) {
		cerr << "Send DMX failed (2)" << endl;
	}
	ola_client.Stop();
	return 0;
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
	usleep(8000000);
	cout << "Done waiting" << endl;
}

