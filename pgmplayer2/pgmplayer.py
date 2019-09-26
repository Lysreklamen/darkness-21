#!/usr/bin/env python3

##############################################################################
# This script requires python 3.5 or above
#
# Example usage:
# python3 pgmplayer.py \
#  --channel-mapping ../simulator/patterns/UKA-19/logical_to_physical.txt \
#  --playlist ../simulator/sequences/uka19/testlist.txt \
#  --device  /dev/ttyUSB0
#
# usage: pgmplayer.py [-h] [--channel-mapping CHANNEL_MAPPING]
#                     [--playlist PLAYLIST] [--single-cycle] [--pgm PGM]
#                     [--device DEVICE] [--fps FPS] [--verify]
#
# pgmplayer version 2
#
# optional arguments:
#   -h, --help            show this help message and exit
#   --channel-mapping CHANNEL_MAPPING
#                         A channel mapping file
#   --playlist PLAYLIST   A list of pgm files to play
#   --single-cycle        Only play the playlist once and exit
#   --pgm PGM             The PGM file to play
#   --device DEVICE       The OVDMX device to play to. I.e. /dev/ttyAMA0
#   --fps FPS             Override the default frame rate of 20fps
#   --verify              Scan the playlist/pgm file and check for errors
##############################################################################
import struct
import sys
import termios
import time
import typing
from pathlib import Path
from io import StringIO
import argparse


class ChannelMap:
    """ A class that loads and handles channel mapping """

    def __init__(self, path_or_file: typing.Union[typing.TextIO, str, Path]):
        if isinstance(path_or_file, (str, Path)):
            file = open(path_or_file, 'r')
        else:
            file = path_or_file

        self._map = {}  # type: dict[int, int]
        for line_number, line in enumerate(file.readlines()):
            line = line.strip()
            parts = line.split(' ')
            if len(parts) == 1:
                parts = line.split('\t')

            if len(parts) != 2 or any(not x.isnumeric() for x in parts):
                raise IOError('The channel mapping file {} is malformed on line {}'.format(file, line_number))

            map_from = int(parts[0]) - 1  # Convert from 1-indexing to 0-indexing
            map_to = int(parts[1]) - 1  # Convert from 1-indexing to 0-indexing

            if map_from in self._map.keys():
                raise IOError('The channel mapping file {} contains duplicate mapping for the channel {}'
                              .format(file, map_from))

            self._map[map_from] = map_to

        if len(set(self._map.values())) != len(self._map.values()):
            raise IOError('The channel mapping contains duplicate mappings to the same channel')

    def __getitem__(self, item):
        if type(item) is not int:
            raise ValueError('The channel mapping must be indexed with ints')
        if item in self._map:
            return self._map[item]
        return -1  # Not found


class Playlist:
    """
    A class to read playlist files.

    A playlist file is simply a list of relative paths to the .pgm files to play.
    The default directory can be overwritten as an argument to the constructor.
    """

    def __init__(self, path_or_file: typing.Union[typing.TextIO, str, Path], *,
                 base_dir: typing.Optional[Path] = None,
                 repeating: bool = True):
        if isinstance(path_or_file, (str, Path)):
            self.file = open(path_or_file, 'r')
        else:
            self.file = path_or_file

        self.repeating = repeating
        self.directory = Path.cwd()
        if base_dir:
            self.directory = base_dir
        elif hasattr(self.file, 'name'):
            # Default to the directory of the playlist if it is a real file
            self.directory = Path(self.file.name).absolute().parent

        self.pgm_index = 0

    def read_entry(self) -> typing.Optional[Path]:
        line = self.file.readline().strip()
        if not line:
            if self.repeating:
                if self.pgm_index == 0:
                    raise IOError('The playlist file is empty')
                self.file.seek(0)
                self.pgm_index = 0
                return self.read_entry()

        path = Path(line)
        self.pgm_index += 1
        if path.is_absolute():
            return path.resolve()
        return Path(self.directory, path).resolve()

    def entry_generator(self) -> typing.Generator[typing.Tuple[int, Path], None, None]:
        while True:
            entry = self.read_entry()
            if not entry:
                return
            yield self.pgm_index, entry


