package com.example.he.BatteryInfo;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by HE on 2015/12/6.
 */
public class BatteryInfo implements KvmSerializable{
    String No;
    String Id;
    String Capacity;

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return No;
            case 1:
                return Id;
            case 2:
                return Capacity;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 3;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i){
            case 0:
                No = o.toString();
                break;
            case 1:
                Id = o.toString();
                break;
            case 2:
                Capacity = o.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i){
            case 0:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "No";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "Id";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "Capacity";
                break;
        }
    }
}
