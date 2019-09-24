# Given a pattern file (which maps each bulb ID to a unique logical DMX channel)
# and a box file (which maps each DMX box and LED to a unique bulb ID), this script
# generates a systematic mapping from each logical DMX channel to a physical DMX channel.
# Given a 1-indexed `box`, a 1-indexed `led` within that box (between 1 and 6),
# and a color that is either red (0), green (1), or blue (2), the physical
# channel will be assigned according to this formula:
# (box - 1) * 18 + (led - 1) * 3 + (color + 2)
# (TODO: Remember why we add 2...)

from sys import argv, exit

bulbs = {}
patternFile = argv[1]
boxFile = argv[2]
for line in open(patternFile):
	items = line.split()
	if not items or not items[0].isdigit():
		continue
	bulb = int(items[0])
	rgb = [int(x) for x in items[4:7]]
	bulbs[bulb] = rgb

seenBoxItems = set()
for line in open(boxFile):
	items = line.split()
	if not items or items[1] == '-':
		continue
	boxItems = items[0].split('.')
	box = int(boxItems[0])
	boxLed = int(boxItems[1])
	boxItem = (box, boxLed)
	if boxItem in seenBoxItems:
		print "Duplicate box:", boxItem
		exit(1)
	bulb = int(items[1])
	rgb = bulbs[bulb]
	for color in range(len(rgb)):
		physical = (box - 1) * 18 + (boxLed - 1) * 3 + (color + 2)
		print('%d\t%d' % (rgb[color], physical))

# The two seven-segment displays that make up the counter
for display in range(2):
	for segment in range(7):
		logical = display * 7 + segment + 101
		physical = display * 9 + segment + 488
		print('%d\t%d' % (logical, physical))
