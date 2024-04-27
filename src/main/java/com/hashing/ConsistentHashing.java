package com.hashing;

import java.util.*;
import java.util.function.Function;

public class ConsistentHashing {

    Function<Integer, Integer> hashFunction;
    TreeMap<Integer, Integer> serverPositionToIdMap = new TreeMap<>();
    Map<Integer, Set<Integer>> serverIdToPosition = new HashMap<>();

    private int pointMultiplier;

    void printSetup() {
        System.out.println(serverIdToPosition);
        System.out.println(serverPositionToIdMap);
    }

    ConsistentHashing(int multiplier, Function<Integer, Integer> hasFunc) {
        hashFunction = hasFunc;
        this.pointMultiplier = multiplier;
    }

    private int hash(int value) {
        return hashFunction.apply(value);
    }

    private void checkServerExistence(){
        if(serverPositionToIdMap.isEmpty()) {
            throw new RuntimeException("Nodes doesn't exist");
        }
    }

    private int findNextServerPosition(int position) {
        Integer serverPosition = serverPositionToIdMap.ceilingKey(position);
        return serverPosition == null ? serverPositionToIdMap.firstKey() : serverPosition;
    }

    private int findServerId(int serverPosition) {
        return serverPositionToIdMap.get(serverPosition);
    }


    private boolean checkSlotAvailability(int serverPosition, int serverId, Map<Integer,Integer> serverMap){
        if(serverMap.containsKey(serverPosition)) {
            System.out.println("Server already exist at position = " + serverPosition + " for server = " + serverMap.get(serverPosition));
            System.out.println("Avoiding adding serve at position = " + serverPosition + " for server = " + serverId);
            return false;
        }
        return true;
    }

    private void assignVirtualSlot(int ringPosition, int serverId){
        serverPositionToIdMap.put(ringPosition, serverId);
        if(!serverIdToPosition.containsKey(serverId)) serverIdToPosition.put(serverId, new HashSet<>());
        serverIdToPosition.get(serverId).add(ringPosition);
    }

    void addServer(int serverId, int nInstances) {
        assignVirtualServer(serverId, nInstances);
    }

    void assignVirtualServer(int serverId, int nInstances) {
        for (int virtualNode = 0; virtualNode < nInstances; virtualNode++) {
            int virtualNodePosition = hash(virtualNode + serverId);
            if (checkSlotAvailability(virtualNodePosition, serverId, serverPositionToIdMap)) {
                assignVirtualSlot(virtualNodePosition, serverId);
            }
        }
    }

    private void removeVirtualServer(int serverId) {
        Set<Integer> serverPositions = serverIdToPosition.remove(serverId);
        for(final int position: serverPositions) {
            serverPositionToIdMap.remove(position, serverId);
        }
    }

    void removeServer(int serverId) {
        if(serverIdToPosition.containsKey(serverId))
            removeVirtualServer(serverId);
        else {
            System.out.println("server id = " +  serverId + " doesn't exist");
        }
    }

    int getServerIdForKey(int key) {
        checkServerExistence();
        int ringPosition = hash(key);
        int nextServerPosition = findNextServerPosition(ringPosition);
        return findServerId(nextServerPosition);
    }

}
