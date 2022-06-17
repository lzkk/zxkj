1、jps
   （全称JVM Process Status Tool）JVM进程状态工具
   jps -q 禁止类名称、JAR文件名和传递给main方法的参数的输出，仅生成本地JVM标识的列表
   jps -m 显示传递给main方法的参数，对于嵌入式JVM，输出可能为null
   jps -l 显示应用程序主类的完整软件包名称或应用程序JAR文件的完整路径名称
   jps -v 显示传递给JVM的参数
   jps -V 压低类名，JAR文件名和传递给main方法的参数的输出，仅生成本地JVM标识的列表
   jps -J[option] 将option传递给JVM，其中option是Java应用程序启动器的参考页上描述的选项之一。例如,-J-Xms48m将启动内存设置为48m
   jps -mlvV 可以获取到这个进程的pid、jar包的名称以及JVM参数等。
2、jstat
   (JVM Statics Monitoring Tool)
   jstat -gc pid 显示gc的信息，查看gc的次数和时间
   jstat -gccapacity pid 内存GC分区中各对象的使用和占用大小
   jstat -gcutil pid 统计gc信息统计
   jstat -gcnew pid 年轻代对象的信息
   jstat -gcnewcapacity pid 年轻代对象的信息及其占用量
   jstat -gcold pid old old代对象的信息
   jstat -gcoldcapacity pid old old代对象的信息及其占用量
   jstat -class pid 显示加载class的数量，及所占空间等信息
   jstat -compiler pid 显示VM实时编译的数量等信息
   jstat -printcompilation pid 当前VM执行的信息
   ####例子
   jstat -gc pid time times【可以先用ps -ef|grep java找出对应的线程ID】查看的是总量
   jstat -gc 22866 300 5   表示每隔300ms查看线程22866的垃圾回收情况，一共打印5次
   jstat -gcutil 22866  2000 3   表示每隔2000ms查看线程22866已用空间占总空间的百分比，一共打印3次
3、jcmd
   JVM诊断命令工具-将诊断命令请求发送到正在运行的Java虚拟机，可以查看 JVM 信息
   jcmd           查看本地的java进程列表，获取其pid
   jcmd pid help  查看其支持的命令列表
   jcmd pid Thread.print -l 打印线程栈
   jcmd pid VM.command_line 打印启动命令及参数
   jcmd pid GC.heap_dump /data/filename.dump 查看JVM的Heap Dump
   jcmd pid GC.class_histogram 查看类的统计信息
   jcmd pid VM.system_properties 查看系统属性内容
   jcmd pid VM.uptime 查看虚拟机启动时间
   jcmd pid PerfCount.print 查看性能统计
4、jmap
   jmap是一个重要的工具，查看内存详细信息，可以dump到文件中
   jmap [-F] -dump:live,format=b,file=/tmp/a pid
   -F 表示强制执行，live表示收集存活的对象，file存储到某个文件。dump文件下来后，可以使用工具来分析堆内存，包括jmap -histo、mat等
   jmap [-F] -histo pid
   查看堆内存的情况，按照对象数和对象占用的内存排队，可以初步定位到是哪个对象占用过多内存，内存泄漏的地方。后面使用mat工具能分析到哪个来存储的这个对象，也就是GCroot在哪
   jmap [-F] heap pid
   heap能查看当前使用的垃圾收集器是什么和一些参数策略，还有具体的内存分布，一般用不到。参数可以用jinfo，内存分布可以用jstat
5、jhat
   jhat会分析一个dump文件，然后把结果发布到一个html服务器上，有一定的用途，html也是主要看histogram。
   和的jmap -histo功能类似，用处不大。
6、jstack (Stack Trace for Java)
   jstack是比较有用的一个命令，查看线程的情况，包含锁，打印Java进程，核心文件或远程调试服务器的Java线程堆栈跟踪,俗称javacore
   jstack [-F] [-l] pid
   jstack -F 22866 表示正常输出的请求不被响应时，强制输出堆栈的信息
   jstack -l 22866  表示除堆栈外，显示锁的附加信息
   jstack -m 22866 表示如果调用到本地方法话，可以显示C/C++的堆栈
7、jconsole
   查看jvm的内存，cpu信息，线程，参数，类信息
8、jvisualvm
   是一个比较好用的工具，界面功能更强大，界面更友好。
   也是监控内存，cpu，线程，类信息和参数的。还可以执行dump和javacore的生成。
   如果产线允许的话，可以直接连接到产线发现解决问题。设置远程ip和jmx端口。当然产线的java启动需要设置jmx配置：
   -Dcom.sun.management.jmxremote=true
   -Dcom.sun.management.jmxremote.port=18080
9、mat
   mat是一个比较强大的分析堆溢出的工具。把之前dump文件导入到工具中。

10、top
   1、top -c
   每隔5秒显式进程的资源占用情况，并显示进程的命令行参数(默认只有进程名)
   2、查看进程中CPU占比最高的线程
   top -H -p 3147(3147为进程id)
   printf '%x\n' 3168(3168为占用cpu最高的线程id)，得到线程id对应的十六进制数据c60
   jstack 3147 | grep c60 -A 50