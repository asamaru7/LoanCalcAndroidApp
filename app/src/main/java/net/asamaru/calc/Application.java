package net.asamaru.calc;

import org.acra.annotation.ReportsCrashes;
import org.androidannotations.annotations.EApplication;

@ReportsCrashes(
		formKey = "", // This is required for backward compatibility but not used
		formUri = "http://dev.coregisul.kr/rpc/tracker4app.php?trackerId=AppCrashB9"
)
@EApplication
public class Application extends net.asamaru.bootstrap.Application {
}
