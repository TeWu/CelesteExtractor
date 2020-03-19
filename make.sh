#!/usr/bin/env bash
mkdir -p out/production/CelesteExtractor
echo "Running javac"
javac -source 1.8 -target 1.8 -d out/production/CelesteExtractor src/pl/geek/tewu/celeste_extractor/*.java
echo "javac done"
mkdir -p out/release
jar cvfm out/release/CelesteExtractor.jar src/META-INF/MANIFEST.MF -C out/production/CelesteExtractor .
cp -av scripts/* out/release/
7z a -tzip CelesteExtractor_`git describe --tags --broken --dirty`.zip ./out/release/*
echo "All done"