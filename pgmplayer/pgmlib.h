#ifndef PGMLIB_H
#define PGMLIB_H

#include <map>
#include <vector>

void storeFramesToPgm(unsigned char ** frames, int numFrames, const char * filename);
unsigned char ** loadFramesFromPgm(const char * filename, std::map<int, std::vector<int> > channelMapping, int * numFrames);
void loadChannelMapping(const char * filename, std::map<int, std::vector<int> > & channelMapping);

#endif
