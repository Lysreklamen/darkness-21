sudo apt-get update

# OLA needs a particular version of Protobuf, so we need to build it ourselves
sudo apt-get install autoconf automake libtool curl make g++ unzip
curl https://github.com/google/protobuf/releases/download/v2.6.1/protobuf-2.6.1.tar.gz --output ~/protobuf-2.6.1.tar.gz
tar xzvf ~/protobuf-2.6.1.tar.gz
cd ~/protobuf-2.6.1
./configure
make -j 4
sudo make install
sudo ldconfig

# Install our custom OLA
sudo apt-get install libcppunit-dev libcppunit-1.13-0v5 uuid-dev pkg-config libncurses5-dev libtool autoconf automake g++ libmicrohttpd-dev libmicrohttpd10 zlib1g-dev bison flex make libftdi-dev libftdi1 libusb-1.0-0-dev liblo-dev libavahi-client-dev python-numpy
cd ~/ola-ovdmx
autoreconf -i
./configure
make -j 4
sudo make install
sudo ldconfig
cd ~/.ola
sed -i "s/enabled = true/enabled = false/g" *.conf
echo -e "device = /dev/ttyDMX\nenabled = true" > ola-ovdmx.conf
cd

# After this, you need to log out and log in again
sudo usermod -aG uucp lysreklamen
