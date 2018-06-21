package com.zk.kfcloud.Utils.wechat;

public class textMessage extends baseMessage{
    private String Content;
    private Integer MsgId;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Integer getMsgId() {
        return MsgId;
    }

    public void setMsgId(Integer msgId) {
        MsgId = msgId;
    }

    @Override
    public String toString() {
        return "textMessage{" +
                "Content='" + Content + '\'' +
                ", MsgId=" + MsgId +
                '}';
    }
}
