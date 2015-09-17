#!/usr/bin/env python3

f_big = open('uka15.txt')
lines_big = f_big.readlines()

f_mini = open('uka15_mini.txt')
lines_mini = f_mini.readlines()

if len(lines_big) != len(lines_mini):
    print("The files is not the same size. %s vs %s"%(len(lines_big), len(lines_mini)))
    exit()

import re
pattern = re.compile("^([0-9]+) ([0-9\\.]+) ([0-9\\.]+) R ([0-9]+) ([0-9]+) ([0-9]+)")
for i in range(0, len(lines_big)):
    match_big = pattern.match(lines_big[i])
    match_mini = pattern.match(lines_mini[i])
    if not match_big:
        print("The pattern did not match line %s in the big pattern file"%(i,))
        exit()

    if not match_mini:
        print("The pattern did not match line %s in the mini pattern file"%(i,))
        exit()

    if match_big.group(1) != match_mini.group(1):
        print("The bulb on the same line in both files does not have the same id");
        exit();

    id = match_big.group(1)
    pos_x = match_big.group(2)
    pos_y = match_big.group(3)
    r = int(match_mini.group(4))+2
    g = int(match_mini.group(5))+2
    b = int(match_mini.group(6))+2

    print("%s %s %s R %s %s %s"%(id, pos_x, pos_y, r, g, b))

