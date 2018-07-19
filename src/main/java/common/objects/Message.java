package common.objects;

import com.alibaba.fastjson.JSONObject;

import javax.print.attribute.standard.NumberUp;

public class Message extends Body{
    private int messageId;
    private int dialogId;
    private String sender;
    private String text;
    private long time;

    public Message(int messageId, int dialogId, String sender, String text, long time) {
        this.messageId = messageId;
        this.dialogId = dialogId;
        this.sender = sender;
        this.text = text;
        this.time = time;
    }
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

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject(){
            @Override
            public Object put(String key, Object value) {
                if (value == null || value instanceof Number && ((Number) value).intValue() == 0)
                    return value;
                return super.put(key, value);
            }
        };
        o.put("messageId", messageId);
        o.put("dialogId", dialogId);
        o.put("sender", sender);
        o.put("text", text);
        o.put("time", time);
        return o;
    }
}
