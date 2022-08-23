package org.example.evresponseserver.config;

public enum EvCrt {
    SUNGBIN(0),
    HYUNDAI(1),
    NAVI(2),
    OTIS(3),
    CHEONJO(4),
    COMMAX(5),
    SINBO(6),
    MISSBC(7),
    HOMENET_HDC(8);

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