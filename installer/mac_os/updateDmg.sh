#!/bin/bash
VERSION=1.0-RC3
cp Open\ DELTA.dmg /tmp
hdiutil attach /tmp/Open\ DELTA.dmg
cd ../../target
unzip open-delta-$VERSION-mac_os_bundle.zip
cd open-delta-$VERSION
cp -R Open\ DELTA /Volumes/Open\ DELTA/
cp -R .open-delta /Volumes/Open\ DELTA/Open\ DELTA/
hdiutil detach /Volumes/Open\ DELTA
hdiutil convert -format UDCO -o ../Open\ DELTA.dmg /tmp/Open\ DELTA.dmg

