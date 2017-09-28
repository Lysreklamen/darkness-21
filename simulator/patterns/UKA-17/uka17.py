from math import sin, cos, pi, asin, acos


currentX = 0
currentY = 0
shiftX = 0
shiftY = 0.12


def set(x, y):
    global currentX, currentY
    currentX = x
    currentY = y


def shift(x, y):
    global currentX, currentY
    currentX += x
    currentY += y


def bulb(id):
    print "{0} ({1:.2f}, {2:.2f}) R {3} {4} {5}".format(id, currentX + shiftX, currentY + shiftY, 200 + id, 300 + id, 400 + id)


def bulbAbs(id, x, y):
    global currentX, currentY
    currentX = x
    currentY = y
    bulb(id)


def bulbRel(id, x, y):
    global currentX, currentY
    currentX += x
    currentY += y
    bulb(id)


def startAlu(closed=True):
    print "ALU" if closed else "ALUOPEN",


def alu(x, y):
    print "({0:.3f}, {1:.3f});".format(x + shiftX, y + shiftY),


def aluAbs(x, y):
    global currentX, currentY
    currentX = x
    currentY = y
    alu(currentX, currentY)


def aluRel(x, y):
    global currentX, currentY
    currentX += x
    currentY += y
    alu(currentX, currentY)


def endAlu():
    print


def fullAlu(points):
    startAlu()
    for point in points:
        aluAbs(*point)
    endAlu()


def fullAluRel(start, points):
    startAlu()
    aluAbs(*start)
    for point in points:
        aluRel(*point)
    endAlu()


def aluArc(centerX, centerY, radius, segments, fromAngle, toAngle):
    for i in xrange(segments + 1):
        angle = fromAngle + (toAngle - fromAngle) * i / segments
        x = centerX + radius * cos(angle)
        y = centerY + radius * sin(angle)
        aluAbs(x, y)


def rightAluArc(centerX, centerY, radius, segments, height):
    angle = asin(height / 2 / radius)
    aluArc(centerX, centerY, radius, segments, -angle, angle)


def leftAluArc(centerX, centerY, radius, segments, height):
    angle = asin(height / 2 / radius)
    aluArc(centerX, centerY, radius, segments, pi - angle, pi + angle)


def bulbArc(startId, centerX, centerY, radius, bulbs, fromAngle, toAngle):
    for i in xrange(bulbs):
        angle = fromAngle + (toAngle - fromAngle) * i / (bulbs - 1)
        x = centerX + radius * cos(angle)
        y = centerY + radius * sin(angle)
        bulbAbs(startId + i, x, y)


def rightBulbArc(startId, centerX, centerY, radius, bulbs, height):
    angle = asin(height / 2 / radius)
    bulbArc(startId, centerX, centerY, radius, bulbs, -angle, angle)


def leftBulbArc(startId, centerX, centerY, radius, bulbs, height):
    angle = asin(height / 2 / radius)
    bulbArc(startId, centerX, centerY, radius, bulbs, pi - angle, pi + angle)


def repeatedLetter(startId, shift):
    leftBulbArc(startId, 2.03 + 5.15 + shift, 1.01, 5.10, 4, 1.32)
    leftBulbArc(startId + 4, 2.29 + 5.15 + shift, 1.01, 5.10, 4, 1.12)
    rightBulbArc(startId + 8, 1.70 + shift, 1.01, 1.18, 6, 1.84)
    basePoints = [(2.29, 0.00), (2.18, 0.10), (2.08, 0.22), (1.98, 0.37), (1.98, 1.65), (2.08, 1.80), (2.18, 1.92), (2.29, 2.02), (2.25, 1.77), (2.22, 1.52), (2.21, 1.27), (2.20, 1.01), (2.21, 0.75), (2.22, 0.50), (2.25, 0.25)]
    fullAlu([(p[0] + shift, p[1]) for p in basePoints])
    startAlu()
    rightAluArc(1.70 + shift, 1.01, 1.23, 16, 2.02)
    leftAluArc(2.29 + 5.15 + shift, 1.01, 5.15, 8, 2.02)
    endAlu()
    startAlu()
    rightAluArc(1.70 + shift, 1.01, 1.13, 16, 1.69)
    leftAluArc(2.29 + 5.15 + shift, 1.01, 5.05, 8, 1.69)
    endAlu()


print "OFFSET -1.785 2"
print "SCALE 1 -1"
print

for i in xrange(6):
    bulbAbs(i, 0.34 if i % 2 == 0 else 0.44, 0.30 * (i + 1) - 0.12)
bulbAbs(6, 0.14, 1.50)
for i in xrange(7, 11):
    bulbAbs(i, 0.64, 0.38 * (i - 6))
bulbAbs(11, 0.69, 1.92)

fullAlu([(0.25, 0.00), (0.25, 1.52), (0.06, 1.40), (0.00, 1.48), (0.52, 1.84), (0.52, 0.00)])
fullAlu([(0.59, 0.00), (0.59, 1.90), (0.79, 2.05), (0.85, 1.97), (0.69, 1.85), (0.69, 0.00)])
print

