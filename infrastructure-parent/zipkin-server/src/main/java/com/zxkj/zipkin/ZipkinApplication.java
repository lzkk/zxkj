package com.zxkj.zipkin;

public class ZipkinApplication {
    public static void main(String[] args) {
        // 获得可执行文件路径
        String filePath = getZipkinBatPath(ZipkinApplication.class);
        // 执行文件
        openWindowsFile(filePath);
    }

    /**
     * 获得可执行文件路径
     *
     * @param clz
     * @return
     */
    private static String getZipkinBatPath(Class<?> clz) {
        String path = clz.getClassLoader().getResource("").getPath();
        path = path.substring(1);
        path = path.replaceAll("/", "\\\\");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cmd.exe /c start ");
        stringBuilder.append("java -jar ");
        stringBuilder.append(path + "WEB-INF\\lib\\zipkin-server-2.11.1-exec.jar");
        stringBuilder.append(" --HTTP_COLLECTOR_ENABLED=false");
        stringBuilder.append(" --KAFKA_BOOTSTRAP_SERVERS=127.0.0.1:9092");
        stringBuilder.append(" --KAFKA_TOPIC=myZipkin");
        stringBuilder.append(" --STORAGE_TYPE=elasticsearch");
        stringBuilder.append(" --ES_HOSTS=http://172.17.53.138:9200");
        return stringBuilder.toString();
    }

    /**
     * 用 Java 调用windows系统的exe文件，比如notepad，calc之类
     *
     * @param command
     */
    public static void openWindowsFile(final String command) {
        System.out.println(command);
        final Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (final Exception e) {
            e.printStackTrace();
            System.out.println("openWindowsFile execute error!");
        }
    }

}



