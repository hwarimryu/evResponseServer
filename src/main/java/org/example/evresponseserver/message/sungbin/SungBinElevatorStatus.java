package org.example.evresponseserver.message.sungbin;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 성빈EV 감시반으로 부터 수신한 호기의 상태 정보
 * <p>
 * Created by hwarim 2021-12-07
 */
public class SungBinElevatorStatus {
    private String dong;
    private Integer evId;
    private Integer evIdSub;
    private Integer currentFloor;

    private byte[] orgMsg;

    private List<Integer> downHallCalls;
    private List<Integer> upHallCalls;

    // direction
    private boolean isUp;
    private boolean isDown;

    // door
    private boolean isClosed;
    private boolean isOpen;

    // status
    private boolean isRunning;
    private boolean isFault;    // 고장

    private boolean isFull; // 만원
    private boolean isOverload;

    private void setStatus(byte status) {
        isUp = ((0x80 >>> 7) & status) != 0;
        isDown = ((0x80 >>> 6) & status) != 0;
        isClosed = ((0x80 >>> 5) & status) != 0;
        isOpen = ((0x80 >>> 4) & status) != 0;
        isRunning = ((0x80 >>> 3) & status) != 0;
        isFull = ((0x80 >>> 2) & status) != 0;
        isOverload = ((0x80 >>> 1) & status) != 0;
        isFault = ((0x80) & status) != 0;
    }


    SungBinElevatorStatus(byte[] status) {
        orgMsg = status;
        evId = ((Byte) status[0]).intValue();
        dong = String.valueOf((status[1] & 0xff) * 256 + (status[2] & 0xff));
        evIdSub = ((Byte) status[3]).intValue();
        currentFloor = ((Byte) status[4]).intValue();
        setStatus(status[6]);
        upHallCalls = new ArrayList<>();
        downHallCalls = new ArrayList<>();
        setUpHallCalls();
        setDownHallCalls();
    }

    private void setUpHallCalls() {
        byte bits0 = orgMsg[7];
        byte bits1 = orgMsg[8];
        byte bits2 = orgMsg[9];
        byte bits3 = orgMsg[10];
        byte bits4 = orgMsg[11];
        byte bits5 = orgMsg[12];

        for (int i = 0; i < 8; i++) {
            if (((0x80 >>> 7 - i) & bits0) != 0)
                upHallCalls.add(i);
            if (((0x80 >>> 7 - i) & bits1) != 0)
                upHallCalls.add(i + 8);
            if (((0x80 >>> 7 - i) & bits2) != 0)
                upHallCalls.add(i + 16);
            if (((0x80 >>> 7 - i) & bits3) != 0)
                upHallCalls.add(i + 24);
            if (((0x80 >>> 7 - i) & bits4) != 0)
                upHallCalls.add(i + 32);
            if (((0x80 >>> 7 - i) & bits5) != 0)
                upHallCalls.add(i + 40);
        }

        Collections.sort(upHallCalls);
    }

    private void setDownHallCalls() {
        byte bits0 = orgMsg[13];
        byte bits1 = orgMsg[14];
        byte bits2 = orgMsg[15];
        byte bits3 = orgMsg[16];
        byte bits4 = orgMsg[17];
        byte bits5 = orgMsg[18];

        for (int i = 0; i < 8; i++) {
            if (((0x80 >>> 7 - i) & bits0) != 0)
                downHallCalls.add(i);
            if (((0x80 >>> 7 - i) & bits1) != 0)
                downHallCalls.add(i + 8);
            if (((0x80 >>> 7 - i) & bits2) != 0)
                downHallCalls.add(i + 16);
            if (((0x80 >>> 7 - i) & bits3) != 0)
                downHallCalls.add(i + 24);
            if (((0x80 >>> 7 - i) & bits4) != 0)
                downHallCalls.add(i + 32);
            if (((0x80 >>> 7 - i) & bits5) != 0)
                downHallCalls.add(i + 40);
        }

        Collections.sort(downHallCalls);
    }

    public Integer getEvId() {
        return evId;
    }

    public Integer getCurrentFloor() {
        return Integer.valueOf(currentFloor);
    }


    public String upHallCallsToString() {
        return upHallCalls.stream().map(integer -> Integer.toString(integer)).collect(Collectors.joining(", "));
    }

    public String upDownHallCallsToString() {
        return downHallCalls.stream().map(integer -> Integer.toString(integer)).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return String.format("{ Dong=%-4s, EvID=%02d, Mode=%-5s, currentFloor=%-2s, direction=%-5s, door=%-6s, [ upHallCalls=%-20s ], [ downHallCalls=%-20s ] }",
                dong, evId, "TEST", currentFloor, isUp ? "UP" : (isDown ? "DOWN" : "NONE"), isOpen ? "OPEN" : (isClosed ? "CLOSED" : "STOP"), upHallCallsToString(), upDownHallCallsToString());
    }

    public String getDong() {
        return dong;
    }

    public boolean isUp() {
        return isUp;
    }

    public boolean isDown() {
        return isDown;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isFault() {
        return isFault;
    }

    public boolean isFull() {
        return isFull;
    }

    public boolean isOverload() {
        return isOverload;
    }
}