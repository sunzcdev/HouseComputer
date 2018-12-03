package net.sunzc.housecomputer;

import java.io.Serializable;

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
        return "KVObject{" +
                "K='" + K + '\'' +
                ", V='" + V + '\'' +
                '}';
    }
}
