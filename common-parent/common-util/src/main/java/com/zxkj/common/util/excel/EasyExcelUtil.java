package com.zxkj.common.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.cache.selector.SimpleReadCacheSelector;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * easyExcel工具类
 *
 * @author ：yuhui
 * @date ：Created in 2020/12/2 17:16
 */
public class EasyExcelUtil {

    // 默认读取excel标题行号
    private static final Integer headRowNum = 1;
    // 默认截断阈值
    private static final Integer truncationThreshold = 0;
    // 默认批次大小
    private static final Integer fetchPageSize = 100;


    /**
     * 导出excel
     *
     * @param response
     * @param list
     * @param fileName
     * @param clz
     * @param <T>
     * @throws Exception
     */
    public static <T> void writeExcel(HttpServletResponse response, List<T> list, String fileName, Class<T> clz) throws Exception {
        response.setCharacterEncoding("utf8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx"); //文件名
        response.setHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "max-age=0");
        writeExcel(response.getOutputStream(), list, fileName, clz);
    }

    /**
     * 导出excel
     *
     * @param outputStream
     * @param list
     * @param sheetName
     * @param clz
     * @param <T>
     * @throws Exception
     */
    public static <T> void writeExcel(OutputStream outputStream, List<T> list, String sheetName, Class<T> clz) throws Exception {
        EasyExcel.write(outputStream, clz).sheet(sheetName).doWrite(list);
    }

    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param headRowNumber
     * @param analysisEventListener
     * @return
     */
    private static <T> void readExcel(InputStream inputStream, Class clz, int headRowNumber, AnalysisEventListener<T> analysisEventListener) {
        // 默认文件处理(如果excel文件小，效率还可以，文件若大，就比较慢)
        //EasyExcel.read(inputStream, clz, analysisEventListener).sheet().headRowNumber(headRowNumber).doRead();
        // 定制文件处理(处理excel文件，不走文件存储，全部走内存)
        //EasyExcel.read(inputStream, clz, analysisEventListener).readCache(new MapCache()).sheet().headRowNumber(headRowNumber).doRead();
        // 定制文件处理(处理excel文件，采用文件存储，用90m内存来处理excel，内存命中直接读取，否则加载文件)
        EasyExcel.read(inputStream, clz, analysisEventListener).readCacheSelector(new SimpleReadCacheSelector(20, 90)).sheet().headRowNumber(headRowNumber).doRead();
    }

    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param statConsumer
     * @param <T>
     */
    public static <T> void readExcel(InputStream inputStream, Class<T> clz, Consumer<EasyExcelStatHelp> statConsumer) {
        readExcel(inputStream, clz, headRowNum, truncationThreshold, statConsumer, fetchPageSize, null);
    }

    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param headRowNum
     * @param statConsumer
     * @param <T>
     */
    public static <T> void readExcel(InputStream inputStream, Class<T> clz, int headRowNum, Consumer<EasyExcelStatHelp> statConsumer) {
        readExcel(inputStream, clz, headRowNum, truncationThreshold, statConsumer, fetchPageSize, null);
    }

    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param headRowNum
     * @param statConsumer
     * @param ruleCheckHelp
     * @param <T>
     */
    public static <T> void readExcel(InputStream inputStream, Class<T> clz, int headRowNum, Consumer<EasyExcelStatHelp> statConsumer, EasyExcelRuleCheckHelp ruleCheckHelp) {
        readExcel(inputStream, clz, headRowNum, truncationThreshold, statConsumer, fetchPageSize, ruleCheckHelp);
    }

    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param headRowNum
     * @param statConsumer
     * @param pageSize
     * @param ruleCheckHelp
     * @param <T>
     */
    public static <T> void readExcel(InputStream inputStream, Class<T> clz, int headRowNum, Consumer<EasyExcelStatHelp> statConsumer, int pageSize, EasyExcelRuleCheckHelp ruleCheckHelp) {
        readExcel(inputStream, clz, headRowNum, truncationThreshold, statConsumer, pageSize, ruleCheckHelp);
    }


    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param headRowNum
     * @param truncationThreshold
     * @param statConsumer
     * @param <T>
     */
    public static <T> void readExcel(InputStream inputStream, Class<T> clz, int headRowNum, int truncationThreshold, Consumer<EasyExcelStatHelp> statConsumer) {
        readExcel(inputStream, clz, headRowNum, truncationThreshold, statConsumer, fetchPageSize, null);
    }

    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param statConsumer
     * @param pageSize
     * @param <T>
     */
    public static <T> void readExcel(InputStream inputStream, Class<T> clz, Consumer<EasyExcelStatHelp> statConsumer, int pageSize) {
        readExcel(inputStream, clz, headRowNum, truncationThreshold, statConsumer, pageSize, null);
    }

    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param headRowNum
     * @param statConsumer
     * @param pageSize
     * @param <T>
     */
    public static <T> void readExcel(InputStream inputStream, Class<T> clz, int headRowNum, Consumer<EasyExcelStatHelp> statConsumer, int pageSize) {
        readExcel(inputStream, clz, headRowNum, truncationThreshold, statConsumer, pageSize, null);
    }

    /**
     * 读取excel
     *
     * @param inputStream
     * @param clz
     * @param headRowNum
     * @param truncationThreshold
     * @param statConsumer
     * @param pageSize
     * @param ruleCheckHelp
     * @param <T>
     */
    public static <T> void readExcel(InputStream inputStream, Class<T> clz, int headRowNum, int truncationThreshold, Consumer<EasyExcelStatHelp> statConsumer, int pageSize, EasyExcelRuleCheckHelp ruleCheckHelp) {
        AnalysisEventListener analysisEventListener = getEventListener(headRowNum, truncationThreshold, statConsumer, pageSize, ruleCheckHelp);
        readExcel(inputStream, clz, headRowNum, analysisEventListener);
    }

    /**
     * 分批次处理任务
     *
     * @param headRowNum
     * @param truncationThreshold
     * @param statConsumer
     * @param pageSize
     * @param ruleCheckHelp
     * @param <T>
     * @return
     */
    public static <T> AnalysisEventListener<T> getEventListener(int headRowNum, int truncationThreshold, Consumer<EasyExcelStatHelp> statConsumer, int pageSize, EasyExcelRuleCheckHelp ruleCheckHelp) {
        return new AnalysisEventListener<T>() {
            private LinkedList<T> linkedList = new LinkedList<T>();
            private EasyExcelStatHelp easyExcelStatHelp = new EasyExcelStatHelp();
            private Map<Integer, CellData> headerMap = new HashMap<>();
            private StringBuilder errMsgStringBuilder = new StringBuilder();
            private int sucRecordCount = 0; // 成功记录数
            private int errRecordCount = 0; // 失败记录数
            private boolean hasNext = true; // 是否读取下一行

            /**
             * 解析excel头
             * @param headMap
             * @param context
             */
            @Override
            public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
                Integer rowIndex = context.readRowHolder().getRowIndex();
                rowIndex += 1;
                if (headRowNum == rowIndex && ruleCheckHelp != null && ruleCheckHelp.getCols() != null) {
                    checkTitle(ruleCheckHelp.getCols(), headMap.keySet().size());
                }
                this.headerMap = headMap;
                super.invokeHead(headMap, context);
            }

            /**
             * 遍历excel每行
             * @param t
             * @param analysisContext
             */
            @Override
            public void invoke(T t, AnalysisContext analysisContext) {
                if (ruleCheckHelp != null) {
                    checkData(t, analysisContext);
                }
                sucRecordCount++;
                linkedList.add(t);
                if (truncationThreshold <= 0) {  // 不截断
                    if (linkedList.size() == pageSize) {
                        processConsumer();
                        linkedList.clear();
                    }
                } else {
                    int truncationRemainSucCount = truncationThreshold - errRecordCount;
                    if (linkedList.size() == truncationRemainSucCount) {
                        // 开始截断，不继续执行下面的行(两种方式,1:hasNext;2抛出异常ExcelAnalysisStopException)
                        processConsumer();
                        hasNext = false;
                    }
                }
            }

            /**
             * excel内置解析器发现异常处理
             * @param exception
             * @param context
             * @throws Exception
             */
            @Override
            public void onException(Exception exception, AnalysisContext context) throws Exception {
                // 如果是某一个单元格的转换异常 能获取到具体行号,如果要获取头的信息 配合invokeHeadMap使用
                errRecordCount++;
                if (exception instanceof ExcelDataConvertException) {
                    ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
                    int col = excelDataConvertException.getColumnIndex();
                    int row = excelDataConvertException.getRowIndex();
                    CellData cellData = excelDataConvertException.getCellData();
                    String title = this.headerMap.get(col).getStringValue();
                    errMsgStringBuilder.append("第").append(row + 1).
                            append("行，列:").append(title).
                            append(",异常数据为:").append(cellData.getStringValue()).append(";");
                } else {

                }
            }

            /**
             * 是否继续读取下一行
             * @param context
             * @return
             */
            @Override
            public boolean hasNext(AnalysisContext context) {
                return hasNext;
            }

            /**
             * 文件全部读取完毕后续处理
             * @param analysisContext
             */
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                processConsumer();
            }

            /**
             * 校验excel头合法性
             * @param cols
             * @param fileCols
             */
            private void checkTitle(Integer cols, int fileCols) {
                if (fileCols != cols) {
                    errMsgStringBuilder.append("由于文件列数不正确，操作终止！");
                    processConsumer();
                    hasNext = false;
                }
            }

            /**
             *  校验excel每行数据合法性
             * @param t
             * @param analysisContext
             */
            private void checkData(T t, AnalysisContext analysisContext) {
                Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
                rowIndex += 1;
                Map<String, Object> ben2Map = JSON.parseObject(JSON.toJSONString(t), Map.class);
                List<String> notNullKeyList = ruleCheckHelp.getNotNullKeyList();
                StringBuilder stringBuilder = new StringBuilder();
                if (notNullKeyList != null && notNullKeyList.size() > 0) {
                    Map<String, Integer> fieldIndexMap = ruleCheckHelp.getFieldIndexMap();
                    for (String notNullKey : notNullKeyList) {
                        Object valObj = ben2Map.get(notNullKey);
                        if (valObj == null) {
                            String title = this.headerMap.get(fieldIndexMap.get(notNullKey)).getStringValue();
                            stringBuilder.append("列:" + title + ",数据为空.");
                        }
                    }
                }
                List<String> valueEqualKeyList = ruleCheckHelp.getValueEqualKeyList();
                if (valueEqualKeyList != null && valueEqualKeyList.size() > 0) {
                    Object valObj = null;
                    for (String valueEqualKey : valueEqualKeyList) {
                        Object tmpValObj = ben2Map.get(valueEqualKey);
                        if (valObj == null) {
                            valObj = tmpValObj;
                        } else {
                            if (!valObj.equals(tmpValObj)) {
                                stringBuilder.append("列:" + getValueEqualText(valueEqualKeyList) + ",这几列的数据必须一致.");
                                break;
                            }
                        }
                    }
                }
                if (stringBuilder.length() > 0) {
                    errMsgStringBuilder.append("第").append(rowIndex).
                            append("行，").append(stringBuilder.toString()).append(";");
                    throw new RuntimeException();
                }
            }

            /**
             * 消费者消息处理
             */
            private void processConsumer() {
                if (linkedList.size() > 0) {
                    easyExcelStatHelp.setData(linkedList);
                }
                easyExcelStatHelp.setErrMsg(errMsgStringBuilder.toString());
                easyExcelStatHelp.setSucRecordCount(sucRecordCount);
                easyExcelStatHelp.setErrRecordCount(errRecordCount);
                statConsumer.accept(easyExcelStatHelp);
            }

            /**
             * 处理不同列要求等值标题展示
             * @param valueEqualKeyList
             * @return
             */
            private String getValueEqualText(List<String> valueEqualKeyList) {
                if (valueEqualKeyList == null || valueEqualKeyList.size() == 0) {
                    return "";
                }
                Map<String, Integer> fieldIndexMap = ruleCheckHelp.getFieldIndexMap();
                String firstVal = valueEqualKeyList.get(0);
                String result = this.headerMap.get(fieldIndexMap.get(firstVal)).getStringValue();
                for (int k = 1; k < valueEqualKeyList.size(); k++) {
                    String tmp = valueEqualKeyList.get(k);
                    String defaultVal = this.headerMap.get(fieldIndexMap.get(tmp)).getStringValue();
                    result += "、" + defaultVal;
                }
                return result;
            }

        };
    }

}