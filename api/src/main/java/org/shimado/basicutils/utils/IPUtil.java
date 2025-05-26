package org.shimado.basicutils.utils;

import org.shimado.basicutils.BasicUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class IPUtil {

    public static String getServerPropertiesIP(){
        return BasicUtils.getPlugin().getServer().getIp();
    }


    public static String getServerIP(){
        try {
            URL ipService = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(ipService.openStream()));
            return in.readLine();
        } catch (Exception e) {
            return "";
        }
    }

}
