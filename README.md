![RTLion](https://user-images.githubusercontent.com/24392180/57021451-ed7cf780-6c34-11e9-8522-84bcd39728d4.png)

# RTLion App <a href="https://github.com/RTLion-Framework/RTLion-app/releases"><img src="https://img.shields.io/github/release/RTLion-Framework/RTLion-app.svg"/></a>

### Android Application of RTLion Framework.

<a href="https://github.com/RTLion-Framework/RTLion-app/issues"><img src="https://img.shields.io/github/issues/RTLion-Framework/RTLion-app.svg"/></a>
<a href="https://github.com/RTLion-Framework/RTLion-app/pulls"><img src="https://img.shields.io/github/issues-pr/RTLion-Framework/RTLion-app.svg"/></a>
<a href="https://github.com/RTLion-Framework/RTLion-app/stargazers"><img src="https://img.shields.io/github/stars/RTLion-Framework/RTLion-app.svg"/></a>
<a href="https://github.com/RTLion-Framework/RTLion-app/network"><img src="https://img.shields.io/github/forks/RTLion-Framework/RTLion-app.svg"/></a>
<a href="https://github.com/RTLion-Framework/RTLion-app/blob/master/LICENSE"><img src="https://img.shields.io/github/license/RTLion-Framework/RTLion-app.svg"/></a>

[RTLion](https://github.com/KeyLo99/RTLion) is a framework for `RTL2832` based `DVB-T` receivers and it supports various features such as `spectral density visualizing` and `frequency scanning` remotely. As well as the Web interface, using RTLion features is possible via the mobile application. All framework features are currently available on the app.

## Usage

### Starting RTLion Server

RTLion server must be running in order to use the mobile application due to the need of server connection. For more info about command line arguments and framework, see [RTLion Framework](https://github.com/KeyLo99/RTLion) repository.

![Starting RTLion Server](https://user-images.githubusercontent.com/24392180/57334572-cd63a180-7128-11e9-8a9b-291b065d5f25.gif)

### Connecting to the Server

After installing the [latest release](https://github.com/KeyLo99/RTLion-app#releases) of the app, connection to the server can be made on the main menu using the host and port information.

![RTLion Main Menu](https://user-images.githubusercontent.com/24392180/57201478-15a48780-6fa2-11e9-9a72-c897a2bfd33e.gif)

After the connection has been made, RTLion would show the client browser details which can be helpful as a verbose info.

### Updating Settings

RTL-SDR device settings can be changed easily on the `Settings` page.

![RTLion Settings Page](https://user-images.githubusercontent.com/24392180/57200824-1553be80-6f99-11e9-905c-592a35ce33aa.gif)

('1.4' is a supported gain value for my RTL-SDR device.) [RTL2832U, 820T2]

### Power Spectrum 

As on the Web interface, `Power Spectrum (FFT Graph)` visualizer feature is provided on the `Graph` page. It's usable just by determining the `center frequency`, `read interval` and `read count` parameters.

![RTLion Graph Page](https://user-images.githubusercontent.com/24392180/57201005-7aa8af00-6f9b-11e9-99f5-48399cf0fd5a.gif)

Also center frequency can be changed real time via the `SeekBar` view below the graph.

### Frequency Scanner

Frequency scanner aims to find the peaks on a power spectrum for miscellaneous applications with using a sorting method. For using this feature on the mobile app, it's enough to set `frequency range` and `sensitivity` parameters. Afterall, RTLion would find the maximum power (dB) values on the graph and add them to the `ListView` which is located below the graph and can be real time tracked.

Also it's possible to change `sensitivity` value while the scanning operation continues. 

![RTLion Scanner Page](https://user-images.githubusercontent.com/24392180/57201340-79c64c00-6fa0-11e9-9675-1897a4d2ed69.gif)

RTLion provides a feature for showing the graph of scanned value. After the scan finished or during the scan, choosing a value from the `scanned values ListView` and selecting the `Show Graph` option will redirect you to the `graph page` and it shows you the spectrum as soon as the scan operation stops or the RTL-SDR device prepares.

![RTLion Show Graph Option](https://user-images.githubusercontent.com/24392180/57201318-10463d80-6fa0-11e9-9e19-9b1b18328b8d.gif)

## IoT

There is more information about the concept at the [RTLion Framework](https://github.com/RTLion-Framework/RTLion#iot) repository.

![RTLion - IoT RTL-SDR](https://user-images.githubusercontent.com/24392180/57582244-a4456700-74c2-11e9-8735-7a97b8f89629.png)

### SSH 

RTLion server can be started after establishing SSH connection to Raspberry Pi as shown below.

![Starting RTLion Server on SSH](https://user-images.githubusercontent.com/24392180/57569099-c8d50c80-73f8-11e9-9a73-80fc2a5476c9.jpg)

### Accessing the Server

After starting the RTLion server, RTL-SDR functions can be accessed via the Android application.

## TODO(s)

_Considerable for future versions._
* Improve the mobility for graph image sizes
* Fix step size calculation for not wide frequency ranges
* Fix server connection issues which caused by the WebView
* Fix cache and hardware acceleration issues of the WebView
* Show more server/client info on the main menu
* Test other Android devices for the xml layouts 
* Multiple language support

## APK

[Download APK](https://github.com/RTLion-Framework/RTLion-app/raw/master/app/dist/rtlion-app.apk)

## Contribution

RTLion Project is open to contributions.[*](https://github.com/RTLion-Framework/RTLion-app/CONTRIBUTING.md)

## License

GNU General Public License v3. (see [gpl](https://www.gnu.org/licenses/gpl.txt))

## Credit

Copyright (C) 2019 by KeyLo99 
https://www.github.com/KeyLo99






