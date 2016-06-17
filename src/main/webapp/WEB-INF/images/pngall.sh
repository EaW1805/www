files=`find . -type f`
for f in $files
do
	if [[ "$f" != *.svn* ]]
	then
		if [[ "$f" == *.png ]]
		then
			pngquant 256 $f 
			mv ${f:0:${#f}-4}-fs8.png $f
		fi
	fi
done
