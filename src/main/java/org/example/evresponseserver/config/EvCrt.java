package org.example.evresponseserver.config;

public enum EvCrt {
    SUNGBIN(0),
    HYUNDAI(1);

    int crt;
    EvCrt(int crt) {
        this.crt = crt;
    }

    public int getDirection() {
        return crt;
    }

    public static EvCrt nameOf (int name) {
        for (EvCrt crt : EvCrt.values()) {
            if (crt.crt == name)
                return crt;
        }

        return SUNGBIN;
    }
}