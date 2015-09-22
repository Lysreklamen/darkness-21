channels = [int(x) for x in open('DefaultFrame.pgm').readlines()[3].split()]
mapping = {}
for line in open('../../patterns/logical_to_physical.txt'):
	items = line.split('\t')
	mapping[int(items[0])] = int(items[1])
for i in range(len(channels)):
	if channels[i]:
		print mapping[i + 1], channels[i]

