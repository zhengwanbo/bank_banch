typeset -i begin=6214680000000000
typeset -i end=6214680001000000
#for var in {1...10000}
for((i=1;i<10000;i++));
do
typeset -i begin1=0
typeset -i end1=0
((begin1=begin+i))
((end1=end+i))
echo $begin1'|'$end1'|'100 >> param.txt
done

