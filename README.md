# darkness
Darkness is a utility program used for making the LED sign for the culture festival UKA in Trondheim, Norway. 
It was developed by Lysreklamen, UKA-15 which was named Lurifax. The project was closed source until the revealing of UKA-15's name, Lurifax, on October the 1.st 2015.

Darkness is written in Java and was written as a replacement for an older simulator named Luminance.


## Quickstart

This is an example on how to get started running the simulator on Ubuntu 18.04 LTS.

### Installing the dependencies

```bash
# apt-get update -y 
# apt-get install -y git openjdk-11-jdk gradle
```
On alternative distros like arch there have been compile errors with jdk-11 and -12 due to library dependency problems, so jdk-8 has been used as seems to work fine.
### Fetching the code

```bash
# git clone git@github.com:aasmundeldhuset/darkness-19.git
# cd darknes-19
```

Optionally change to the branch `combined-svg`
```bash
# git checkout combined-svg
```

### Running the simulator
The below command should compile and then run the simulator with default parameters.
```bash
# ./gradlew :simulator:run
```

The first run will likely take some time as it will download all the dependencies and compile the whole project. 
From here on out it should onlu do incremental compiles, which should be much faster.

This uses a gradle wrapper, which means it will automatically download a compatible version of gradle.
The equivalent script is `gradlew.bat` if running on windows.

#### Changing the script thats run by default

To change the command line arguments that is sent to the simulator you can edit file `simulator/build.gradle`
and edit the section:
```
run {
    args = ["--pattern", "./patterns/UKA-19/uka19.txt", "--playlist", "../pgmplayer/playlist.txt"]
}
```

to ie: (will automatically generate a sequence from the given script.kt file)
```
run {
    args = ["--pattern", "./patterns/UKA-19/uka19.txt", "--script", "SphericalRainbow"]
}
```

or if you want to play a specific pgm file
```
run {
    args = ["--pattern", "./patterns/UKA-19/uka19.txt", "--sequence", "/path/to/SphericalRainbow.pgm"]
}
```
