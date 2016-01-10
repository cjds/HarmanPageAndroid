package cjds.harmanpage;

import com.harman.hkwirelessapi.DeviceObj;
import com.harman.hkwirelessapi.GroupObj;
import com.harman.hkwirelessapi.HKWirelessHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjds on 12/16/15.
 */
public class Util {
    public static final int MSG_PCM_PLAY = 2;
    public static final int MSG_PCM_PAUSE = 3;

    public static final String MSG_TYPE_MUSIC = "msg";
    public static final String MSG_URL_MUSIC = "url";

    public boolean getRoomStatus(int position) {
        return rooms.get(position).status;
    }

    public void removeRoomFromSession(long groupId) {
        long[] devices=hkwireless.getDeviceGroupById(groupId).deviceList;
        for(int i=0;i<devices.length;i++)
            hkwireless.removeDeviceFromSession(devices[i]);
        updateRoomStatus(groupId,false);
    }

    public void addRoomToSession(long groupId) {
        long[] devices=hkwireless.getDeviceGroupById(groupId).deviceList;
        for(int i=0;i<devices.length;i++)
            hkwireless.addDeviceToSession(devices[i]);

        updateRoomStatus(groupId,true);
    }

    public class DeviceData {
        public DeviceObj deviceObj;
        public Boolean status;
    }

    public class RoomData{
        public GroupObj groupObj;
        public Boolean status;
    }

    private List<DeviceData> devices = new ArrayList<DeviceData>();
    private List<RoomData> rooms = new ArrayList<RoomData>();

    public  HKWirelessHandler hkwireless = new HKWirelessHandler();
    private static Util instance = new Util();


    public static Util getInstance() {
        return instance;
    }

    public void initDeviceInfor() {
        synchronized (this) {
            devices.clear();
            if (!hkwireless.isInitialized())return;

            for (int i=0; i<hkwireless.getDeviceCount(); i++) {
                DeviceData device = new DeviceData();
                device.deviceObj = hkwireless.getDeviceInfoByIndex(i);
                device.status = hkwireless.isDeviceActive(device.deviceObj.deviceId);
                devices.add(device);
            }

            for (int i=0;i<hkwireless.getGroupCount();i++){
                RoomData room = new RoomData();
                room.groupObj=hkwireless.getDeviceGroupByIndex(i);
                room.status=false;
            }
        }
    }

    public void refreshDeviceInfoOnce(){
        hkwireless.refreshDeviceInfoOnce();
    }

    public List<DeviceData> getDevices() {
        return devices;
    }

    public List<RoomData> getRooms() {
        return rooms;
    }

    public boolean getDeviceStatus(int position) {
        return devices.get(position).status;
    }

    public void updateDeviceStatus(long deviceid){
        for (int i=0; i<devices.size(); i++) {
            DeviceData device = devices.get(i);
            if (device.deviceObj.deviceId == deviceid) {
                device.deviceObj = hkwireless.findDeviceFromList(deviceid);
                if (device.deviceObj == null) {
                    devices.remove(i);
                } else {
                    device.status = hkwireless.isDeviceActive(device.deviceObj.deviceId);
                    devices.set(i, device);
                }
                break;
            }
        }
    }

    public void updateRoomStatus(long groupID, boolean active){
        for(int i=0;i<rooms.size();i++){
            RoomData room= rooms.get(i);
            if(room.groupObj.groupId==groupID){
                room.groupObj.active=active;
                rooms.set(i,room);
            }
        }

    }

    public boolean removeDeviceFromSession(long deviceid){
        boolean ret = hkwireless.removeDeviceFromSession(deviceid);
        if (ret) {
            updateDeviceStatus(deviceid);
        }
        return ret;
    }

    public boolean addDeviceToSession(long deviceid){
        boolean ret = hkwireless.addDeviceToSession(deviceid);
        if (ret) {
            updateDeviceStatus(deviceid);
        }
        return ret;
    }



}
