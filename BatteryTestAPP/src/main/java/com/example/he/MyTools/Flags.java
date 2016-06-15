package com.example.he.MyTools;

import java.util.HashMap;

/**
 * Created by HE on 2016/3/13.
 */
public class Flags extends HashMap<String,Boolean>{
    private static Flags flags = null;

    private Flags(){
    }

    public static Flags getInstance(){
        if(null == flags){
            flags = new Flags();
        }
        return flags;
    }

    public void setFlag_false(String flagname) {
        flags.put(flagname, false);
    }

    public void setFlag_true(String flagname) {
        flags.put(flagname, true);
    }

    public boolean checkFlagState(String flagname) {
        if (!flags.containsKey(flagname)) {
            return false;
        } else return flags.get(flagname);
    }
}
