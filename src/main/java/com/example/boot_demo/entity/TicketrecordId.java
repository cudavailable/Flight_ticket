package com.example.boot_demo.entity;

import java.io.Serializable;

public class TicketrecordId implements Serializable {
    private long uId;
    private long fId;

    // 构造函数、Getter 和 Setter 方法

    public TicketrecordId(long uId, long fId) {
        this.uId = uId;
        this.fId = fId;
    }

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public long getfId() {
        return fId;
    }

    public void setfId(long fId) {
        this.fId = fId;
    }
}
