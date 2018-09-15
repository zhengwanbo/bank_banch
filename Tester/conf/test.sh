typeset -i begin=6214680000000000
#for var in {1...10000}
for((i=1;i<10000;i++));
do
typeset -i begin1=0
typeset -i step=0
((step=i*1000))
((begin1=begin+step))
((end1=end+i))
echo $begin1'|'1000'|a|CNY' >> param.txt
done

