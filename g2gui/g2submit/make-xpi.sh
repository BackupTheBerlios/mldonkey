#!/bin/sh

version="$1"

if [ -z "$version" ]; then
    echo "Missing argument - version"
    exit 1
fi

cd package

mkdir /tmp/g2submit-dist
cp -pr content /tmp/g2submit-dist
local_dir=`pwd`

cd /tmp/g2submit-dist
jar -cM content > "$local_dir/g2submit.jar"
rm -rf /tmp/g2submit-dist
cd "$local_dir"

zip -r ../g2submit-$version.xpi g2submit.jar install.js
rm g2submit.jar

echo
echo "*****************************************************************"
echo "   Make sure you didn't forget to update version in:"
echo
echo "   install.js"
echo "   contents.rdf"
echo "   g2submit-pref.xul and g2submitPrevDialogue.xul"
echo "*****************************************************************"
echo
