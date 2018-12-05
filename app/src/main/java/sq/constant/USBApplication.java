package sq.constant;

import android.content.Context;

/**
 * Create By：Pst on 2018/11/2 0002 10:16
 * DescriBe:
 */
public class USBApplication extends android.app.Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
