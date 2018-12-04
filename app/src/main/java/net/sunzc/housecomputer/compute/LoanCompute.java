package net.sunzc.housecomputer.compute;

import net.sunzc.housecomputer.entity.HouseType;
import net.sunzc.housecomputer.entity.KVObject;

import java.util.List;

/**
 * @author Administrator
 * @date 2018-12-04 12:54
 **/
public interface LoanCompute {
    List<KVObject> compute(HouseType t);
}
