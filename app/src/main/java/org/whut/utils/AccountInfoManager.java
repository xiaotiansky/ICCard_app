package org.whut.utils;

import org.whut.database.entities.User;

/**
 * Created by baisu on 15-5-26.
 * 保存登录用户信息
 */
public class AccountInfoManager {

    private static AccountInfoManager mInstance = null;

    private User mUser = null;

    private AccountInfoManager() {

    }

    public static AccountInfoManager getmInstance() {
        if (mInstance == null) {
            synchronized (AccountInfoManager.class) {
                if (mInstance == null) {
                    mInstance = new AccountInfoManager();
                }
            }
        }
        return mInstance;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public User getmUser() {
        return mUser;
    }
}
