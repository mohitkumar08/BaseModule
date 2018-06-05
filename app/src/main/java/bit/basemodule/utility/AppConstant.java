package bit.basemodule.utility;

import okhttp3.MediaType;

/**
 * Created by bit on 14/12/17.
 */

public interface AppConstant {


    String POST = "POST";
    String GET = "GET";
    String EMPTY = "";
    String AND = "& ";

    MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    int SUCCESS = 200;
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;
    int ERROR = 500;
    int FAILED = 400;

    int SocketException = 1107;
    int EXCEPTION = 1099;
    int IOException = 1112;
    int BindException = 1100;
    int ClosedChannelException = 1101;
    int ConnectException = 1102;
    int InterruptedIOException = 1103;
    int NoRouteToHostException = 1104;
    int PortUnreachableException = 1105;
    int ProtocolException = 1106;
    int SocketTimeoutException = 1108;
    int SSLException = 1109;
    int UnknownHostException = 1110;
    int UnknownServiceException = 1111;

    enum Status {
        PC("Pending Confirmation"), CF("Confirmed"), CC("Cancelled");
        private String status;

        Status(String p) {
            status = p;
        }

        public String getStatus() {
            return status;
        }
    }
}