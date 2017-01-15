package net.emittam.pamphletmaker;

import java.util.HashMap;

/**
 * Created by i53 on 2016/11/27.
 */

public class CivicPOIModel {
    public String name;
    public String desc;
    public String img;
    public String lat;
    public String lng;

    CivicPOIModel(HashMap<String, String> hashmap) {
        this.name = hashmap.get("name");
        this.desc = hashmap.get("desc");
        this.img = hashmap.get("img");
        this.lat = hashmap.get("lat");
        this.lng = hashmap.get("lng");
    }
}
