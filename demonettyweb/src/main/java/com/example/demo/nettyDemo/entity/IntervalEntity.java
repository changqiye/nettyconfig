package com.example.demo.nettyDemo.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2020/8/13.
 */
public class IntervalEntity {
    private Integer resourceId;
    private Integer fragmentId;
    private String giftName;
    private BigDecimal giftAmount;
    private Integer index;
    private Integer interactionMinute;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(Integer fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public BigDecimal getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(BigDecimal giftAmount) {
        this.giftAmount = giftAmount;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getInteractionMinute() {
        return interactionMinute;
    }

    public void setInteractionMinute(Integer interactionMinute) {
        this.interactionMinute = interactionMinute;
    }
}
