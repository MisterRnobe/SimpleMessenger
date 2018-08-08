package common.objects.requests;

import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

import java.util.List;

public class CreateGroupRequest extends Body {
    private String title;
    private List<String> partners;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPartners() {
        return partners;
    }

    public void setPartners(List<String> partners) {
        this.partners = partners;
    }
    public void addPartner(String partner)
    {
        partners.add(partner);
    }

    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject();
    }
}
