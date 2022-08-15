package com.zxkj.xxl.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.admin.core.model.XxlJobGroup;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XxlHttpClient工具类
 *
 * @author ：yuhui
 * @date ：Created in 2022/2/21 15:46
 */
public class XxlHttpClientUtil {

    private static String cookie = null;

    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "123456";

    /**
     * 查询执行器
     *
     * @param path
     * @param appname
     * @return
     */
    public static XxlJobGroup getJobGroup(String path, String appname) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appname", appname);
        if (cookie == null || cookie.trim().length() == 0) {
            cookie = getCookie(path, USER_NAME, PASSWORD);
        }
        HttpResponse response = HttpRequest.post(path + "/jobgroup/loadByAppname").cookie(cookie).form(paramMap).execute();
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(response.body());
        XxlJobGroup xxlJobGroup = JSON.parseObject(String.valueOf(jsonObject.get("content")), XxlJobGroup.class);
        return xxlJobGroup;
    }

    /**
     * 创建执行器
     *
     * @param path
     * @param appname
     * @return
     */
    public static String createJobGroup(String path, String appname) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appname", appname);
        paramMap.put("title", appname);
        paramMap.put("addressType", 0); // 0自动注册 1手动注册
        if (cookie == null || cookie.trim().length() == 0) {
            cookie = getCookie(path, USER_NAME, PASSWORD);
        }
        HttpResponse response = HttpRequest.post(path + "/jobgroup/save").cookie(cookie).form(paramMap).execute();
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            return "network error";
        }
        JSONObject jsonObject = JSON.parseObject(response.body());
        return jsonObject.getString("content");
    }

    /**
     * 创建任务
     *
     * @param path
     * @param jobGroupId
     * @param executorHandler
     * @param scheduleConf
     * @param executorParam
     * @param jobDesc
     * @return
     */
    public static int createJobInfo(String path, int jobGroupId, String executorHandler, String scheduleConf, String executorParam, String jobDesc) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("jobGroup", jobGroupId);
        paramMap.put("jobDesc", jobDesc);
        paramMap.put("scheduleConf", scheduleConf);
        paramMap.put("scheduleType", "CRON");
        paramMap.put("misfireStrategy", "DO_NOTHING");
        paramMap.put("executorRouteStrategy", "ROUND");
        paramMap.put("executorHandler", executorHandler); // 此处hander需提前在项目中定义
        paramMap.put("executorParam", executorParam);
        paramMap.put("executorBlockStrategy", "SERIAL_EXECUTION");
        paramMap.put("executorTimeout", 0);
        paramMap.put("executorFailRetryCount", 0);
        paramMap.put("glueType", "BEAN");
        paramMap.put("author", "admin");
        paramMap.put("glueRemark", jobDesc);
        paramMap.put("triggerStatus", 1);
        if (cookie == null || cookie.trim().length() == 0) {
            cookie = getCookie(path, USER_NAME, PASSWORD);
        }
        HttpResponse response = HttpRequest.post(path + "/jobinfo/add").cookie(cookie).form(paramMap).execute();
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            return -1;
        }
        JSONObject jsonObject = JSON.parseObject(response.body());
        int jobId = jsonObject.getIntValue("content");
        return jobId;
    }

    /**
     * 获取xxl管理后台的cookies
     *
     * @param path
     * @param userName
     * @param password
     * @return
     */
    private static String getCookie(String path, String userName, String password) {
        Map<String, Object> hashMap = new HashMap();
        hashMap.put("userName", userName);
        hashMap.put("password", password);
        HttpResponse response = HttpRequest.post(path + "/login").form(hashMap).execute();
        List<HttpCookie> cookies = response.getCookies();
        StringBuilder sb = new StringBuilder();
        for (HttpCookie cookie : cookies) {
            sb.append(cookie.toString());
        }
        return sb.toString();
    }

}
