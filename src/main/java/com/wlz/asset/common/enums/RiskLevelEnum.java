package com.wlz.asset.common.enums;

public enum RiskLevelEnum {

    /** 保守型 */
    CONSERVATIVE(1, "保守型"),
    /** 稳健型 */
    MODERATE(2, "稳健型"),
    /** 平衡型 */
    BALANCED(3, "平衡型"),
    /** 进取型 */
    AGGRESSIVE(4, "进取型"),
    /** 激进型 */
    RADICAL(5, "激进型");

    /** 风险等级编码（对应数据库 tinyint 字段） */
    private final int riskLevel;

    /** 风险等级名称 */
    private final String riskName;

    /**
     * 构造方法
     * @param riskLevel 风险等级编码
     * @param riskName 风险等级名称
     */
    RiskLevelEnum(int riskLevel, String riskName) {
        this.riskLevel = riskLevel;
        this.riskName = riskName;
    }

    // ========== 基础 Getter 方法 ==========
    public int getRiskLevel() {
        return riskLevel;
    }

    public String getRiskName() {
        return riskName;
    }

    // ========== 常用工具方法（核心） ==========

    /**
     * 根据风险等级编码获取枚举实例（推荐使用）
     * @param riskLevel 风险等级编码（1-5）
     * @return 对应枚举实例，无匹配则返回 null
     */
    public static RiskLevelEnum getByRiskLevel(int riskLevel) {
        for (RiskLevelEnum enumItem : values()) {
            if (enumItem.riskLevel == riskLevel) {
                return enumItem;
            }
        }
        // 若需要严格校验，可抛出 IllegalArgumentException 异常
        // throw new IllegalArgumentException("无效的风险等级编码：" + riskLevel);
        return null;
    }

    /**
     * 根据风险等级编码获取等级名称（简化调用）
     * @param riskLevel 风险等级编码（1-5）
     * @return 等级名称，无匹配则返回空字符串
     */
    public static String getRiskNameByLevel(int riskLevel) {
        RiskLevelEnum enumItem = getByRiskLevel(riskLevel);
        return enumItem == null ? "" : enumItem.getRiskName();
    }

    /**
     * 校验风险等级编码是否有效
     * @param riskLevel 风险等级编码
     * @return true=有效，false=无效
     */
    public static boolean isValidRiskLevel(int riskLevel) {
        return getByRiskLevel(riskLevel) != null;
    }

}
