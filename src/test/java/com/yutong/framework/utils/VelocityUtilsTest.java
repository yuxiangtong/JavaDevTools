package com.yutong.framework.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VelocityUtilsTest {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("name", "velocity");
        map.put("date", (new Date()).toString());

        List<String> list = new ArrayList<String>();
        list.add("a1");
        list.add("a2");
        map.put("list", list);

        String velocityContext = VelocityUtils
                .contentToString("resources/bjson/hellovelocity.vm", map);

        System.out.println(velocityContext);
    }

}
