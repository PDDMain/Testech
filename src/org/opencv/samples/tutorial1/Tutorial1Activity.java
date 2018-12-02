package org.opencv.samples.tutorial1;

import android.graphics.Color;
import android.view.*;
import android.widget.TextView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.*;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import org.opencv.imgproc.Imgproc;

public class Tutorial1Activity extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    private Mat mRgba;
    private Mat mGray;

    private Double MID1 = 0.0;
    private Double MID2 = 0.0;
    private Double MID3 = 0.0;

    private int QUANTITY_X = 7;
    private int QUANTITY_Y = 4;

    public double SIDE_OF_SQUARE = 1.0 / 15.0;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public Tutorial1Activity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.tutorial1_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        int h = (int) mRgba.size().height-290;     //display.getHeight();
        int w = (int) mRgba.size().width;    //display.getWidth();
//        Display display = getWindowManager().getDefaultDisplay();
//        mRgba = midRect(mRgba, mGray, new Point(w / 2, h / 2));
        for (int i = 0; i < QUANTITY_X; i++) {
            for (int j = 0; j < QUANTITY_Y; j++) {
                if (QUANTITY_X > 1 && QUANTITY_Y > 1) {
                    int l_x = (int) (1.0 * w / (QUANTITY_X));
                    int l_y = (int) (1.0 * h / (QUANTITY_Y));
                    mRgba = midRect(mRgba, mGray, new Point(l_x/2 + i * l_x, l_y/2 + j * l_y));
                } else if (QUANTITY_X > 1) {
                    int l_x = (int) (1.0 * w / (QUANTITY_X));
                    mRgba = midRect(mRgba, mGray, new Point(l_x/2 + i * l_x, h / 2));
                } else if (QUANTITY_Y > 1) {
                    int l_y = (int) (1.0 * h / (QUANTITY_Y));
                    mRgba = midRect(mRgba, mGray, new Point(w / 2, l_y/2 + j * l_y));
                } else if (QUANTITY_Y == 1 && QUANTITY_X == 1) {
                    mRgba = midRect(mRgba, mGray, new Point(w / 2, h / 2));
                }
            }
        }
        return mRgba;
    }

    private Mat midRect(Mat mRgba, Mat mGray, Point centr) {
//        Display display = getWindowManager().getDefaultDisplay();
        int h = (int) mRgba.size().height;     //display.getHeight();
        int w = (int) mRgba.size().width;    //display.getWidth();
        int side = (int) (w * SIDE_OF_SQUARE);
        Point leftRight = new Point(centr.x - side / 2, centr.y - side / 2);
        Point rightLeft = new Point(centr.x + side / 2, centr.y + side / 2);
//        Imgproc.line(mRgba, new Point(0, 3 * h / 7), new Point(w, 3 * h / 7), new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
//        Imgproc.line(mRgba, new Point(0, 4 * h / 7), new Point(w, 4 * h / 7), new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
//        Imgproc.line(mRgba, new Point((w / 2) - h / 14, 0), new Point((w / 2) - h / 14, h), new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
//        Imgproc.line(mRgba, new Point((w / 2) + h / 14, 0), new Point((w / 2) + h / 14, h), new Scalar(Color.red(0), Color.green(0), Color.blue(0)));


        Rect rect = new Rect(leftRight, rightLeft);
//        int sum = 0;
//        for (int i = rect.x; i < rect.x + rect.width; i++) {
//            for (int j = rect.y; j < rect.y + rect.height; j++) {
//                if (mGray.get(j, i) != null) {
//                    sum += (int) mGray.get(j, i)[0];
//                }
//            }
//        }
//
//        MID1 = (1.0 * sum) / (rect.height * rect.height);

        MatOfDouble mean = new MatOfDouble(0, 0, 0, 0);
        MatOfDouble stddev = new MatOfDouble(0, 0, 0, 0);
//        MatOfByte mask = new MatOfByte(w, h);
//        for(int i = (int) leftRight.x; i < leftRight.x + w*SIDE_OF_SQUARE; i++){
//            for(int j = (int) leftRight.y; j < leftRight.y + w*SIDE_OF_SQUARE; j++){
//                byte[] arr = new byte[1];
//                arr[0] = 1;
//                mask.get(i, j, arr);
//            }
//        }
        Core.meanStdDev(mGray.submat(rect), mean, stddev/*, mask*/);
        MID2 = mean.toArray()[0];
        MID3 = stddev.toArray()[0];

//        Imgproc.putText(mRgba, toShortString(MID1, 7), new Point(leftRight.x, leftRight.y + 15), 3, 0.7, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
        Scalar colorOfRect = new Scalar(Color.red(235), Color.green(255), Color.blue(0));
        if(MID2 > 95 && MID3 < 30){
            colorOfRect = new Scalar(Color.red(33), Color.green(255), Color.blue(0));
            Imgproc.putText(mRgba, "false", rightLeft, 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));

        }else if(MID2 < 50 && MID3 < 20){
            colorOfRect = new Scalar(Color.red(255), Color.green(12), Color.blue(0));
            Imgproc.putText(mRgba, "true", rightLeft, 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
        }else{
            Imgproc.putText(mRgba, "not found", rightLeft, 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
        }
        Imgproc.rectangle(mRgba, leftRight, rightLeft, colorOfRect);
        Imgproc.putText(mRgba, String.format("%05.2f", MID2), new Point(leftRight.x, leftRight.y), 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
        Imgproc.putText(mRgba, String.format("%05.2f", MID3), new Point(leftRight.x, leftRight.y + rect.height + 10), 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));

        return mRgba;
    }

    private String toShortString(Double d, int leangth) {
        String res = Double.toString(d);
        while (res.length() < leangth + 1) {
            res += "0";
        }
        return res.substring(0, leangth);
    }
}