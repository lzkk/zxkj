package com.zxkj.common.condition;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 接收手机端参数
 * 手机端接口继承该类
 *
 */
public class MobileCondition implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "记录订单下单渠道,如ios,android等")
    private String platform;

	@ApiModelProperty(value = "did号")
    private String did;

	@ApiModelProperty(value = "wifi")
    private String wifi;

	@ApiModelProperty(value = "租户ID(winretailsaler(门店).winretaildealer(经销商).winretailsr(业代))")
    private String grp;

	@ApiModelProperty(value = "门店下单时所用设备")
    private String imei;

	@ApiModelProperty(value = "维度")
    private String lat;

	@ApiModelProperty(value = "经度")
    private String lon;
	
	@ApiModelProperty(value = "坐标类型经度")
	private String coordinateType;

	@ApiModelProperty(value = "src")
    private String src;

	@ApiModelProperty(value = "版本号")
    private String ver;

	@ApiModelProperty(value = "lang")
    private String lang;

	@ApiModelProperty(value = "token")
    private String token;

	@ApiModelProperty(value = "安卓模拟器ID，用于记录日志使用")
	private String emulatordid;

	@ApiModelProperty(value = "数盟ID，用于判断手机是否多终端登录")
	private String smDid;
	
	@ApiModelProperty(value = "每次请求唯一ID")
	private Long currentTimeMillis;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public String getGrp() {
		return grp;
	}

	public void setGrp(String grp) {
		this.grp = grp;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getCoordinateType() {
		return coordinateType;
	}

	public void setCoordinateType(String coordinateType) {
		this.coordinateType = coordinateType;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmulatordid() {
		return emulatordid;
	}

	public void setEmulatordid(String emulatordid) {
		this.emulatordid = emulatordid;
	}

	public String getSmDid() {
		return smDid;
	}

	public void setSmDid(String smDid) {
		this.smDid = smDid;
	}

	public Long getCurrentTimeMillis() {
		return currentTimeMillis;
	}

	public void setCurrentTimeMillis(Long currentTimeMillis) {
		this.currentTimeMillis = currentTimeMillis;
	}
}