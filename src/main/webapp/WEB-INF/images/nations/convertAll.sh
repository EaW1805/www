#!/bin/sh
for i in $(seq 1 17);
do
  rm nation-${i}-36Slc.png
  convert nation-${i}Slc.png -scale 36x24 nation-$i-36Slc.png
  convert nation-${i}.png -colorspace Gray nation-${i}NA.png 
done
