package common.objects;

import com.alibaba.fastjson.JSONObject;

public class Message extends Body{
    private int messageId;
    private int dialogId;
    private String sender;
    private String text;
    private long time;
    private boolean isSystem;

    public Message()
    {}

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean system) {
        isSystem = system;
    }

    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject();
    }
}
