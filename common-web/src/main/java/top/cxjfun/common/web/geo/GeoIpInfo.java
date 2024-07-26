package top.cxjfun.common.web.geo;


import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

@Getter
public class GeoIpInfo {

    private String letter=" ";

    private String country;

    private String province;

    private String city;

    private String ISP;

    private String address="";

    public GeoIpInfo() {
        this(null,null);
    }

    public GeoIpInfo(String geoStr, String letter) {

        if(ObjectUtil.isNotEmpty(letter)){
            this.letter = letter;
        }

        if(ObjectUtil.isNotEmpty(geoStr)){
            this.parse(geoStr);
        }
    }

    public GeoIpInfo parse(String geoStr){
        String[] a = geoStr.split("\\|");
        if(!a[0].equals("0")){
            this.country=a[0];
            address+=this.country;
        }

        if(!a[2].equals("0")){
            this.province=a[2];
            address+=this.letter+this.province;
        }

        if(!a[3].equals("0")){
            this.city=a[3];
            address+=this.letter+this.city;
        }

        if(!a[4].equals("0")){
            this.ISP=a[4];
            address+=this.letter+this.ISP;
        }

        return this;
    }


}