bulbAbs(15,  0.82, 0.17)
bulbAbs(16,  1.10, 0.17)
bulbAbs(17,  0.96, 0.30)
bulbAbs(18,  0.96, 0.50)
bulbAbs(19,  1.29, 0.17)
bulbRel(20, -0.10, 0.28)
bulbRel(21, -0.10, 0.28)

fullAlu([(0.73, 0.00), (0.96, 0.68), (1.19, 0.00), (1.10, 0.00), (1.02, 0.19), (0.90, 0.19), (0.82, 0.00)])
fullAlu([(1.25, 0.00), (0.98, 0.77), (1.07, 1.04), (1.43, 0.00)])
print

bulbAbs(25, 1.605, 0.52)
bulbAbs(26, 1.605, 0.73)

fullAlu([(1.48, 0.42), (1.48, 0.62), (1.73, 0.62), (1.73, 0.42)])
fullAlu([(1.48, 0.68), (1.48, 0.78), (1.73, 0.78), (1.73, 0.68)])
print

repeatedLetter(30, 0)
print

bulbAbs(45,  3.17,  0.12)
bulbRel(46,  0.00,  0.28)
bulbRel(47,  0.00,  0.28)
bulbRel(48,  0.00,  0.22)
bulbAbs(49,  3.55,  0.05)
bulbAbs(50,  3.36,  0.05)
bulbAbs(51,  3.36,  0.37)
bulbAbs(52,  3.36,  0.68)
bulbAbs(53,  3.36,  0.98)
bulbAbs(54,  3.55,  0.98)

fullAluRel((3.09, 0.00), [
    ( 0.00,  0.63),
    (-0.10,  0.00),
    ( 0.00,  0.10),
    ( 0.10,  0.00),
    ( 0.00,  0.30),
    ( 0.16,  0.00),
    ( 0.00, -0.30),
    ( 0.06,  0.00),
    ( 0.00,  0.30),
    ( 0.33,  0.00),
    ( 0.00, -0.10),
    (-0.23,  0.00),
    ( 0.00, -0.20),
    ( 0.10,  0.00),
    ( 0.00, -0.10),
    (-0.10,  0.00),
    ( 0.00, -0.53),
    ( 0.23,  0.00),
    ( 0.00, -0.10),
    (-0.33,  0.00),
    ( 0.00,  0.63),
    (-0.06,  0.00),
    ( 0.00, -0.63),
])
print

bulbAbs(55, 3.865, 0.52)
bulbAbs(56, 3.8655, 0.73)

fullAlu([(3.74, 0.42), (3.74, 0.62), (3.99, 0.62), (3.99, 0.42)])
fullAlu([(3.74, 0.68), (3.74, 0.78), (3.99, 0.78), (3.99, 0.68)])
print

repeatedLetter(60, 2.27)
print

bulbAbs(75, 5.37, 0.86)
bulbAbs(76, 5.37, 0.49)
bulbAbs(77, 5.40, 0.18)
bulbAbs(78, 5.62, 0.06)
bulbAbs(79, 5.84, 0.18)
bulbAbs(80, 5.87, 0.49)
bulbAbs(81, 5.87, 0.86)
bulbAbs(82, 5.545, 0.33)
bulbRel(83, 0.00, 0.29)
bulbRel(84, 0.00, 0.29)

startAlu()
aluAbs( 5.32,  0.30)
aluRel( 0.00,  0.76)
aluRel( 0.10,  0.00)
aluRel( 0.00, -0.76)
aluArc(5.62, 0.30, 0.20, 16, pi, 2 * pi)
aluAbs( 5.82,  0.30)
aluRel( 0.00,  0.76)
aluRel( 0.10,  0.00)
aluRel( 0.00, -0.76)
aluArc(5.62, 0.30, 0.30, 16, 2 * pi, pi)
endAlu()
startAlu()
aluAbs( 5.47,  0.30)
aluRel( 0.00,  0.76)
aluRel( 0.15,  0.00)
aluRel( 0.00, -0.91)
aluArc(5.62, 0.30, 0.15, 8, 3 * pi / 2, pi)
endAlu()
print

bulbAbs(85, 6.17, 0.50)
for i in range(4):
    bulbRel(86 + i, -0.013, 0.33)
bulbAbs(90, 6.32, 0.50)
for i in range(4):
    bulbRel(91 + i, 0.003, 0.33)
bulbAbs(95, 6.25, 0.14)

fullAlu([(6.12, 0.32), (6.04, 2.02), (6.17, 2.02), (6.22, 0.32)])
fullAlu([(6.28, 0.32), (6.23, 2.02), (6.43, 2.02), (6.38, 0.32)])
startAlu()
aluArc(6.25, 0.14, 0.14, 32, 0, 2 * pi * 31 / 32)
endAlu()
