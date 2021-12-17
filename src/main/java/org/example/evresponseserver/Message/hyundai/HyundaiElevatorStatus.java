package org.example.evresponseserver.Message.hyundai;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 성빈EV 감시반으로 부터 수신한 호기의 상태 정보
 * <p>
 * Created by hwarim 2021-12-07
 */
public class HyundaiElevatorStatus {
    private String orgMsg;
    private String dong;
    private Integer evId;
//    private OperatingStatus status;
//    private String currentFloor;
//    private Direction direction;
//    private DoorStatus door;
    private String stopScheduleFloor;
    private List<Integer> registerCarCalls;
    private List<Integer> registerUpHallCalls;

    HyundaiElevatorStatus(String info) {
        orgMsg =info;
        dong = info.substring(0,4);
        evId = Integer.valueOf(info.substring(4,6));
//        status = OperatingStatus.nameOf(info.substring(6,8));
//        currentFloor = info.substring(8,10);
//        direction = Direction.nameOf(Integer.valueOf(info.substring(10,11)));
//        door = DoorStatus.nameOf(Integer.valueOf(info.substring(11,12)));
        stopScheduleFloor = info.substring(12,14);
        registerCarCalls = analysisTargetFloors(info.substring(14,30));
        registerUpHallCalls = analysisTargetFloors(info.substring(30,46));
    }

    public Integer getEvId() {
        return evId;
    }
//
//    public Integer getCurrentFloor() {
//        return Integer.valueOf(currentFloor);
//    }
//
//    public Direction getDirection() {
//        return direction;
//    }
//
//    public OperatingStatus getStatus() {
//        return status;
//    }
//
//    public DoorStatus getDoorStatus() {
//        return door;
//    }

    @Override
    public String toString() {
        return orgMsg;
//        return String.format("{ Dong=%-4s, EvID=%02d, status=%-13s, currentFloor=%-2s, direction=%-4s, door=%-6s, stopScheduleFloor=%-2s, carCalls=%-20s,upHallCalls=%-20s }",
//                dong, evId, status, currentFloor, direction, door, stopScheduleFloor, registerCarCalls, registerUpHallCalls);
    }

    private List<Integer> analysisTargetFloors(String info) {
        List<Integer> floors = new ArrayList<>();
        for (int index=0; index<info.length(); index++) {
            char c = info.charAt(index);
            int stand = 1;

            // 한자씩 가지고 와서 뒤에 4bit 중 1인 곳의 층 확인 추가.
            for (int j=0; j<4; j++) {
                if (((c & stand) >> j ) == 1)
                    floors.add(j + index*4 + 1);
                stand *= 2;
            }
        }
        return floors;
    }
}