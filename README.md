# darkness
Darkness is a utility program used for making the LED sign for the culture festival UKA in Trondheim, Norway. 
It was developed by Lysreklamen, UKA-15 which was named Lurifax. The project was closed source until the revealing of UKA-15's name, Lurifax, on October the 1.st 2015.

Darkness is written in Java and was written as a replacement for an older simulator named Luminance.

For UKA-21 a new browser based simulator was developed, and the simulator part of darkness was removed. The sequence generation part of the project remains.

## Requirements

Using darkness for developing new sequences requires `docker` to be installed on the development machine.

## Quickstart

- Open the browser based simulator in Google Chrome:
  - [https://uka.saltvedt.tech](https://uka.saltvedt.tech)
- To access the secret signs you must authenticate in the upper right part of the web page.
- Click the sign you want to work with
  - A 3D model with the sign will show up. 
- Click on the "Development" tab in the tabbed panel on the bottom of the page. 
  - The panel will contain the current connection status to the sequence generation software.
- Start the development docker container using the command `./start-dev.sh` in the terminal.
- The development panel should show that a connection to the sequence generation software has been established.
- Follow the instructions in the terminal.