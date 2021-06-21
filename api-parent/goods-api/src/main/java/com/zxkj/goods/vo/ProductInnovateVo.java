package com.zxkj.goods.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInnovateVo {

    private Integer id;

    private String code;

    private String name;

    private String manager;

    private Byte isScaled;

    private Byte isTest;

    private Byte isWdl;

    private BigDecimal scaleAmount;

    private BigDecimal scaleAlarmAmount;

    private BigDecimal hasInvestAmount;

    private BigDecimal remainAmount;

    private BigDecimal minPurchaseAmount;

    private BigDecimal increaseAmount;

    private Integer scalePersion;

    private Integer scaleAlarmPersion;

    private Byte source;

    private Integer productTypeId;

    private Byte achievementType;

    private Byte attribute;

    private String period;

    private String periodRemark;

    private String riskLevel;

    private String allowRiskEvaluate;

    private String investCategory;

    private String hightlights;

    private Byte isExceedPay;

    private BigDecimal exceedPayAmount;

    private BigDecimal lfFoldRatio;

    private BigDecimal pxFoldRatio;

    private BigDecimal commissionProportion;

    private String productReview;

    private String riskPrompt;

    private Byte productState;

    private Byte auditState;

    private Long onlineTime;

    private Long establishTime;

    private Long raiseEndTime;

    private Long terminatePublishTime;

    private Long expireTime;

    private Long liquidationTime;

    private Long cashTime;

    private Long createTime;

    private Byte yn;

    private String hightlights2;

    private String hightlights3;

    private Byte currency;

    private Byte liquidationAuditState;

    private String closedPeriod;

    private Integer parentProductTypeId;

    private String productReview2;

    private String productReview3;

    private String productReview4;

    private String productReview5;

    private Byte isrecover;

    private String backImgForAndroid;

    private String backImgForIos;

    private Byte raiseType;

    private Byte isAppDisplay;

    private Byte contractSignatureState;

    private Byte processNew;

    private Byte isPublic;

    private Integer productTagId;

    private String investmentTrack;

    private String investmentLogic;

    private Byte isDualRecord;

    private String productPublishTip;

    private Integer managerId;

    private Integer jfProductTypeId;

    private Integer jfParentProductTypeId;

    private String contractPre;

    private Byte isElectronicContract;

    private Byte riskEvaluationModel;

    private Byte isNeedAssetCertification;

    private Byte productMark;

}