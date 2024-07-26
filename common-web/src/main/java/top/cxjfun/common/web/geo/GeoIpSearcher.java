package top.cxjfun.common.web.geo;

import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;

/**
 * GEO IP 全球IP坐标定位搜索
 */
public class GeoIpSearcher extends Searcher {

    public GeoIpSearcher(String dbFile, byte[] vectorIndex, byte[] cBuff) throws IOException {
        super(dbFile, vectorIndex, cBuff);
    }

    public GeoIpInfo searchInfo(String ipStr) throws Exception {
        String s = this.search(ipStr);
        return new GeoIpInfo(s,null);
    }

    public GeoIpInfo searchInfo(long ip) throws IOException {
        String s = this.search(ip);
        return new GeoIpInfo(s,null);
    }
}
