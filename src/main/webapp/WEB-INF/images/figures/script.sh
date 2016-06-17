#!/bin/sh
for i in `seq -f "%02g" 0 1 10` 
do
  svn move $1/MUIFleet$2$i.png $1/FleetMap$i.png
  cp $1/MUIArmy$2$i.png $1/UnitMap$i.png
  svn remove $1/MUIArmy$2$i.png
done
svn add $1/UnitMap00.png
svn remove $1/UnitMap11.png

