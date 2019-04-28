
svg_template = '''
<circle
   r="4"
   style="display:inline;fill:#ff0000;fill-opacity:1;stroke-width:1"
   id="bulb-BULB_ID"
   cx="BULB_X"
   cy="BULB_Y" />
'''

with open('./uka17.txt', 'r') as f:
    for line in f.readlines():
        line = line.replace('(', '').replace(')', '').replace(',', '')
        parts = line.split(' ')
        if len(parts) == 0:
            continue
        try:
            num = int(parts[0])
        except:
            continue

        posX = float(parts[1])*100
        posY = float(parts[2])*-100

        svg_elem = svg_template.replace('BULB_ID', str(num)).replace('BULB_X', str(posX)).replace('BULB_Y', str(posY))
        print(svg_elem)


