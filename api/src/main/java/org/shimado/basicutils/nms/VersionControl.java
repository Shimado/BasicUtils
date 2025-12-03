package org.shimado.basicutils.nms;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class VersionControl {

    public VersionControl(){
        getVersion();
    }

    private String version = null;
    private IVersionControl versionControl = null;
    private IInvHandler invHandler = null;
    private IAnvilHandler anvilHandler = null;


    public String getVersion(){
        if(version == null){
            switch (Bukkit.getServer().getBukkitVersion().split("-")[0]){
                case "1.12":
                case "1.12.1":
                case "1.12.2": version = "v1_12_R1"; break;

                case "1.13": version = "v1_13_R1"; break;
                case "1.13.1":
                case "1.13.2": version = "v1_13_R2"; break;

                case "1.14":
                case "1.14.1":
                case "1.14.2":
                case "1.14.3":
                case "1.14.4": version = "v1_14_R1"; break;

                case "1.15":
                case "1.15.1":
                case "1.15.2": version = "v1_15_R1"; break;

                case "1.16":
                case "1.16.1": version = "v1_16_R1"; break;
                case "1.16.2":
                case "1.16.3": version = "v1_16_R2"; break;
                case "1.16.4":
                case "1.16.5": version = "v1_16_R3"; break;

                case "1.17":
                case "1.17.1": version = "v1_17_R1"; break;

                case "1.18":
                case "1.18.1": version = "v1_18_R1"; break;
                case "1.18.2": version = "v1_18_R2"; break;

                case "1.19":
                case "1.19.1":
                case "1.19.2": version = "v1_19_R1"; break;
                case "1.19.3": version = "v1_19_R2"; break;
                case "1.19.4": version = "v1_19_R3"; break;

                case "1.20":
                case "1.20.1": version = "v1_20_R1"; break;
                case "1.20.2": version = "v1_20_R2"; break;
                case "1.20.3":
                case "1.20.4": version = "v1_20_R3"; break;
                case "1.20.5":
                case "1.20.6": version = "v1_20_R4"; break;

                case "1.21":
                case "1.21.1": version = "v1_21_R1"; break;
                case "1.21.2":
                case "1.21.3": version = "v1_21_R2"; break;
                case "1.21.4": version = "v1_21_R3"; break;
                case "1.21.5": version = "v1_21_R4"; break;
                case "1.21.6":
                case "1.21.7":
                case "1.21.8": version = "v1_21_R5"; break;
                case "1.21.9":
                case "1.21.10": version = "v1_21_R6"; break;
            }
            try {
                versionControl = (IVersionControl) Class.forName("org.shimado.basicutils." + version + ".VersionInstance").getConstructor().newInstance(null);
                invHandler = (IInvHandler) Class.forName("org.shimado.basicutils." + version + ".InvHandler").getConstructor().newInstance(null);
                anvilHandler = (IAnvilHandler) Class.forName("org.shimado.basicutils." + version + ".AnvilHandler").getConstructor().newInstance(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return version;
    }


    @Nonnull
    public IVersionControl getVersionControl(){
        return versionControl;
    }

    @Nonnull
    public IInvHandler getInvHandler(){
        return invHandler;
    }

    @Nonnull
    public IAnvilHandler getAnvilHandler(){
        return anvilHandler;
    }


    public boolean isLegacy(){
        return version.equals("v1_12_R1");
    }


    public boolean isCustomModelData(){
        return Arrays.asList(
                "v1_12_R1",
                "v1_13_R1",
                "v1_13_R2"
        ).stream().noneMatch(it -> version.equals(it));
    }


    public boolean isHex(){
        return Arrays.asList(
                "v1_12_R1",
                "v1_13_R1",
                "v1_13_R2",
                "v1_14_R1",
                "v1_15_R1",
                "v1_16_R1",
                "v1_16_R2",
                "v1_16_R3"
        ).stream().noneMatch(it -> version.equals(it));
    }


    public boolean isHeadMetaUpdated() {
        return Arrays.asList(
                "v1_12_R1",
                "v1_13_R1",
                "v1_13_R2",
                "v1_14_R1",
                "v1_15_R1",
                "v1_16_R1",
                "v1_16_R2",
                "v1_16_R3",
                "v1_17_R1",
                "v1_18_R1",
                "v1_18_R2",
                "v1_19_R1",
                "v1_19_R2",
                "v1_19_R3",
                "v1_20_R1"
        ).stream().noneMatch(it -> version.equals(it));
    }


    public boolean isGlowingAndHiddenNamesUpdated(){
        return Arrays.asList(
                "v1_12_R1",
                "v1_13_R1",
                "v1_13_R2",
                "v1_14_R1",
                "v1_15_R1",
                "v1_16_R1",
                "v1_16_R2",
                "v1_16_R3",
                "v1_17_R1",
                "v1_18_R1",
                "v1_18_R2",
                "v1_19_R1",
                "v1_19_R2",
                "v1_19_R3",
                "v1_20_R1",
                "v1_20_R2",
                "v1_20_R3"
        ).stream().noneMatch(it -> version.equals(it));
    }

}
