#!/bin/sh
for i in `seq -f "%02g" 0 1 10` 
do  
  mv ArmyMap$i.png UnitMap$i.png
done

