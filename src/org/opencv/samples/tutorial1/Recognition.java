package org.opencv.samples.tutorial1;

import android.graphics.Color;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class Recognition {

    public static double MIN_MID2_FOR_TRUE = 88.0;
    public static double MAX_MID2_FOR_FALSE = 88.0;
    public static double MAX_MID3_FOR_TRUE = 42.0;
    public static double MAX_MID3_FOR_FALSE = 42.0;

    public static boolean[][] algo1(boolean[][] res, Mat mGray, double[][] mid2, double[][] mid3, int i, int j, Rect rect){
                MatOfDouble mean = new MatOfDouble(0, 0, 0, 0);
                MatOfDouble stddev = new MatOfDouble(0, 0, 0, 0);
                Core.meanStdDev(mGray.submat(rect), mean, stddev/*, mask*/);
                mid2[i][j] = mean.toArray()[0];
                mid3[i][j] = stddev.toArray()[0];
//        Imgproc.putText(mRgba, toShortString(MID1, 7), new Point(leftRight.x, leftRight.y + 15), 3, 0.7, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
                Scalar colorOfRect = new Scalar(Color.red(235), Color.green(255), Color.blue(0));
                if (mid2[i][j] < MAX_MID2_FOR_FALSE && mid3[i][j] < MAX_MID3_FOR_FALSE) {
                    res[i][j] = false;
                } else if (mid2[i][j] > MIN_MID2_FOR_TRUE && mid3[i][j] < MAX_MID3_FOR_TRUE) {
                    res[i][j] = true;
                } else {
                    res[i][j] = false;
                }
        return res;
    }
}
