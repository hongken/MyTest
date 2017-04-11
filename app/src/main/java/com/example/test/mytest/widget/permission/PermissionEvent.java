package com.example.test.mytest.widget.permission;

import java.util.ArrayList;

/**
 * Created by Harry on 2017. 4. 3..
 */

public class PermissionEvent {

//    public boolean grantedPermission;
//    public boolean callFromGuideView;
//
//    public ArrayList<String> deniedPermissions;
//
//    public PermissionEvent(boolean grantedPermission, boolean callFromGuideView, ArrayList<String> deniedPermissions) {
//        this.grantedPermission = grantedPermission;
//        this.deniedPermissions = deniedPermissions;
//    }
//
//    public boolean getGrantedPermission() {
//        return grantedPermission;
//    }
//
//    public boolean getCallFromGuideView() {
//        return callFromGuideView;
//    }
//
//    public ArrayList<String> getDeniedPermissions() {
//        return deniedPermissions;
//    }

    enum PermissionResultType {
        GRANTED,
        DENIED,
        DENIED_NEED_DIALOG_SETTING
    }

    public PermissionResultType permissionResultType;
    public ArrayList<String> deniedPermissions;

    public PermissionEvent(PermissionResultType permissionResultType, ArrayList<String> deniedPermissions) {
        this.permissionResultType = permissionResultType;
        this.deniedPermissions = deniedPermissions;
    }

    public PermissionResultType getGrantedPermission() {
        return permissionResultType;
    }

    public ArrayList<String> getDeniedPermissions() {
        return deniedPermissions;
    }
}
