package com.example.test.mytest.widget.permission;

import java.util.ArrayList;

/**
 * Created by Harry on 2017. 4. 3..
 */

public interface PermissionListener {

    /**
     * 권한 모두 허용
     */
    void onPermissionGranted();

    /**
     * 권한 거부 - 다시 묻지 않음(check box) 체크 유무 관계 없음
     * @param deniedPermissions 권한 목록
     */
    void onPermissionDenied(ArrayList<String> deniedPermissions);

    /**
     * 권한 거부 - 다시 묻지 않음(check box) 체크 후 거부하여 수동 설정 안내 창 표시
     * @param deniedPermissions 권한 목록
     */
    void onPermissionDeniedNeedShowDialogSetting(ArrayList<String> deniedPermissions);

}
