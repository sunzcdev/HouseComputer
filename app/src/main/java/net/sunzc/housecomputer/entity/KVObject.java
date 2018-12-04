package net.sunzc.housecomputer.entity;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Administrator
 * @date 2018-12-03 18:48
 **/
public class KVObject implements Serializable {
    public String K, V;

    public KVObject(String k, String v) {
        K = k;
        V = v;
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "%s:%s", K, V);
    }
}
