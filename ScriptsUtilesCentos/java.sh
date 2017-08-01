if ! echo ${PATH} | grep -q /opt/jdk1.8.0_131/bin ; then
   export JAVA_HOME=/opt/jdk1.8.0_131/bin:${PATH}
fi
if ! echo ${PATH} | grep -q /opt/jdk1.8.0_131/jre/bin ; then
   export PATH=/opt/jdk1.8.0_131/jre/bin:${PATH}
fi
export JAVA_HOME=/opt/jdk1.8.0_131
export JRE_HOME=/opt/jdk1.8.0_131/jre
export CLASSPATH=.:/opt/jdk1.8.0_131/lib/tools.jar:/opt/jdk1.8.0_131/jre/lib/rt.jar