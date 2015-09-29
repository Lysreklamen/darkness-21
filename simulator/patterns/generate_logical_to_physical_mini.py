from sys import argv

def readFile(fileName):
	bulbs = {}
	for line in open(fileName):
		items = line.split()
		bulb = int(items[0])
		rgb = [int(x) for x in items[4:7]]
		bulbs[bulb] = rgb
	return bulbs

logical = readFile(argv[1])
physical = readFile(argv[2])
assert len(logical) == len(physical)
for id in logical:
	for i in range(3):
		print('%d\t%d' % (logical[id][i], physical[id][i]))
