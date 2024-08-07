package com.fire.lib;

import android.util.Log;

public class OtaPacketParser {

    private static final String TAG = "OtaPacketParser";
    private int total;
    private int index = -1;
    private byte[] data;
    private int progress;

    public void set(byte[] data) {
        this.clear();

        this.data = data;
        int length = this.data.length;
        int size = 16;

        if (length % size == 0) {
            total = length / size;
        } else {
            total = (int) Math.floor(length / size + 1);
        }
    }

    public void clear() {
        this.progress = 0;
        this.total = 0;
        this.index = -1;
        this.data = null;
    }

    public boolean hasNextPacket() {
        return this.total > 0 && (this.index + 1) < this.total;
    }

    public boolean isLast() {
        return (this.index + 1) == this.total;
    }

    public int getNextPacketIndex() {
        return this.index + 1;
    }

    public byte[] getNextPacket() {

        int index = this.getNextPacketIndex();
        byte[] packet = this.getPacket(index);
        this.index = index;

        return packet;
    }

    public byte[] getPacket(int index) {

        int length = this.data.length;
        int size = 16;
        int packetSize;

        if (length > size) {
            if ((index + 1) == this.total) {
                packetSize = length - index * size;
            } else {
                packetSize = size;
            }
        } else {
            packetSize = length;
        }

        packetSize = packetSize + 4;
        byte[] packet = new byte[20];
        for (int i = 0; i < 20; i++) {
            packet[i] = (byte) 0xFF;
        }
        System.arraycopy(this.data, index * size, packet, 2, packetSize - 4);

        this.fillIndex(packet, index);
        int crc = this.crc16(packet);
        this.fillCrc(packet, crc);
//        if (Logger.isLoggableDebug()) {
//            Log.d(TAG, "ota packet ---> index : " + index + " total : " + this.total + " crc : " + crc + " content : " + ByteUtils.bytes2HexString(packet));
//        }
        return packet;
    }

    public byte[] getCheckPacket() {
        byte[] packet = new byte[16];
        for (int i = 0; i < 16; i++) {
            packet[i] = (byte) 0xFF;
        }

        int index = this.getNextPacketIndex();
        this.fillIndex(packet, index);
        int crc = this.crc16(packet);
        this.fillCrc(packet, crc);
//        if (Logger.isLoggableDebug()) {
//            Logger.d(TAG, "ota check packet ---> index : " + index + " crc : " + crc + " content : " + ByteUtils.bytes2HexString(packet));
//        }
        return packet;
    }

    public void fillIndex(byte[] packet, int index) {
        int offset = 0;
        packet[offset++] = (byte) (index & 0xFF);
        packet[offset] = (byte) (index >> 8 & 0xFF);
    }

    public void fillCrc(byte[] packet, int crc) {
        int offset = packet.length - 2;
        packet[offset++] = (byte) (crc & 0xFF);
        packet[offset] = (byte) (crc >> 8 & 0xFF);
    }

    public int crc16(byte[] packet) {

        int length = packet.length - 2;
        short[] poly = new short[]{0, (short) 0xA001};
        int crc = 0xFFFF;
        int ds;

        for (int j = 0; j < length; j++) {

            ds = packet[j];

            for (int i = 0; i < 8; i++) {
                crc = (crc >> 1) ^ poly[(crc ^ ds) & 1] & 0xFFFF;
                ds = ds >> 1;
            }
        }

        return crc;
    }

    public boolean invalidateProgress() {

        float a = this.getNextPacketIndex();
        float b = this.total;

        int progress = (int) Math.floor((a / b * 100));

        if (progress == this.progress)
            return false;

        this.progress = progress;

        return true;
    }

    public int getProgress() {
        return this.progress;
    }

    public int getIndex() {
        return this.index;
    }
}