class PGMReader:
    """ A PGM file format parser """

    def __init__(self, path_or_file: typing.Union[typing.TextIO, str, Path], *,
                 channel_map: typing.Optional[ChannelMap] = None):
        if isinstance(path_or_file, (str, Path)):
            self.file = open(path_or_file, 'r')
        else:
            self.file = path_or_file

        self.channel_map = channel_map

        file_type = self.file.readline().strip()
        if file_type != 'P2':
            raise IOError('The pgm file {} is not of the PGMv2 format'.format(self.file))

        dimensions = self.file.readline().strip().split(' ')
        if len(dimensions) != 2:
            raise IOError(
                'The second line in the PGM file {} should be [channel_count] [frame_count]'.format(self.file))
        if any((not x.isnumeric() for x in dimensions)):
            raise IOError(
                'The second line in the PGM file {} should be [channel_count] [frame_count] any be numeric'
                    .format(self.file))

        self.channel_count = int(dimensions[0])
        self.frame_count = int(dimensions[1])

        max_channel_value = self.file.readline().strip()
        if not max_channel_value.isnumeric():
            raise IOError('The third line in the PGM file {} should be [max_channel_value]'.format(self.file))
        if int(max_channel_value) != 255:
            raise NotImplementedError('Only a max channel value of 255 is currently supported')

        self.current_frame_index = 0

    def read_frame(self) -> bytes:
        raw_frame_data = self.file.readline()
        if not raw_frame_data:
            raise EOFError('End of PGM file {}'.format(self.file))

        self.current_frame_index += 1

        raw_frame_channels = raw_frame_data.strip().split(' ')
        frame = bytearray(self.channel_count)
        for channel_index, channel_value in enumerate(raw_frame_channels):
            if channel_index >= self.channel_count:
                raise IOError('The PGM file {} has more than {} channels in frame {}'.format(self.file,
                                                                                             self.channel_count,
                                                                                             self.current_frame_index))
            channel_value = int(channel_value)
            if self.channel_map:
                mapped_channel = self.channel_map[channel_index]
                if mapped_channel >= 0: # Only map valid channels
                    frame[mapped_channel] = channel_value
            else:
                # No channel map. Copy everything raw
                frame[channel_index] = channel_value

        return frame

    def frames(self) -> typing.Generator[typing.Tuple[int, bytes], None, None]:
        while True:
            try:
                frame = self.read_frame()
                yield self.current_frame_index, frame
            except EOFError:
                return

    def __str__(self):
        return "pgm_file: {}".format(self.file.name)


class DMXOutput:
    """ A DMX output device interface """

    def push_frame(self, frame: bytes):
        raise NotImplementedError('This is an abstract function and must be overridden')


class DummyDMXOutput(DMXOutput):
    """ A dummy DMX device which just discards all frames """

    def push_frame(self, frame: bytes):
        return


class OVDMXOutput(DMXOutput):
    """ The OVDMX device over a USB CDC serial connection

     The format of the packet is as such
     struct {
          uint8_t magic[2];
          uint8_t type;
          uint16_t data_length;
          uint8_t data[DMX_UNIVERSE_SIZE]; // 512
          uint16_t crc;
     } dmx_packet;
    """
    OFFSET_MAGIC = 0
    OFFSET_TYPE = 2
    OFFSET_DATA_LENGTH = 3
    OFFSET_DATA = 5
    DATA_LENGTH = 512
    OFFSET_CRC = OFFSET_DATA + DATA_LENGTH

    def __init__(self, device: typing.Union[str, Path]):
        self.dev = open(device, 'wb')
        if self.dev.isatty():
            # Make the terminal raw. That is disable everything related to automatic echo, \n -> \r\n
            # This does the same as the `cfmakeraw` function in glibc
            attr = termios.tcgetattr(self.dev.fileno())
            c_iflag = 0
            c_oflag = 1
            c_cflag = 2
            c_lflag = 3
            c_ispeed = 4
            c_ospeed = 5

            attr[c_iflag] = attr[c_iflag] & ~(termios.IGNBRK | termios.BRKINT | termios.PARMRK | termios.ISTRIP
                                              | termios.INLCR | termios.IGNCR | termios.ICRNL | termios.IXON)
            attr[c_oflag] = attr[c_oflag] & ~termios.OPOST
            attr[c_lflag] = attr[c_lflag] & ~(termios.ECHO | termios.ECHONL | termios.ICANON | termios.ISIG
                                              | termios.IEXTEN)
            attr[c_cflag] = attr[c_cflag] & ~(termios.CSIZE | termios.PARENB)
            attr[c_cflag] = attr[c_cflag] | termios.CS8

            # Set the speed to something high if we are testing this on some device that is not a real OVDMX device,
            # such as a USB to serial converter
            attr[c_ispeed] = termios.B1152000
            attr[c_ospeed] = termios.B1152000

            # Save the attributes
            termios.tcsetattr(self.dev.fileno(), termios.TCSANOW, attr)

        # Preallocate the packet and set static values
        self.usb_packet = bytearray(2 + 1 + 2 + 512 + 2)
        self.usb_packet[self.OFFSET_MAGIC:self.OFFSET_MAGIC + 2] = b'OV'
        self.usb_packet[self.OFFSET_TYPE] = ord('D')
        self.usb_packet[self.OFFSET_DATA_LENGTH:self.OFFSET_DATA_LENGTH + 2] = struct.pack('!H', 512)
        self.usb_packet[self.OFFSET_CRC:self.OFFSET_CRC + 2] = b'\0\0'

    def push_frame(self, frame: bytes):
        self.usb_packet[self.OFFSET_DATA:self.OFFSET_DATA + self.DATA_LENGTH] = frame[0:self.DATA_LENGTH]
        self.dev.write(self.usb_packet)
        self.dev.flush()


class FrameRateController:
    """ A helper class to keep us at a given frame rate """

    def __init__(self, frame_rate: int):
        self.frame_rate = frame_rate
        self.frame_period = 1.0 / float(frame_rate)
        self.last_frame_time = time.time()

    def reset(self):
        self.last_frame_time = time.time()

    def next_frame(self):
        next_frame_time = self.last_frame_time + self.frame_period
        time_now = time.time()
        if next_frame_time < time_now:
            print(
                "WARNING! Can not keep up frame rate! Lagging behind {:.2f} seconds".format(time_now - next_frame_time))
        else:
            time.sleep(next_frame_time - time_now)
        self.last_frame_time = time.time()


def main():
    parser = argparse.ArgumentParser(description='pgmplayer version 2')
    parser.add_argument('--channel-mapping', type=argparse.FileType(mode='r'), help='A channel mapping file')
    parser.add_argument('--playlist', type=argparse.FileType(mode='r'), help='A list of pgm files to play')
    parser.add_argument('--playlist-dir', help='Override the folder the playlist files are relative to')
    parser.add_argument('--single-cycle', action='store_true', help='Only play the playlist once and exit')
    parser.add_argument('--pgm', type=argparse.FileType(mode='r'), help='The PGM file to play')
    parser.add_argument('--device', help='The OVDMX device to play to. I.e. /dev/ttyAMA0')
    parser.add_argument('--fps', type=int, default=20, help='Override the default frame rate of 20fps')
    parser.add_argument('--verify', action='store_true', help='Scan the playlist/pgm file and check for errors')

    args = parser.parse_args()
    if not args.playlist and not args.pgm:
        print('At least one pgm file or a playlist must be specified', file=sys.stderr)
        exit(1)

    if not args.device and not args.verify:
        print('A device must be specified if not running a format verification', file=sys.stderr)
        exit(1)

    if args.device and args.verify:
        print('A device can not be specified if running a format verification', file=sys.stderr)
        exit(1)

    channel_mapping = None
    if args.channel_mapping:
        channel_mapping = ChannelMap(args.channel_mapping)

    repeating = not args.single_cycle
    if args.pgm:
        # Create a playlist with a single item
        playlist = Playlist(StringIO(str(Path(args.pgm.name).absolute())), repeating=repeating)
    else:
        base_dir = None
        if args.playlist_dir:
            base_dir = args.playlist_dir
        playlist = Playlist(args.playlist, base_dir=base_dir, repeating=repeating)

    frame_rate = args.fps
    if args.verify:
        frame_rate = 200  # Override for verify to something way high
    frame_rate_controller = FrameRateController(frame_rate)

    if args.device:
        dmx_output = OVDMXOutput(args.device)
    elif args.verify:
        dmx_output = DummyDMXOutput()
    else:
        raise ValueError('Unknown DMX ouput')

    # Iterate the playlist and play the files
    for pgm_index, pgm in playlist.entry_generator():
        print("Starting pgm file #{}: {}".format(pgm_index, pgm))
        pgm_reader = PGMReader(pgm, channel_map=channel_mapping)
        frame_rate_controller.reset()
        for frame_index, frame in pgm_reader.frames():
            if frame_index % frame_rate == 0:
                print("frame #{:3d} in {}".format(frame_index, pgm_reader))
            frame_rate_controller.next_frame()
            dmx_output.push_frame(frame)


if __name__ == "__main__":
    main()
